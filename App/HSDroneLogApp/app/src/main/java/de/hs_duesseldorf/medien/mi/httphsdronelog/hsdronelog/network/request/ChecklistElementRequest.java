package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class ChecklistElementRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allChecklistElements request*/
    public final static String ALL_CHECKLIST_ELEMENTS_TAG = "allChecklistElements";
    /**The identification tag of a addChecklistElement request*/
    public final static String ADD_CHECKLIST_ELEMENT_TAG = "addChecklistElement";
    /**The identification tag of a showChecklistElement request*/
    public final static String SHOW_CHECKLIST_ELEMENT_TAG = "showChecklistElement";
    /**The identification tag of a editChecklistElement request*/
    public final static String EDIT_CHECKLIST_ELEMENT_TAG = "editChecklistElement";
    /**The identification tag of a deleteChecklistElement request*/
    public final static String DELETE_CHECKLIST_ELEMENT_TAG = "deleteChecklistElement";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new ChecklistElementRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public ChecklistElementRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all checklist elements request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void allChecklistElements() throws IllegalStateException
    {
        this.setRequestData(true, ALL_CHECKLIST_ELEMENTS_TAG, "/index.php/checklistelement/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new add checklist element request to be send over the network.<br><br>
     * @param designation The designation of the checklist element
     * @param explanation The explanation of the checklist element
     * @param checklists The checklists this element belongs to separated by ,. format: 1,2,3
     * @throws IllegalStateException If one of the values is null
     */
    public void addChecklistElement(String designation, String explanation, String checklists) throws IllegalStateException
    {
        this.setRequestData(true, ADD_CHECKLIST_ELEMENT_TAG, "/index.php/checklistelement/add_element",
                this.byteArrayOutputStream("bezeichnung=" + designation + "&erklaerung=" + explanation
                            + "&checklists[]=" + checklists), false);
    }

    /**
     * Sets a new show checklist element request to be send over the network.<br><br>
     * @param checklistElementId The id of the checklist element
     * @throws IllegalStateException If one of the values is null
     */
    public void showChecklistElement(int checklistElementId) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_CHECKLIST_ELEMENT_TAG, "/index.php/checklistelement/show/" + checklistElementId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit checklist element request to be send over the network.<br><br>
     * @param checklistElementId The id of the checklist element
     * @param designation The designation of the checklist element
     * @param explanation The explanation of the checklist element
     * @param checklists The checklists this element belongs to separated by ,. format: 1,2,3
     * @throws IllegalStateException If one of the values is null
     */
    public void editChecklistElement(int checklistElementId, String designation, String explanation,
                                     String checklists) throws IllegalStateException
    {
        this.setRequestData(true, EDIT_CHECKLIST_ELEMENT_TAG, "/index.php/checklistelement/edit_element/" + checklistElementId,
                this.byteArrayOutputStream("bezeichnung=" + designation + "&erklaerung=" + explanation
                        + "&checklists[]=" + checklists), false);
    }

    /**
     * Sets a new delete checklist element request to be send over the network.<br><br>
     * @param checklistElementId The id of the checklist element
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteChecklistElement(int checklistElementId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_CHECKLIST_ELEMENT_TAG, "/index.php/checklistelement/delete_element/" + checklistElementId,
                this.byteArrayOutputStream(""), false);
    }
}
