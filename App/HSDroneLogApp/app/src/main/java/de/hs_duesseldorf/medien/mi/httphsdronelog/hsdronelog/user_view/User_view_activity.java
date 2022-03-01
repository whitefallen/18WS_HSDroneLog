package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.user_view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.drone_view.Drone_view_activity;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.UserRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this class handels the view of a specific user
 * @author Henrik Kother
 */
public class User_view_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {

    //------- Gui Variables ----
    private TextView nameUser;
    private TextView surnameUser;
    private TextView emailUser;
    private TextView courseUser;
    private TextView passwortUser;
    private TextView headerUserCreate;
    private Switch switchUser;
    private Switch switchActiveUser;
    private int switchUserValue;
    private boolean switchActiveUserValue;
    private FloatingActionButton fabButtonEdit;//the floating action button for the edit of the flight
    private FloatingActionButton fabButtonCancel;//the floating action button for cancel that is hidden
    private FloatingActionButton fabButtonConfirmEdit;//the floating action button for cancel that is hidden
    private ImageButton trashButtonUser;
    private ImageButton backbuttonUserCreate;
    private ProgressBar progressBarUserActivity;
    //-----Image Variables -------
    private Bitmap userImageBitmap;
    private String imageFileName;
    private ImageView userImage;
    private Button openImageGalleryUserPicture;
    private Button rotateImageUserPicture;
    //------Extra Variables-------
    private Bundle extras;
    private String choosenId;
    private String roleBit;

    /**
     * this method is called once an instance of the class is created
     * @param savedInstanceState the saved state from before
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_activity);

        //Link the Gui Variables
        this.nameUser = findViewById(R.id.nameUser);
        this.surnameUser = findViewById(R.id.surnameUser);
        this.emailUser = findViewById(R.id.emailUser);
        this.courseUser = findViewById(R.id.courseUser);
        this.passwortUser = findViewById(R.id.passwortUser);
        this.switchUser = findViewById(R.id.switchUser);
        this.switchActiveUser=findViewById(R.id.switchActiveUser);
        this.headerUserCreate = findViewById(R.id.headerUserCreate);
        this.openImageGalleryUserPicture = findViewById(R.id.openImageGalleryUserPicture);
        this.openImageGalleryUserPicture.setOnClickListener(this);
        this.userImage = findViewById(R.id.userImage);
        this.rotateImageUserPicture=findViewById(R.id.rotateImageUserPicture);
        this.rotateImageUserPicture.setOnClickListener(this);
        this.progressBarUserActivity=findViewById(R.id.progressBarUserActivity);
        this.progressBarUserActivity.setVisibility(View.GONE);

        //Link the Floating action buttons
        this.fabButtonEdit = findViewById(R.id.fabButtonEditUser);
        this.fabButtonEdit.setOnClickListener(this);
        this.fabButtonCancel =findViewById(R.id.fabButtonCancelUser);
        this.fabButtonCancel.setOnClickListener(this);
        this.fabButtonConfirmEdit = findViewById(R.id.fabButtonConfirmEditUser);
        this.fabButtonConfirmEdit.setOnClickListener(this);
        this.trashButtonUser = findViewById(R.id.trashButtonUser);
        this.trashButtonUser.setVisibility(View.GONE);
        this.trashButtonUser.setOnClickListener(this);
        this.backbuttonUserCreate = findViewById(R.id.backbuttonUserCreate);
        this.backbuttonUserCreate.setOnClickListener(this);

        //Get the bundle extra
        this.extras = getIntent().getExtras();
        this.choosenId = extras.getString("id");
        this.roleBit=extras.getString("roleBit");
        //Is the purpos to view or to create?
        if (this.extras.getString("viewOrCreate").equals("view")) {
            progressBarUserActivity.setVisibility(View.VISIBLE);
            this.headerUserCreate.setText("Nutzer ansehen");
            if(this.roleBit.equals("0")){
                this.switchUser.setVisibility(View.GONE);
                this.switchActiveUser.setVisibility(View.GONE);
            }
            //disable the inputs
            Drone_view_activity.changeEditMode(nameUser,false);
            Drone_view_activity.changeEditMode(surnameUser,false);
            Drone_view_activity.changeEditMode(emailUser,false);
            Drone_view_activity.changeEditMode(courseUser,false);
            Drone_view_activity.changeEditMode(passwortUser,false);
            this.switchUser.setEnabled(false);
            this.switchActiveUser.setEnabled(false);
            this.rotateImageUserPicture.setVisibility(View.GONE);
            this.openImageGalleryUserPicture.setVisibility(View.GONE);
            //disable the other inputs
            fabButtonConfirmEdit.setVisibility(View.GONE);
            getUser();
        }else if(this.extras.getString("viewOrCreate").equals("create")){
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.GONE);
            this.headerUserCreate.setText("Nutzer erstellen");
        }
    }

    //---------------------------------- User Input Methods ----------------------------------------

    /**
     * this is called once a click on the
     * @param v the selected view v
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        if(v.equals(backbuttonUserCreate)){
            this.finish();
        }else if(v.equals(fabButtonCancel)){
            this.recreate();
        }else if(v.equals(fabButtonEdit)){
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.VISIBLE);
            this.backbuttonUserCreate.setVisibility(View.GONE);
            this.fabButtonConfirmEdit.setVisibility(View.VISIBLE);
            this.rotateImageUserPicture.setVisibility(View.VISIBLE);
            this.openImageGalleryUserPicture.setVisibility(View.VISIBLE);
            this.trashButtonUser.setVisibility(View.VISIBLE);
            Drone_view_activity.changeEditMode(nameUser,true);
            Drone_view_activity.changeEditMode(surnameUser,true);
            Drone_view_activity.changeEditMode(emailUser,true);
            Drone_view_activity.changeEditMode(courseUser,true);
            Drone_view_activity.changeEditMode(passwortUser,true);
            this.switchUser.setEnabled(true);
            this.switchActiveUser.setEnabled(true);

        }else if(v.equals(fabButtonConfirmEdit)&&this.extras.getString("viewOrCreate").equals("create")){
            if(checkAllRequiredTextfieldsareFull()) {
                progressBarUserActivity.setVisibility(View.VISIBLE);
                //user is creating a new user and wants to confirm
                //check the switch
                if (this.switchUser.isChecked()) {
                    this.switchUserValue = 1;
                } else {
                    this.switchUserValue = 0;
                }

                //check the switch
                if (this.switchActiveUser.isChecked()) {
                    this.switchActiveUserValue = false;
                } else {
                    this.switchActiveUserValue = true;
                }
                //send the network request
                UserRequest userRequest = new UserRequest(this.getApplicationContext());
                userRequest.addUser(this.emailUser.getText().toString(),
                        this.passwortUser.getText().toString(),
                        this.nameUser.getText().toString(),
                        this.surnameUser.getText().toString(),
                        this.courseUser.getText().toString(),
                        this.switchUserValue,
                        this.switchActiveUserValue,
                        this.userImageBitmap,
                        this.imageFileName
                );
                HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
                this.switchUser.setEnabled(false);
                this.switchActiveUser.setEnabled(false);
            }
        }else if(v.equals(openImageGalleryUserPicture)){
            //user wants to choose a pictue! Saves it to the Variable Picturelink!
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }else if(v.equals(rotateImageUserPicture)){
            this.userImageBitmap = this.translatedUserBitmap(false, true);
            this.userImage.setImageBitmap(this.translatedUserBitmap(true, false));
        }else if(v.equals(trashButtonUser)){
            //user wants to delete the user!
            try {
                UserRequest userRequest = new UserRequest(this.getApplicationContext());
                userRequest.deleteUser(Integer.parseInt(this.choosenId));
                HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
            } catch (IllegalStateException e) {
                progressBarUserActivity.setVisibility(View.GONE);
                sendDeleteUser();
            }
        }else if(v.equals(fabButtonConfirmEdit)&&this.extras.getString("viewOrCreate").equals("view")){
            if(checkAllRequiredTextfieldsareFull()) {
                //user wants to edit the user
                progressBarUserActivity.setVisibility(View.VISIBLE);
                //check the switch
                if (this.switchUser.isChecked()) {
                    this.switchUserValue = 1;
                } else {
                    this.switchUserValue = 0;
                }

                //check the switch
                //0 = inaktiv
                if (this.switchActiveUser.isChecked()) {
                    this.switchActiveUserValue = false;
                } else {
                    this.switchActiveUserValue = true;
                }
                this.rotateImageUserPicture.setVisibility(View.GONE);
                this.openImageGalleryUserPicture.setVisibility(View.GONE);
                //send the network request
                UserRequest userRequest = new UserRequest(this.getApplicationContext());
                userRequest.editUser(Integer.parseInt(this.choosenId),
                        this.emailUser.getText().toString(),
                        this.passwortUser.getText().toString(),
                        this.nameUser.getText().toString(),
                        this.surnameUser.getText().toString(),
                        this.courseUser.getText().toString(),
                        this.switchUserValue,
                        this.switchActiveUserValue,
                        this.userImageBitmap,
                        this.imageFileName
                );
                HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
                this.fabButtonConfirmEdit.setVisibility(View.GONE);
                this.fabButtonEdit.setVisibility(View.VISIBLE);
                this.trashButtonUser.setVisibility(View.GONE);
                this.backbuttonUserCreate.setVisibility(View.VISIBLE);
                Drone_view_activity.changeEditMode(nameUser, false);
                Drone_view_activity.changeEditMode(surnameUser, false);
                Drone_view_activity.changeEditMode(emailUser, false);
                Drone_view_activity.changeEditMode(courseUser, false);
                Drone_view_activity.changeEditMode(passwortUser, false);
                this.switchUser.setEnabled(false);
                this.switchActiveUser.setEnabled(false);
            }
        }
    }

    /**
     * checks if all textfields are filled
     * @return if all textfields are filled
     */
    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.nameUser.getText())){
            this.nameUser.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.surnameUser.getText())){
            this.surnameUser.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.emailUser.getText())){
            this.emailUser.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.courseUser.getText())){
            this.courseUser.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }
    //---------------------------------- Control the Gallery --------------------------------------

    /**
     * this is needed for refreshing the activity
     * @param requestCode the code for requesting
     * @param resultCode the result code
     * @param data the data inside the code
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            String pictureLink=targetUri.getPath();
            try {
                this.userImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                this.imageFileName = new File(pictureLink).getName();
                this.userImage.setImageBitmap(this.userImageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //---------------------------------Load the old picture ----------------------------------------

    /**
     * this loads the image
     * @param relativePath the image path
     */
    private void loadUserImage(String relativePath)
    {
        if (relativePath != null) {
            try {
                File imageFile = new File(this.getApplicationContext().getFilesDir().getPath() + relativePath);
                this.imageFileName = imageFile.getName();
                FileInputStream in = new FileInputStream(imageFile);
                this.userImageBitmap = BitmapFactory.decodeStream(in);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this translates the bitmap
     * @param scaleImage needs to be scaled?
     * @param rotateImage needs to be rotated?
     * @return the bitmap of the finished image
     */
    private Bitmap translatedUserBitmap(boolean scaleImage, boolean rotateImage) {
        Bitmap translatedBitmap;
        Matrix matrix = new Matrix();
        if (scaleImage)
        {
            float scale = 300f / this.userImageBitmap.getWidth(); //300 px
            matrix.postScale(scale, scale);
        }
        if (rotateImage)
        {
            matrix.postRotate(90);
        }

        translatedBitmap = Bitmap.createBitmap(
                    this.userImageBitmap,
                    0,
                    0,
                    this.userImageBitmap.getWidth(),
                    this.userImageBitmap.getHeight(),
                    matrix,
                    true
            );

        return translatedBitmap;
    }

    //--------------------------------- Network Methods --------------------------------------------

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
        switch(requestIdentificationTag){
            case UserRequest.SHOW_USER_TAG:
                progressBarUserActivity.setVisibility(View.GONE);
                for (Map<String, String> row : results) {
                    nameUser.setText(row.get("vorname"));
                    surnameUser.setText(row.get("nachname"));
                    emailUser.setText(row.get("email_adresse"));
                    courseUser.setText(row.get("studiengang"));
                    loadUserImage(row.get("profilbild"));
                    userImage.setImageBitmap(translatedUserBitmap(true, false));
                    if (row.get("rolle").equals("0"))
                        this.switchUser.setChecked(false);
                    else if (row.get("rolle").equals("1"))
                        this.switchUser.setChecked(true);

                    if (row.get("aktivitaet").equals("1")){
                        this.switchActiveUser.setChecked(false);}
                    else if(row.get("aktivitaet").equals("0")){
                        this.switchActiveUser.setChecked(true);}
                        //0 = inaktiv
                }
                break;
            case UserRequest.DELETE_USER_TAG:
                progressBarUserActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
            case UserRequest.ADD_USER_TAG:
                progressBarUserActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
                break;
            case UserRequest.EDIT_USER_TAG:
                progressBarUserActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
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
    public void requestWasNotSuccessful(String message, String requestIdentificationTag) {
        progressBarUserActivity.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
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
     * @param e The exception instance.
     * @param message The message send from the web server. If no message exists this value is null.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        progressBarUserActivity.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    /**
     * this gets a specific user from the database
     */
    private void getUser(){
        try {
            UserRequest userRequest = new UserRequest(this.getApplicationContext());
            userRequest.showUser(Integer.parseInt(this.choosenId));
            HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
        } catch (IllegalStateException e) {
            progressBarUserActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getUser();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this sends a delete request
     */
    private void sendDeleteUser(){
        try {
            UserRequest userRequest = new UserRequest(this.getApplicationContext());
            userRequest.deleteUser(Integer.parseInt(this.choosenId));
            HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
        } catch (IllegalStateException e) {
            progressBarUserActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendDeleteUser();
                        }
                    });
            snackbar.show();
        }
    }
    //---------------------------- Give back Methods -----------------------------------------------
    //return

    /**
     * this refreshes the return page
     */
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }
}
