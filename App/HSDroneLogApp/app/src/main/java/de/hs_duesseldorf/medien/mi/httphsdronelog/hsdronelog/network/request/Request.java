package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.NetworkRequestParser;

/**
 * The Request class provides a flexible use of Requests in the Network.
 * The NetworkThread uses this class to determine the data send over the network to a specific php location.
 */
public abstract class Request
{
    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**This boolean describes if the type of the request was set or not.*/
    private boolean m_isInitialized;
    /**This tag is used to identify the request type. This value should be set by subclasses
     * with a public static variable so that the user can check for request type.*/
    private String m_identificationTag;
    /**If the session should be send or not*/
    private boolean m_sendSession;
    /**the relativ path to the php for this request.*/
    private String m_urlString;
    /**The post request data is a byte stream which contains all the information
     * which should be send to the web server.*/
    private ByteArrayOutputStream m_postRequestData;
    /**If the session should be deleted when starting the network request.*/
    private boolean m_deleteSession;
    /**The parser for this request to parse the result of this request into a format
     * the network listener can deal with.*/
    private NetworkRequestParser m_requestParser;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new Request with default values
     * @param requestParser The parser for this request. The parser will parse the result of the request
     *                      into a format the network listener can deal with.
     * @throws IllegalArgumentException if the parser value is null
     */
    public Request(NetworkRequestParser requestParser) throws IllegalArgumentException
    {
        if(requestParser == null)
            throw new IllegalArgumentException("Parameter values cant be null!");

        //set default values
        this.m_isInitialized = false;
        this.m_identificationTag = null;
        this.m_sendSession = false;
        this.m_urlString = null;
        this.m_postRequestData = new ByteArrayOutputStream();
        this.m_deleteSession = false;
        this.m_requestParser = null;

        this.m_requestParser = requestParser;
    }

    //PUBLIC METHODS//////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the send session string.
     * @return The send session boolean for the network request. True if the session shoulb be send over network.
     * @throws NullPointerException If the request type was not specified.
     */
    public boolean sendSession()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_sendSession;
    }

    /**
     * Gets the send session string.
     * @return The identification tag of the request. This can be used to identify the request in detail.
     * Look at subclasses public static variables to get the reference value.
     * @throws NullPointerException If the request type was not specified.
     */
    public String identificationTag()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_identificationTag;
    }

    /**
     * Gets the url string for this request.
     * @return The url string for the network request.
     * @throws NullPointerException If the request type was not specified.
     */
    public String urlString()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_urlString;
    }

    /**
     * Gets the post request string.
     * @return The post request data for the network request. This stream contains all
     * the content which should be sent to the web server.
     * @throws NullPointerException If the request type was not specified.
     */
    public ByteArrayOutputStream postRequestData()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_postRequestData;
    }

    /**
     * Gets the delete session boolean.
     * @return The delete session boolean for the network request. If true, the session will be deleted at
     * the beginning of the network request.
     * @throws NullPointerException If the request type was not specified.
     */
    public boolean deleteSession()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_deleteSession;
    }

    /**
     * Gets the network request parser instance.
     * @return The network request parser instance for the network request. This objet will parse
     * the result of the request into a format the network listener can deal with.
     * @throws NullPointerException If the request type was not specified.
     */
    public NetworkRequestParser requestParser()
    {
        //check if initialized
        this.checkIfInitialized();
        //return value
        return this.m_requestParser;
    }

    @Override
    public String toString()
    {
        return "IsInitialized: " + this.m_isInitialized + " identificationTag: " + m_identificationTag + " sendSession: " + this.m_sendSession
                + " urlString: " + this.m_urlString + " postRequestString: " + this.m_postRequestData.toString()
                + " deleteSession: " + this.m_deleteSession;
    }

    //DEFAULT METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Sets the data of a new request for the network thread.
     * @param sendSession Determines if the id of the session should also be send.
     * @param identificationTag The tag to identify the current request. This value has to be unique for
     *                          each request.
     * @param urlString The url as String. If this is a relative path, then the network thread will
     *                  add the drone log web server in front of this string
     * @param postRequestData The Data to send over the network with the POST method of HTTP.
     * @param deleteSession If the current session should be deleted when the network requests starts.
     *                      Only set this value to true if the request is the logout request.
     * @throws IllegalStateException If one of the values is null
     */
    void setRequestData(boolean sendSession, String identificationTag, String urlString
            , ByteArrayOutputStream postRequestData, boolean deleteSession) throws IllegalArgumentException
    {
        if(urlString == null || postRequestData == null)
            throw new IllegalArgumentException("Parameter values cant be null!");

        this.m_isInitialized = true;
        this.m_sendSession = sendSession;
        this.m_identificationTag = identificationTag;
        this.m_urlString = urlString;
        this.m_postRequestData = postRequestData;
        this.m_deleteSession = deleteSession;
    }

    /**
     * Creates a {@link ByteArrayOutputStream} from the given content.
     * @param content The content which should be add to the stream.
     * @return An instance of the ByteArrayOutputStream class which contains
     * the given content.
     * @throws IllegalStateException If content is null or contains null
     */
    ByteArrayOutputStream byteArrayOutputStream(String content) throws IllegalStateException
    {
        if (content == null || content.contains("null"))
            throw new IllegalArgumentException("Parameter values cant be null!");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            stream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }

    /**
     * Creates a {@link ByteArrayOutputStream} from the given content.
     * @param content The content which should be add to the stream.
     * @param image The image to be uploaded.
     * Note that the image attribute name has to be added to the content's end.
     * It will not be added in this method.
     * @return An instance of the ByteArrayOutputStream class which contains.
     * the given content.
     * @throws IllegalStateException If content is null or image is null
     */
    ByteArrayOutputStream byteArrayOutputStream(String content, Bitmap image) throws IllegalStateException
    {
        if (image == null)
            throw new IllegalArgumentException("Parameter values cant be null!");

        ByteArrayOutputStream stream = this.byteArrayOutputStream(content);

        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream;
    }

    //PRIVATE METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the request type was specified and so all necessary components are initialized.
     */
    private void checkIfInitialized()
    {
        if(!this.m_isInitialized)
        {
            throw new NullPointerException("You have to specify and initialize the request first before using it. ");
        }
    }
}
