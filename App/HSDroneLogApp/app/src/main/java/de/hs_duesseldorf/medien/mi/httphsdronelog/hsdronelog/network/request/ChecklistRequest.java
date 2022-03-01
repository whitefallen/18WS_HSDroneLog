package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class ChecklistRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allChecklists request*/
    public final static String ALL_CHECKLISTS_TAG = "allChecklists";
    /**The identification tag of a addChecklist request*/
    public final static String ADD_CHECKLIST_TAG = "addChecklist";
    /**The identification tag of a showChecklist request*/
    public final static String SHOW_CHECKLIST_TAG = "showChecklist";
    /**The identification tag of a editChecklist request*/
    public final static String EDIT_CHECKLIST_TAG = "editChecklist";
    /**The identification tag of a deleteChecklist request*/
    public final static String DELETE_CHECKLIST_TAG = "deleteChecklist";
    /**The identification tag of a deleteChecklist request*/
    public final static String CHECKLIST_FLIGHT_STATE_TAG = "checklistFlightState";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new ChecklistRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public ChecklistRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all checklists request to be send over the network.<br><br>
     * @param offset Gets the next 20 entries from with the offset as first entry. Starting at 0.
     * @throws IllegalStateException If one of the values is null
     */
    public void allChecklists(int offset) throws IllegalStateException
    {
        this.setRequestData(true, ALL_CHECKLISTS_TAG, "/index.php/checklist/",
                this.byteArrayOutputStream("offset=" + offset), false);
    }
    /**
     * Sets a new add checklist request to be send over the network.<br><br>
     * @param designation The designation of the checklist
     * @param explanation The explanation of the checklist
     * @param elements The checklist elements which belong to this checklist separated by ,. format: 1,2,3
     * @throws IllegalStateException If one of the values is null
     */
    public void addChecklist(String designation, String explanation, String elements) throws IllegalStateException
    {
        this.setRequestData(true, ADD_CHECKLIST_TAG, "/index.php/checklist/add_checklist",
                this.byteArrayOutputStream("bezeichnung=" + designation + "&erklaerung=" + explanation
                        + "&elements[]=" + elements), false);
    }

    /**
     * Sets a new show checklist request to be send over the network.<br><br>
     * @param checklistId The id of the checklist to show.
     * @throws IllegalStateException If one of the values is null
     */
    public void showChecklist(int checklistId) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_CHECKLIST_TAG, "/index.php/checklist/show/" + checklistId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new show checklist request to be send over the network.<br><br>
     * @param checklistId The id of the checklist to edit.
     * @param designation The designation of the checklist
     * @param explanation The explanation of the checklist
     * @param elements The checklist elements which belong to this checklist separated by ,. format: 1,2,3
     * @throws IllegalStateException If one of the values is null
     */
    public void editChecklist(int checklistId, String designation, String explanation, String elements) throws IllegalStateException
    {
        this.setRequestData(true, EDIT_CHECKLIST_TAG, "/index.php/checklist/edit_checklist/" + checklistId,
                this.byteArrayOutputStream("bezeichnung=" + designation + "&erklaerung=" + explanation
                        + "&elements[]=" + elements), false);
    }

    /**
     * Sets a new delete checklist request to be send over the network.<br><br>
     * @param checklistId The id of the checklist to delete.
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteChecklist(int checklistId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_CHECKLIST_TAG, "/index.php/checklist/delete_checklist/" + checklistId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new delete checklist request to be send over the network.<br><br>
     * @param flightId The id of the checklist to show.
     * @param  checklistElementIds An array of all checklist elements ids of a flight
     * @param  isActive  An array of all checklist elements of a flight if they are active
     * @param  comments An array of all checklist elements of a flight for the comment
     * @throws IllegalStateException If one of the values is null
     * @throws IllegalArgumentException If an invalid combination of parameters are given.
     */
    public void checklistFlightState(int flightId, int[] checklistElementIds, boolean[] isActive, String[] comments)
            throws IllegalStateException, IllegalArgumentException
    {
        if (checklistElementIds.length != comments.length || checklistElementIds.length != isActive.length)
            throw new IllegalArgumentException("length of checklistElementIds and length of comments has to be the same.");

        String data = "";
        for (int i = 0 ; i < checklistElementIds.length  ;i++)
        {
            data += "elements[" + checklistElementIds[i] + "][wert]=";
            if ( isActive[i] == true)
                data += "1";
            else
                data += "0";

            data += "&elements[" + checklistElementIds[i] + "][kommentar]=" + comments[i];

            if (i !=  checklistElementIds.length - 1)
                data += "&";

        }

        this.setRequestData(true, CHECKLIST_FLIGHT_STATE_TAG, "/index.php/checklist/checklist_state/" + flightId,
                this.byteArrayOutputStream(data), false);
    }
}
