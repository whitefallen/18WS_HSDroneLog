package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.checklist_view;


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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.OnFragmentSwitchListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Checklist_fragment extends Fragment implements NetworkListener, View.OnClickListener {
    //------ Layouts ------
    private TableLayout checklistTableLayout;
    //------ Buttons ------
    private FloatingActionButton faButtonChecklistCreate;
    //------ IV -----------
    private String pilotId;
    private String roleBit;
    private ArrayList<Map<String, String>> checklistData;

    private Activity activity;
    private OnFragmentSwitchListener callback;

    private ProgressBar progressBarChecklistFragment;

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


    public static final Checklist_fragment newInstance(String pilot_id, String role_bit, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        Checklist_fragment fragment = new Checklist_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(2);
        bundle.putString("pilot_id", pilot_id);
        bundle.putString("role_bit", role_bit);
        fragment.setArguments(bundle);

        return fragment;
    }

    public Checklist_fragment() {
        this.checklistData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get parameters and initialize values
        Bundle arguments = getArguments();
        this.pilotId = arguments.getString("pilot_id");
        this.roleBit = arguments.getString("role_bit");
        this.activity = getActivity();


        // Inflate the layout for this fragment
        View checklistView = inflater.inflate(R.layout.fragment_checklist_fragment, container, false);
        this.progressBarChecklistFragment=checklistView.findViewById(R.id.progressBarChecklistFragment);
        this.progressBarChecklistFragment.setVisibility(View.GONE);
        //get scroll view to set listener
        ScrollView scrollView = checklistView.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener( (scrollview, x, y, oldx, oldy) -> {
            if (!scrollView.canScrollVertically(1)) {
                Log.d("SCROLLEN", "bottom");
                if (!HSDroneLogNetwork.networkInstance().isRequesting() && this.checklistData.size() % 20 == 0) {
                    getallChecklists();
                }
            }
        });

        checklistTableLayout = (TableLayout) checklistView.findViewById(R.id.checklistTableLayout);

        //FloatingActionButton
        this.faButtonChecklistCreate = checklistView.findViewById(R.id.fabButtonAddChecklist);
        this.faButtonChecklistCreate.setOnClickListener(this);

        //restore saved instance state if one exists
        if (savedInstanceState != null)
        {
            this.checklistData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("ChecklistData");
            this.processChecklistData(this.checklistData);
        } else {
            getallChecklists();
        }

        return checklistView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ChecklistData", this.checklistData);
    }

    //--------------------------- Network Methods --------------------------------------------------
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag) {
        if (results != null) {
            this.progressBarChecklistFragment.setVisibility(View.GONE);
            this.checklistData.addAll(results);
            Log.d("checklistData Entrys", this.checklistData.size() + "");

            processChecklistData(results);
        }
    }

    private void processChecklistData(List<Map<String, String>> results) {
        for (Map<String, String> row : results) {
            createAccuTableRow(row.get("bezeichnung"),
                    row.get("checkliste_name_id"),
                    row.get("erklaerung"));
        }
    }

    @Override
    public void sessionHasExpired(String message, String requestIdentificationTag) {
        Intent i = this.getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( this.getActivity().getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void requestWasNotSuccessful(String message, String requestIdentificationTag) {
        this.progressBarChecklistFragment.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
                .show();
    }

    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        this.progressBarChecklistFragment.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    private void getallChecklists(){
        try {
            this.progressBarChecklistFragment.setVisibility(View.VISIBLE);
            ChecklistRequest checklistRequest = new ChecklistRequest(this.activity.getApplicationContext());
            checklistRequest.allChecklists(this.checklistData.size());
            HSDroneLogNetwork.networkInstance().startRequest(this, checklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistFragment.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getallChecklists();
                        }
                    });

            snackbar.show();
        }
    }


    //-------------------------- OnClick ------------------------

    @Override
    public void onClick(View v) {
        if(v.equals(faButtonChecklistCreate)){
            Intent droneViewActivity = new Intent(this.activity, Checklist_view_activity.class);
            droneViewActivity.putExtra("viewOrCreate", "create");
            droneViewActivity.putExtra("role", this.roleBit);
            droneViewActivity.putExtra("pilotid", this.pilotId);
            droneViewActivity.putExtra("roleBit",this.roleBit);
            startActivityForResult(droneViewActivity,9);
        }else {
            //must be a press on the table
            int rowId = v.getId();
            Intent droneViewActivity = new Intent(this.activity, Checklist_view_activity.class);
            droneViewActivity.putExtra("checklistid", Integer.toString(rowId));
            droneViewActivity.putExtra("viewOrCreate", "view");
            droneViewActivity.putExtra("pilotid", this.pilotId);
            droneViewActivity.putExtra("roleBit",this.roleBit);
            startActivityForResult(droneViewActivity,9);
        }
    }

    //---------------------------- Custom Table Methods --------------------------------------------
    private void createAccuTableRow(String bezeichnung, String checkliste_name_id, String erklaerung){
        TableRow trAccumulator = new TableRow(this.activity);
        trAccumulator.setPaddingRelative(0, 25, 0, 25);
        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, bezeichnung);
        trAccumulator.addView(tv1);

        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, erklaerung);
        trAccumulator.addView(tv2);

        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3, getResources().getString(R.string.editAccuTable));
        tv3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        trAccumulator.addView(tv3);
        trAccumulator.setOnClickListener(this);

        //set the row id
        trAccumulator.setId(Integer.parseInt(checkliste_name_id));
        checklistTableLayout.addView(trAccumulator);
        //set the space between
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        checklistTableLayout.addView((space));
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


}
