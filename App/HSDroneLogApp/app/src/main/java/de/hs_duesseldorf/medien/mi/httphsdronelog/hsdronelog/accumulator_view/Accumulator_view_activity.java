package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.accumulator_view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.BatteryRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.DroneRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * @author Henrik Kother
 */
public class Accumulator_view_activity extends AppCompatActivity implements View.OnClickListener, de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener {

    private String intentId;
    private String roleBit;
    private Bundle extras;
    private Boolean isAllDronesRequestButCreate=false;
    //---------------- XML Items ---------------
    private ImageButton backButton;
    private ImageButton trashButtonAccu;
    private TextView accuDescription;
    private TextView accuNumber;
    private TextView headerbattery;
    private FloatingActionButton fabButtonEdit;//the floating action button for the edit of the flight
    private FloatingActionButton fabButtonCancel;//the floating action button for cancel that is hidden
    private FloatingActionButton fabButtonConfirmEdit;//the floating action button for cancel that is hidden
    private TableLayout accumulatorCheckboxDronesTable;
    private String checkedDrones;//all drones that are already checked
    private Map<String, String> checkedItemsMap = new HashMap<String, String>();//the checked items storage
    private String[] checkedDronesSplit;
    private String returnString;
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();

    private ProgressBar progressBarAccumulatorActivity;

    /**
     * This Method is called during the create Process
     * @param savedInstanceState the saved instance from before
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accumulator_view_activity);

        //Link the custom BackButtons
        backButton = findViewById(R.id.backbuttonBatteryCreate);
        backButton.setOnClickListener(this);
        trashButtonAccu = findViewById(R.id.trashButtonAccu);
        trashButtonAccu.setOnClickListener(this);
        fabButtonEdit = findViewById(R.id.fabButtonEditAccu);
        fabButtonEdit.setOnClickListener(this);
        fabButtonCancel =findViewById(R.id.fabButtonCancelAccu);
        fabButtonCancel.setOnClickListener(this);
        fabButtonConfirmEdit = findViewById(R.id.fabButtonConfirmEditAccu);
        fabButtonConfirmEdit.setOnClickListener(this);
        this.progressBarAccumulatorActivity=findViewById(R.id.progressBarAccumulatorActivity);
        this.progressBarAccumulatorActivity.setVisibility(View.GONE);
        //link the textfields
        accuDescription = findViewById(R.id.accuDescription);
        accuNumber = findViewById(R.id.accuNumber);
        this.headerbattery=findViewById(R.id.headerbattery);
        //Link the Table Layout
        accumulatorCheckboxDronesTable = findViewById(R.id.accumulatorCheckboxDronesTable);

        this.extras = getIntent().getExtras();
        this.roleBit = this.extras.getString("role");
        this.intentId = extras.getString("id");
        if(this.extras.getString("viewOrCreate").equals("create")) {
            this.headerbattery.setText("Akku erstellen");
            fabButtonEdit.setVisibility(View.GONE);
            fabButtonCancel.setVisibility(View.GONE);
            trashButtonAccu.setVisibility(View.GONE);
            isAllDronesRequestButCreate=true;
            getallDroneswithoutOffset();
        } else {
            this.headerbattery.setText("Akku anschauen");
            changeEditModeAccu(accuDescription,false);
            changeEditModeAccu(accuNumber,false);
            fabButtonCancel.setVisibility(View.GONE);
            fabButtonConfirmEdit.setVisibility(View.GONE);
            trashButtonAccu.setVisibility(View.GONE);
            //must be a view request

            //ask for the battery data from the network
            showBatterie();
        }

        //if the user is not a admin
        if(this.roleBit.equals("0")){
            this.fabButtonConfirmEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.GONE);
            this.fabButtonEdit.setVisibility(View.GONE);
            this.trashButtonAccu.setVisibility(View.GONE);
        }
    }

    /**
     * This Method is Called when the user makes a touchscreen input
     * @param view the Activity view
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        if (view.equals(backButton)) {
            this.finish();
        }else if(view.equals(fabButtonEdit)){
            this.headerbattery.setText("Akku bearbeiten");
            //the admin-user wants to edit an accu
            this.fabButtonConfirmEdit.setVisibility(View.VISIBLE);
            this.fabButtonCancel.setVisibility(View.VISIBLE);
            this.fabButtonEdit.setVisibility(View.GONE);
            this.backButton.setVisibility(View.GONE);
            this.trashButtonAccu.setVisibility(View.VISIBLE);
            changeEditModeAccu(accuDescription,true);
            changeEditModeAccu(accuNumber,true);
            changeEditModeCheckboxes(true);
        }else if(view.equals(fabButtonCancel)){
            //the user wants to cancel the action
            this.recreate();
        }else if(view.equals(fabButtonConfirmEdit) && this.extras.getString("viewOrCreate").equals("view")){
            if(checkAllRequiredTextfieldsareFull()) {
                //send the data, user wants to edit
                this.progressBarAccumulatorActivity.setVisibility(View.VISIBLE);
                this.headerbattery.setText("Akku anschauen");
                this.checkedDrones = convertMapToStringWithAllOnes(this.checkedItemsMap);
                if(checkedDrones==null){

                    this.progressBarAccumulatorActivity.setVisibility(View.GONE);
                    //set a custom snackbar to show the message
                    Snackbar.make(findViewById(android.R.id.content), "Bitte wählen sie eine passende Drohne aus", LENGTH_LONG)
                            .show();
                    return;
                }
                editBattery();
                //set everything back
                this.fabButtonConfirmEdit.setVisibility(View.GONE);
                this.fabButtonCancel.setVisibility(View.GONE);
                this.fabButtonEdit.setVisibility(View.VISIBLE);
                this.backButton.setVisibility(View.VISIBLE);
                this.trashButtonAccu.setVisibility(View.GONE);
                changeEditModeAccu(accuDescription, false);
                changeEditModeAccu(accuNumber, false);
                changeEditModeCheckboxes(false);
            }
        }else if(view.equals(fabButtonConfirmEdit) && this.extras.getString("viewOrCreate").equals("create")){
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarAccumulatorActivity.setVisibility(View.VISIBLE);
                //the user wants to add a battery
                this.checkedDrones = convertMapToStringWithAllOnes(this.checkedItemsMap);
                if(checkedDrones==null){
                    //set a custom snackbar to show the message
                    Snackbar.make(findViewById(android.R.id.content), "Bitte wählen sie eine passende Drohne aus", LENGTH_LONG)
                            .show();
                    return;
                }else {
                    createBatteries();

                }
            }
        }else if(view.equals(trashButtonAccu)){

            //create dialog
            AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
            saveNameAlert.setMessage("Wollen sie den Akku wirklich löschen?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            deleteThisAccuAndExitThisActivity();
                        }
                    })
                    .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();


        }
    }

    /**
     * This methods deletes a accu and exits this activity
     */
    private void deleteThisAccuAndExitThisActivity(){
        try {
            BatteryRequest deleteBatteriesRequest = new BatteryRequest(getApplicationContext());
            deleteBatteriesRequest.deleteBattery(Integer.parseInt(this.intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, deleteBatteriesRequest);
        } catch (IllegalStateException e) {
            this.progressBarAccumulatorActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteThisAccuAndExitThisActivity();
                        }
                    });

            snackbar.show();
        }
    }
    //------------------------------------ Change XML Methods --------------------------------------

    /**
     * This Method creates a new TableRow
     * @param tl The used Tablelayout
     * @param text Sting Text (Name of the drone)
     * @param id the id of the drone
     */
    private void createTableRowAllDrones(TableLayout tl, String text, String id){
        TableRow tableRow = new TableRow(this);

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.matteBlack));

        CheckBox box = new CheckBox(this);
        //set the id from the accu
        box.setId(Integer.parseInt(id));
        //if the user wants to create an new battery!
        if(!this.extras.getString("viewOrCreate").equals("create")){
            box.setEnabled(false);
        }
        //add the checkbox to the arraylist!
        this.checkBoxes.add(box);
        //check the list for true checkboxes
        if(checkedItemsMap.get(id).equals("1")){
            box.setChecked(true);
        }else{
            box.setChecked(false);
        }
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                //what happens when the it's unchecked or checked
                if(arg1){
                    //checked
                    checkedItemsMap.replace(String.valueOf(box.getId()),"1");
                }else{
                    //arg1 is unchecked, remove it from the list of checked drones
                    checkedItemsMap.replace(String.valueOf(box.getId()),"0");
                }
            }
        });

        //add it to the layout
        tableRow.addView(box);
        tableRow.addView(textView);
        tableRow.setPaddingRelative(20, 15, 0, 15);
        tl.addView(tableRow);

        TableRow space = new TableRow(this);
        space.setBackgroundColor(getResources().getColor(R.color.gray));
        TextView spaceText = new TextView(this);
        spaceText.setText(" ");
        spaceText.setTextSize(15);
        spaceText.setTextColor(getResources().getColor(R.color.matteBlack));
        spaceText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        space.setMinimumHeight(1);
        tl.addView((space));
    }


    /**
     * This method changes the edit mode of a view
     * @param view          the view to be changed
     * @param enableDisable the edit mode enable(true) or disable(false)
     */
    private void changeEditModeAccu(TextView view, boolean enableDisable) {
        view.setFocusable(enableDisable);
        view.setFocusableInTouchMode(enableDisable);
        view.setClickable(enableDisable);
    }

    /**
     * Changes the Edit mode of all checkboxes
     * @param isEnabled if the checkbox is enabled
     */
    private void changeEditModeCheckboxes(boolean isEnabled){
        for(CheckBox checkBox:this.checkBoxes){
            checkBox.setEnabled(isEnabled);
        }
    }

    /**
     * Checks if the Required textfields are full
     * @return if the textfields are full
     */
    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.accuDescription.getText())){
            this.accuDescription.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.accuNumber.getText())){
            this.accuNumber.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }

    //--------------------------------------- Network Methods -------------------------------

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
            case BatteryRequest.SHOW_BATTERY_TAG:
                for (Map<String, String> row : results) {
                    accuDescription.setText(row.get("bezeichnung"));
                    accuNumber.setText(row.get("anzahl"));
                    this.checkedDrones = row.get("drones");
                }
                //receve all drones
                DroneRequest droneRequest = new DroneRequest(this.getApplicationContext());
                droneRequest.allDrones(0); //check if correct
                HSDroneLogNetwork.networkInstance().startRequest(this, droneRequest);
                break;
            case DroneRequest.ALL_DRONES_TAG:
                this.progressBarAccumulatorActivity.setVisibility(View.GONE);
                if (!this.isAllDronesRequestButCreate)
                {
                    //must be droneRequest
                    for (Map<String, String> row : results) {
                        this.checkedItemsMap.put(row.get("drohne_id"),"0");
                    }
                    //check all items who are already checked!
                    //split all items from the array
                    if(checkedDrones != null) {
                        this.checkedDronesSplit = checkedDrones.split(",");
                        //check if the item is in the given splittet string, if so replace the bit string with 1!
                        for (int i = 0; i < this.checkedDronesSplit.length; i++) {
                            if (this.checkedItemsMap.containsKey(this.checkedDronesSplit[i])) {
                                this.checkedItemsMap.replace(this.checkedDronesSplit[i], "1");//replace the item with true
                            }
                        }
                    }
                    for (Map<String, String> row : results) {
                        createTableRowAllDrones(accumulatorCheckboxDronesTable, row.get("drohnen_modell"),row.get("drohne_id"));
                    }
                }
                else
                {
                    isAllDronesRequestButCreate =false;
                    for (Map<String, String> row : results) {
                        this.checkedItemsMap.put(row.get("drohne_id"),"0");
                        createTableRowAllDrones(accumulatorCheckboxDronesTable, row.get("drohnen_modell"),row.get("drohne_id"));
                    }
                }
                break;
            case BatteryRequest.ADD_BATTERY_TAG:
                this.progressBarAccumulatorActivity.setVisibility(View.GONE);
                refreshReturnPage();
                this.finish();
                break;
            case BatteryRequest.EDIT_BATTERY_TAG:
                refreshReturnPage();
                this.progressBarAccumulatorActivity.setVisibility(View.GONE);
                this.fabButtonConfirmEdit.setVisibility(View.GONE);
                this.fabButtonCancel.setVisibility(View.GONE);
                this.fabButtonEdit.setVisibility(View.VISIBLE);
                this.backButton.setVisibility(View.VISIBLE);
                this.trashButtonAccu.setVisibility(View.GONE);
                changeEditModeAccu(accuDescription, false);
                changeEditModeAccu(accuNumber, false);
                changeEditModeCheckboxes(false);
                break;
            case BatteryRequest.DELETE_BATTERY_TAG:
                this.progressBarAccumulatorActivity.setVisibility(View.GONE);
                refreshReturnPage();
                this.finish();
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
        this.progressBarAccumulatorActivity.setVisibility(View.GONE);
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
        this.progressBarAccumulatorActivity.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    /**
     * Converts the map to strings
     * @param map the map with all checked accus
     * @return a string splitted by ,
     */
    private String convertMapToStringWithAllOnes(Map<String, String> map){
        this.returnString="";
        for (Map.Entry entry : map.entrySet()) {
            if(entry.getValue().equals("1")){
                this.returnString = this.returnString + entry.getKey() + ",";
            }
        }
        //delete the last ,
        if(returnString!=null){
            Log.i("----",returnString);
            this.returnString = this.returnString.substring(0, this.returnString.length() - 1);
        }else{
            progressBarAccumulatorActivity.setVisibility(View.GONE);
        }
        return returnString;
    }
    //---------------------------- Give back Methods -----------------------------------------------
    /**
     * This method refreshes the fragment
     */
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }

    //-------------------------- Network Meth‚ods --------------------------------------

    /**
     * this method gets all Drones without an offset
     */
    private void getallDroneswithoutOffset(){
        try{
            this.progressBarAccumulatorActivity.setVisibility(View.VISIBLE);
            DroneRequest droneRequest = new DroneRequest(this.getApplicationContext());
            droneRequest.allDrones(0); //check if correct
            HSDroneLogNetwork.networkInstance().startRequest(this, droneRequest);
        } catch (IllegalStateException e) {
            isAllDronesRequestButCreate=false;
            this.progressBarAccumulatorActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getallDroneswithoutOffset();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method shows a specific battery
     */
    private void showBatterie(){
        try {
            this.progressBarAccumulatorActivity.setVisibility(View.VISIBLE);
            BatteryRequest specificBatteriesRequest = new BatteryRequest(this.getApplicationContext());
            specificBatteriesRequest.showBattery(Integer.parseInt(intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, specificBatteriesRequest);
        } catch (IllegalStateException e) {
            this.progressBarAccumulatorActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showBatterie();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method edits a specific battery
     */
    private void editBattery(){
        try {
            BatteryRequest editBatteriesRequest = new BatteryRequest(this.getApplicationContext());
            editBatteriesRequest.editBattery(
                    Integer.parseInt(this.intentId),
                    this.accuDescription.getText().toString(),
                    Integer.parseInt(this.accuNumber.getText().toString()),
                    this.checkedDrones
            );
            refreshReturnPage();
            HSDroneLogNetwork.networkInstance().startRequest(this, editBatteriesRequest);
        } catch (IllegalStateException e) {
            this.progressBarAccumulatorActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editBattery();
                        }
                    });

            snackbar.show();
        }
    }

    /**
     * this method creates a new battery
     */
    private void createBatteries(){
        try {
            this.progressBarAccumulatorActivity.setVisibility(View.VISIBLE);
            BatteryRequest createBatteriesRequest = new BatteryRequest(this.getApplicationContext());
            createBatteriesRequest.addBattery(
                    this.accuDescription.getText().toString(),
                    Integer.parseInt(this.accuNumber.getText().toString()),
                    checkedDrones
            );
            de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork.networkInstance().startRequest(this, createBatteriesRequest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            this.progressBarAccumulatorActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            createBatteries();
                        }
                    });
            snackbar.show();
        }
    }

}
