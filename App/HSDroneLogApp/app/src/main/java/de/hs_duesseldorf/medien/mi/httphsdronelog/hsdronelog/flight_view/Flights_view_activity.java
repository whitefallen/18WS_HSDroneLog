package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.flight_view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistElementRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.FlightRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.UserRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * The Flight view/Create Activity class. It manages the extended view on a flight and the edit or creation of one
 * flight.
 *
 * @author Henrik Kother
 */
public class Flights_view_activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, NetworkListener {

    private String intentId;
    private String pilot_id;//the id of the pilot using the application
    private Bundle extras;//the extras from the past screen
    private String roleBit;
    //---------- GUI Variables --------
    private ImageButton backButton;
    private ImageButton trashButton;
    private ImageButton saveFlightPersonalChecklist;
    private FloatingActionButton fabButtonEdit;//the floating action button for the edit of the flight
    private FloatingActionButton fabButtonCancel;//the floating action button for cancel that is hidden
    private FloatingActionButton fabButtonConfirmEdit;//the floating action button for cancel that is hidden
    private Button setDateTimeButton;
    private ConstraintLayout constraintLayout;
    private Button uploadDjiFlightlogButton;
    private Button showDJIFlightlog;

    //---------- TableLayout ----------
    private TableLayout flightChecklistElementsLayout;
    private String choosenChecklist;
    private String checkedElements;
    private String[] checkedElementsSplit;
    //----------- Textviews -----------
    private TextView droneViewFlightlocation;
    private TextView droneViewDate;
    private TextView droneViewFlighttimeBegin;
    private TextView droneViewFlighttimeEnd;
    private TextView flightDescription;
    private TextView flightExtraDescription;
    private TextView noTimeSelected;
    private TextView flightPilotText;
    private ProgressBar progressBarFlightsActivity;

    //--------- Spinner ----------
    //Dronespinner:
    private Spinner droneSpinner;
    private List<String> droneSpinnerData = new ArrayList<>();
    private int droneIndex;
    private List<Integer> droneIdList = new ArrayList<>();//for saving ids because the spinner can not do it
    private ArrayAdapter<String> dataAdapterDrone;
    //checklistSpinner:
    private Spinner checklistSpinner;
    private ArrayAdapter<String> dataAdapterChecklist;
    private List<Integer> checklistIdList = new ArrayList<>();//for saving ids because the spinner can not do it
    private List<String> checklistSpinnerData = new ArrayList<>();
    private int checklistIndex; //for saving the id of the spinner item
    //pilotSpinner(only for Admin)
    private Spinner pilotSpinner;
    private ArrayAdapter<String> dataAdapterPilot;
    private List<Integer> pilotIdList = new ArrayList<>();//for saving ids because the spinner can not do it
    private List<String> pilotSpinnerData = new ArrayList<>();
    private int pilotSpinnerIndex; //for saving the id of the spinner item
    private String oldflightOwner;
    //--------- Check if... Variables --------
    private boolean createFlightRequest=false;
    private boolean viewFlightRequest=false;
    private boolean userInEditMode = false;
    private boolean setDateTimeButtonWasPressed;

    //------- Date/Time Picker ---------
    private DatePicker datePicker;
    private DatePickerDialog dateDialog;
    private int year;
    private int month;
    private int day;
    private String changedDate;
    private TimePickerDialog timeDialogFlightBegin;
    private TimePickerDialog timeDialogFlightEnd;
    private String changedTimeBeginTime;
    private String changedTimeEndTime;
    private int hour;
    private int minute;
    //---------- Send the data Variables ----------
    private String choosenDroneFromThePast;
    //----------- Checklist variables --------
    private Map<Integer,CheckBox> checkBoxList = new HashMap<>();
    private Map<Integer,TextView> textViewList = new HashMap<>();
    private Map<Integer,String> checkboxChecked=new HashMap<>();
    private Map<Integer,String> textviewText=new HashMap<>();
    /**
     * This method is called when the activity is Created. It contains everything that has to be done
     * at the startup of the activity. The activity can be startet in view ( for viewing a flight and maybe edit it)
     * and in create mode(for creating a new flight).
     *
     * @param savedInstanceState
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_view_activity);

        //set the headerString
        setHeaderString(R.string.viewFlight);
        this.progressBarFlightsActivity=findViewById(R.id.progressBarFlightsActivity);
        this.progressBarFlightsActivity.setVisibility(View.GONE);

        //Link the custom BackButton and trash button for admins
        backButton = (ImageButton) findViewById(R.id.backbuttonFlightCreate);
        backButton.setOnClickListener(this);
        trashButton = (ImageButton) findViewById(R.id.trashButtonFlight);
        trashButton.setOnClickListener(this);
        trashButton.setVisibility(View.GONE);

        //Link the FlightsSpinner
        droneSpinner = (Spinner) findViewById(R.id.chooseDroneViewFlight);
        droneSpinner.setOnItemSelectedListener(this);
        //Receve all drones and add them to the spinner
        dataAdapterDrone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, droneSpinnerData);
        dataAdapterDrone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        droneSpinner.setAdapter(dataAdapterDrone);
        droneSpinner.setEnabled(false);

        //link the ChecklistSpinner
        checklistSpinner = (Spinner) findViewById(R.id.checkListFlightSpinner);
        checklistSpinner.setOnItemSelectedListener(this);
        //Receve all drones and add them to the spinner
        dataAdapterChecklist = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, checklistSpinnerData);
        dataAdapterChecklist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistSpinner.setAdapter(dataAdapterChecklist);
        checklistSpinner.setEnabled(false);

        //link the pilot spinner
        pilotSpinner = (Spinner) findViewById(R.id.pilotSpinner);
        pilotSpinner.setOnItemSelectedListener(this);
        //Receve all drones and add them to the spinner
        dataAdapterPilot = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pilotSpinnerData);
        dataAdapterPilot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilotSpinner.setAdapter(dataAdapterPilot);
        pilotSpinner.setEnabled(false);

        //Link the button
        showDJIFlightlog=findViewById(R.id.showDJIFlightlog);
        showDJIFlightlog.setOnClickListener(this);

        this.saveFlightPersonalChecklist = findViewById(R.id.saveFlightPersonalChecklist);
        this.saveFlightPersonalChecklist.setOnClickListener(this);

        //Link the floating action buttons
        this.fabButtonEdit = (FloatingActionButton) findViewById(R.id.fabButtonEdit);
        this.fabButtonEdit.setOnClickListener(this);
        this.fabButtonCancel = (FloatingActionButton) findViewById(R.id.fabButtonCancel);
        this.fabButtonCancel.setOnClickListener(this);
        this.fabButtonConfirmEdit = (FloatingActionButton) findViewById(R.id.fabButtonConfirmEdit);
        this.fabButtonConfirmEdit.setOnClickListener(this);
        this.fabButtonConfirmEdit.setVisibility(View.GONE);

        //Link the buttons
        this.setDateTimeButton = findViewById(R.id.setDateTimeButton);
        this.setDateTimeButton.setOnClickListener(this);
        this.uploadDjiFlightlogButton=findViewById(R.id.uploadDjiFlightlogButton);
        this.uploadDjiFlightlogButton.setOnClickListener(this);
        //TableRow Link
        this.flightChecklistElementsLayout = findViewById(R.id.flightChecklistElementsLayout);

        //constraintlayout link
        this.constraintLayout=findViewById(R.id.constraintLayout);
        //Link Textviews
        this.droneViewFlightlocation = (TextView) findViewById(R.id.droneViewFlightlocation);
        this.droneViewDate = (TextView) findViewById(R.id.droneViewDate);
        this.droneViewFlighttimeBegin = (TextView) findViewById(R.id.droneViewFlighttimeBegin);
        this.droneViewFlighttimeEnd = (TextView) findViewById(R.id.droneViewFlighttimeEnd);
        this.flightDescription = (TextView) findViewById(R.id.flightDescription);
        this.flightDescription.setOnClickListener(this);
        this.flightExtraDescription = (TextView) findViewById(R.id.flightExtraDescription);
        this.flightExtraDescription.setOnClickListener(this);
        this.noTimeSelected = findViewById(R.id.noTimeSelected);
        this.noTimeSelected.setVisibility(View.GONE);
        this.flightPilotText=findViewById(R.id.flightPilotText);


        //get the extras from the fragment
        this.extras = getIntent().getExtras();
        //find the users id
        this.pilot_id = extras.getString("pilot_id");
        this.roleBit = extras.getString("roleBit");
        //if the user is not an admin, hide the spinner(PILOT)
        if(this.roleBit.equals("0")){
            this.pilotSpinner.setVisibility(View.GONE);
            this.flightPilotText.setVisibility(View.GONE);
        }

        //if the view is loaded for viewing
       if(this.extras.get("viewOrCreate").equals("view")) {
           this.progressBarFlightsActivity.setVisibility(View.VISIBLE);
           this.viewFlightRequest=true;
           this.setDateTimeButtonWasPressed=false;
           //Change the editModes from the textfields to false because on startup
           changeEditMode(droneViewFlightlocation, false);
           changeEditMode(flightDescription,false);
           changeEditMode(flightExtraDescription,false);
           setDateTimeButton.setVisibility(View.GONE);
           //load the Intent ID from the fragment class flights_fragment
           this.intentId = extras.getString("id");
           //Network request for all checklist items
           getAllChecklist();
       }else if(this.extras.get("viewOrCreate").equals("create")){//if the view is created for creating a flight
           this.trashButton.setVisibility(View.GONE);
           this.createFlightRequest=true;
           setHeaderString(R.string.newFlight);
           fabButtonConfirmEdit.setVisibility(View.VISIBLE);
           fabButtonCancel.setVisibility(View.GONE);
           fabButtonEdit.setVisibility(View.GONE);
           checklistSpinner.setEnabled(true);
           pilotSpinner.setEnabled(true);
           this.userInEditMode = true;
           noTimeSelected.setVisibility(View.VISIBLE);
           getAllChecklist();

           //Enable the Dialogs and give them the current date
           Date date = new Date();
           LocalDate dateToday = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
           this.year = dateToday.getYear();
           this.month = dateToday.getMonthValue();
           this.day = dateToday.getDayOfMonth();
           encryptDate(this.year,this.month,this.day);
           droneViewDate.setText(this.changedDate);
           //Enable the Dialogs and give them the current time
            this.hour = 12;
            this.minute = 0;
            changedTimeBeginTime = encryptTime(this.hour, this.minute);
            changedTimeEndTime = changedTimeBeginTime;
            droneViewFlighttimeBegin.setText(changedTimeBeginTime);
            droneViewFlighttimeEnd.setText(changedTimeEndTime);
        }
    }

    /**
     * The onclick method manages all user Interactions.
     * Result from user interactions is controlled here. The Check if variables defined in the header
     * give the method additional information if the user wants to edit or create a flight. This is
     * decided from outside the view_activity.
     *
     * @param view the view that is choosen by the user(MUST have a .setOnClickListener to be considered in the method!)
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        if (view.equals(backButton)) {
            this.finish();
        } else if (view.equals(fabButtonEdit)) {
            /*The user enters the edit mode, all fields become visible
             and the Edit variable is set to true!*/
            setHeaderString(R.string.editFlight);
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonConfirmEdit.setVisibility(View.VISIBLE);
            this.fabButtonCancel.setVisibility(View.VISIBLE);
            this.backButton.setVisibility(View.GONE);
            changeEditMode(droneViewFlightlocation, true);
            changeEditMode(flightDescription,true);
            changeEditMode(flightExtraDescription,true);
            this.droneSpinner.setEnabled(true);
            this.checklistSpinner.setEnabled(true);
            this.pilotSpinner.setEnabled(true);
            this.userInEditMode = true;
            this.trashButton.setVisibility(View.VISIBLE);
            this.setDateTimeButton.setVisibility(View.VISIBLE);
        } else if (view == fabButtonCancel) {
            //delete all user Input (restart the activity)
            recreate();

        } else if (view == fabButtonConfirmEdit) {
            if (createFlightRequest) {
                this.progressBarFlightsActivity.setVisibility(View.VISIBLE);
                if(checkAllRequiredTextfieldsareFull()) {
                    //user is creating a flight
                    String sendLocation = this.droneViewFlightlocation.getText().toString();
                    String sendFlightDescription = this.flightDescription.getText().toString();
                    sendAddedFlight(sendLocation,sendFlightDescription);
                }
            } else {
                if(checkAllRequiredTextfieldsareFull()) {
                    this.progressBarFlightsActivity.setVisibility(View.VISIBLE);
                    //the user is editing a flight and confirmes the edit
                    setHeaderString(R.string.viewFlight);
                    this.backButton.setVisibility(View.VISIBLE);
                    this.fabButtonEdit.setVisibility(View.VISIBLE);
                    this.fabButtonConfirmEdit.setVisibility(View.GONE);
                    this.trashButton.setVisibility(View.GONE);
                    changeEditMode(droneViewFlightlocation, false);
                    changeEditMode(flightDescription,false);
                    this.droneSpinner.setEnabled(false);
                    this.userInEditMode = false;
                    this.setDateTimeButton.setVisibility(View.GONE);
                    this.flightExtraDescription.setEnabled(false);
                    this.checklistSpinner.setEnabled(false);
                    this.pilotSpinner.setEnabled(false);
                    String sendLocation = this.droneViewFlightlocation.getText().toString();
                    String sendFlightDescription = this.flightDescription.getText().toString();
                    sendEditedFlight(sendLocation,sendFlightDescription);
                }
            }
        }else if(view == trashButton && this.extras.get("viewOrCreate").equals("view")){
            this.progressBarFlightsActivity.setVisibility(View.VISIBLE);
            //create dialog
            AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
            saveNameAlert.setMessage("Wollen sie den Flug wirklich löschen?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteThisFlightAndExitThisActivity();
                        }
                    })
                    .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            //if the view is in edit mode and the date is choosen
        } else if (view == setDateTimeButton && userInEditMode) {
            //empty the old data
            droneSpinnerData.clear();
            droneSpinner.setEnabled(false);
            this.setDateTimeButtonWasPressed=true;
            dataAdapterDrone.notifyDataSetChanged();
            openDateDialog();
        }else if (view.equals(this.uploadDjiFlightlogButton)){
            //start the upload  browser
            Intent djiUploadActivity = new Intent(this, Flightlog_upload_activity.class);
            djiUploadActivity.putExtra("id",this.intentId);
            startActivity(djiUploadActivity);
        }else if(view.equals(showDJIFlightlog)){
            //start the show activity
            Intent djiShowFlightlog = new Intent(this, Show_flightlog_activity.class);
            djiShowFlightlog.putExtra("id",this.intentId);
            startActivity(djiShowFlightlog);
        }else if(view.equals(this.saveFlightPersonalChecklist)){
            //id of item
            int[] idlistOfallCheckboxItems = new int[checkBoxList.size()];
            boolean[] isActiveCheckboxes = new boolean[checkBoxList.size()];
            int index = 0;
            for (Map.Entry<Integer, CheckBox> mapEntry : checkBoxList.entrySet()) {
                idlistOfallCheckboxItems[index] = mapEntry.getKey();
                //boolean isChecked
                isActiveCheckboxes[index] = mapEntry.getValue().isChecked();
                index++;
            }
            //string comment
            String[] stringComments = new String[checkBoxList.size()];
            int i = 0;
            for (Map.Entry<Integer, TextView> mapEntry : textViewList.entrySet()) {
                stringComments[i] = mapEntry.getValue().getText().toString();
                i++;
            }

            getChecklistFlightState(idlistOfallCheckboxItems,isActiveCheckboxes,stringComments);
        }
    }



    //---------------------------- Open dialog methods ------------------------------------

    /**
     * this opens a new Date Picker dialog
     */
    private void openDateDialog(){
        //decrypt the given date to display the dialog
        decryptDate(this.changedDate);
        //Create a new Dialog
        dateDialog = new DatePickerDialog(Flights_view_activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //As described in the Android SDK, months are indexed starting at 0
                month++;
                //Encrypt the choosed date
                //Check if the date needs a zero infront of it
                //add it if it is necessary
                encryptDate(year, month, dayOfMonth);
                //Set the textfield changes
                droneViewDate.setText(changedDate);

                //open the next dialog
                openTimeBeginDialog();

            }
        }, this.year, this.month, this.day);
        dateDialog.setMessage("Datum");
        dateDialog.show();
    }

    /**
     * this opens a new dialog for the begin time
     */
    private void openTimeBeginDialog(){
        //decrypt the time for displaying in the dialog
        decryptTime(this.changedTimeBeginTime);
        //Create a new dialog for Picking the time
        timeDialogFlightBegin = new TimePickerDialog((Flights_view_activity.this), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                changedTimeBeginTime = encryptTime(hourOfDay, minute);
                droneViewFlighttimeBegin.setText(changedTimeBeginTime);

                //open the next dialog
                openTimeEndDialog();
            }
        }, this.hour, this.minute, true);

        timeDialogFlightBegin.setMessage("Startzeit");
        timeDialogFlightBegin.show();
    }

    /**
     * this opens a new time end dialog
     */
    private void openTimeEndDialog(){
        //decrypt the time for displaying in the dialog
        decryptTime(this.changedTimeEndTime);

        //Create a new dialog for Picking the time
        timeDialogFlightEnd = new TimePickerDialog((Flights_view_activity.this), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                changedTimeEndTime = encryptTime(hourOfDay, minute);
                droneViewFlighttimeEnd.setText(changedTimeEndTime);
                //is the endtime after the begin time?

                //sent network request for all avalable flights
                updateAvailableDronesRequest();
            }
        }, this.hour, this.minute, true);
        timeDialogFlightEnd.setMessage("Landezeit");
        timeDialogFlightEnd.show();
    }

    /**
     * this updates all avalable drones at the specific time
     */
    private void updateAvailableDronesRequest(){
        if(endtimeIsAfterBeginTime(changedTimeBeginTime,changedTimeEndTime)) {
            if (setDateTimeButtonWasPressed)
                this.droneSpinner.setEnabled(true);
            noTimeSelected.setVisibility(View.GONE);
            getAvalableDrones();

        }else{
            this.droneSpinner.setEnabled(false);
            //show snackbar that time does not match
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "Endzeit liegt vor oder während der Startzeit", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    //---------------------------- Date/Time Picker methods --------------------------------
    //------------- ALL METHODS CONTROL THE CONVERSION FROM JAVA TO DATABASE DATES ----------------

    /**
     * Decrypts the date into the 3 vatiables Day Month and year
     *
     * @param recevedDate the encrypted date from the database
     */
    private void decryptDate(String recevedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-LL-dd", Locale.GERMAN);
        LocalDate date = LocalDate.parse(recevedDate, formatter);
        this.day = date.getDayOfMonth();
        this.month = date.getMonthValue();
        this.month--;//because the month start at 0
        this.year = date.getYear();
    }

    /**
     * Encrypts the data from the dialog with the user to a database frendly format!
     *
     * @param year       the choosen year
     * @param month      the choosen month
     * @param dayOfMonth the choosen day
     */
    private void encryptDate(int year, int month, int dayOfMonth) {
        if (month <= 9 && dayOfMonth <= 9) {
            this.changedDate = year + "-" + "0" + month + "-" + "0" + dayOfMonth;
        } else if (month <= 9) {
            this.changedDate = year + "-" + "0" + month + "-" + dayOfMonth;
        } else if (dayOfMonth <= 9) {
            this.changedDate = year + "-" + month + "-" + "0" + dayOfMonth;
        } else {
            this.changedDate = year + "-" + month + "-" + dayOfMonth;
        }
    }

    /**
     * Decrypts the Time intor the 2 variables hour and minute from the database datatype
     *
     * @param recevedTime
     */
    private void decryptTime(String recevedTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMAN);
        LocalTime time = LocalTime.parse(recevedTime, timeFormatter);
        this.hour = time.getHour();
        this.minute = time.getMinute();
    }

    /**
     * Encrypts the time because the Dialog does not give a 0 back if the value is under 10
     * This is needed for the database answer from the app(Cant read java datatypes!)
     *
     * @param hourOfDay the given hour
     * @param minute    the given minute
     */
    private String encryptTime(int hourOfDay, int minute) {
        String changedTime;

        if (hourOfDay <= 9 && minute <= 9) {
            changedTime = "0" + hourOfDay + ":" + "0" + minute + ":" + "00";
        } else if (hourOfDay <= 9) {
            changedTime = "0" + hourOfDay + ":" + minute + ":" + "00";
        } else if (minute <= 9) {
            changedTime = hourOfDay + ":" + "0" + minute + ":" + "00";
        } else {
            changedTime = hourOfDay + ":" + minute + ":" + "00";
        }

        return changedTime;
    }

    /**
     * this checks if the endtime is after the begin time
     * @param beginTime self explaining
     * @param endTime self explaining
     * @return if the endtime is after the begin time
     */
     boolean endtimeIsAfterBeginTime(String beginTime, String endTime){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMAN);
        LocalTime time = LocalTime.parse(beginTime, timeFormatter);
        int beginHour=time.getHour();
        int beginMinute=time.getMinute();
        //Get the endTime
        time = LocalTime.parse(endTime, timeFormatter);
        int endHour = time.getHour();
        int endMinute=time.getMinute();
        if(beginHour<endHour){
            return true;//Hour is smaller
        }else if(beginHour==endHour&&beginMinute<endMinute){
            return true;//Minute is smaller and hour is the same
        }else {
            return false;//End is set before begin
        }

    }

    //---------------------------- Custom Spinner Methods -------------------------------------------------

    /**
     * not used
     * @param arg0
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {

    }

    /**
     * not used
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * this gets the selected id-item
     * @return the id of the item
     */
    private int getselectedItemIdDroneSpinner(){
        return this.droneIdList.get(droneSpinner.getSelectedItemPosition());
    }


    /**
     * this gets the selected id-item
     * @return the id of the item
     */
    private int getselectedItemIdSpinnerChecklist(){
        return this.checklistIdList.get(checklistSpinner.getSelectedItemPosition());
    }


    /**
     * this gets the selected id-item
     * @return the id of the item
     */
    private int getselectedItemIdSpinnerPilot(){
        if(roleBit.equals("0")){
            return Integer.parseInt(pilot_id);//if the user is not an admin, the Pilot spinner is empty!
        }else{
            return this.pilotIdList.get(pilotSpinner.getSelectedItemPosition());
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
        TextView header = (TextView) findViewById(R.id.headerFlightCreate);
        header.setText(headerString);
    }

    /**
     * This method changes the edit mode of a view
     *
     * @param view          the view to be changed
     * @param enableDisable the edit mode enable(true) or disable(false)
     */
    private void changeEditMode(TextView view, boolean enableDisable) {
        view.setFocusable(enableDisable);
        view.setFocusableInTouchMode(enableDisable);
        view.setClickable(enableDisable);
    }

    /**
     * this checks if all reqired textfields are full
     * @return if the textfields are full
     */
    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.flightDescription.getText())){
            this.flightDescription.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.droneViewFlightlocation.getText())){
            this.droneViewFlightlocation.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }
    //------------------------- Network Methods ----------------------------------------------------

    /**
     *
     * @param results The results from the request in a List of Maps. Each Map
     * represents an object in the data array received from the web server.
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag) {
        switch (requestIdentificationTag)
        {
            case ChecklistRequest.ALL_CHECKLISTS_TAG:
                processAllChecklistsResult(results);
                break;
            case FlightRequest.AVAILABLE_DRONES_TAG:
                processAvailableDronesResult(results);
                break;
            case FlightRequest.SHOW_FLIGHT_TAG:
                processShowFlightResult(results);
                break;
            case ChecklistElementRequest.ALL_CHECKLIST_ELEMENTS_TAG:
                processAllChecklistElementsResult(results);
                break;
            case ChecklistRequest.SHOW_CHECKLIST_TAG:
                processShowChecklistElementResult(results);
                break;
            case UserRequest.ALL_USER_DATA_TAG:
                this.progressBarFlightsActivity.setVisibility(View.GONE);
                processAllUserDataResult(results);
                break;
            case FlightRequest.DELETE_FLIGHT_TAG:
                this.progressBarFlightsActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
                break;
            case FlightRequest.ADD_FLIGHT_TAG:
                this.progressBarFlightsActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                this.finish();
                break;
            case FlightRequest.EDIT_FLIGHT_TAG:
                this.progressBarFlightsActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                break;
            case ChecklistRequest.CHECKLIST_FLIGHT_STATE_TAG:
                //set a custom snackbar to show the error
                Snackbar.make(findViewById(android.R.id.content), "Erfolgreich geändert!", LENGTH_LONG)
                        .show();
                break;
        }
    }

    /**
     * this method processes all user data from the network request
     * @param results
     */
    private void processAllUserDataResult(List<Map<String, String>> results) {
        if(this.viewFlightRequest){
            for (Map<String, String> row : results) {
                //for pilot spinner:
                pilotSpinnerData.add(row.get("email_adresse"));
                //save all ids into a list!
                this.pilotSpinnerIndex = Integer.parseInt(row.get("pilot_id"));
                this.pilotIdList.add(pilotSpinnerIndex);
            }
            //tell the ui that the data has changed to show it
            dataAdapterPilot.notifyDataSetChanged();
            pilotSpinner.setSelection(dataAdapterPilot.getPosition(oldflightOwner));
            //sent network request for all avalable flights
            updateAvailableDronesRequest();
        }else if(this.createFlightRequest) {
            for (Map<String, String> row : results) {
                //for pilot spinner:
                pilotSpinnerData.add(row.get("email_adresse"));
                //save all ids into a list!
                this.pilotSpinnerIndex = Integer.parseInt(row.get("pilot_id"));
                this.pilotIdList.add(pilotSpinnerIndex);
            }
            //tell the ui that the data has changed to show it
            dataAdapterPilot.notifyDataSetChanged();
            pilotSpinner.setSelection(pilotIdList.indexOf(Integer.parseInt(this.pilot_id)));
        }
    }

    /**
     * this processes the show checklist element result
     * @param results the network request results
     */
    private void processShowChecklistElementResult(List<Map<String, String>> results) {
            for (Map<String, String> row : results) {
                this.checkedElements = row.get("elements");
            }
            if(this.checkedElements != null) {
                this.checkedElementsSplit = checkedElements.split(",");
            }

        //In the end
        //Get all checklist items and show them at the bottom
        getallChecklistElements();
    }

    /**
     * this processes the all checkliste element result
     * @param results the network request results
     */
    private void processAllChecklistElementsResult(List<Map<String, String>> results) {
        if(checkedElementsSplit!=null) {
            for (Map<String, String> row : results) {
                for (int i = 0; i < checkedElementsSplit.length; i++) {
                    if (checkedElementsSplit[i].equals(row.get("element_id"))) {
                        TableRow tableRow = new TableRow(this);
                        TextView textView = new TextView(this);
                        textView.setText(row.get("bezeichnung"));
                        textView.setTextSize(20);
                        textView.setTextColor(getResources().getColor(R.color.matteBlack));

                        CheckBox box = new CheckBox(this);
                        checkBoxList.put(Integer.parseInt(row.get("element_id")),box);
                        if(checkboxChecked.get(Integer.parseInt(row.get("element_id"))).equals("1")) {
                            box.setChecked(true);
                        }

                        TextView spaceText3 = new TextView(this);
                        tableRow.addView(box);
                        tableRow.addView(textView);
                        tableRow.addView(spaceText3);
                        this.flightChecklistElementsLayout.addView(tableRow);

                        TableRow explanationRow = new TableRow(this);
                        TextView textViewleft = new TextView(this);
                        TextView explainTextView = new TextView(this);
                        TextView textViewRight = new TextView(this);
                        explainTextView.setText(row.get("erklaerung"));
                        explanationRow.addView(textViewleft);
                        explanationRow.addView(explainTextView);
                        explanationRow.addView(textViewRight);
                        this.flightChecklistElementsLayout.addView(explanationRow);

                        TableRow textInputRow = new TableRow(this);
                        TextView textView1 = new TextView(this);
                        EditText elementComment = new EditText(this);
                        elementComment.setText(textviewText.get(Integer.parseInt(row.get("element_id"))));
                        textViewList.put(Integer.parseInt(row.get("element_id")),elementComment);
                        textInputRow.addView(textView1);
                        textInputRow.addView(elementComment);
                        this.flightChecklistElementsLayout.addView(textInputRow);
                        //Add space between rows of checklists!
                        TableRow space = new TableRow(this);
                        space.setBackgroundColor(getResources().getColor(R.color.gray));
                        TextView spaceText = new TextView(this);
                        spaceText.setText(" ");
                        spaceText.setTextSize(15);
                        spaceText.setTextColor(getResources().getColor(R.color.matteBlack));
                        spaceText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        space.setMinimumHeight(1);
                        this.flightChecklistElementsLayout.addView((space));
                    }
                }
            }
        }
    }


    /**
     * this processes the show flight result
     * @param results the network request results
     */
    private void processShowFlightResult(List<Map<String, String>> results) {
        //1 map:
        for (int i = 0; i < results.size(); i++) {
            if (i == 0) {
                Map<String, String> row = results.get(i);
                //pre set the content
                loadDroneContentIntoViews(row.get("drohnen_modell"), row.get("einsatzort_name"), row.get("flugdatum"), row.get("einsatzbeginn"), row.get("einsatzende"), row.get("flugbezeichnung"), row.get("besondere_vorkommnisse"));
                this.choosenDroneFromThePast = row.get("drohnen_modell");
                //for drone spinner:
                droneSpinnerData.add(this.choosenDroneFromThePast);
                //tell the ui that the data has changed to show it
                dataAdapterDrone.notifyDataSetChanged();
                //save all ids into a list!
                if (row.get("drohne_id").equals("null")) {
                    //if the flight has no drone, because it was Stolen by an admin!
                    this.droneIndex = 0;
                    this.droneIdList.add(droneIndex);
                } else {
                    this.droneIndex = Integer.parseInt(row.get("drohne_id"));
                    this.droneIdList.add(droneIndex);
                }
                //pre set the spinner
                droneSpinner.setSelection(dataAdapterDrone.getPosition(row.get("drohnen_modell")));
                //pre set the spinner
                checklistSpinner.setSelection(dataAdapterChecklist.getPosition(row.get("bezeichnung")));
                //pre set the spinner
                this.oldflightOwner = row.get("email_adresse");
                this.choosenChecklist = row.get("bezeichnung");
                //set the date/time variable
                this.changedDate = row.get("flugdatum");
                this.changedTimeBeginTime = row.get("einsatzbeginn");
                this.changedTimeEndTime = row.get("einsatzende");

                //Check if the flight is from the past and if the user is able to edit it
                if (this.roleBit.equals("0") && !Flights_fragment.isFlightFromTheFuture(changedDate)) {
                    this.fabButtonEdit.setVisibility(View.GONE);
                    this.fabButtonCancel.setVisibility(View.GONE);
                }
            }else{
                //all other maps:(checklist elements)
                Map<String, String> row = results.get(i);
                //checkboxen
                checkboxChecked.put(Integer.parseInt(row.get("element_id")),row.get("angekreuzt"));
                //textfelder
                textviewText.put(Integer.parseInt(row.get("element_id")),row.get("kommentar"));

            }
        }


        if(viewFlightRequest){
            getAllUserData();
        }else{
            //sent network request for all avalable flights
            updateAvailableDronesRequest();
        }

    }

    /**
     * this processes the avalable drones result
     * @param results the network request results
     */
    private void processAvailableDronesResult(List<Map<String, String>> results) {
        if(this.viewFlightRequest){
            for (Map<String, String> row : results) {
                //for drone spinner:
                droneSpinnerData.add(row.get("drohnen_modell"));
                //save all ids into a list!
                this.droneIndex = Integer.parseInt(row.get("drohne_id"));
                this.droneIdList.add(droneIndex);
            }

            //tell the ui that the data has changed to show it
            dataAdapterDrone.notifyDataSetChanged();

            showChecklist();
        }else if(this.createFlightRequest) {
            for (Map<String, String> row : results) {
                //for drone spinner:
                droneSpinnerData.add(row.get("drohnen_modell"));
                //save all ids into a list!
                this.droneIndex = Integer.parseInt(row.get("drohne_id"));
                this.droneIdList.add(droneIndex);
            }

            //tell the ui that the data has changed to show it
            dataAdapterDrone.notifyDataSetChanged();
        }
    }

    /**
     * this processes all checklists result
     * @param results the network request results
     */
    private void processAllChecklistsResult(List<Map<String, String>> results) {
        if(this.viewFlightRequest){
            for (Map<String, String> row : results) {
                //for drone spinner:
                checklistSpinnerData.add(row.get("bezeichnung"));
                //save all ids into a list!
                this.checklistIndex = Integer.parseInt(row.get("checkliste_name_id"));
                this.checklistIdList.add(checklistIndex);
            }
            //tell the ui that the data has changed to show it
            dataAdapterChecklist.notifyDataSetChanged();

            //receve the next data
            getFlight();
        }else if(this.createFlightRequest) {
            for (Map<String, String> row : results) {
                //for drone spinner:
                checklistSpinnerData.add(row.get("bezeichnung"));
                //save all ids into a list!
                this.checklistIndex = Integer.parseInt(row.get("checkliste_name_id"));
                this.checklistIdList.add(checklistIndex);
            }
            //tell the ui that the data has changed to show it
            dataAdapterChecklist.notifyDataSetChanged();

            //load the pilot spinner

            getAllUserData();
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
        this.progressBarFlightsActivity.setVisibility(View.GONE);
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
        this.progressBarFlightsActivity.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                    .show();
    }

    /**
     * this gets all checklists from the network
     */
    private void getAllChecklist(){
        try {
            ChecklistRequest allChecklistRequest = new ChecklistRequest(getApplicationContext());
            allChecklistRequest.allChecklists(0);
            HSDroneLogNetwork.networkInstance().startRequest(this, allChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllChecklist();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this sents an added flight via the network
     * @param sendLocation the flight location
     *
     * @param sendFlightDescription the flight description
     */
    private void sendAddedFlight(String sendLocation, String sendFlightDescription){
        try {
            FlightRequest createFlight = new FlightRequest(getApplicationContext());
            createFlight.addFlight(
                    getselectedItemIdSpinnerPilot(),
                    getselectedItemIdDroneSpinner(),
                    sendLocation,
                    this.changedDate,
                    this.changedTimeBeginTime,
                    this.changedTimeEndTime,
                    getselectedItemIdSpinnerChecklist(),
                    sendFlightDescription,
                    this.flightExtraDescription.getText().toString()
            );
            HSDroneLogNetwork.networkInstance().startRequest(this, createFlight);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendAddedFlight(sendLocation,sendFlightDescription);
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this selds an edited flight via the network
     * @param sendLocation the location
     * @param sendFlightDescription the flight description
     */
    private void sendEditedFlight(String sendLocation, String sendFlightDescription){
        try {
            FlightRequest editFlightRequest = new FlightRequest(getApplicationContext());
            editFlightRequest.editFlight(Integer.parseInt(this.intentId),
                    getselectedItemIdSpinnerPilot(),
                    getselectedItemIdDroneSpinner(),
                    sendLocation,
                    this.changedDate,
                    this.changedTimeBeginTime,
                    this.changedTimeEndTime,
                    getselectedItemIdSpinnerChecklist(),
                    sendFlightDescription,
                    this.flightExtraDescription.getText().toString()
            );
            HSDroneLogNetwork.networkInstance().startRequest(this, editFlightRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendEditedFlight(sendLocation,sendFlightDescription);
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this gets the flight sate via the network
     * @param idlistOfallCheckboxItems the list of all checkboxes
     * @param isActiveCheckboxes the list of active checkboxes
     * @param stringComments the string comments
     */
    private void getChecklistFlightState(int[] idlistOfallCheckboxItems,boolean[] isActiveCheckboxes, String[] stringComments){
        try {
            ChecklistRequest checklistRequest = new ChecklistRequest(this.getApplicationContext());
            checklistRequest.checklistFlightState(Integer.parseInt(intentId),idlistOfallCheckboxItems,isActiveCheckboxes,stringComments);
            HSDroneLogNetwork.networkInstance().startRequest(this, checklistRequest);
        } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getChecklistFlightState(idlistOfallCheckboxItems,isActiveCheckboxes,stringComments);
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this gets the avalable drones
     */
    private void getAvalableDrones(){
        try {
            this.setDateTimeButtonWasPressed=false;
            FlightRequest allDronesRequest = new FlightRequest(getApplicationContext());
            allDronesRequest.avalibleDrones(this.changedTimeBeginTime, this.changedTimeEndTime, this.changedDate);
            HSDroneLogNetwork.networkInstance().startRequest(this, allDronesRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAvalableDrones();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this gets all checklist elements
     */
    private void getallChecklistElements(){
        try {
        ChecklistElementRequest specificChecklistRequest = new ChecklistElementRequest(this.getApplicationContext());
        specificChecklistRequest.allChecklistElements();
        HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
    } catch (IllegalStateException e) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getallChecklistElements();
                        }
                    });
            snackbar.show();
    }
    }

    /**
     * this gets all user data
     */
    private void getAllUserData(){
        try {
            UserRequest userRequest = new UserRequest(this.getApplicationContext());
            userRequest.allUserData();
            HSDroneLogNetwork.networkInstance().startRequest(this, userRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllUserData();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this shows all checklists
     */
    private void showChecklist(){
        try {
            ChecklistRequest specificChecklistRequest = new ChecklistRequest(this.getApplicationContext());
            specificChecklistRequest.showChecklist(getselectedItemIdSpinnerChecklist());
            HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showChecklist();
                        }
                    });
            snackbar.show();
        }
    }

    /**
     * this gets a flight
     */
    private void getFlight(){
        try {
            FlightRequest specificFlightRequest = new FlightRequest(getApplicationContext());
            specificFlightRequest.showFlight(Integer.parseInt(this.intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, specificFlightRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFlight();
                        }
                    });
            snackbar.show();
        }
    }

    //-------------------------- Handle Content Methods ----------------------------------------------

    /**
     * This method is loading the Content into the views that are definded by the XML file.
     * This happens at various points because the Textviews must be filled after the Inflation of the
     * layout!
     *
     * @param drone          the drone of the flight
     * @param flightLocation the flightLocation of the flight
     * @param date           the date of the flight
     * @param flightBegin    the flightBegin of the flight
     * @param flightEnd      the flightEnd of the flight
     */
    private void loadDroneContentIntoViews(String drone, String flightLocation, String date, String flightBegin, String flightEnd,String flightDescription,String flightExtraDescription) {
        this.droneViewFlightlocation.setText(flightLocation);
        this.droneViewDate.setText(date);
        this.droneViewFlighttimeBegin.setText(flightBegin);
        this.droneViewFlighttimeEnd.setText(flightEnd);
        this.flightDescription.setText(flightDescription);
        this.flightExtraDescription.setText(flightExtraDescription);
    }

    /**
     * this deletes the current flight and finishes the activity
     */
    private void deleteThisFlightAndExitThisActivity(){
        try {
            FlightRequest deleteFlightRequest = new FlightRequest(getApplicationContext());
            deleteFlightRequest.deleteFlight(Integer.parseInt(intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, deleteFlightRequest);
        } catch (IllegalStateException e) {
            this.progressBarFlightsActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteThisFlightAndExitThisActivity();
                        }
                    });
            snackbar.show();
        }
    }
    //---------------------------- Give back Methods -----------------------------------------------

    /**
     * this refreshes the return page
     */
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }
}
