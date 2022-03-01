package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.IndexRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * This class handles the password activity
 * @author Henrik Kother
 */
public class Forgot_password_activity extends AppCompatActivity
        implements View.OnClickListener, NetworkListener {

    private ImageButton backButton;
    private Button confirmButton;
    private EditText emailEditText;


    /**
     * this method is called once the activit is created
     * @param savedInstanceState the old saved insance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_activity);

        backButton = (ImageButton) findViewById(R.id.backButtonForgetPassword);
        backButton.setOnClickListener(this);

        confirmButton = (Button) findViewById(R.id.confirmForgotPasswordButton);
        confirmButton.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.editText);
    }

    /**
     * this is called once a user input is detected
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == backButton) {
            //end this activity
            this.finish();
        }else if(view == confirmButton){
            IndexRequest forgotPasswordRequest = new IndexRequest(getApplicationContext());
            forgotPasswordRequest.forgotPassword(emailEditText.getText().toString());
            HSDroneLogNetwork.networkInstance().startRequest(this, forgotPasswordRequest);
        }

    }

    /**
     * This method creates a new Dialog after the forgot password activity is finished
     */
    void finishedDialog() {
        //create dialog
        AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
        saveNameAlert.setMessage(R.string.confrimPasswordMessage)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeForgotPassword();
                    }
                }).show();
    }

    /**
     * this closes the passwort activity
     */
    private void closeForgotPassword() {
        this.finish();
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
        this.finishedDialog();
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
