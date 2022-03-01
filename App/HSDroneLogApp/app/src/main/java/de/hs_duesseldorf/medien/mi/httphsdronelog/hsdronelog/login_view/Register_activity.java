package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.login_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
 * this class handles the register activty
 * @author Henrik Kother
 */
public class Register_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {
    private ImageButton backButton;
    private Button registerButton;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private EditText courseOfStudy;
    private EditText email;

    /**
     * this is called once this activity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        firstname = findViewById(R.id.registerName);
        lastname = findViewById(R.id.registerSurname);
        courseOfStudy = findViewById(R.id.registerCurse);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        registerButton = (Button) findViewById(R.id.final_register_button);
        registerButton.setOnClickListener(this);
    }

    /**
     * this finishes this activity
     */
    private void finishThisActivity() {
        this.finish();
    }

    /**
     * this is called once a user input is detected
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == backButton) {
            finishThisActivity();
        } else if (view == registerButton) {
            //code for
            //end this activity
            //finishThisActivity();
            if(checkAllRequiredTextfieldsareFull()) {
                sendRegisterUser();
            }
        }
    }

    //NETWORK/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this checks if all reqired textfields are full
     * @return if the textfields are full
     */
    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.email.getText())){
            this.email.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.password.getText())){
            this.password.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.firstname.getText())){
            this.firstname.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.lastname.getText())){
            this.lastname.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.courseOfStudy.getText())){
            this.courseOfStudy.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }

    /**
     * this sends the registered user to the database
     */
    private void sendRegisterUser(){
        try {
            IndexRequest registerUserRequest = new IndexRequest(getApplicationContext());
            registerUserRequest.registerUser(
                    firstname.getText().toString(),
                    lastname.getText().toString(),
                    courseOfStudy.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    null,
                    null
            );
            HSDroneLogNetwork.networkInstance().startRequest(this, registerUserRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendRegisterUser();
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
        finishThisActivity();
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
