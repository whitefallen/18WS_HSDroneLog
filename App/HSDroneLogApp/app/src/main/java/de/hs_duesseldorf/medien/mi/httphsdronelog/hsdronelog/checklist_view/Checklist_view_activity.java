package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.checklist_view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.BatteryRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistElementRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class Checklist_view_activity extends AppCompatActivity implements View.OnClickListener, de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener {
    //IV
    private String intentId;
    private String pilotid;
    private String roleBit;
    private ImageButton backButton;
    private ImageButton trashButtonChecklist;
    private TextView name;
    private TextView explanation;
    private TextView headerChecklist;
    private Button createNewChecklistElementButton;

    //FloatingactionButtons
    private Bundle extras;
    private FloatingActionButton fabButtonEdit;//the floating action button for the edit of the flight
    private FloatingActionButton fabButtonCancel;//the floating action button for cancel that is hidden
    private FloatingActionButton fabButtonConfirmEdit;//the floating action button for cancel that is hidden

    //Checkboxes
    private TableLayout checklistCheckboxTable;
    private String checkedElements;//all drones that are already checked
    private Map<String, String> checkedElementsMap = new HashMap<String, String>();//the checked items storage
    private String[] checkedElementsSplit;
    private String returnString;
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();

    //ImageButtons
    private List<ImageButton> allImageButtons = new ArrayList<>();

    private ProgressBar progressBarChecklistActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_view_activity);

        //link the image back button
        this.backButton = (ImageButton) findViewById(R.id.backbuttonChecklistCreate);
        this.backButton.setOnClickListener(this);
        this.trashButtonChecklist = findViewById(R.id.trashButtonChecklist);
        this.trashButtonChecklist.setOnClickListener(this);
        this.trashButtonChecklist.setVisibility(View.GONE);
        //link the edit buttons
        this.fabButtonEdit = findViewById(R.id.fabButtonEditChecklist);
        this.fabButtonEdit.setOnClickListener(this);
        this.fabButtonCancel =findViewById(R.id.fabButtonCancelChecklist);
        this.fabButtonCancel.setOnClickListener(this);
        this.fabButtonConfirmEdit = findViewById(R.id.fabButtonConfirmChecklistEdit);
        this.fabButtonConfirmEdit.setOnClickListener(this);
        this.createNewChecklistElementButton=findViewById(R.id.createNewChecklistElementButton);
        this.createNewChecklistElementButton.setOnClickListener(this);
        //Link the textviews
        this.name = (TextView) findViewById(R.id.nameOfChecklist);
        this.name.setOnClickListener(this);
        this.explanation = (TextView) findViewById(R.id.checklistExplanation);
        this.name.setOnClickListener(this);
        this.headerChecklist = findViewById(R.id.headerChecklist);

        this.progressBarChecklistActivity=findViewById(R.id.progressBarChecklistActivity);
        this.progressBarChecklistActivity.setVisibility(View.GONE);
        //Link the Table Layout
        this.checklistCheckboxTable = findViewById(R.id.checklistTableLayout);
        //get the extras!
        this.extras = getIntent().getExtras();
        this.intentId = extras.getString("checklistid");
        this.pilotid = extras.getString("pilotid");
        this.roleBit = extras.getString("roleBit");
        if(this.extras.getString("viewOrCreate").equals("view")) {
            if(this.roleBit.equals("0")){
                //is normal user and can not edit/delete
                this.fabButtonEdit.setVisibility(View.GONE);
                this.fabButtonCancel.setVisibility(View.GONE);
            }
            //set visibilities
            this.fabButtonConfirmEdit.setVisibility(View.GONE);
            this.headerChecklist.setText("Checkliste ansehen");
            this.name.setEnabled(false);
            this.explanation.setEnabled(false);
            //ask for the checklist data from the network
            showChecklist();
        }else if(this.extras.getString("viewOrCreate").equals("create")) {
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonCancel.setVisibility(View.GONE);
            this.headerChecklist.setText("Checkliste erstellen");
            allChecklistElements();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(backButton)) {
            this.finish();
        }else if(v.equals(fabButtonEdit)){
            this.backButton.setVisibility(View.GONE);
            this.fabButtonEdit.setVisibility(View.GONE);
            this.fabButtonConfirmEdit.setVisibility(View.VISIBLE);
            this.fabButtonCancel.setVisibility(View.VISIBLE);
            this.trashButtonChecklist.setVisibility(View.VISIBLE);
            changeEditModeCheckboxes(true);
            this.name.setEnabled(true);
            this.explanation.setEnabled(true);
            this.headerChecklist.setText("Checkliste bearbeiten");
            //activate all imageButtons(Make them visible)
            for(int i = 0;i<allImageButtons.size();i++){
                allImageButtons.get(i).setVisibility(View.VISIBLE);
            }

        }else if(v.equals(fabButtonCancel)){
            this.recreate();
        }else if(v.equals(fabButtonConfirmEdit)&&this.extras.getString("viewOrCreate").equals("view")){
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarChecklistActivity.setVisibility(View.VISIBLE);
                //convert to the string
                this.checkedElements = convertMapToStringWithAllOnes(this.checkedElementsMap);

                //send the edited data(User has edited a list!)
                sendEditedChecklist();
                //set everything back
                this.fabButtonConfirmEdit.setVisibility(View.GONE);
                this.fabButtonCancel.setVisibility(View.GONE);
                this.fabButtonEdit.setVisibility(View.VISIBLE);
                this.backButton.setVisibility(View.VISIBLE);
                this.trashButtonChecklist.setVisibility(View.GONE);
                this.name.setEnabled(false);
                this.explanation.setEnabled(false);
                changeEditModeCheckboxes(false);
                this.headerChecklist.setText("Checkliste ansehen");
                //activate all imageButtons(Make them visible)
                for (int i = 0; i < allImageButtons.size(); i++) {
                    allImageButtons.get(i).setVisibility(View.GONE);
                }
            }
        }else if(v.equals(trashButtonChecklist)){
            //the user wants to delete this checklist!(send data)
            //create dialog
            AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
            saveNameAlert.setMessage("Wollen sie die Checkliste wirklich löschen?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            deleteThisAndExitThisActivity();
                        }
                    })
                    .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();


        }else if(v.equals(fabButtonConfirmEdit)&&this.extras.getString("viewOrCreate").equals("create")){
            if(checkAllRequiredTextfieldsareFull()){
                this.progressBarChecklistActivity.setVisibility(View.VISIBLE);
                //convert to the string
                this.checkedElements = convertMapToStringWithAllOnes(this.checkedElementsMap);
                addChecklist();
            }
        }else if(v.equals(createNewChecklistElementButton)){
            Intent droneViewActivity = new Intent(this, ChecklistElement_view_activity.class);
            droneViewActivity.putExtra("viewOrCreate", "create");
            droneViewActivity.putExtra("pilotid", this.pilotid);
            startActivityForResult(droneViewActivity,8);
        }else{
            for(int i=0; i<allImageButtons.size();i++){
                if(v.equals(allImageButtons.get(i))){
                    Intent droneViewActivity = new Intent(this, ChecklistElement_view_activity.class);
                    droneViewActivity.putExtra("viewOrCreate", "view");
                    droneViewActivity.putExtra("pilotid", this.pilotid);
                    droneViewActivity.putExtra("checklistid",String.valueOf(allImageButtons.get(i).getId()));
                    startActivityForResult(droneViewActivity,8);
                }
            }
        }
    }

    private void deleteThisAndExitThisActivity(){
        try {
            ChecklistRequest specificChecklistRequest = new ChecklistRequest(this.getApplicationContext());
            specificChecklistRequest.deleteChecklist(Integer.parseInt(intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteThisAndExitThisActivity();
                        }
                    });
            snackbar.show();
        }
    }

    //------------------------------------ Change XML Methods -----------------------
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


    private void createTableRowAllChecklistElements(TableLayout tl, String text, String id, String explanation){
        TableRow tableRow = new TableRow(this);

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.matteBlack));

        CheckBox box = new CheckBox(this);
        //set the id from the accu
        box.setId(Integer.parseInt(id));
        //add the checkbox to the arraylist!
        this.checkBoxes.add(box);
        //check the list for true checkboxes
        if(checkedElementsMap.get(id).equals("1")){
            box.setChecked(true);
        }else{
            box.setChecked(false);
        }
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                //what happens when the it's unchecked or checked
                if(arg1){
                    //checked
                    checkedElementsMap.replace(String.valueOf(box.getId()),"1");
                }else{
                    //arg1 is unchecked, remove it from the list of checked drones
                    checkedElementsMap.replace(String.valueOf(box.getId()),"0");
                }
            }
        });
        if(this.extras.getString("viewOrCreate").equals("view")){
            box.setEnabled(false);
        }
        //add it to the layout
        tableRow.addView(box);
        tableRow.addView(textView);
        tableRow.setPaddingRelative(20, 15, 20, 15);
        //add edit button
        ImageButton imageButton = new ImageButton(this);
        imageButton.setVisibility(View.GONE);
        imageButton.setOnClickListener(this);
        imageButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        imageButton.setId(Integer.parseInt(id));
        allImageButtons.add(imageButton);
        imageButton.setBackgroundColor(00000000);
        tableRow.addView(imageButton);
        //set 4th item
        TextView spaceText2 = new TextView(this);
        spaceText2.setText(" ");
        tableRow.addView(spaceText2);
        tl.addView(tableRow);
        TableRow explanationRow = new TableRow(this);
        TextView textViewleft=new TextView(this);
        TextView explainTextView = new TextView(this);
        TextView textViewRight = new TextView(this);
        explainTextView.setText(explanation);
        explanationRow.addView(textViewleft);
        explanationRow.addView(explainTextView);
        explanationRow.addView(textViewRight);
        tl.addView(explanationRow);
        //Add space between rows of checklists!
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

    private void changeEditModeCheckboxes(boolean isEnabled){
        for(CheckBox checkBox:this.checkBoxes){
            checkBox.setEnabled(isEnabled);
        }
    }

    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.name.getText())){
            this.name.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.explanation.getText())){
            this.explanation.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }
    //-------------------------------- Network Methods --------------------------------
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag) {
        switch (requestIdentificationTag)
        {
            case ChecklistRequest.SHOW_CHECKLIST_TAG:
                for (Map<String, String> row : results) {
                    this.name.setText(row.get("bezeichnung"));
                    this.explanation.setText(row.get("erklaerung"));
                    this.checkedElements = row.get("elements");
                }
                allChecklistElements();
                break;
            case ChecklistElementRequest.ALL_CHECKLIST_ELEMENTS_TAG:
                this.progressBarChecklistActivity.setVisibility(View.GONE);
                for (Map<String, String> row : results) {
                    this.checkedElementsMap.put(row.get("element_id"), "0");
                }
                //Check if elements are splittable
                if(checkedElements != null) {
                    this.checkedElementsSplit = checkedElements.split(",");
                    //check if the item is in the given splittet string, if so replace the bit string with 1!
                    for (int i = 0; i < this.checkedElementsSplit.length; i++) {
                        if (this.checkedElementsMap.containsKey(this.checkedElementsSplit[i])) {
                            this.checkedElementsMap.replace(this.checkedElementsSplit[i], "1");//replace the item with true
                        }
                    }
                }
                for (Map<String, String> row : results) {
                    createTableRowAllChecklistElements(checklistCheckboxTable,
                            row.get("bezeichnung"),
                            row.get("element_id"),
                            row.get("erklaerung"));
                }
                break;
            case ChecklistRequest.DELETE_CHECKLIST_TAG:
                this.refreshReturnPage();
            case ChecklistRequest.ADD_CHECKLIST_TAG:
                this.refreshReturnPage();
                this.finish();
                break;
            case ChecklistRequest.EDIT_CHECKLIST_TAG:
                this.progressBarChecklistActivity.setVisibility(View.GONE);
                this.refreshReturnPage();
                break;
        }
    }

    @Override
    public void sessionHasExpired(String message, String requestIdentificationTag) {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void requestWasNotSuccessful(String message, String requestIdentificationTag) {
        this.progressBarChecklistActivity.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
                .show();
    }

    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        this.progressBarChecklistActivity.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    private String convertMapToStringWithAllOnes(Map<String, String> map){
        for (Map.Entry entry : map.entrySet()) {
            if(entry.getValue().equals("1")){
                this.returnString = this.returnString + entry.getKey() + ",";
            }
        }
        //delete the last ,
        if(returnString!=null){
            this.returnString = this.returnString.substring(4, this.returnString.length() - 1);
        }else{
            returnString="";
        }
        return returnString;
    }

    private void showChecklist(){
        try {
            this.progressBarChecklistActivity.setVisibility(View.VISIBLE);
            ChecklistRequest specificChecklistRequest = new ChecklistRequest(this.getApplicationContext());
            specificChecklistRequest.showChecklist(Integer.parseInt(intentId));
            HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistActivity.setVisibility(View.GONE);
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

    private void allChecklistElements(){
        try {
            this.progressBarChecklistActivity.setVisibility(View.VISIBLE);
            ChecklistElementRequest checklistElementRequest = new ChecklistElementRequest(this.getApplicationContext());
            checklistElementRequest.allChecklistElements();
            HSDroneLogNetwork.networkInstance().startRequest(this, checklistElementRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allChecklistElements();
                        }
                    });
            snackbar.show();
        }
    }

    private void sendEditedChecklist(){
        try {
            ChecklistRequest specificChecklistRequest = new ChecklistRequest(this.getApplicationContext());
            specificChecklistRequest.editChecklist(Integer.parseInt(intentId),
                    this.name.getText().toString(),
                    this.explanation.getText().toString(),
                    checkedElements);
            HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendEditedChecklist();
                        }
                    });
            snackbar.show();
        }
    }

    private void addChecklist(){
        try {
            ChecklistRequest specificChecklistRequest = new ChecklistRequest(this.getApplicationContext());
            specificChecklistRequest.addChecklist(this.name.getText().toString(),
                    this.explanation.getText().toString(),
                    checkedElements);
            HSDroneLogNetwork.networkInstance().startRequest(this, specificChecklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistActivity.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addChecklist();
                        }
                    });
            snackbar.show();
        }
    }


    //---------------------------- Give back Methods -----------------------------------------------
    //return
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }

    /**
     * Get the result from other activitys
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==8){
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra("refresh").equals("true")){
                    //refresh activity
                    this.recreate();
                }
            }
        }
    }
}
