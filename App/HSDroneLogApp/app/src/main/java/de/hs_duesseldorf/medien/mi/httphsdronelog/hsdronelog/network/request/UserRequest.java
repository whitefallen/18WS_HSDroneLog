package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class UserRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a logout request*/
    public final static String LOGOUT_TAG = "logout";
    /**The identification tag of a allUserData request*/
    public final static String ALL_USER_DATA_TAG = "allUserData";
    /**The identification tag of a addUser request*/
    public final static String ADD_USER_TAG = "addUser";
    /**The identification tag of a deleteUser request*/
    public final static String DELETE_USER_TAG = "deleteUser";
    /**The identification tag of a editUser request*/
    public final static String EDIT_USER_TAG = "editUser";
    /**The identification tag of a showUser request*/
    public final static String SHOW_USER_TAG = "showUser";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new UserRequset instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public UserRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new logout request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void logout() throws IllegalStateException
    {
        this.setRequestData(true, LOGOUT_TAG, "/index.php/user/logout/",
                this.byteArrayOutputStream(""), true);
    }

    /**
     * Sets a new all user data request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void allUserData() throws IllegalStateException
    {
        this.setRequestData(true, ALL_USER_DATA_TAG, "/index.php/user/index/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new add user request to be send over the network.<br><br>
     * @param email The email of the user to.
     * @param password The password of the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param courseOfStudy The course of study of the user.
     * @param role The role of the user.
     * @param isActive True if the user should be active.
     * @param image The image to set as new drone image. Null for no edit
     * @param imageFileName The name of the image file. Null if image is null.
     * @throws IllegalStateException If one of the values is null
     */
    public void addUser(String email, String password, String firstname, String lastname,
                        String courseOfStudy, int role, boolean isActive, Bitmap image,
                        String imageFileName) throws IllegalStateException
    {
        if (image != null)
            this.setRequestData(true, ADD_USER_TAG, "/index.php/user/add_user",
                    this.byteArrayOutputStream("email_adresse=" + email + "&passwort=" + password + "&vorname=" + firstname
                            + "&nachname=" + lastname + "&studiengang=" + courseOfStudy + "&rolle=" + role
                            + "&aktivitaet=" + this.booleanToInt(isActive) + "&filename=" + imageFileName + "&profilbild=", image)
                    , false);
        else
            this.setRequestData(true, ADD_USER_TAG, "/index.php/user/add_user",
                    this.byteArrayOutputStream("email_adresse=" + email + "&passwort=" + password + "&vorname=" + firstname
                            + "&nachname=" + lastname + "&studiengang=" + courseOfStudy + "&rolle=" + role
                            + "&aktivitaet=" + this.booleanToInt(isActive))
                    , false);
    }

    /**
     * Sets a new delete user request to be send over the network.<br><br>
     * @param userId The id of the user
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteUser(int userId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_USER_TAG, "/index.php/user/delete_user/" + userId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit user request to be send over the network.<br><br>
     * @param userId The id of the user.
     * @param email The email of the user to.
     * @param password The password of the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param courseOfStudy The course of study of the user.
     * @param role The role of the user.
     * @param isActive True if the user should be active.
     * @param image The image to set as new drone image. Null for no edit
     * @param imageFileName The name of the image file. Null if image is null.
     * @throws IllegalStateException If one of the values is null
     */
    public void editUser(int userId, String email, String password, String firstname, String lastname,
                         String courseOfStudy, int role, boolean isActive, Bitmap image,
                         String imageFileName) throws IllegalStateException
    {
        String postRequestString = "email_adresse=" + email + "&vorname=" + firstname
                + "&nachname=" + lastname + "&studiengang=" + courseOfStudy + "&rolle=" + role
                + "&aktivitaet=" + this.booleanToInt(isActive);

        if (!password.isEmpty())
            postRequestString += "&passwort=" + password;

        if (image != null) {
            postRequestString += "&filename=" + imageFileName + "&profilbild=";
            this.setRequestData(true, EDIT_USER_TAG, "/index.php/user/edit_user/" + userId,
                    this.byteArrayOutputStream(postRequestString, image), false);
        }
        else
        {
            this.setRequestData(true, EDIT_USER_TAG, "/index.php/user/edit_user/" + userId,
                    this.byteArrayOutputStream(postRequestString), false);
        }
    }

    /**
     * Sets a new show user request to be send over the network.<br><br>
     * @param userId The id of the user.
     * @throws IllegalStateException If one of the values is null
     */
    public void showUser(int userId) throws  IllegalStateException
    {
        this.setRequestData(true, SHOW_USER_TAG, "/index.php/user/show/" + userId,
                this.byteArrayOutputStream("") , false);
    }

    /**
     * Parses a boolean into an int.
     * @param value A boolean value.
     * @return 1 if value is true, 0 otherwise.
     */
    private int booleanToInt(boolean value)
    {
        if (value)
            return 1;
        else
            return 0;
    }
}
