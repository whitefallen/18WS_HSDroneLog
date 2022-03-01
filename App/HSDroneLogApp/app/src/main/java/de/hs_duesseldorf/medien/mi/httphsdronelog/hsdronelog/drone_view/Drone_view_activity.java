package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.drone_view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.DroneRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * this method is needed for creating the drone view activity
 * @author Henrik Kother
 */
public class Drone_view_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener, AdapterView.OnItemSelectedListener {

    private String intentId;
    private ImageButton backButton;
    private ImageButton trashButtonDrone;
    private TextView weightInGramm;
    private TextView diagonalSizeInMM;
    private TextView maxFlightTime;
    private TextView maxFlightHeight;
    private TextView maxSpeedInKmH;
    private TextView droneNameTextfield;
    private Bundle extras;
    private FloatingActionButton fabButtonEdit;//the floating action button for the edit of the flight
    private FloatingActionButton fabButtonCancel;//the floating action button for cancel that is hidden
    private FloatingActionButton fabButtonConfirmEdit;//the floating action button for cancel that is hidden
    private ProgressBar progressBarDroneActivity;

    //------Choose Image-------
    private ImageView droneImage;
    private Button openImageGalleryDronePicture;
    private Button rotateImageDronePicture;
    private Bitmap droneImageBitmap;
    private String imageFileName;

    /**
     * this method is called once this activity is created
     * @param savedInstanceState
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drone_view_activity);

        //set the header to view a specific drone
        setHeaderString(R.string.lookupDrone);
        this.progressBarDroneActivity=findViewById(R.id.progressBarDroneActivity);
        this.progressBarDroneActivity.setVisibility(View.GONE);

        //link the image back button
        this.backButton = (ImageButton) findViewById(R.id.backbuttonDroneCreate);
        this.backButton.setOnClickListener(this);
        this.trashButtonDrone = findViewById(R.id.trashButtonDrone);
        this.trashButtonDrone.setOnClickListener(this);
        this.trashButtonDrone.setVisibility(View.GONE);
        //link the edit buttons
        fabButtonEdit = findViewById(R.id.fabButtonEditDrone);
        fabButtonEdit.setOnClickListener(this);
        fabButtonCancel =findViewById(R.id.fabButtonCancelDrone);
        fabButtonCancel.setOnClickListener(this);
        fabButtonConfirmEdit = findViewById(R.id.fabButtonConfirmDroneEdit);
        fabButtonConfirmEdit.setOnClickListener(this);

        //Choose Image Link
        this.droneImage = findViewById(R.id.droneImage);
        this.openImageGalleryDronePicture=findViewById(R.id.openImageGalleryDronePicture);
        this.openImageGalleryDronePicture.setOnClickListener(this);
        this.rotateImageDronePicture=findViewById(R.id.rotateImageDronePicture);
        this.rotateImageDronePicture.setOnClickListener(this);

        //Link the textviews
        this.weightInGramm = (TextView) findViewById(R.id.weightInGramm);
        weightInGramm.setOnClickListener(this);
        this.diagonalSizeInMM = (TextView) findViewById(R.id.diagonalSizeInMM);
        diagonalSizeInMM.setOnClickListener(this);
        this.maxFlightTime = (TextView) findViewById(R.id.maxFlightTime);
        maxFlightTime.setOnClickListener(this);
        this.maxFlightHeight = (TextView) findViewById(R.id.maxFlightHeight);
        maxFlightHeight.setOnClickListener(this);
        this.maxSpeedInKmH = (TextView) findViewById(R.id.maxSpeedInKmH);
        maxSpeedInKmH.setOnClickListener(this);
        this.droneNameTextfield = findViewById(R.id.droneNameTextfield);
        droneNameTextfield.setOnClickListener(this);

        //Get the bundle extras
        this.extras = getIntent().getExtras();
        if(this.extras.getString("role").equals("0")){
            //the user is not an admin
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.GONE);
        }

        if (this.extras.getString("viewOrCreate").equals("view")) {

            //make the views not editable
            changeEditMode(weightInGramm, false);
            changeEditMode(diagonalSizeInMM, false);
            changeEditMode(maxFlightTime, false);
            changeEditMode(maxFlightHeight, false);
            changeEditMode(maxSpeedInKmH, false);
            changeEditMode(droneNameTextfield,false);
            fabButtonConfirmEdit.setVisibility(View.GONE);
            this.rotateImageDronePicture.setVisibility(View.GONE);
            this.openImageGalleryDronePicture.setVisibility(View.GONE);
            //get the id from the table layout of the drone that is edited.
            this.intentId = extras.getString("droneid");
            showDrone();
        }else{
            //set the header to create a specific drone
            setHeaderString(R.string.createDrone);
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.GONE);
        }
    }

    /**
     * this method is called once a interaction is detected
     * @param view the currend view
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        if (view.equals(backButton)) {
            //if the custom back button is pressed, go back and finish this activity!
            this.finish();
        }else if(view.equals(fabButtonEdit)) {
            //make the views editable
            changeEditMode(weightInGramm, true);
            changeEditMode(diagonalSizeInMM, true);
            changeEditMode(maxFlightTime, true);
            changeEditMode(maxFlightHeight, true);
            changeEditMode(maxSpeedInKmH, true);
            changeEditMode(droneNameTextfield, true);
            trashButtonDrone.setVisibility(View.VISIBLE);
            fabButtonEdit.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            fabButtonConfirmEdit.setVisibility(View.VISIBLE);
            this.rotateImageDronePicture.setVisibility(View.VISIBLE);
            this.openImageGalleryDronePicture.setVisibility(View.VISIBLE);
            setHeaderString(R.string.editDrone);
        }else if(view.equals(fabButtonCancel)){
            recreate();
        }else if(view.equals(fabButtonConfirmEdit) && this.extras.getString("viewOrCreate").equals("create")){
            //User wants to create a new Drone
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarDroneActivity.setVisibility(View.VISIBLE);
                addDrone();
                //cancel this activity
                this.finish();
            }
        }else if(view.equals(fabButtonConfirmEdit) && this.extras.getString("viewOrCreate").equals("view")){
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarDroneActivity.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                changeEditMode(weightInGramm, false);
                changeEditMode(diagonalSizeInMM, false);
                changeEditMode(maxFlightTime, false);
                changeEditMode(maxFlightHeight, false);
                changeEditMode(maxSpeedInKmH, false);
                changeEditMode(droneNameTextfield, false);
                trashButtonDrone.setVisibility(View.GONE);
                fabButtonEdit.setVisibility(View.VISIBLE);
                fabButtonConfirmEdit.setVisibility(View.GONE);
                this.rotateImageDronePicture.setVisibility(View.GONE);
                this.openImageGalleryDronePicture.setVisibility(View.GONE);
                this.progressBarDroneActivity.setVisibility(View.VISIBLE);
                setHeaderString(R.string.lookupDrone);
                //send data
                editDrone();
            }
        }else if(view.equals(trashButtonDrone)){
            //create dialog
            AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
            saveNameAlert.setMessage("Wollen sie die Drohne wirklich löschen?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            deleteThisAndExitThisActivity();
                        }
                    })
                    .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }else if(view.equals(openImageGalleryDronePicture)){
            //user wants to choose a pictue! Saves it to the Variable Picturelink!
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }else if(view.equals(rotateImageDronePicture)){
            this.droneImageBitmap = this.translatedDroneBitmap(false, true);
            this.droneImage.setImageBitmap(this.translatedDroneBitmap(true, false));
        }
    }

    /**
     * this method is called when the drone needs to be deleted
     */
    private void deleteThisAndExitThisActivity(){
        //delete this
        deleteDrone();
        this.finish();
    }

    //---------------------------------- Control the Gallery --------------------------------------

    /**
     * this method is called for the gallery
     * @param requestCode the code of the request
     * @param resultCode the code of the result
     * @param data the data of the request
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            String pictureLink=targetUri.getPath();
            try {
                this.droneImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                this.imageFileName = new File(pictureLink).getName();
                this.droneImage.setImageBitmap(this.droneImageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //-------------------------- Change the XML Methods --------------------------------------------

    /**
     * This method sets the HeaderString in the head section of the activity
     *
     * @param headerStringId
     */
    private void setHeaderString(int headerStringId) {
        String headerString = getString(headerStringId);
        //set the header Name
        TextView header = (TextView) findViewById(R.id.headerDrone);
        header.setText(headerString);
    }

    /**
     * This method changes the edit mode of a view
     *
     * @param view          the view to be changed
     * @param enableDisable the edit mode enable(true) or disable(false)
     */
    public static void changeEditMode(TextView view, boolean enableDisable) {
        view.setFocusable(enableDisable);
        view.setFocusableInTouchMode(enableDisable);
        view.setClickable(enableDisable);
    }

    /**
     * checks if all textFields are full
     * @return if all textfields are full
     */
    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.weightInGramm.getText())){
            this.weightInGramm.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.diagonalSizeInMM.getText())){
            this.diagonalSizeInMM.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.maxFlightTime.getText())){
            this.maxFlightTime.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.maxFlightHeight.getText())){
            this.maxFlightHeight.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.maxSpeedInKmH.getText())){
            this.maxSpeedInKmH.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.droneNameTextfield.getText())){
            this.droneNameTextfield.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }
    //--------------------------- Network Methods --------------------------------------------------

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
            case DroneRequest.SHOW_DRONE_TAG:
                //show drone Data
                this.progressBarDroneActivity.setVisibility(View.GONE);
                for (Map<String, String> row : results) {
                    loadDroneContentIntoViews(row.get("fluggewicht_in_gramm"),
                            row.get("diagonale_groesse_in_mm"),
                            row.get("flugzeit_in_min"),
                            row.get("maximale_flughoehe_in_m"),
                            row.get("hoechstgeschwindigkeit_in_kmh"),
                            row.get("drohnen_modell"));
                    this.loadDroneImage(row.get("bild"));
                    this.droneImage.setImageBitmap(this.translatedDroneBitmap(true, false));
                }
                break;
            case DroneRequest.DELETE_DRONE_TAG:
                this.progressBarDroneActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
            case DroneRequest.ADD_DRONE_TAG:
                this.progressBarDroneActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
                break;
            case DroneRequest.EDIT_DRONE_TAG:
                this.progressBarDroneActivity.setVisibility(View.GONE);
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
    public void sessionHasExpired(String message, String requestIdentificationTag) {
        this.progressBarDroneActivity.setVisibility(View.GONE);
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
        this.progressBarDroneActivity.setVisibility(View.GONE);
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
     * shows the drone by a network request
     */
    private void showDrone(){
        try {
            this.progressBarDroneActivity.setVisibility(View.VISIBLE);
            DroneRequest droneRequest = new DroneRequest(getApplicationContext());
            droneRequest.showDrone(Integer.parseInt(extras.getString("droneid")));
            HSDroneLogNetwork.networkInstance().startRequest(this, droneRequest);
        } catch (IllegalStateException e) {
            this.progressBarDroneActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDrone();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * adds a drone by a network request
     */
    private void addDrone(){
        try {
            DroneRequest createDroneRequest = new DroneRequest(getApplicationContext());
            createDroneRequest.addDrone(
                    this.droneNameTextfield.getText().toString(),
                    Integer.parseInt(this.weightInGramm.getText().toString()),
                    Integer.parseInt(this.maxFlightTime.getText().toString()),
                    Integer.parseInt(this.diagonalSizeInMM.getText().toString()),
                    Integer.parseInt(this.maxFlightHeight.getText().toString()),
                    Integer.parseInt(this.maxSpeedInKmH.getText().toString()),
                    this.droneImageBitmap,
                    this.imageFileName
            );
            HSDroneLogNetwork.networkInstance().startRequest(this,createDroneRequest);
        } catch (IllegalStateException e){
            this.progressBarDroneActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addDrone();
                        }
                    });
            snackbar.show();        }
    }

    /**
     * this edits a specific drone
     */
    private void editDrone(){
        try {
            DroneRequest createDroneRequest = new DroneRequest(getApplicationContext());
            createDroneRequest.editDrone(
                    Integer.parseInt(this.intentId),
                    this.droneNameTextfield.getText().toString(),
                    Integer.parseInt(this.weightInGramm.getText().toString()),
                    Integer.parseInt(this.maxFlightTime.getText().toString()),
                    Integer.parseInt(this.diagonalSizeInMM.getText().toString()),
                    Integer.parseInt(this.maxFlightHeight.getText().toString()),
                    Integer.parseInt(this.maxSpeedInKmH.getText().toString()),
                    this.droneImageBitmap,
                    this.imageFileName
            );
            HSDroneLogNetwork.networkInstance().startRequest(this, createDroneRequest);
        } catch (IllegalStateException e) {
            this.progressBarDroneActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editDrone();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this method deletes a specific drone
     */
    private void deleteDrone(){
        try {
            DroneRequest deleteDroneRequest = new DroneRequest(getApplicationContext());
            deleteDroneRequest.deleteDrone(Integer.parseInt(this.intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this,deleteDroneRequest);
        } catch (IllegalStateException e){
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteDrone();
                        }
                    });
            snackbar.show();
        }
    }
    //---------------------------- Give back Methods -----------------------------------------------
    //return

    /**
     * this method refreshes the return page
     */
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }

    //-------------------------- Load Content Methods ----------------------------------------------

    /**
     * this method loads the following content into the views
     * @param weight self explaining
     * @param size self explaining
     * @param time the maximum flight time
     * @param height self explaining
     * @param speed self explaining
     * @param name self explaining
     */
    private void loadDroneContentIntoViews(String weight, String size, String time, String height, String speed, String name) {
        this.weightInGramm.setText(weight);
        this.diagonalSizeInMM.setText(size);
        this.maxFlightTime.setText(time);
        this.maxFlightHeight.setText(height);
        this.maxSpeedInKmH.setText(speed);
        this.droneNameTextfield.setText(name);
    }

    //---------------------------------Load the old picture ----------------------------------------

    /**
     * this method loads a drone image into the bitmap
     * @param relativePath the path to the image
     */
    private void loadDroneImage(String relativePath)
    {
        if (relativePath != null) {
            try {
                File imageFile = new File(this.getApplicationContext().getFilesDir().getPath() + relativePath);
                this.imageFileName = imageFile.getName();
                FileInputStream in = new FileInputStream(imageFile);
                this.droneImageBitmap = BitmapFactory.decodeStream(in);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this function translates the drone bitmap
     * @param scaleImage the image needs to be scaled
     * @param rotateImage the image needs to be rotated
     * @return the image as a bitmap
     */
    private Bitmap translatedDroneBitmap(boolean scaleImage, boolean rotateImage) {
        Bitmap translatedBitmap;
        Matrix matrix = new Matrix();
        if (scaleImage)
        {
            float scale = 300f / this.droneImageBitmap.getWidth(); //300 px
            matrix.postScale(scale, scale);
        }
        if (rotateImage)
        {
            matrix.postRotate(90);
        }

        translatedBitmap = Bitmap.createBitmap(
                this.droneImageBitmap,
                0,
                0,
                this.droneImageBitmap.getWidth(),
                this.droneImageBitmap.getHeight(),
                matrix,
                true
        );

        return translatedBitmap;
    }

    //--------------------------- Spinner methods --------------------------------------------------

    /**
     *is not used
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * is not used
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
