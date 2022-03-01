package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class IndexRequest extends Request
{

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a login request*/
    public final static String LOGIN_TAG = "login";
    /**The identification tag of a registerUser request*/
    public final static String REGISTER_USER_TAG = "registerUser";
    /**The identification tag of a forgotPassword request*/
    public final static String FORGOR_PASSWORD_TAG = "forgotPassword";
    /**The identification tag of a showAdminEmail request*/
    public final static String SHOW_ADMIN_EMAIL_TAG = "showAdminEmail";
    /**The identification tag of a changeAdminEmail request*/
    public final static String CHANGE_ADMIN_EMAIL_TAG = "changeAdminEmail";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new IndexRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public IndexRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new login request to be send over the network.
     * @param email The mail address of the user to login.
     * @param password The password of the user to login.
     * @throws IllegalStateException If one of the values is null
     */
    public void login(String email, String password)  throws IllegalStateException
    {
        this.setRequestData(false, LOGIN_TAG, "/index.php/index/login_user",
                this.byteArrayOutputStream("email_adresse=" + email + "&passwort=" + password), false);
    }

    /**
     * Sets a new register user request to be send over the network.<br><br>
     * @param firstname The first name of the user to register.
     * @param lastname The last name of the user to register.
     * @param courseOfStudy The course of study of the user to register.
     * @param email The email of the user to register.
     * @param password The password of the user to register.
     * @param image The image to set as new drone image. Null for no edit
     * @param imageFileName The name of the image file. Null if image is null.
     * @throws IllegalStateException If one of the values is null
     */
    public void registerUser(String firstname, String lastname, String courseOfStudy, String email,
                             String password, Bitmap image, String imageFileName) throws IllegalStateException
    {
        if (image != null)
            this.setRequestData(false, REGISTER_USER_TAG, "/index.php/index/register_user",
                    this.byteArrayOutputStream("email_adresse=" + email + "&passwort=" + password + "&vorname=" + firstname
                            + "&nachname=" + lastname + "&studiengang=" + courseOfStudy
                            + "&filename=" + imageFileName + "&profilbild=", image)
                    , false);
        else
            this.setRequestData(false, REGISTER_USER_TAG, "/index.php/index/register_user",
                    this.byteArrayOutputStream("email_adresse=" + email + "&passwort=" + password + "&vorname=" + firstname
                            + "&nachname=" + lastname + "&studiengang=" + courseOfStudy)
                    , false);
    }

    /**
     * Sets a new forgot password request to be send over the network.<br><br>
     * @param email The email of the user to register.
     * @throws IllegalStateException If one of the values is null
     **/
    public void forgotPassword(String email) throws IllegalStateException
    {
        this.setRequestData(false, FORGOR_PASSWORD_TAG, "/index.php/index/password_reset",
                this.byteArrayOutputStream("mail=" + email), false);
    }

    /**
     * Sets a new showAdminEmail request to be send over the network.<br><br>
     **/
    public void showAdminEmail()
    {
        this.setRequestData(true, SHOW_ADMIN_EMAIL_TAG, "/index.php/email/index",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new changeAdminEmail request to be send over the network.<br><br>
     * @param email The email of the user to register.
     * @param password The password for the mail server.
     * @param  smtpServer The route to the smtp server.
     * @param port The port.
     * @throws IllegalStateException If one of the values is null
     **/
    public void changeAdminEmail(String email, String password, String smtpServer, int port)
    {
        this.setRequestData(true, CHANGE_ADMIN_EMAIL_TAG, "/index.php/email/add_email",
                this.byteArrayOutputStream("email_addresse=" + email + "&passwort="
                        + password + "&smtp_server=" + smtpServer + "&port=" + port)
                , false);
    }
}
