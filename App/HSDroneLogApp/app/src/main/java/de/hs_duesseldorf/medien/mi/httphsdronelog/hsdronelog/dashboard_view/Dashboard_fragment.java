package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view.Flights_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.DashboardRequest;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 * @author Henrik Kother
 */
@SuppressLint("ValidFragment")
public class Dashboard_fragment extends Fragment implements View.OnClickListener, NetworkListener {

    //data for restoring instance state
    private ArrayList<Map<String, String>> todayFlightData;
    private ArrayList<Map<String, String>> longestFlightData;
    private ArrayList<Map<String, String>> pastFlightData;
    private ArrayList<Map<String, String>> plannedFlightData;
    private ArrayList<Map<String, String>> mostUsedDronesData;
    private ArrayList<Map<String, String>> flightData;
    private ArrayList<Map<String, String>> flightsWithoutDronesData;
    private ArrayList<Map<String, String>> pilotoftheWeekData;

    //View from fragment for getID
    private View standartView;
    private ImageButton expandLevelIcon;
    private ImageButton expandMapIcon;
    private ImageButton expandTutorialIcon;
    //---------- Map dashboard -------
    private ConstraintLayout expandTutorial;
    private MapView mapDashboard;
    //------ Welcome massage Dashboard --------
    private TextView welcomeMessage;
    private String nameUser;
    private String surnameUser;
    private String curseUser;
    private String rollUser;
    private int rollUserBinary;
    //------Table layouts------
    private TableLayout flightsTodayTable;
    private TableLayout longestFlightTimeTable;
    private TableLayout pastFlightTable;
    private TableLayout plannedFlightTable;
    private TableLayout flightswithoutDronesTable;
    private TableLayout pilotOfTheWeekTable;

    //swipeRefresh
    private SwipeRefreshLayout swipeRefreshLayout;

    //----------- card views ---------
    private CardView plannedFlights;
    private CardView flightsToday;
    private CardView pastFlights;
    private CardView longestFlightTime;
    private CardView pieChart;
    private CardView flightswithoutDrones;
    private CardView pilotoftheWeek;

    //------ Chart ------
    private PieChart chart;
    List<PieEntry> entries = new ArrayList<>();

    //------ other -----
    private String pilot_id;

    private Activity activity;
    private Resources resources;
    private OnFragmentSwitchListener callback;
    private ProgressBar progressBarDashboard;

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
     * This method is called once a new Instance of the Fragment is created
     * @param vorname
     * @param nachname
     * @param studiengang
     * @param rolle
     * @param pilot_id
     * @param onFragmentSwitchListener
     * @return
     */
    public static final Dashboard_fragment newInstance(String vorname, String nachname, String studiengang,
                                                       String rolle, String pilot_id, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        Dashboard_fragment fragment = new Dashboard_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(5);
        bundle.putString("vorname", vorname);
        bundle.putString("nachname", nachname);
        bundle.putString("studiengang", studiengang);
        bundle.putInt("rolle", Integer.parseInt(rolle));
        bundle.putString("pilot_id", pilot_id);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * this is the constructor of the fragment
     */
    @SuppressLint("ValidFragment")
    public Dashboard_fragment() {
        todayFlightData = new ArrayList<>();
        longestFlightData = new ArrayList<>();
        pastFlightData = new ArrayList<>();
        plannedFlightData = new ArrayList<>();
        mostUsedDronesData = new ArrayList<>();
        flightData = new ArrayList<>();
        flightsWithoutDronesData=new ArrayList<>();
        pilotoftheWeekData=new ArrayList<>();
    }

    /**
     * this method is called once the view is created
     * @param inflater
     * @param container
     * @param savedInstanceState an old instance
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get parameters and initialize values
        Bundle arguments = getArguments();
        this.nameUser = arguments.getString("vorname");
        this.surnameUser = arguments.getString("nachname");
        this.curseUser = arguments.getString("studiengang");
        //set the string representation of the user
        if (arguments.getInt("rolle") == 1) {
            this.rollUser = "Admin";
        } else {
            this.rollUser = "User";
        }
        this.rollUserBinary = arguments.getInt("rolle");
        this.pilot_id = arguments.getString("pilot_id");
        this.activity = getActivity();
        this.resources = getResources();

        // Inflate the layout for this fragment
        standartView = inflater.inflate(R.layout.fragment_dashboard_fragment, container, false);
        standartView.setClickable(false);
        this.progressBarDashboard=standartView.findViewById(R.id.progressBarDashboard);
        this.progressBarDashboard.setVisibility(View.GONE);
        //Expand tutorial
        expandTutorialIcon = (ImageButton) standartView.findViewById(R.id.showMoreWelcomeInformation);
        expandTutorialIcon.setOnClickListener(this);
        expandTutorial = (ConstraintLayout) standartView.findViewById(R.id.expandMoreWelcomeInformation);
        //map + Ask for permission at the OpenstreetMapServer
        Context ctx = standartView.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mapDashboard = (MapView) standartView.findViewById(R.id.mapviewDashboard);
        mapDashboard.setTileSource(TileSourceFactory.MAPNIK);
        mapDashboard.setBuiltInZoomControls(true);
        mapDashboard.setMultiTouchControls(false);
        //Move the map to a start point
        IMapController mapController = mapDashboard.getController();
        mapController.setZoom(5.0);
        GeoPoint startPoint = new GeoPoint(51.163375, 10.447683);
        mapController.setCenter(startPoint);

        //cardviews
        this.plannedFlights = standartView.findViewById(R.id.plannedFlights);
        this.flightsToday = standartView.findViewById(R.id.flightsToday);
        this.pastFlights = standartView.findViewById(R.id.pastFlights);
        this.pieChart = standartView.findViewById(R.id.pieChart);
        this.longestFlightTime = standartView.findViewById(R.id.longestFlightTime);
        this.flightswithoutDrones=standartView.findViewById(R.id.flightswithoutDrones);
        this.pilotoftheWeek=standartView.findViewById(R.id.pilotoftheWeek);

        //Tablelayouts
        flightsTodayTable = standartView.findViewById(R.id.flightsTodayTable);
        longestFlightTimeTable = standartView.findViewById(R.id.longestFlightTimeTable);
        pastFlightTable = standartView.findViewById(R.id.pastFlightsTable);
        plannedFlightTable = standartView.findViewById(R.id.plannedFlightsTable);
        flightswithoutDronesTable=standartView.findViewById(R.id.flightswithoutDronesTable);
        this.pilotOfTheWeekTable=standartView.findViewById(R.id.pilotOfTheWeekTable);
        //Swipe to refresh!
        this.swipeRefreshLayout=standartView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                callback.activityShouldRefresh();
            }
        });
        swipeRefreshLayout.setEnabled(false);
        //set welcome message
        welcomeMessage = (TextView) standartView.findViewById(R.id.welcomeString);
        welcomeMessage.setText("Willkommen, "+ this.nameUser + " " + this.surnameUser + ". (" +  this.rollUser + ")" );

        //Chart
        this.chart = standartView.findViewById(R.id.chart);

        //restore saved instance state if one exists
        if (savedInstanceState != null)
        {
            this.todayFlightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("TodayFlightData");
            this.processTodayFlightData(this.todayFlightData);
            this.longestFlightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("LongestFlightData");
            this.processLongestFlightData(this.longestFlightData);
            this.pastFlightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("PastFlightData");
            this.processPastFlightData(this.pastFlightData);
            this.plannedFlightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("PlannedFlightData");
            this.processPlannedFlightData(this.plannedFlightData);
            this.mostUsedDronesData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("MostUsedDronesData");
            this.processMostUsedDronesData(this.mostUsedDronesData);
            this.flightData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("FlightData");
            this.processFlightData(this.flightData);
            this.flightsWithoutDronesData=(ArrayList<Map<String, String>>) savedInstanceState.getSerializable("FlightsWithoutDronesData");
            this.processFlightWithoutDronesData(this.flightsWithoutDronesData);
            this.pilotoftheWeekData=(ArrayList<Map<String, String>>) savedInstanceState.getSerializable("PilotoftheWeekData");
            processPilotOfTheWeek(pilotoftheWeekData);
        } else {
            getFlightDataToday();
        }

        //return the Standart View
        return standartView;
    }

    /**
     * this method is used for an old Instance state
     * @param outState the instancestate for the old instance state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("TodayFlightData", this.todayFlightData);
        outState.putSerializable("LongestFlightData", this.longestFlightData);
        outState.putSerializable("PastFlightData", this.pastFlightData);
        outState.putSerializable("PlannedFlightData", this.plannedFlightData);
        outState.putSerializable("MostUsedDronesData", this.mostUsedDronesData);
        outState.putSerializable("FlightData", this.flightData);
        outState.putSerializable("FlightsWithoutDronesData", this.flightsWithoutDronesData);
        outState.putSerializable("PilotoftheWeekData",this.pilotoftheWeekData);
    }

    /**
     * called once a interaction is detected
     * @param view the current view
     */
    public void onClick(View view) {
        if (view == expandTutorialIcon){
            collapseExpandConstraint(expandTutorial,expandTutorialIcon);
        }
    }

    /**
     * This method creates an expandable Constraintlayout
     *
     * @param expandItems the items that need to be expanded
     * @param arrowButton the touchable button (Must be seen at all times)
     */
    void collapseExpandConstraint(ConstraintLayout expandItems, ImageButton arrowButton) {
        if (expandItems.getVisibility() == View.GONE) {
            //expand
            expandItems.setVisibility(View.VISIBLE);
            arrowButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            //collapse
            expandItems.setVisibility(View.GONE);
            arrowButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
    }
    //--------------------------------------- Network Methods --------------------------------------

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
        switch (requestIdentificationTag)
        {
            case DashboardRequest.FLIGHT_DATA_TODAY_TAG:
                this.todayFlightData.clear();
                this.todayFlightData.addAll(results);

                this.processTodayFlightData(results);

                //receive the next data
                getlongestFlight();
                break;
            case DashboardRequest.LONGEST_FLIGHT_TAG:
                this.longestFlightData.clear();
                this.longestFlightData.addAll(results);

                this.processLongestFlightData(results);

                //receive the next data
                getFlightDataBefore();
                break;
            case DashboardRequest.FLIGHT_DATA_BEFORE_TAG:
                this.pastFlightData.clear();
                this.pastFlightData.addAll(results);

                this.processPastFlightData(results);

                getFlightAfter();
                break;
            case DashboardRequest.FLIGHT_DATA_AFTER_TAG:
                this.plannedFlightData.clear();
                this.plannedFlightData.addAll(results);
                this.processPlannedFlightData(results);

                getFlightData();
                break;
            case DashboardRequest.FLIGHT_DATA_TAG:
                this.flightData.clear();
                this.flightData.addAll(results);
                this.processFlightData(this.flightData);

                getMostUsedDrones();
                break;
            case DashboardRequest.MOST_USED_DRONES_TAG:
                this.mostUsedDronesData.clear();
                this.mostUsedDronesData.addAll(results);
                this.processMostUsedDronesData(results);
                getflightsWithoutDrones();
                break;
            case DashboardRequest.FLIGHTS_WITHOUT_DRONES_TAG:
                this.flightsWithoutDronesData.clear();
                this.flightsWithoutDronesData.addAll(results);
                this.progressBarDashboard.setVisibility(View.GONE);
                standartView.setClickable(true);
                swipeRefreshLayout.setEnabled(true);
                processFlightWithoutDronesData(results);
                getPilotOfTheWeek();
                break;
            case DashboardRequest.PILOT_OF_THE_WEEK_TAG:
                this.pilotoftheWeekData.clear();
                this.pilotoftheWeekData.addAll(results);
                processPilotOfTheWeek(results);
                break;
        }
    }

    private void processPilotOfTheWeek(List<Map<String, String>> results){
        for (Map<String, String> row : results) {
            if (results.isEmpty()) {
                //disable if the List is empty!
                this.pilotoftheWeek.setVisibility(View.GONE);
            }
            createflightswithoutDronesTable(pilotOfTheWeekTable, row.get("vorname") + " " + row.get("nachname"));
        }
    }
    /**
     * processes the data for the most used drones
     * @param results the results from the network request
     */
    private void processMostUsedDronesData(List<Map<String, String>> results) {
        if (results != null) {
            if(results.isEmpty()){
                //disable if the List is empty!
                this.pieChart.setVisibility(View.GONE);
            }
            for (Map<String, String> row : results) {
                //add pie data
                PieEntry pieEntry = new PieEntry(Float.parseFloat(row.get("total")), row.get("drohnen_modell"));
                this.entries.add(pieEntry);
            }
            //create data sets
            PieDataSet famousDrones = new PieDataSet(entries,"");
            final int[] DRONELOG_COLORS = {
                    Color.rgb(65, 141, 170), Color.rgb(3, 81, 109), Color.rgb(182, 232, 251),
                    Color.rgb(68, 202, 252), Color.rgb(0, 150, 255)
            };
            famousDrones.setColors(DRONELOG_COLORS);
            PieData data = new PieData(famousDrones);
            chart.setData(data);
            chart.notifyDataSetChanged();
            chart.invalidate(); // refresh
            chart.setEntryLabelColor(000000);
            chart.setHoleRadius(30);
            Description description = new Description();
            description.setText("");
            chart.setDescription(description);
            return;
        }
        //create data sets
        PieDataSet famousDrones = new PieDataSet(entries, "Beliebteste Drohnen");
        famousDrones.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(famousDrones);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh
    }

    /**
     * this method processes the flight data
     * @param results the result from the network request
     */
    private void processFlightData(ArrayList<Map<String, String>> results) {
        for (Map<String, String> row : results) {
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(row.get("breitengrad")),
                    Double.parseDouble(row.get("laengengrad")));

            Marker startMarker = new Marker(mapDashboard);
            startMarker.setPosition(geoPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            Drawable customPointer;
            if(Flights_fragment.isFlightFromTheFuture(row.get("flugdatum"))){
                customPointer = getResources().getDrawable(R.mipmap.dronelogicon_blue);
            } else {
                customPointer = getResources().getDrawable(R.mipmap.dronelogicon);
            }
            startMarker.setIcon(customPointer);
            mapDashboard.getOverlays().add(startMarker);


        }
    }

    /**
     * this method processes the data from the network request
     * @param results the data from the network request
     */
    private void processPlannedFlightData(List<Map<String, String>> results) {
        if (results != null) {
            //disable the element if it is empty!
            if(results.isEmpty()){
                this.plannedFlights.setVisibility(View.GONE);
            }
            for (Map<String, String> row : results) {
                createplannedFlightTable(plannedFlightTable, row.get("einsatzort_name"), row.get("flugdatum"), row.get("drohnen_modell"));
            }
        }
    }

    /**
     * this network request processes the past flight data
     * @param results the data from the Network
     */
    private void processPastFlightData(List<Map<String, String>> results) {
        if (results != null) {
            if(results.isEmpty()){
                //disable if the List is empty!
                this.pastFlights.setVisibility(View.GONE);
            }
            for (Map<String, String> row : results) {
                createPastFlightTable(pastFlightTable, row.get("einsatzort_name"), row.get("flugdatum"), row.get("drohnen_modell"));
            }
        }
    }

    /**
     * this method processes the longest flight data
     * @param results the data from the Network
     */
    private void processLongestFlightData(List<Map<String, String>> results) {
        if (results != null) {
            //disable the element if it is empty!
            if(results.isEmpty()){
                this.longestFlightTime.setVisibility(View.GONE);
            }
            for (Map<String, String> row : results) {
                createLongestFlightTable(longestFlightTimeTable, row.get("count"), row.get("flugdauer"), row.get("sum"), row.get("vorname"));
            }
        }
    }

    /**
     * this method processes the flight today data
     * @param results the data from the Network
     */
    private void processTodayFlightData(List<Map<String, String>> results) {
        if (results != null) {
            //disable the element if it is empty!
            if(results.isEmpty()){
                this.flightsToday.setVisibility(View.GONE);
            }

            for (Map<String, String> row : results) {
                createFlightTodayTable(flightsTodayTable,
                        row.get("einsatzbeginn") + " - " + row.get("einsatzende"),
                        row.get("einsatzort_name"));

            }
        }
    }

    /**
     * this method processes the longest flight data
     * @param results the data from the Network
     */
    private void processFlightWithoutDronesData(List<Map<String, String>> results) {
        if (results != null) {
            //disable the element if it is empty!
            if(results.isEmpty()){
                this.flightswithoutDrones.setVisibility(View.GONE);
            }
            for (Map<String, String> row : results) {
                createflightswithoutDronesTable(flightswithoutDronesTable, row.get("flugbezeichnung"));
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
        standartView.setClickable(true);
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
        this.progressBarDashboard.setVisibility(View.GONE);
        standartView.setClickable(true);
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
        this.progressBarDashboard.setVisibility(View.GONE);
        standartView.setClickable(true);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    /**
     * this request mehtod gets the flight data for thoday
     */
    private void getFlightDataToday(){
        try {
            this.progressBarDashboard.setVisibility(View.VISIBLE);
            DashboardRequest flightsTodayRequest = new DashboardRequest(this.activity.getApplicationContext());
            flightsTodayRequest.flightDataToday();
            HSDroneLogNetwork.networkInstance().startRequest(this, flightsTodayRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlightDataToday();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method gets the longest flight data
     */
    private void getlongestFlight(){
        try {
            DashboardRequest longestFlightRequest = new DashboardRequest(this.activity.getApplicationContext());
            longestFlightRequest.longestFlight();
            HSDroneLogNetwork.networkInstance().startRequest(this, longestFlightRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getlongestFlight();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method gets the flight data before today
     */
    private void getFlightDataBefore(){
        try {
            DashboardRequest pastFlightRequest = new DashboardRequest(this.activity.getApplicationContext());
            pastFlightRequest.flightDataBefore();
            HSDroneLogNetwork.networkInstance().startRequest(this, pastFlightRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlightDataBefore();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method gets the flight data after today
     */
    private void getFlightAfter(){
        try {
            DashboardRequest plannedFlightRequest = new DashboardRequest(this.activity.getApplicationContext());
            plannedFlightRequest.flightDataAfter();
            HSDroneLogNetwork.networkInstance().startRequest(this, plannedFlightRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlightAfter();
                        }
                    });

            snackbar.show();
        }
    }

    private void getPilotOfTheWeek(){
        try {
            DashboardRequest pilotofTheWeekRequest = new DashboardRequest(this.activity.getApplicationContext());
            pilotofTheWeekRequest.pilotOfTheWeek();
            HSDroneLogNetwork.networkInstance().startRequest(this, pilotofTheWeekRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getPilotOfTheWeek();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this mehtod gets the flight data
     */
    private void getFlightData(){
        try {
            DashboardRequest pilotOfTheWeekRequest = new DashboardRequest(this.activity.getApplicationContext());
            pilotOfTheWeekRequest.flightData();
            HSDroneLogNetwork.networkInstance().startRequest(this, pilotOfTheWeekRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlightData();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method gets the most used Drones
     */
    private void getMostUsedDrones(){
        try {
            DashboardRequest chartRequest = new DashboardRequest(this.activity.getApplicationContext());
            chartRequest.mostUsedDrones();
            HSDroneLogNetwork.networkInstance().startRequest(this, chartRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMostUsedDrones();
                        }
                    });

            snackbar.show();
        }
    }


    /**
     * this method gets the most used Drones
     */
    private void getflightsWithoutDrones(){
        try {
            DashboardRequest flightsWithoutDronesRequest = new DashboardRequest(this.activity.getApplicationContext());
            flightsWithoutDronesRequest.flightsWithoutDrones();
            HSDroneLogNetwork.networkInstance().startRequest(this, flightsWithoutDronesRequest);
        } catch (IllegalStateException e) {
            this.progressBarDashboard.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getflightsWithoutDrones();
                        }
                    });

            snackbar.show();
        }
    }
    //--------------------------------- Edit XML Methods -------------------------------------------

    /**
     * this method creates a new flight today table row
     * @param tableLayout the table layout to use
     * @param time the time to use
     * @param location the location to use
     */
    private void createFlightTodayTable(TableLayout tableLayout,String time, String location){
        TableRow tr = new TableRow(this.activity);
        tr.setPaddingRelative(0, 25, 0, 25);

        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(this.resources.getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));

        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, time);
        tr.addView(tv1);

        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, location);
        tr.addView(tv2);

        tableLayout.addView(tr);
    }

    /**
     * this mehtod creates the logntest flight table
     * @param tableLayout the layout that is used
     */
    private void createLongestFlightTable(TableLayout tableLayout,String count, String flugdauer, String sum, String name){
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(this.resources.getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));

        TableRow tr = new TableRow(this.activity);
        tr.setPaddingRelative(0, 25, 0, 25);
        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, name);
        tr.addView(tv1);
        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, "Gesamte Flugzeit: "+ Integer.parseInt(sum)/60 + " Minuten");
        tr.addView(tv2);
        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3, "In " + count + " Flügen");
        tr.addView(tv3);

        tableLayout.addView(tr);
    }

    /**
     * this method creates a past flight table
     */
    private void createPastFlightTable(TableLayout tableLayout,String einsatzort_name, String flugdatum, String drohnen_modell){
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(this.resources.getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));

        TableRow tr = new TableRow(this.activity);
        tr.setPaddingRelative(0, 25, 0, 25);
        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, einsatzort_name);
        tr.addView(tv1);
        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2,drohnen_modell);
        tr.addView(tv2);
        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3,flugdatum);
        tr.addView(tv3);

        tableLayout.addView(tr);
    }

    /**
     * this mehtod creates a new planned flight table
     */
    private void createplannedFlightTable(TableLayout tableLayout,String einsatzort_name, String flugdatum, String drohnen_modell){
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(this.resources.getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));

        TableRow tr = new TableRow(this.activity);
        tr.setPaddingRelative(0, 25, 0, 25);
        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, einsatzort_name);
        tr.addView(tv1);
        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2,drohnen_modell);
        tr.addView(tv2);
        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3,flugdatum);
        tr.addView(tv3);
        tableLayout.addView(tr);
    }

    /**
     * this mehtod creates a new planned flight table
     */
    private void createflightswithoutDronesTable(TableLayout tableLayout,String einsatzort_name){
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(this.resources.getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));

        TableRow tr = new TableRow(this.activity);
        tr.setPaddingRelative(0, 25, 0, 25);
        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, einsatzort_name);
        tr.addView(tv1);
        tableLayout.addView(tr);
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
        tv.setTextColor(this.resources.getColor(R.color.matteBlack));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }



}
