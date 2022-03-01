package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.dashboard_view.Dashboard_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.externalFileManagement.OptionsSaveFile;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view.Forgot_password_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view.Login_settings_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view.Register_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.IndexRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * Class for the login Activity
 *
 * @author Henrik Kother
 */
public class Login_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {

    //IV
    private EditText emailTextfield;
    private EditText passwordTextfield;
    private Button loginButton;
    private Button registerButton;
    private TextView forgotPasswordTextView;
    private Map<String, String> result;
    private OptionsSaveFile optionsFile;
    private ImageButton imageButtonLoginSettings;

    /**
     * this is called once the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        optionsFile = new OptionsSaveFile(this.getApplicationContext());
        HSDroneLogNetwork.setWebServerUrl(optionsFile.webServerURL());

        emailTextfield = findViewById(R.id.email);
        passwordTextfield = findViewById(R.id.password);

        emailTextfield.setText(optionsFile.loginName());

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPasswordTextView.setOnClickListener(this);

        imageButtonLoginSettings=findViewById(R.id.imageButtonLoginSettings);
        imageButtonLoginSettings.setOnClickListener(this);

        //Ask for Permissions for the Map
        ActivityCompat.requestPermissions(Login_activity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(Login_activity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    //------------------------------------- Methods for starting activitys -------------------------

    /**
     * this is called to start the dashboard activity
     * @param results the results from the network
     */
    private void startDashboardActivity(Map<String, String> results) {
        Intent dashboard = new Intent(this, Dashboard_activity.class);

        //Exchange data to the dashboard activity
        dashboard.putExtra("email_adresse", results.get("email_adresse"));
        dashboard.putExtra("passwort", results.get("passwort"));
        dashboard.putExtra("vorname", results.get("vorname"));
        dashboard.putExtra("nachname", results.get("nachname"));
        dashboard.putExtra("rolle", results.get("rolle"));
        dashboard.putExtra("studiengang", results.get("studiengang"));
        dashboard.putExtra("aktivitaet", Integer.parseInt(results.get("aktivitaet")));
        dashboard.putExtra("loeschberechtigung", Integer.parseInt(results.get("loeschberechtigung")));
        dashboard.putExtra("pilot_id", results.get("pilot_id"));
        dashboard.putExtra("profilbild", results.get("profilbild"));

        //starts the Activity
        startActivity(dashboard);
    }
    //------------------------------------User Interaction----------------------------------------------------------

    /**
     * this is called upon user interaction
     * @param view the view that is selected
     */
    @Override
    public void onClick(View view) {
        if (view == loginButton) {

            //If the network does not function(Use this!)
            //Intent dashboard = new Intent(this, Dashboard_activity.class);
            //startActivity(dashboard);

            //check if the email is a real email
            if (isValidEmail(emailTextfield.getText().toString())) {
                try {
                    IndexRequest loginRequest = new IndexRequest(getApplicationContext());
                    loginRequest.login(
                            emailTextfield.getText().toString(),
                            passwordTextfield.getText().toString()
                    );
                    HSDroneLogNetwork.networkInstance().startRequest(this, loginRequest);
                } catch (IllegalStateException e) {}
            } else {
                //if the email is not valid, show error
                emailTextfield.setError("Keine gültige E-mail!");
            }
        } else if (view == registerButton) {
            Intent registerActivity = new Intent(this, Register_activity.class);
            startActivity(registerActivity);
        } else if (view == forgotPasswordTextView) {
            Intent passwordActivity = new Intent(this, Forgot_password_activity.class);
            startActivity(passwordActivity);
        } else if(view.equals(imageButtonLoginSettings)){
            Intent loginSettingsActivity = new Intent(this, Login_settings_activity.class);
            loginSettingsActivity.putExtra("optionsFile", this.optionsFile);
            startActivityForResult(loginSettingsActivity,1234);
        }
    }

    /**
     * Get the result from other activitys
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1234){
            if (resultCode == RESULT_OK) {
                this.optionsFile = (OptionsSaveFile) data.getSerializableExtra("optionsFile");
                this.optionsFile.initializeObjects(getApplicationContext());
                this.emailTextfield.setText(this.optionsFile.loginName());
            }
        }
    }

    /**
     * Checks if the email is a valid email with java regix
     *The email must have:
     * some Characters,
     * an @ with some characters behind and
     * a dot with a 2-6 character ending after it.
     *
     * @param email the email that is checked if it is valid
     * @return true, if the email is a valid email
     */
    private boolean isValidEmail(String email) {
        Pattern ValidEmailPattern = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher isItValitPatternmatcher = ValidEmailPattern.matcher(email);
        return isItValitPatternmatcher.find();
    }

    /**
     * The Dialog if the name is saved or not
     * If it is not saved, the Textfields are Cleared.
     */
    private void saveNameDialog() {
        //create dialog
        AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
        saveNameAlert.setMessage("Wollen sie ihren Benutzernamen speichern?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //save the name
                        optionsFile.setLoginName(emailTextfield.getText().toString());
                        emailTextfield.setText(optionsFile.loginName());
                        passwordTextfield.getText().clear();
                        startDashboardActivity(result);
                    }
                })
                .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //delete everything
                        emailTextfield.setText(optionsFile.loginName());
                        emailTextfield.setText(optionsFile.loginName());
                        passwordTextfield.getText().clear();
                        startDashboardActivity(result);
                    }
                }).show();
    }

    /**
     * This Method handels the asked permissions returns.
     * The user needs to submit them all to use the app correctly.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
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
        result = results.get(0);
        if (optionsFile.isShowSavingDialog() && !emailTextfield.getText().toString().equals(optionsFile.loginName()))
        {
            saveNameDialog();
        }
        else
        {
            emailTextfield.setText(optionsFile.loginName());
            passwordTextfield.getText().clear();
            startDashboardActivity(result);
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
        //Should never happen
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
}
