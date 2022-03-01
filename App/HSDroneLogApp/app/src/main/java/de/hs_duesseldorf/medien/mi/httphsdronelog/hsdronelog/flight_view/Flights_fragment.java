package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.OnFragmentSwitchListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 * @author Henrik Kother
 */
@SuppressLint("ValidFragment")
public class Flights_fragment extends Fragment implements View.OnClickListener, NetworkListener {

    private TableLayout flightTableLayout;
    private FloatingActionButton faButtonFlightCreate;
    private String pilotId;
    private String roleBit;
    private ArrayList<Map<String, String>> flightData;

    private Activity activity;
    private OnFragmentSwitchListener callback;
    private ProgressBar progressBarFlightsFragment;

    //-------------------- Manage return values ----------------------------------------------------

    /**
     * Get the result from other activitys
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==9){
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra("refresh").equals("true")){
                    //refresh fragment
                    callback.activityShouldRefresh();
                }
            }
        }
    }

    /**
     * this is called once a new Instance is created
     */
    public static final Flights_fragment newInstance(String pilot_id, String role_bit, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        Flights_fragment fragment = new Flights_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(1);
        bundle.putString("pilot_id", pilot_id);
        bundle.putString("role_bit", role_bit);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * this is the constructor of the fragment
     */
    @SuppressLint("ValidFragment")
    public Flights_fragment() {
        this.flightData = new ArrayList<>();
    }

    /**
     * this is called once the fragment is created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get parameters and initialize values
        Bundle arguments = getArguments();
        this.pilotId = arguments.getString("pilot_id");
        this.roleBit = arguments.getString("role_bit");
        this.activity = getActivity();

        // Inflate the layout for this fragment
        View flightView = inflater.inflate(R.layout.fragment_flights_fragment, container, false);
        this.progressBarFlightsFragment=flightView.findViewById(R.id.progressBarFlightsFragment);
        this.progressBarFlightsFragment.setVisibility(View.GONE);
        //get scroll view to set listener
        ScrollView scrollView = flightView.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener( (scrollview, x, y, oldx, oldy) -> {
            if (!scrollView.canScrollVertically(1)) {
                Log.d("SCROLLEN", "bottom");
                if (!HSDroneLogNetwork.networkInstance().isRequesting() && this.flightData.size() % 20 == 0)
                {
                    allFlights();
                }
            }
        });

        flightTableLayout = (TableLayout) flightView.findViewById(R.id.flightTableLayout);

        //FloatingActionButton
        faButtonFlightCreate = (FloatingActionButton) flightView.findViewById(R.id.fabButtonAdd);
        faButtonFlightCreate.setOnClickListener(this);

        //restore saved instance state if one exists
        if (savedInstanceState != null)
        {
            this.flightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("FlightData");
            processFlightData(this.flightData);
        } else {
            allFlights();
        }

        return flightView;
    }

    /**
     * this is called when there is a old instance state
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("FlightData", this.flightData);
    }

    /**
     * this is called when a interaction is detected
     * @param view the current view
     */
    @Override
    public void onClick(View view) {
        if (view == faButtonFlightCreate) {
            //create a new flight
            Intent flightViewActivity = new Intent(this.activity, Flights_view_activity.class);
            flightViewActivity.putExtra("viewOrCreate", "create");
            flightViewActivity.putExtra("pilot_id", this.pilotId);
            flightViewActivity.putExtra("roleBit",this.roleBit);
            startActivityForResult(flightViewActivity,9);

        } else {
            //View a Flight and maybe edit it
            int rowId = view.getId();
            Intent flightViewActivity = new Intent(this.activity, Flights_view_activity.class);
            flightViewActivity.putExtra("id", Integer.toString(rowId));
            flightViewActivity.putExtra("viewOrCreate", "view");
            flightViewActivity.putExtra("pilot_id", this.pilotId);
            flightViewActivity.putExtra("roleBit",this.roleBit);
            startActivityForResult(flightViewActivity,9);
        }
    }

    //---------------------------------- Table Methods ---------------------------------------------

    /**
     * Creates a new Row in the given TableLayout for showing a flight Table
     *
     * @param tableLayout
     * @param id
     * @param vorname
     * @param nachname
     * @param drone
     * @param startTime
     * @param endTime
     * @param description
     * @param location
     * @param date
     */
    private void createFlightTableRow(TableLayout tableLayout, String id, String vorname, String nachname, String drone, String startTime, String endTime, String description, String location, String date) {
        //Datum von Bezeichnung Drohne
        //Ort   Bis             Pilot

        TableRow trInsideAbove = new TableRow(this.activity);
        TableRow trInsideUnder = new TableRow(this.activity);
        trInsideAbove.setPaddingRelative(0, 25, 0, 0);
        trInsideUnder.setPaddingRelative(0, 0, 0, 25);

        //-------- ABOVE ---------
        TextView tvA1 = new TextView(this.activity);
        createNewTableView(tvA1, date);
        trInsideAbove.addView(tvA1);

        TextView tvA2 = new TextView(this.activity);
        createNewTableView(tvA2, startTime);
        trInsideAbove.addView(tvA2);

        TextView tvA3 = new TextView(this.activity);
        createNewTableView(tvA3, location);
        trInsideAbove.addView(tvA3);

        TextView tvA4 = new TextView(this.activity);
        createNewTableView(tvA4, drone);
        trInsideAbove.addView(tvA4);


        //--------- UNDER ------------

        TextView tvU1 = new TextView(this.activity);
        createNewTableView(tvU1, vorname + " " + nachname);
        trInsideUnder.addView(tvU1);

        TextView tvU2 = new TextView(this.activity);
        createNewTableView(tvU2, endTime);
        trInsideUnder.addView(tvU2);

        TextView tvU3 = new TextView(this.activity);
        createNewTableView(tvU3, description);
        trInsideUnder.addView(tvU3);

        TextView tvU4 = new TextView(this.activity);
        if(isFlightFromTheFuture(date)){
            createNewTableView(tvU4, "Anschauen & Bearbeiten >");
            //set color to future flights
            trInsideAbove.setBackgroundColor(getResources().getColor(R.color.lightBlue));
            trInsideUnder.setBackgroundColor(getResources().getColor(R.color.lightBlue));
            tvU4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if(roleBit.equals("1")) {
            //if the user is admin
            createNewTableView(tvU4, "Anschauen & Bearbeiten >");
            tvU4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            createNewTableView(tvU4, "Anschauen >");
            tvU4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        trInsideUnder.addView(tvU4);

        //set the click listener
        trInsideAbove.setOnClickListener(Flights_fragment.this);
        trInsideAbove.setId(Integer.parseInt(id));
        trInsideUnder.setOnClickListener(Flights_fragment.this);
        trInsideUnder.setId(Integer.parseInt(id));

        //add the view to the layout
        tableLayout.addView(trInsideAbove);
        tableLayout.addView(trInsideUnder);

        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));
    }

    /**
     * Creates a new TextView and saves the input in it as a string
     *
     * @param tv   the textview that needs to be managed
     * @param text the String text for filling
     */
    private void createNewTableView(TextView tv, String text) {
        tv.setText(text);
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.matteBlack));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    /**
     * This function calculates if the flight is from the future or from the past
     * @param date the String date In the format of the database
     * @return boolean value if the flight is from the future!
     */
    public static boolean isFlightFromTheFuture(String date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-LL-dd", Locale.GERMAN);
            LocalDate givenDate = LocalDate.parse(date, formatter);
            if (givenDate.isBefore(LocalDate.now(ZoneId.of("ECT")))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //NETWORK/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param results The results from the request in a List of Maps. Each Map
     * represents an object in the data array received from the web server.
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag) {
        if (results != null) {
            this.progressBarFlightsFragment.setVisibility(View.GONE);
            this.flightData.addAll(results);
            Log.d("flightData Entrys", this.flightData.size() + "");

            processFlightData(results);
        }
    }

    /**
     * processes the flight data
     * @param results the results from the network request
     */
    private void processFlightData(List<Map<String, String>> results) {
        if (results != null)
        {
            for (Map<String, String> row : results) {
                createFlightTableRow(
                        flightTableLayout, row.get("flug_id"),
                        row.get("vorname"),
                        row.get("nachname"),
                        row.get("drohnen_modell"),
                        row.get("einsatzbeginn"),
                        row.get("einsatzende"),
                        row.get("flugbezeichnung"),
                        row.get("einsatzort_name"),
                        row.get("flugdatum")
                );
            }
        }
    }

    /**
     *
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @Override
    public void sessionHasExpired(String message, String requestIdentificationTag) {
        Intent i = this.getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( this.getActivity().getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    /**
     *
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @Override
    public void requestWasNotSuccessful(String message, String requestIdentificationTag) {
        this.progressBarFlightsFragment.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
                .show();
    }

    /**
     *
     * @param e The exception instance.
     * @param message The message send from the web server. If no message exists this value is null.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        this.progressBarFlightsFragment.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    /**
     * this requests all flights
     */
    private void allFlights(){
        try {
            this.progressBarFlightsFragment.setVisibility(View.VISIBLE);
            FlightRequest allFlightsRequest = new FlightRequest(this.activity.getApplicationContext());
            allFlightsRequest.allFlights(this.flightData.size());
            HSDroneLogNetwork.networkInstance().startRequest(this, allFlightsRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsFragment.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allFlights();
                        }
                    });

            snackbar.show();
        }
    }
}
