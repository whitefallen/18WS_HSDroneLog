package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.drone_view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.OnFragmentSwitchListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.DroneRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Drone_fragment extends Fragment implements View.OnClickListener, NetworkListener {
    //IV
    private TableLayout droneTableLayout;
    private FloatingActionButton fabButtonAddDrone;
    private String roleBit;
    /**The data you received over the network*/
    private ArrayList<Map<String, String>> droneData;
    private ProgressBar progressBarDroneFragment;
    private Activity activity;
    private OnFragmentSwitchListener callback;

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

    public static final Drone_fragment newInstance(String role_bit, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        Drone_fragment fragment = new Drone_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(1);
        bundle.putString("role_bit", role_bit);
        fragment.setArguments(bundle);

        return fragment;
    }

    @SuppressLint("ValidFragment")
    public Drone_fragment() {
        this.droneData = new ArrayList<>();
    }


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
        View droneView = inflater.inflate(R.layout.fragment_drone_fragment, container, false);
        this.droneTableLayout = (TableLayout) droneView.findViewById(R.id.droneTableLayout);
        this.fabButtonAddDrone = droneView.findViewById(R.id.fabButtonAddDrone);
        this.fabButtonAddDrone.setOnClickListener(this);
        this.progressBarDroneFragment=droneView.findViewById(R.id.progressBarDroneFragment);
        this.progressBarDroneFragment.setVisibility(View.GONE);
        //get scroll view to set listener
        ScrollView scrollView = droneView.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener( (scrollview, x, y, oldx, oldy) -> {
            if (!scrollView.canScrollVertically(1)) {
                Log.d("SCROLLEN", "bottom");
                if (!HSDroneLogNetwork.networkInstance().isRequesting() && this.droneData.size() % 20 == 0) {
                    getAllDrones();
                }
            }
        });

        //if the user is not an admin
        if(this.roleBit.equals("0")){
            this.fabButtonAddDrone.setVisibility(View.GONE);
        }

        //restore saved instance state if one exists
        if (savedInstanceState != null)
        {
            this.droneData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("DroneData");
            for (Map<String, String> row : this.droneData) {
                createDroneTable(this.droneTableLayout, row.get("bild"), row.get("drohnen_modell"), row.get("drohne_id"));
            }
        } else {
            getAllDrones();
        }

        return droneView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DroneData", this.droneData);
    }

    //------------------------------ Touch Methods -------------------------------------------------
    @Override
    public void onClick(View view) {
        if(view.equals(fabButtonAddDrone)){
            Intent droneViewActivity = new Intent(this.activity, Drone_view_activity.class);
            droneViewActivity.putExtra("viewOrCreate", "create");
            droneViewActivity.putExtra("role", this.roleBit);
            startActivityForResult(droneViewActivity,9);
        }else {
            //must be a press on the table
            int rowId = view.getId();
            Intent droneViewActivity = new Intent(this.activity, Drone_view_activity.class);
            droneViewActivity.putExtra("droneid", Integer.toString(rowId));
            droneViewActivity.putExtra("viewOrCreate", "view");
            droneViewActivity.putExtra("role", this.roleBit);
            startActivityForResult(droneViewActivity,9);
        }
    }

    //------------------------------ Network Methods -----------------------------------------------
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag)
    {
        if (results != null)
        {
            this.progressBarDroneFragment.setVisibility(View.GONE);
            this.droneData.addAll(results);
            Log.d("droneData Entrys", this.droneData.size() + "");
            for (Map<String, String> row : results) {
                createDroneTable(this.droneTableLayout, row.get("bild"), row.get("drohnen_modell"), row.get("drohne_id"));
            }
        }
    }

    @Override
    public void sessionHasExpired(String message, String requestIdentificationTag) {
        this.progressBarDroneFragment.setVisibility(View.GONE);
        Intent i = this.getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( this.getActivity().getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void requestWasNotSuccessful(String message, String requestIdentificationTag) {
        this.progressBarDroneFragment.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
                .show();
    }

    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    private void getAllDrones(){
        try {
            this.progressBarDroneFragment.setVisibility(View.VISIBLE);
            DroneRequest allDronesRequest = new DroneRequest(this.activity.getApplicationContext());
            allDronesRequest.allDrones(this.droneData.size());
            HSDroneLogNetwork.networkInstance().startRequest(this, allDronesRequest);
        } catch (IllegalStateException e) {
            this.progressBarDroneFragment.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllDrones();
                        }
                    });

            snackbar.show();
        }
    }
    //---------------------------- Create DroneTable Methods -----------------------------------
    private void createDroneTable(TableLayout tableLayout, String pictureLink, String droneName, String droneID) {
        //Log.d("TableEntry", "link: " + pictureLink + " name: " + droneName + " id: " + droneID + " Thread: " + Thread.currentThread().getName());

        TableRow trDroneData = new TableRow(this.activity);
        trDroneData.setPaddingRelative(0, 25, 0, 25);

        //TextView tv1 = new TextView(this.activity);
        ImageView im1 = new ImageView(this.activity);
        createNewTableView(im1, pictureLink);
        trDroneData.addView(im1);

        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, droneName);
        trDroneData.addView(tv2);

        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3, "Details >");
        tv3.setTextColor(ContextCompat.getColor(this.activity.getApplicationContext(), R.color.colorPrimaryDark));
        trDroneData.addView(tv3);


        //If the user wants to see a specific flight
        trDroneData.setId(Integer.parseInt(droneID));
        trDroneData.setOnClickListener(this);
        //add this to the Gui layout
        tableLayout.addView(trDroneData);

        //Add space row
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(ContextCompat.getColor(this.activity.getApplicationContext(), R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));
    }

    private void createNewTableView(TextView tv, String text) {
        tv.setText(text);
        tv.setTextSize(15);
        tv.setTextColor(ContextCompat.getColor(this.activity.getApplicationContext(), R.color.matteBlack));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private void createNewTableView(ImageView im, String relativPath) {
        if (relativPath != null)
        {
            try {
                FileInputStream in = new FileInputStream(
                        this.activity.getApplicationContext().getFilesDir().getPath() + relativPath);

                Bitmap img = BitmapFactory.decodeStream(in);
                Matrix matrix = new Matrix();
                float scale = 300f / img.getWidth(); //300 px
                matrix.setScale(scale, scale);

                img = Bitmap.createBitmap(
                        img,
                        0,
                        0,
                        img.getWidth(),
                        img.getHeight(),
                        matrix,
                        false
                );

                im.setImageBitmap(img);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
