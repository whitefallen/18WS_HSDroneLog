package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this class handles the upload of a flightlog
 * @author Henrik Kother
 */
public class Flightlog_upload_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {
    private ViewPager viewPager;
    private int[] layoutFlightlog;
    private ViewPagerAdapter viewPagerAdapter;
    private Button nextButton;
    private Button activeButton;
    private int pagePosition;
    private Uri selectedDroneFile;
    private ImageButton backbuttonLog;
    private int intentId;

    private ProgressBar progressBarFlightlog;

    //------------------------- Startup ---------------------------------------------

    /**
     * this method is called once the upload activity is created
     * @param savedInstanceState the old saved instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightlog_upload_activity);


        progressBarFlightlog = findViewById(R.id.progressBarFlightlog);
        Bundle extras = getIntent().getExtras();
        //get the id of the flight
        this.intentId = Integer.parseInt(extras.getString("id"));
        //link viewPager
        this.viewPager=findViewById(R.id.viewPager);

        //add all items to the flight log layout
        layoutFlightlog=new int[]{
                R.layout.flightlogslide_layout1,
                R.layout.flightlogslide_layout2,
                R.layout.flightlogslide_layout3
        };

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        this.activeButton=findViewById(R.id.activeButton);
        this.activeButton.setOnClickListener(this);

        this.backbuttonLog=findViewById(R.id.backbuttonLog);
        this.backbuttonLog.setOnClickListener(this);
        nextButton=findViewById(R.id.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);
                if (current < layoutFlightlog.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }else{
                    finishthis();
                }
            }
        });
    }

    /**
     * this finishes this activity
     */
    private void finishthis(){this.finish();}

    /**
     * this gets the current item
     */
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            pagePosition=position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * this is called when a user interaction is registered
     * @param v the clicked view
     */
    @Override
    public void onClick(View v) {
        if(v.equals(activeButton)&&pagePosition==1){
            Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
            openURL.setData(Uri.parse("https://www.phantomhelp.com/LogViewer/Upload/"));
            startActivity(openURL);
        }else if(v.equals(activeButton)&&pagePosition==2){
            //open dialog
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Wählen sie den Download aus"), 9);
            //send log after Result!(onActivityResult)
        }else if(v.equals(backbuttonLog)){
            this.finish();
        }
    }

    /**
     * the activity result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 9 && resultCode == RESULT_OK) {
            selectedDroneFile = data.getData(); //The uri with the location of the file
            uploadLog();
        }
    }

    /**
     * this uploads the log to the network
     */
    private void uploadLog(){
        try {
            FlightRequest flightRequest = new FlightRequest(getApplicationContext());
            flightRequest.uploadLog( this.intentId, getContentResolver().openInputStream(selectedDroneFile));
            HSDroneLogNetwork.networkInstance().startRequest(this, flightRequest);
            progressBarFlightlog.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            progressBarFlightlog.setVisibility(View.GONE);
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadLog();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this class is an adapter that enables the side slider
     * @author Henrik Kother
     */
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        /**
         * the constructor
         */
        public ViewPagerAdapter() {
        }

        /**
         * this creates an instance of the item
         * @param container the container of the itme
         * @param position the position of the item
         * @return an object of the item
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layoutFlightlog[position], container, false);
            container.addView(view);

            return view;
        }

        /**
         * @return the length of the layout
         */
        @Override
        public int getCount() {
            return layoutFlightlog.length;
        }

        /**
         * gives back if the view is from object
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        /**
         * this removes the item from the container
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

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
        if(requestIdentificationTag.equals(FlightRequest.UPLOAD_LOG_TAG)){
            this.finish();
        }
        //set a custom snackbar to show the message
        Snackbar.make(findViewById(android.R.id.content), "Hochladen erfolgreich!", LENGTH_LONG)
                .show();
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
        progressBarFlightlog.setVisibility(View.GONE);
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
        progressBarFlightlog.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }
}
