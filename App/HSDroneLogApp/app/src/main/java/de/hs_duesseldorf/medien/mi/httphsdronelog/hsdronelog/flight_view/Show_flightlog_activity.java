package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this activity shows the flightlog
 * @author Henrik Kother
 */
public class Show_flightlog_activity extends AppCompatActivity implements NetworkListener, View.OnClickListener {

    private TableLayout showFlightlogTable;
    private int intentId;
    private int amountOfLogLines;
    private ImageButton backbuttonShowFlightlog;

    /**
     * this is called on creation of the activity
     * @param savedInstanceState the old saved insatnce state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flightlog_activity);

        this.amountOfLogLines = 0;

        //Get all extras
        Bundle extras = getIntent().getExtras();
        this.intentId = Integer.parseInt(extras.getString("id"));
        this.showFlightlogTable=findViewById(R.id.showFlightlogTable);

        this.backbuttonShowFlightlog = findViewById(R.id.backbuttonShowFlightlog);
        this.backbuttonShowFlightlog.setOnClickListener(this);

        //get scroll view to set listener
        ScrollView scrollView = findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener( (scrollview, x, y, oldx, oldy) -> {
            if (!scrollView.canScrollVertically(1)) {
                Log.d("SCROLLEN", "bottom");
                if (!HSDroneLogNetwork.networkInstance().isRequesting() && this.amountOfLogLines % 50 == 0) {
                    getFlightlogData(this.amountOfLogLines);
                }
            }
        });

        //NetworkRequest
        getFlightlogData(0);

    }

    /**
     * this gets the flightlog data from the data base
     * @param offset
     */
    private void getFlightlogData(int offset){
        try {
            FlightRequest flightRequest = new FlightRequest(this.getApplicationContext());
            flightRequest.showFlightLog(intentId, offset);
            HSDroneLogNetwork.networkInstance().startRequest(this, flightRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlightlogData(amountOfLogLines);
                        }
                    });
            snackbar.show();
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
        if (results != null)
        {
            this.amountOfLogLines += results.size();
            for (Map<String, String> row : results) {
                //Map<String, String> row = results.get(0);
                TableRow tableRow = new TableRow(this);
                tableRow.setPaddingRelative(0, 25, 0, 25);

                TextView latitude = new TextView(this);
                latitude.setText(row.get("latitude"));
                tableRow.addView(latitude);

                TextView longitude = new TextView(this);
                longitude.setText(row.get("longitude"));
                tableRow.addView(longitude);

                TextView altitude = new TextView(this);
                altitude.setText(row.get("altitude"));
                tableRow.addView(altitude);

                this.showFlightlogTable.addView(tableRow);
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

    @Override
    public void onClick(View v) {
        if(v.equals(backbuttonShowFlightlog)){
            this.finish();
        }
    }
}
