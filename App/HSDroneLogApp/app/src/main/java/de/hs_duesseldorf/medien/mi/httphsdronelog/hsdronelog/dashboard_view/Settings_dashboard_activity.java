package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.IndexRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.MessageRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this class is used for creating the admin dashboard settings
 * @author Henrik Kother
 */
public class Settings_dashboard_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {

    private String roleBit;

    private ImageButton backbuttonSettings;
    private TableLayout dashboardSettingsTableLayout;
    private FloatingActionButton fabButtonConfirmEditSettings;
    private FloatingActionButton fabButtonCancelSettings;
    private FloatingActionButton fabButtonEditSettings;

    //email
    private TextView emailDashboardSetting;
    private TextView passwordDashboardSettings;
    private TextView stmpServerDashboardSettings;
    private TextView portDashboardSettings;

    private Bundle extras;

    //network
    private Map<String,EditText> nameDescriptionMap = new HashMap<>();
    private Iterator<String> descriptionSetIterator;

    /**
     * this method is called once this activty is created
     * @param savedInstanceState the saved old state
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_dashboard_activity);

        this.extras = getIntent().getExtras();
        this.roleBit = this.extras.getString("role");


        backbuttonSettings = findViewById(R.id.backbuttonSettings);
        backbuttonSettings.setOnClickListener(this);
        fabButtonConfirmEditSettings=findViewById(R.id.fabButtonConfirmEditSettings);
        fabButtonConfirmEditSettings.setOnClickListener(this);
        fabButtonConfirmEditSettings.setVisibility(View.GONE);
        fabButtonCancelSettings=findViewById(R.id.fabButtonCancelSettings);
        fabButtonCancelSettings.setOnClickListener(this);
        fabButtonCancelSettings.setVisibility(View.GONE);
        fabButtonEditSettings=findViewById(R.id.fabButtonEditSettings);
        fabButtonEditSettings.setOnClickListener(this);

        //EMaIL
        this.emailDashboardSetting=findViewById(R.id.emailDashboardSetting);
        this.stmpServerDashboardSettings=findViewById(R.id.stmpServerDashboardSettings);
        this.portDashboardSettings=findViewById(R.id.portDashboardSettings);
        this.passwordDashboardSettings=findViewById(R.id.passwordDashboardSettings);

        emailDashboardSetting.setEnabled(false);
        stmpServerDashboardSettings.setEnabled(false);
        portDashboardSettings.setEnabled(false);
        passwordDashboardSettings.setEnabled(false);
        //dashboardSettingsTableLayout link
        this.dashboardSettingsTableLayout=findViewById(R.id.dashboardSettingsTableLayout);

        if(this.roleBit.equals("0")){
            fabButtonEditSettings.setVisibility(View.GONE);
            fabButtonCancelSettings.setVisibility(View.GONE);
        }



        //network request
        getAllMessages();
    }
    //-------------------------------- Control user input methods ---------------------------------

    /**
     * this method is called once a user makes an input.
     * @param view the view that is currently active
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        if(view.equals(backbuttonSettings)){
            this.finish();
        }else if(view.equals(fabButtonCancelSettings)){
            this.recreate();//user wants to cancel
        }else if(view.equals(fabButtonConfirmEditSettings)){
            //user wants to save all strings
            Set<String> keys = nameDescriptionMap.keySet();
            this.descriptionSetIterator = keys.iterator();
            if(this.descriptionSetIterator.hasNext()){
                sendEditMessage();
            } else {
                this.descriptionSetIterator = null;
            }
            this.finish();
        }else if(view.equals(fabButtonEditSettings)){
            emailDashboardSetting.setEnabled(true);
            stmpServerDashboardSettings.setEnabled(true);
            portDashboardSettings.setEnabled(true);
            passwordDashboardSettings.setEnabled(true);
            this.fabButtonCancelSettings.setVisibility(View.VISIBLE);
            this.fabButtonConfirmEditSettings.setVisibility(View.VISIBLE);
            this.fabButtonEditSettings.setVisibility(View.GONE);
            //make all EditText editable
            Set<String> keys = nameDescriptionMap.keySet();
            for(String key:keys){
                nameDescriptionMap.get(key).setEnabled(true);
            }
        }
    }
    //--------------------------------- Network methods -------------------------------------------

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
            case MessageRequest.ALL_MESSAGES_TAG:
                for (Map<String, String> row : results) {
                    TableRow tableRow = new TableRow(this);
                    TextView textViewName = new TextView(this);
                    textViewName.setText(row.get("bezeichnung"));
                    tableRow.addView(textViewName);
                    EditText editTextDesc = new EditText(this);
                    editTextDesc.setText(row.get("nachricht"));
                    editTextDesc.setEnabled(false);
                    tableRow.addView(editTextDesc);

                    //Save the values into the map
                    nameDescriptionMap.put(row.get("bezeichnung"), editTextDesc);

                    this.dashboardSettingsTableLayout.addView(tableRow);
                }
                showAdminEmail();
                break;
            case MessageRequest.EDIT_MESSAGE_TAG:
                if(this.descriptionSetIterator.hasNext()){
                    sendEditedMessage();
                } else {
                    this.descriptionSetIterator = null;
                    sendChangedAdminEmail();
                }
                break;
            case IndexRequest.SHOW_ADMIN_EMAIL_TAG:
                for (Map<String, String> row : results) {
                    this.emailDashboardSetting.setText(row.get("email_adresse"));
                    this.passwordDashboardSettings.setText(row.get("passwort"));
                    this.stmpServerDashboardSettings.setText(row.get("smtp_server"));
                    this.portDashboardSettings.setText(row.get("port"));
                }
                break;
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

    /**
     * this method gets all messages
     */
    private void getAllMessages(){
        try {
            MessageRequest messageRequest = new MessageRequest(this.getApplicationContext());
            messageRequest.allMessages();
            HSDroneLogNetwork.networkInstance().startRequest(this, messageRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllMessages();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this method sends the edited method
     */
    private void sendEditMessage(){
        try {
            String key = this.descriptionSetIterator.next();
            MessageRequest messageRequest = new MessageRequest(this.getApplicationContext());
            messageRequest.editMessage(key,nameDescriptionMap.get(key).getText().toString());
            HSDroneLogNetwork.networkInstance().startRequest(this, messageRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendEditMessage();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this method requests the admin email
     */
    private void showAdminEmail(){
        try {
            IndexRequest indexRequest = new IndexRequest(this.getApplicationContext());
            indexRequest.showAdminEmail();
            HSDroneLogNetwork.networkInstance().startRequest(this, indexRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAdminEmail();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this method sends the edited message
     */
    private void sendEditedMessage(){
        try {
            String key = this.descriptionSetIterator.next();
            MessageRequest messageRequest = new MessageRequest(this.getApplicationContext());
            messageRequest.editMessage(key,nameDescriptionMap.get(key).getText().toString());
            HSDroneLogNetwork.networkInstance().startRequest(this, messageRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendEditedMessage();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this method sends the changed Admin Email
     */
    private void sendChangedAdminEmail(){
        try {
            IndexRequest indexRequest = new IndexRequest(this.getApplicationContext());
            indexRequest.changeAdminEmail(emailDashboardSetting.getText().toString(),
                    passwordDashboardSettings.getText().toString(),
                    stmpServerDashboardSettings.getText().toString(),
                    Integer.parseInt(portDashboardSettings.getText().toString()));
            HSDroneLogNetwork.networkInstance().startRequest(this, indexRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendChangedAdminEmail();
                        }
                    });
        }
    }
}
