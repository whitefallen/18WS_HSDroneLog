package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.checklist_view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.R;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.HSDroneLogNetwork;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkListener;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistElementRequest;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.ChecklistRequest;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

public class ChecklistElement_view_activity extends AppCompatActivity implements View.OnClickListener, NetworkListener {

    //IV
    private FloatingActionButton fabButtonConfirmChecklistElementEdit;
    private FloatingActionButton fabButtonCancelChecklistElement;
    private ImageButton trashButtonChecklistElement;
    private TextView nameOfChecklist;
    private TextView checklistElementExplanation;
    private TextView headerChecklistElement;

    //Bundle
    private Bundle extras;
    private String pilotid;
    private String checklistid;
    //Checkboxes
    private TableLayout checklistCheckboxTable;
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private String checkedElements;//all drones that are already checked
    private Map<String, String> checkedElementsMap = new HashMap<String, String>();//the checked items storage
    private String[] checkedElementsSplit;
    private String returnString;
    private ProgressBar progressBarChecklistElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_element_view_activity);

        this.fabButtonConfirmChecklistElementEdit = findViewById(R.id.fabButtonConfirmChecklistElementEdit);
        this.fabButtonConfirmChecklistElementEdit.setOnClickListener(this);
        this.fabButtonCancelChecklistElement = findViewById(R.id.fabButtonCancelChecklistElement);
        this.fabButtonCancelChecklistElement.setOnClickListener(this);
        this.trashButtonChecklistElement = findViewById(R.id.trashButtonChecklistElement);
        this.trashButtonChecklistElement.setOnClickListener(this);
        this.nameOfChecklist = findViewById(R.id.checklistElementName);
        this.checklistElementExplanation=findViewById(R.id.checklistElementExplanation);
        this.headerChecklistElement=findViewById(R.id.headerChecklistElement);
        this.checklistCheckboxTable=findViewById(R.id.checklistElementTableLayout);
        this.progressBarChecklistElement=findViewById(R.id.progressBarChecklistElement);
        this.progressBarChecklistElement.setVisibility(View.GONE);

        this.extras = getIntent().getExtras();
        this.checklistid = extras.getString("checklistid");
        this.pilotid = extras.getString("pilotid");
        if(this.extras.getString("viewOrCreate").equals("create")) {
            this.trashButtonChecklistElement.setVisibility(View.GONE);
            this.headerChecklistElement.setText("Element erstellen");
            allChecklist();
        }else{
            this.headerChecklistElement.setText("Element bearbeiten");
            //load the data
            showChecklistElement();
        }

    }
    //---------------------------------- User Interaction ------------------------------------------
    @Override
    public void onClick(View v) {
        if(v.equals(fabButtonCancelChecklistElement)){
            this.finish();
        }else if(v.equals(fabButtonConfirmChecklistElementEdit)&&this.extras.getString("viewOrCreate").equals("view")){
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarChecklistElement.setVisibility(View.VISIBLE);

                //convert to the string
                this.checkedElements = convertMapToStringWithAllOnes(this.checkedElementsMap);
                //user wants to save the edit
                ChecklistElementRequest checklistElementRequest = new ChecklistElementRequest(this.getApplicationContext());
                checklistElementRequest.editChecklistElement(Integer.parseInt(this.checklistid),
                        nameOfChecklist.getText().toString(),
                        checklistElementExplanation.getText().toString(),
                        this.checkedElements);
                HSDroneLogNetwork.networkInstance().startRequest(this, checklistElementRequest);
                refreshReturnPage();
            }
        }else if(v.equals(fabButtonConfirmChecklistElementEdit)&&this.extras.getString("viewOrCreate").equals("create")){
            if(checkAllRequiredTextfieldsareFull()) {
                this.progressBarChecklistElement.setVisibility(View.VISIBLE);

                //convert to the string
                this.checkedElements = convertMapToStringWithAllOnes(this.checkedElementsMap);
                //user wants to create a new element
                ChecklistElementRequest checklistElementRequest = new ChecklistElementRequest(this.getApplicationContext());
                checklistElementRequest.addChecklistElement(nameOfChecklist.getText().toString(),
                        checklistElementExplanation.getText().toString(),
                        checkedElements);
                HSDroneLogNetwork.networkInstance().startRequest(this, checklistElementRequest);
                refreshReturnPage();
            }
        }else if(v.equals(trashButtonChecklistElement)){
            //create dialog
            AlertDialog.Builder saveNameAlert = new AlertDialog.Builder(this);
            saveNameAlert.setMessage("Wollen sie das Checklistenelement wirklich löschen? Achtung, dies hat Auswirkungen auf alle Checklisten!")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            refreshReturnPage();
                            deleteThisAndExitThisActivity();
                        }
                    })
                    .setNegativeButton(R.string.noDoNotWant, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();


        }
    }
    private void deleteThisAndExitThisActivity(){
        //user wants to delete the selected element
        ChecklistElementRequest checklistElementRequest = new ChecklistElementRequest(this.getApplicationContext());
        checklistElementRequest.deleteChecklistElement(Integer.parseInt(this.checklistid));
        HSDroneLogNetwork.networkInstance().startRequest(this, checklistElementRequest);
    }

    private boolean checkAllRequiredTextfieldsareFull(){
        if(TextUtils.isEmpty(this.nameOfChecklist.getText())){
            this.nameOfChecklist.setError("Pflichtfeld");
            return false;
        }else if(TextUtils.isEmpty(this.checklistElementExplanation.getText())){
            this.checklistElementExplanation.setError("Pflichtfeld");
            return false;
        }else{
            return true;
        }
    }
    //--------------------------------- Network methods --------------------------------------------
    @Override
    public void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag) {
        switch (requestIdentificationTag)
        {
            case ChecklistElementRequest.SHOW_CHECKLIST_ELEMENT_TAG:
                for (Map<String, String> row : results) {
                    this.nameOfChecklist.setText(row.get("bezeichnung"));
                    this.checklistElementExplanation.setText(row.get("erklaerung"));
                    this.checkedElements=row.get("checklists");
                }
                allChecklist();
                break;

            case ChecklistElementRequest.DELETE_CHECKLIST_ELEMENT_TAG:
                this.progressBarChecklistElement.setVisibility(View.GONE);
                this.finish();
                break;
            case ChecklistElementRequest.ADD_CHECKLIST_ELEMENT_TAG:
                this.progressBarChecklistElement.setVisibility(View.GONE);
                this.finish();
                break;
            case ChecklistElementRequest.EDIT_CHECKLIST_ELEMENT_TAG:
                this.progressBarChecklistElement.setVisibility(View.GONE);
                this.finish();
                break;
            case ChecklistRequest.ALL_CHECKLISTS_TAG:
                this.progressBarChecklistElement.setVisibility(View.GONE);

                for (Map<String, String> row : results) {
                    this.checkedElementsMap.put(row.get("checkliste_name_id"), "0");
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
                    createTableRowAllChecklistElements(checklistCheckboxTable,row.get("bezeichnung"),
                            row.get("checkliste_name_id"));

                }

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
        this.progressBarChecklistElement.setVisibility(View.GONE);
        //set a custom snackbar to show the message
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.flightErrorSnackBar)+" " + message, LENGTH_LONG)
                .show();
    }

    @Override
    public void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag) {
        this.progressBarChecklistElement.setVisibility(View.GONE);
        e.printStackTrace();
        //set a custom snackbar to show the error
        Snackbar.make(findViewById(android.R.id.content), message, LENGTH_LONG)
                .show();
    }

    //return
    private void refreshReturnPage() {
        Intent data = new Intent();
        data.putExtra("refresh","true");
        setResult(RESULT_OK, data);
    }

    private void allChecklist(){
        try {
            this.progressBarChecklistElement.setVisibility(View.VISIBLE);

            ChecklistRequest checklistRequest = new ChecklistRequest(this.getApplicationContext());
            checklistRequest.allChecklists(0); //nachladen implementieren
            HSDroneLogNetwork.networkInstance().startRequest(this, checklistRequest);
        } catch (IllegalStateException e) {
            this.progressBarChecklistElement.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allChecklist();
                        }
                    });
            snackbar.show();
        }
    }

    private void showChecklistElement(){
        try{
            this.progressBarChecklistElement.setVisibility(View.VISIBLE);
            ChecklistElementRequest checklistElementRequest = new ChecklistElementRequest(this.getApplicationContext());
            checklistElementRequest.showChecklistElement(Integer.parseInt(this.checklistid));
            HSDroneLogNetwork.networkInstance().startRequest(this, checklistElementRequest);
        } catch (IllegalStateException e){
            this.progressBarChecklistElement.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content),R.string.networkError, Snackbar.LENGTH_LONG)
                    .setAction(R.string.tryAgain, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showChecklistElement();
                        }
                    });
            snackbar.show();
        }
    }


    //------------------------ Checklist methods ---------------------------------------------------
    private void createTableRowAllChecklistElements(TableLayout tl, String text, String id){
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
        //add it to the layout
        tableRow.addView(box);
        tableRow.addView(textView);
        tableRow.setPaddingRelative(20, 15, 20, 15);
        tl.addView(tableRow);
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
}
