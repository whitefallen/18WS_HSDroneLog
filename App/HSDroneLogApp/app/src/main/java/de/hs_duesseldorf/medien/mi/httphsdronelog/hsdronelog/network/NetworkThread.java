package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network;

import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request;
import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util.NetworkCharsetConverterReader;

/**
 * The network Thread class can sends requests to a HSDroneLog Web server and receives the incoming data.
 * It then parses the JSON formatted data into its single elements and saves it into a Map.
 * You can access the data with a the {@link #resultingList()} method.
 */
public class NetworkThread extends Thread
{
    //CLASS VARIABLES/////////////////////////////////////////////////////////////////////////////

    /**What value for sending the empty message to {@link NetworkHandler}. This value
     * indicates that the request was successful.*/
    static int MESSAGEOBJECT_WHAT_REQUEST_SUCCESSFUL = 0;
    /**What value for sending the empty message to {@link NetworkHandler}. This value
     * indicates that the request was redirected so the current session has expired.*/
    static int MESSAGEOBJECT_WHAT_REQUEST_REDIRECTED = 1;
    /**What value for sending the empty message to {@link NetworkHandler}. This value
     * indicates that the request was not successful.*/
    static int MESSAGEOBJECT_WHAT_REQUEST_NOT_SUCCESSFUL = 2;
    /**What value for sending the empty message to {@link NetworkHandler}. This value
     * indicates that an Exception occurred during the request.*/
    static int MESSAGEOBJECT_WHAT_EXCEPTION_OCCURED = 3;

    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    //SET REQUEST VALUES///////////////////////////////

    /**This value is true if there is any active request currently.*/
    private boolean m_isRequesting;
    /**The instance of the current request.*/
    private Request m_request;
    /**The url of the web server to call. This value does not include sub-pages of the web server.*/
    private String m_webServerURLString;
    /**The complete url for the current request. This value contains the {@link #m_webServerURLString} and then the request
     * specific part for the php file.*/
    private URL m_completeURL;
    /**The id of the session gained from a login request.*/
    private String m_session;
    /**This instance establishes the HTTP (or HTTPS if url is https) connection to the web server.*/
    private HttpURLConnection m_httpUrlConnection;

    //SEND REQUEST AND READ ANSWER/////////////////////

    /**The exception occured during the current request*/
    private Exception m_exceptionOccured;
    /**This value is set to true if the current request was redirected to another host.*/
    private boolean m_requestWasRedirected;
    /**The outgoing stream is used to send the POST message of a request via the HTTP connection.*/
    private OutputStream m_outgoingStream;
    /**The incoming stream gets the data send by the webserver. Usually this value is parsed as JSON.*/
    private NetworkCharsetConverterReader m_incomingStream;
    /**The char read by the {@link #m_incomingStream}. */
    private int m_charRead;
    /**The result string saves the incoming data from the {@link #m_incomingStream}. This
     * message is in JSON format.*/
    private StringBuilder m_jsonResultStringBuilder;
    /**The json formatted result from the web server for a request. This value is a JSON String.*/
    private String m_rawJSONResult;

    //NOTIFY NETWORK HANDLER////////////////////////

    /**The Handler for this thread to gain access to the ui Thread of the app for modifications.*/
    private NetworkHandler m_networkHandler;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new Network Thread which can send requests to a HSDroneLog web server.
     * The name of the Thread will be 'HSDroneLog Network Thread'.<br><br>
     * <b>Note that before starting the Thread you have to set a {@link NetworkHandler}.</b>
     * @param webServerURL The Url to the web server.
     */
    NetworkThread(String webServerURL)
    {
        this.m_isRequesting = false;
        this.m_request = null;
        this.m_webServerURLString = webServerURL;
        this.m_completeURL = null;
        this.m_session = null;
        this.m_httpUrlConnection = null;

        this.m_exceptionOccured = null;
        this.m_requestWasRedirected = false;
        this.m_outgoingStream = null;
        this.m_incomingStream = null;
        this.m_charRead = 0;
        this.m_jsonResultStringBuilder = new StringBuilder();
        this.m_rawJSONResult = null;

        this.m_networkHandler = null;

        this.setName("HSDroneLog Network Thread");
    }

    //PROTECTED METHODS AND DEFAULT///////////////////////////////////////////////////////////////

    /**
     * Sets the network handler for this Thread. This Handler will be noticed when a request finished.
     * @param threadHandler The Handler for this Thread.
     */
    void setNetworkHandler(NetworkHandler threadHandler)
    {
        this.m_networkHandler = threadHandler;
    }

    /**
     * Sets the session. This method is an interface
     * method for the network request parser.
     * @param session The message from the web server.
     */
    public void setSession(String session)
    {
        this.m_session = session;
    }

    /**
     * Gets the web server url. This method is an interface
     * method for the network request parser.
     */
    public String webServerURLString()
    {
        return this.m_webServerURLString;
    }

    /**
     * Gets the working status of the network Thread.
     * @return true if there is any active request.
     */
    boolean isCurrentlyRequesting()
    {
        return this.m_isRequesting;
    }

    /**
     * Opens the network for a new request
     */
    void openForNewRequest()
    {
        this.m_isRequesting = false;
    }

    /**
     * Gets the parsed results of the request. The maps represent objects with same attributes
     * which were received from the url request.
     * @return An instance of an List with a map for each similar object of the raw result string
     * or null if the parsing was not successful.
     */
    List<Map<String, String>> resultingList()
    {
        return this.m_request.requestParser().resultingList();
    }

    /**
     * Gets the message from the web server for this request. If the network was unable to
     * receive a message from the web server this value will be null.
     * @return The message from the web server or null if unable to receive a message from the server or if no
     * request was send.
     */
    String messageFromWebServer()
    {
        return this.m_request.requestParser().messageFromWebServer();
    }

    /**
     * Gets the exception occured of the last request.
     * @return An Exception or null if no Exception occured.
     */
    Exception exceptionOccured()
    {
        return this.m_exceptionOccured;
    }

    /**
     * Gets the identification tag of the current request.
     * @return The request identification tag of the current request.
     */
    String requestIdentificationTag()
    {
        return this.m_request.identificationTag();
    }

    //New Request Method///////////////////////////////////

    /**
     * Sets the data of a new request for the network thread.
     * @param request The {@link Request} object. Gets the data for the network request from this object.
     *                The request was specified in this object.
     * @throws IllegalStateException If there is an active request running or the thread has been terminated.
     */
    synchronized void setNewRequest(Request request) throws IllegalStateException
    {
        this.checkForIllegalState(request);

        //Try to set new request data
        try {
            //reset important values
            this.m_exceptionOccured = null;
            this.m_requestWasRedirected = false;

            //set and create necessary objects
            this.m_request = request;

            String urlString = request.urlString();
            if (urlString.startsWith("http"))
                this.m_completeURL = new URL(urlString);
            else
                this.m_completeURL = new URL(this.m_webServerURLString + urlString);

            //check if session should be deleted
            if (request.deleteSession())
                this.m_session = null;

            this.m_isRequesting = true;

        } catch (IOException e) {
            //print exception and reset values
            e.printStackTrace();
            this.m_completeURL = null;
            this.m_isRequesting = false;
        }
    }

    //PRIVATE METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Checks for an illegal state when values from a new request should been taken over.
     * @param request The request instance.
     */
    private void checkForIllegalState(Request request) {
        //check if this thread is running
        if(this.getState() == State.TERMINATED)
            throw new IllegalStateException("This Thread has been terminated already.");

        //check if there is any current request
        if(this.isCurrentlyRequesting())
            throw new IllegalStateException("Can not send two requests at same time. Wait until current request is finished.");

        //check if there is any current request
        if(request.sendSession() && this.m_session == null)
            throw new IllegalStateException("This request needs a session id. Log in first to get a session.");
    }

    //METHODS RUNNING ON NETWORK THREAD///////////////////////////////////////////////////////////

    @Override
    public void start()
    {
        try {
            if (this.m_networkHandler == null)
                throw new IllegalStateException("Cannot Start Network Thread when no Network Listener is set.");
            else
                super.start();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        //check if interrupted
        while (!this.isInterrupted())
        {
            //check for a new request
            if(this.isCurrentlyRequesting())
            {
                synchronized(this)
                {
                    try
                    {
                        //open the http connection to web server
                        this.openConnection();
                        //send the post data to web server
                        this.sendPostRequestData();
                        //check if request was redirected so eventually the session has expired
                        this.checkIfRedirected();
                        //gets the result from web server
                        this.receiveResultOfRequest();
                        //parse the result from web server
                        this.m_request.requestParser().parseRawResult(this, this.m_rawJSONResult);

                        //finish the request by resetting some values and sending a message to the network handler
                        if (this.m_request.sendSession() && this.m_requestWasRedirected)
                            this.notifyNetworkHandler(NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_REDIRECTED);

                        if (this.m_request.requestParser().requestIsSuccessful())
                            this.notifyNetworkHandler(NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_SUCCESSFUL);

                        else
                            this.notifyNetworkHandler(NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_NOT_SUCCESSFUL);

                    } catch (Exception e){
                        e.printStackTrace();
                        //set the exception as current exception
                        this.m_exceptionOccured = e;
                        //finish the request by resetting some values and sending a message to the network handler
                        this.notifyNetworkHandler(NetworkThread.MESSAGEOBJECT_WHAT_EXCEPTION_OCCURED);
                    }
                }
            }
            else
            {
                //sleep 100 ms
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {this.interrupt();}
            }
        }
    }

    /**
     * Opens the http ( or https if url is https) connection to the HSDroneLog web server.
     * @throws IOException if an I/O exception occurs.
     */
    private void openConnection() throws IOException
    {
        if (this.m_completeURL.toString().toLowerCase().startsWith("http"))
            this.m_httpUrlConnection = (HttpURLConnection) m_completeURL.openConnection();
        else if (this.m_completeURL.toString().toLowerCase().startsWith("https"))
            this.m_httpUrlConnection = (HttpsURLConnection) m_completeURL.openConnection();

        this.m_httpUrlConnection.setRequestProperty("charset", "utf-8");
    }

    /**
     * Sends data over the network using the POST method of http
     * @throws IOException if an I/O exception occurs.
     */
    private void sendPostRequestData() throws IOException
    {
        this.m_httpUrlConnection.setRequestMethod("POST");
        this.m_outgoingStream = new BufferedOutputStream(this.m_httpUrlConnection.getOutputStream());
        ByteArrayOutputStream postRequestData = this.m_request.postRequestData();

        //send session
        if(this.m_request.sendSession())
        {
            if(postRequestData.size() == 0)
            {
                Log.d("Network Post Data", "session=" + this.m_session + postRequestData.toString());
                this.m_outgoingStream.write(("session=" + this.m_session).getBytes());
            }
            else
            {
                Log.d("Network Post Data", "session=" + this.m_session + "&" + postRequestData.toString());
                this.m_outgoingStream.write(("session=" + this.m_session + "&").getBytes());
            }
        }
        postRequestData.writeTo(this.m_outgoingStream);
        postRequestData.close();
        this.m_outgoingStream.flush();
        this.m_outgoingStream.close();
        this.m_outgoingStream = null;
    }

    /**
     * Checks if a request was redirected.
     * @throws IOException if an I/O exception occurs.
     */
    private void checkIfRedirected() throws IOException
    {
        //get incoming stream
        this.m_incomingStream = new NetworkCharsetConverterReader(new InputStreamReader(
                new BufferedInputStream(m_httpUrlConnection.getInputStream()), StandardCharsets.UTF_8));

        this.m_requestWasRedirected = !this.m_completeURL.getHost().equals(this.m_httpUrlConnection.getURL().getHost());

        Log.d("Network Request", "Redirected: " + this.m_requestWasRedirected);
    }

    /**
     * Gets the response of a request from the HSDroneLog web server.
     * The response will be formatted as json string
     * @throws IOException if an I/O exception occurs.
     */
    private void receiveResultOfRequest() throws IOException
    {
        //already got incoming stream in other method

        //read incoming stream
        while((this.m_charRead = this.m_incomingStream.read()) != -1)
        {
            //append chars read to result string
            this.m_jsonResultStringBuilder.append((char) (this.m_charRead));
        }

        this.m_incomingStream.close();
        this.m_incomingStream = null;

        this.m_rawJSONResult = this.m_jsonResultStringBuilder.toString();
        m_jsonResultStringBuilder.delete(0, m_jsonResultStringBuilder.length());

        Log.d("Network Request Result", "RESULT: " + this.m_rawJSONResult);
    }

    /**
     * This method will be called when the current NetworkRequest ends because it was
     * successful / not successful or an exception occured.
     * This method indicates the parsing of the result of the current request and will call the
     * @param messageWhat The what value of the {@link Message}.
     */
    private void notifyNetworkHandler(int messageWhat)
    {
        this.m_networkHandler.sendEmptyMessage(messageWhat);
        this.m_completeURL = null;
        this.m_httpUrlConnection.disconnect();
        this.m_httpUrlConnection = null;
        this.m_isRequesting = false;
    }
}