package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.user_view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.OnFragmentSwitchListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.UserRequest;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this class handles the user fragment
 * @author Henrik Kother
 */
public class User_fragment extends Fragment implements View.OnClickListener, NetworkListener {

    private TableLayout userTableLayout;
    private FloatingActionButton faButtonUserCreate;
    private String pilotId;
    private String roleBit;
    private ArrayList<Map<String, String>> userData;

    private Activity activity;
    private OnFragmentSwitchListener callback;
    private ProgressBar progressBarUserFragment;

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
     * this is called to create a new instance of the User fragment
     * @param pilot_id the id of the pilot
     * @param role_bit the role bit of the pilot
     * @param onFragmentSwitchListener the listener
     * @return the instance
     */
    public static final User_fragment newInstance(String pilot_id, String role_bit, OnFragmentSwitchListener onFragmentSwitchListener)
    {
        User_fragment fragment = new User_fragment();

        fragment.callback = onFragmentSwitchListener;
        Bundle bundle = new Bundle(1);
        bundle.putString("pilot_id", pilot_id);
        bundle.putString("role_bit", role_bit);
        fragment.setArguments(bundle);

        return fragment;
    }

    //---------------------------------- Constructor -----------------------------------------------

    /**
     * constructor of the user fragment
     */
    @SuppressLint("ValidFragment")
    public User_fragment(){
        this.userData = new ArrayList<>();
    }

    //---------------------------------- On create methods -----------------------------------------

    /**
     * is called once the view is created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get parameters and initialize values
        Bundle arguments = getArguments();
        Log.d("Arguments", (arguments == null) + "");
        this.pilotId = arguments.getString("pilot_id");
        this.roleBit = arguments.getString("role_bit");
        this.activity = getActivity();
        // Inflate the layout for this fragment
        View userView = inflater.inflate(R.layout.fragment_user_fragment, container, false);
        this.faButtonUserCreate = userView.findViewById(R.id.fabButtonAddUser);
        this.faButtonUserCreate.setOnClickListener(this);
        this.userTableLayout = userView.findViewById(R.id.userTableLayout);
        this.progressBarUserFragment=userView.findViewById(R.id.progressBarUserFragment);
        this.progressBarUserFragment.setVisibility(View.GONE);
        //restore saved instance state if one exists
        if (savedInstanceState != null)
        {
            this.userData = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("UserData");
            for (Map<String, String> row : this.userData) {
                createUserTable(this.userTableLayout,
                        row.get("vorname"),
                        row.get("nachname"),
                        row.get("email_adresse"),
                        row.get("pilot_id"),
                        row.get("profilbild"),
                        row.get("rolle"),
                        row.get("aktivitaet"));
            }
        } else {
            getAllUserData();
        }

        //return the layout for this fragment
        return userView;
    }

    /**
     * this method gets all user data from the network
     */
    private void getAllUserData(){
        try {
        progressBarUserFragment.setVisibility(View.VISIBLE);
        UserRequest allUsersRequest = new UserRequest(this.activity.getApplicationContext());
        allUsersRequest.allUserData();
        HSDroneLogNetwork.networkInstance().startRequest(this, allUsersRequest);
    } catch (IllegalStateException e) {
        progressBarUserFragment.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllUserData();
                        }
                    });

            snackbar.show();
    }}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("UserData", this.userData);
    }

    /**
     * this is called once a user action is detected
     * @param view the selected view
     */
    @Override
    public void onClick(View view) {
        if(view.equals(faButtonUserCreate)){
            //create a new User
            Intent userViewActivity = new Intent(this.activity, User_view_activity.class);
            userViewActivity.putExtra("viewOrCreate", "create");
            userViewActivity.putExtra("pilot_id", this.pilotId);
            userViewActivity.putExtra("roleBit",this.roleBit);
            startActivityForResult(userViewActivity,9);
        } else {
            //View a User and maybe edit it
            int rowId = view.getId();
            Intent userViewActivity = new Intent(this.activity, User_view_activity.class);
            userViewActivity.putExtra("id", Integer.toString(rowId));
            userViewActivity.putExtra("viewOrCreate", "view");
            userViewActivity.putExtra("roleBit",this.roleBit);
            userViewActivity.putExtra("pilot_id", this.pilotId);
            startActivityForResult(userViewActivity,9);
        }
    }

    //------------------------------- Network Methods ---------------------------------------------

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
            progressBarUserFragment.setVisibility(View.GONE);
            this.userData.addAll(results);
            for (Map<String, String> row : results) {
                createUserTable(
                        this.userTableLayout,
                        row.get("vorname"),
                        row.get("nachname"),
                        row.get("email_adresse"),
                        row.get("pilot_id"),
                        row.get("profilbild"),
                        row.get("rolle"),
                        row.get("aktivitaet"));
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
        progressBarUserFragment.setVisibility(View.GONE);
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
        progressBarUserFragment.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    //------------------------------- Create Table Methods -----------------------------------------

    /**
     * to create a user table row inside the tablelayout
     * @param tableLayout the table layout that is used
     * @param name the name of the user
     * @param surname the surname of the user
     * @param email the email of the user
     * @param userID the user id of the user
     * @param pictureLink the picture link
     * @param role the role of the user
     * @param activity the activity
     */
    private void createUserTable(TableLayout tableLayout, String name, String surname, String email, String userID,String pictureLink, String role,String activity) {
        TableRow trDroneData = new TableRow(this.activity);
        trDroneData.setPaddingRelative(0, 25, 0, 25);

        ImageView iv1 = new ImageView(this.activity);
        createNewTableView(iv1, pictureLink);
        trDroneData.addView(iv1);

        TextView tv1 = new TextView(this.activity);
        createNewTableView(tv1, name);
        trDroneData.addView(tv1);

        TextView tv2 = new TextView(this.activity);
        createNewTableView(tv2, surname);
        trDroneData.addView(tv2);

        TextView tv3 = new TextView(this.activity);
        createNewTableView(tv3, email);
        trDroneData.addView(tv3);

        TextView tv4 = new TextView(this.activity);
        createNewTableView(tv4, "Details >");
        tv4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        trDroneData.addView(tv4);
        if(role.equals("1")) {
            trDroneData.setBackgroundColor(getResources().getColor(R.color.lightBlue));
        }
        if(activity.equals("0")){
            trDroneData.setBackgroundColor(getResources().getColor(R.color.inactiv));
        }

        //If the user wants to see a specific flight
        trDroneData.setId(Integer.parseInt(userID));
        trDroneData.setOnClickListener(this);
        //add this to the Gui layout
        tableLayout.addView(trDroneData);

        //Add space row
        TableRow space = new TableRow(this.activity);
        space.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView spaceText = new TextView(this.activity);
        createNewTableView(spaceText, " ");
        space.setMinimumHeight(1);
        tableLayout.addView((space));
    }

    /**
     * this creates a new tablerow
     */
    private void createNewTableView(TextView tv, String text) {
        tv.setText(text);
        tv.setTextSize(15);
        tv.setTextColor(getResources().getColor(R.color.matteBlack));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    /**
     * this cereates a new tablerow for pictures
     */
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
