package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class MessageRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allMessages request*/
    public final static String ALL_MESSAGES_TAG = "allMessages";
    /**The identification tag of a showMessage request*/
    public final static String SHOW_MESSAGE_TAG = "showMessage";
    /**The identification tag of a editMessage request*/
    public final static String EDIT_MESSAGE_TAG = "editMessage";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new MessageRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public MessageRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all messages request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void allMessages() throws IllegalStateException
    {
        this.setRequestData(true, ALL_MESSAGES_TAG, "/index.php/message/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new show message request to be send over the network.<br><br>
     * @param messageKey The key of the checklist element
     * @throws IllegalStateException If one of the values is null
     */
    public void showMessage(String messageKey) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_MESSAGE_TAG, "/index.php/message/show/" + messageKey,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit message request to be send over the network.<br><br>
     * @param messageKey The key of the checklist element
     * @param message The message to show
     * @throws IllegalStateException If one of the values is null
     */
    public void editMessage(String messageKey, String message) throws IllegalStateException
    {
        this.setRequestData(true, EDIT_MESSAGE_TAG, "/index.php/message/edit_message/" + messageKey,
                this.byteArrayOutputStream("nachricht=" + message), false);
    }
}
