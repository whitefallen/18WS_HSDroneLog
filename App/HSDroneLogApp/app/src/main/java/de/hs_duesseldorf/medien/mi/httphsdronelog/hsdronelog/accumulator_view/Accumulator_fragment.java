package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.accumulator_view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.Dashboard_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.OnFragmentSwitchListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.BatteryRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 * @author Henrik Kother
 */
@SuppressLint("ValidFragment")
public class Accumulator_fragment extends Fragment implements NetworkListener, View.OnClickListener {
    //---------XML Items----------
    private TableLayout accuTableLayout;
    private FloatingActionButton faButtonAccuCreate;
    //----------- IV --------------
    private String roleBit;
    private ArrayList<Map<String, String>> accuData;
    private Activity activity;
    private OnFragmentSwitchListener callback;

    private ProgressBar progressBarAccumulatorFragment;

    //-------------------- Manage return values ----------------------------------------------------

    /**
     * Get the result from other activitys
     * This method is part of the Refresh the fragment when its data changed.
     * @param requestCode the code for requesting
     * @param resultCode the result from the activity
     * @param data  the Data
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

    //----------------- Startup methods------------------------------------------------------------

    /**
     * This method creates a new Instance of the Accumulator fragment
     * @param role_bit the Role of the User
     * @param onFragmentSwitchListener Listener for switching fragments
     * @return the fragment
     */
    public static final Accumulator_fragment newInstance(String role_bit, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        Accumulator_fragment fragment = new Accumulator_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(1);
        bundle.putString("role_bit", role_bit);
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Constructor of the Accumulator Fragment
     */
    @SuppressLint("ValidFragment")
    public Accumulator_fragment() {
        this.accuData = new ArrayList<>();
    }

    /**
     * This method is executed during the create Fragment process.
     * @param inflater
     * @param container
     * @param savedInstanceState the sate for recreating the fragment
     * @return the View
     */
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get parameters and initialize values
        Bundle arguments = getArguments();
        //receive the role bit
        this.roleBit = arguments.getString("role_bit");
        this.activity = getActivity();
        // Inflate the layout for this fragment
        View accuView = inflater.inflate(R.layout.fragment_accumulator_fragment, container, false);

        this.progressBarAccumulatorFragment=accuView.findViewById(R.id.progressBarAccumulatorFragment);
        this.progressBarAccumulatorFragment.setVisibility(View.GONE);
        this.accuTableLayout = (TableLayout) accuView.findViewById(R.id.accumulatorTableLayout);
        //link the floating Action Button
        this.faButtonAccuCreate = (FloatingActionButton) accuView.findViewById(R.id.fabButtonAddAccu);
        this.faButtonAccuCreate.setOnClickListener(this);

        //if the user is not an admin
        if(this.roleBit.equals("0")){
            this.faButtonAccuCreate.setVisibility(View.GONE);
        }

        //get scroll view to set listener
        ScrollView scrollView = accuView.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener( (scrollview, x, y, oldx, oldy) -> {
            if (!scrollView.canScrollVertically(1)) {
                Log.d("SCROLLEN", "bottom");
                if (!HSDroneLogNetwork.networkInstance().isRequesting() && this.accuData.size() % 20 == 0) {
                    getallBatteries();
                }
            }
        });

        //restore saved instance state if one exists
        if (savedInstanceState != null) {
            this.accuData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("AccuData");
            for (Map<String, String> row : this.accuData) {
                createAccumulatorTable(accuTableLayout, row.get("bezeichnung"), row.get("anzahl"), row.get("akku_id"));
            }
        } else {
            getallBatteries();
        }

        accuView.setTag("accuFragment");

        return accuView;
    }

    //network

    /**
     * Network request to receve all Batteries!
     */
    private void getallBatteries(){
        try {
            this.progressBarAccumulatorFragment.setVisibility(View.VISIBLE);
            BatteryRequest allBatteriesRequest = new BatteryRequest(this.activity.getApplicationContext());
            allBatteriesRequest.allBatteries(this.accuData.size());
            HSDroneLogNetwork.networkInstance().startRequest(this, allBatteriesRequest);
        } catch (IllegalStateException e) {
            this.progressBarAccumulatorFragment.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getallBatteries();
                        }
                    });

            snackbar.show();
        }
    }
    /**
     * This method is used for the recreating of the Fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("AccuData", this.accuData);
    }
    //--------------------- Create Table Methods --------------------

    /**
     * This Method is used when the Accumulator Table needs a new entry
     * @param tableLayout the tableLayout that is used
     * @param batteryDescription the Description of a battery
     * @param batteryCount the count of the batteries that are the same
     * @param accuId the id of the battery
     */
    private void createAccumulatorTable(TableLayout tableLayout, String batteryDescription, String batteryCount,String accuId) {
        TableRow trBatteryData = new TableRow(this.activity);
        trBatteryData.setPaddingRelative(0, 25, 0, 25);

        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, batteryDescription);
        trBatteryData.addView(tv1);

        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, batteryCount);
        trBatteryData.addView(tv2);

        TextView tv3 = new TextView(this.activity);
        if(this.roleBit.equals("0")){
            createNewTableView(tv3, getResources().getString(R.string.viewAccuTable));
        } else {
            createNewTableView(tv3, getResources().getString(R.string.editAccuTable));
        }

        tv3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        trBatteryData.addView(tv3);
        trBatteryData.setOnClickListener(this);
        trBatteryData.setId(Integer.parseInt(accuId));
        tableLayout.addView(trBatteryData);

        //Add space row
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));
    }

    /**
     * this method is used for creating a new TableView
     * @param tv The Textview
     * @param text the Sting Text
     */
    private void createNewTableView(TextView tv, String text) {
        tv.setText(text);
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.matteBlack));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    //--------------------- Network Methods----------------------

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
            this.progressBarAccumulatorFragment.setVisibility(View.GONE);
            this.accuData.addAll(results);
            Log.d("accuData Entrys", this.accuData.size() + "");
            for (Map<String, String> row : results) {
                createAccumulatorTable(accuTableLayout, row.get("bezeichnung"), row.get("anzahl"), row.get("akku_id"));
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
        this.progressBarAccumulatorFragment.setVisibility(View.GONE);
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
        this.progressBarAccumulatorFragment.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    /**
     * This method is called when a click on the touchscreen is registered
     * @param view the View of the Fragment.
     */
    @Override
    public void onClick(View view) {
        if(view == faButtonAccuCreate){
            //must be a click on the Button
            Intent accumulatorViewActivity = new Intent(this.activity, Accumulator_view_activity.class);
            accumulatorViewActivity.putExtra("viewOrCreate", "create");
            accumulatorViewActivity.putExtra("role", this.roleBit);
            startActivityForResult(accumulatorViewActivity,9);
        }else{
            //must be a click on the table
            Intent accumulatorViewActivity = new Intent(this.activity, Accumulator_view_activity.class);
            accumulatorViewActivity.putExtra("viewOrCreate", "view");
            accumulatorViewActivity.putExtra("role", this.roleBit);
            int rowId = view.getId();
            accumulatorViewActivity.putExtra("id", Integer.toString(rowId));
            startActivityForResult(accumulatorViewActivity,9);
        }
    }

}
