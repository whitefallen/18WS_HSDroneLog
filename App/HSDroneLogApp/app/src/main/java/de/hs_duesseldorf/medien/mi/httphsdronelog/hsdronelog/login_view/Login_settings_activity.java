package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.externalFileManagement.OptionsSaveFile;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this class handles the login settings activity
 * @author Henrik Kother
 */
public class Login_settings_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {
    //IV
    private Switch saveTheUserName;
    private Button saveTheURL;
    private EditText serverURL;
    private ImageButton backButtonLoginSettings;
    private Button alreadySavedNameDeleteButton;
    private OptionsSaveFile optionsFile;

    /**
     * this is called once the activity is created
     * @param savedInstanceState the old instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_settings_activity);
        this.backButtonLoginSettings=findViewById(R.id.backButtonLoginSettings);
        this.backButtonLoginSettings.setOnClickListener(this);

        this.optionsFile = (OptionsSaveFile) getIntent().getSerializableExtra("optionsFile");
        this.optionsFile.initializeObjects(getApplicationContext());

        //Switch controlls:
        this.saveTheUserName=findViewById(R.id.saveTheUserName);
        this.saveTheUserName.setChecked(this.optionsFile.isShowSavingDialog());
        this.saveTheUserName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // true if the switch is blue(Turned On).
                optionsFile.setShowSavingDialog(isChecked);
            }
        });

        //ServerURL Link
        this.saveTheURL=findViewById(R.id.saveTheURL);
        this.saveTheURL.setOnClickListener(this);
        //Edit text link
        this.serverURL=findViewById(R.id.serverURL);
        this.serverURL.setText(optionsFile.webServerURL());

        //alreadySavedNameDeleteButton link
        this.alreadySavedNameDeleteButton=findViewById(R.id.alreadySavedNameDeleteButton);
        this.alreadySavedNameDeleteButton.setOnClickListener(this);

        //return
        Intent data = new Intent();
        data.putExtra("optionsFile", this.optionsFile);
        setResult(RESULT_OK,data);
    }

    //------------------------- User Input Methods -------------------------

    /**
     * this is called once a user input is detected
     * @param v the current view
     */
    @Override
    public void onClick(View v) {
        if(v.equals(saveTheURL)){
            try {
                HSDroneLogNetwork.setWebServerUrl(this.serverURL.getText().toString());
                optionsFile.setWebServerURL(this.serverURL.getText().toString());
                showSnackbar("Web Server Url wurde geändert!");
            } catch (IllegalStateException e) {
                showSnackbar("Änderung konnte nicht übernommen werden. Versuchen sie es später erneut!");
            }
        }else if(v.equals(backButtonLoginSettings)){
            //user wants to exit
            //let android destroy this activity
            this.finish();
        }else if(v.equals(alreadySavedNameDeleteButton)){
            //The user wants to delete the saved names
            optionsFile.setLoginName("");

            showSnackbar("Der gespeicherte Name wurde gelöscht!");
        }
    }

    /**
     * this shows a snackbar
     * @param text the text on the snackbar
     */
    private void showSnackbar(String text) {
        //after that -> response
        ConstraintLayout constraintLayout =findViewById(R.id.layout);
        Snackbar snackbar = Snackbar
                .make(constraintLayout, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //---------------------------Network Methods-----------------------------

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
}
