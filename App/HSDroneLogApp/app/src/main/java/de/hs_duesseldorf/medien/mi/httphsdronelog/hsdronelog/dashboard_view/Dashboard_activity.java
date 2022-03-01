package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.accumulator_view.Accumulator_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.checklist_view.Checklist_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.drone_view.Drone_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view.Flights_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.UserRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.user_view.User_fragment;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.user_view.User_view_activity;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * The class for the Dashboard Activity
 *
 * @author Henrik Kother
 */
public class Dashboard_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkListener, OnFragmentSwitchListener, View.OnClickListener {

    private String name;
    private String email;
    private String profileImagePath;
    private MenuItem checkedMenuItem;
    private MenuItem user;
    private Menu menu;
    private Bundle extras;
    private ImageView profileImage;

    /**
     * the constructor of the Dashboard activity
     */
    public Dashboard_activity()
    {
        Log.d("Dashboard activity", "CONSTRUCTOR");
    }

    /**
     * This method is called once the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Dashboard Activity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get all extras from the login activity
        this.extras = getIntent().getExtras();

        //Import data from Login Page:
        Intent intent = getIntent();
        getLoginData(intent);

        //Set the Variables in the Navigation Bar
        NavigationView dashboardNavigation = (NavigationView) findViewById(R.id.nav_view);
        View dashboardHeader = navigationView.getHeaderView(0);
        TextView displayName = (TextView) dashboardHeader.findViewById(R.id.welcomeTextName);
        displayName.setText(name);
        TextView displayEmail = (TextView) dashboardHeader.findViewById(R.id.emailTextName);
        displayEmail.setText(email);
        this.profileImage = dashboardHeader.findViewById(R.id.profileImage);
        this.profileImage.setOnClickListener(this);
        try {
            FileInputStream in = new FileInputStream(
                    this.getApplicationContext().getFilesDir().getPath() + this.profileImagePath);

            profileImage.setImageBitmap(BitmapFactory.decodeStream(in));
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //disable user and admin settings for nomal users
        this.menu = navigationView.getMenu();
        this.user = this.menu.findItem(R.id.userItemMenu);
        if(extras.getString("rolle").equals("0")){
            user.setVisible(false);

        }

        //check restore data is available
        if (savedInstanceState != null)
        {
            Fragment current = (Fragment) getSupportFragmentManager().findFragmentByTag("DashboardActivityContent");

            FragmentTransaction flightsFragment = getSupportFragmentManager().beginTransaction();
            flightsFragment.replace(R.id.framelayoutMain, current, "DashboardActivityContent");
            flightsFragment.commit();

            this.checkedMenuItem = this.menu.findItem(savedInstanceState.getInt("MenuItemSelectedID"));
            this.setChecked(this.checkedMenuItem);
        }
        else
        {
            //set current active menu item to checked
            this.setChecked(this.menu.findItem(R.id.dasboardButton));

            this.setCurrentFragment(this.checkedMenuItem);
        }
    }

    /**
     * this method is called once the activity is recreated
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("MenuItemSelectedID", this.checkedMenuItem.getItemId());
    }

    /**
     * Gets the intent from the login page with the transferred Data
     *
     * @param intent The Intent created in the login page
     */
    public void getLoginData(Intent intent) {
        this.name = "Willkommen," + intent.getStringExtra("vorname");
        this.email = "" + intent.getStringExtra("email_adresse");
        this.profileImagePath = "" + intent.getStringExtra("profilbild");
    }

    /**
     * Controls what happens if the user presses the back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //do nothing, else the user would be sent back to the login screen
        }
    }

    //---------------------------- Navigation Drawer Methods ---------------------------------------

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_activity, menu);
        return true;
    }

    /**
     * // Handle action bar item clicks here. The action bar will
     *         // automatically handle clicks on the Home/Up button, so long
     *         // as you specify a parent activity in AndroidManifest.xml.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Start the dashboard-settings activity
        if (id == R.id.action_settings) {
            Intent flightViewActivity = new Intent(this, Settings_dashboard_activity.class);
            flightViewActivity.putExtra("role",this.extras.getString("rolle"));
            startActivity(flightViewActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Colors the selected Item in the Navigation Drawer
     *
     * @param item the Item to be colored in the main color blue
     */
    private void setChecked(MenuItem item) {
        //if there is already a checked item
        if (checkedMenuItem != null) {
            checkedMenuItem.setChecked(false);
        }
        checkedMenuItem = item;
        item.setChecked(true);
    }

    /**
     * Controls what happens if the user presses a button in the NavigationDrawer
     *
     * @param item The pressed item id
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        this.setCurrentFragment(item);

        return true;
    }

    /**
     * Sets the current fragment or switches between fragments
     * @param item The menu item representing the fragment.
     */
    private void setCurrentFragment(MenuItem item)
    {
        int currentFragmentId = item.getItemId();

        switch (currentFragmentId) {
            case R.id.dasboardButton:
                FragmentTransaction dashboardFragment = getSupportFragmentManager().beginTransaction();
                dashboardFragment.replace(R.id.framelayoutMain, Dashboard_fragment.newInstance(
                        this.extras.getString("vorname"),
                        this.extras.getString("nachname"),
                        this.extras.getString("studiengang"),
                        this.extras.getString("rolle"),
                        this.extras.getString("pilot_id")
                        , this), "DashboardActivityContent");
                dashboardFragment.commit();
                setChecked(item);
                break;
            case R.id.flightsButton:
                FragmentTransaction flightsFragment = getSupportFragmentManager().beginTransaction();
                flightsFragment.replace(R.id.framelayoutMain, Flights_fragment.newInstance(
                        this.extras.getString("pilot_id"),
                        this.extras.getString("rolle")
                        , this), "DashboardActivityContent");
                flightsFragment.commit();
                setChecked(item);
                break;
            case R.id.droneButton:
                FragmentTransaction droneFragment = getSupportFragmentManager().beginTransaction();
                droneFragment.replace(R.id.framelayoutMain, Drone_fragment.newInstance(this.extras.getString("rolle"), this)
                        , "DashboardActivityContent");
                droneFragment.commit();
                setChecked(item);
                break;
            case R.id.checklistButton:
                FragmentTransaction checklistFragment = getSupportFragmentManager().beginTransaction();
                checklistFragment.replace(R.id.framelayoutMain, Checklist_fragment.newInstance(this.extras.getString("pilot_id"),
                        this.extras.getString("rolle"), this), "DashboardActivityContent");
                checklistFragment.commit();
                setChecked(item);
                break;
            case R.id.accuButton:
                FragmentTransaction accuFragment = getSupportFragmentManager().beginTransaction();
                accuFragment.replace(R.id.framelayoutMain, Accumulator_fragment.newInstance(
                        this.extras.getString("rolle"),
                        this
                ), "DashboardActivityContent");
                accuFragment.commit();
                setChecked(item);
                break;
            case R.id.userItemMenu:
                FragmentTransaction userFragment = getSupportFragmentManager().beginTransaction();
                userFragment.replace(R.id.framelayoutMain, User_fragment.newInstance(
                        this.extras.getString("pilot_id"),
                        this.extras.getString("rolle")
                        , this), "DashboardActivityContent");
                userFragment.commit();
                setChecked(item);
                break;
            //If the logout button is pressed, go to the Login Screen
            case R.id.logoutButton:
                try{
                    UserRequest logoutRequest = new UserRequest(getApplicationContext());
                    logoutRequest.logout();
                    HSDroneLogNetwork.networkInstance().startRequest(this, logoutRequest);
                } catch (IllegalStateException e) {}
                this.finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        //Logout
        this.finish();
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
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
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
        //set a custom snackbar to show the message
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
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
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    //------------------------- Fragment wants to refresh ------------------------------------------

    /**
     * this method is used for setting the current fragment
     */
    @Override
    public void activityShouldRefresh() {
        setCurrentFragment(this.checkedMenuItem);
    }
    //------------------------- User Interaction ---------------------------------------------------

    /**
     * this method is called on a click on a button
     * @param v the current view
     */
    @Override
    public void onClick(View v) {
        if(v.equals(this.profileImage)){
            Intent userViewActivity = new Intent(this, User_view_activity.class);
            userViewActivity.putExtra("viewOrCreate", "view");
            userViewActivity.putExtra("id", this.extras.getString("pilot_id"));
            userViewActivity.putExtra("roleBit",this.extras.getString("rolle"));
            startActivityForResult(userViewActivity,9);
        }
    }
}
