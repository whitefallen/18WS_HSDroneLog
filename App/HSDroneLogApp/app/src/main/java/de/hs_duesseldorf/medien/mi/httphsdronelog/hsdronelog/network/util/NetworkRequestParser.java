package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.NetworkThread;


/**
 * The NetworkRequesParser class provides a dynamic parsing of the result of a url network request.
 * The data from the url request can individually be parsed for every request.
 */
public abstract class NetworkRequestParser
{
    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**A list which saves all resulting maps which contains the parsed data of the
     * network request. This List will be returned when finished parsing*/
    protected List<Map<String, String>> m_requestResultsList;

    /**The network thread to notify when the parsing has finished.*/
    private NetworkThread m_networkThread;
    /**The exception occured during the current request*/
    protected Exception m_exceptionOccured;
    /**If the request was successful. This value indicates that the parsed result of
     * the request contains correct values and that there are no faulty results because
     *      *         of any error. */
    protected boolean m_requestIsSuccessful;
    /**The message from the web server for this request*/
    protected String m_messageFromWebServer;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new Network Request Parser which can parse a formatted string
     * received by a network request.
     */
    public NetworkRequestParser()
    {
        this.m_requestResultsList = null;
        this.m_networkThread = null;
        this.m_exceptionOccured = null;
        this.m_requestIsSuccessful = true;
        this.m_messageFromWebServer = null;
    }

    //PUBLIC METHODS//////////////////////////////////////////////////////////////////////////////

    /**
     * Parses the raw result from a network url request into a format the
     * network listener can deal with.
     * @param networkThread The network thread to notify when the paring has finished.
     * @param rawResult The raw result from a network url request.
     * @throws Exception if an exception occurs during the parsing
     */
    public void parseRawResult(NetworkThread networkThread, String rawResult) throws Exception
    {
        this.m_requestResultsList = new ArrayList<>();

        this.m_networkThread = networkThread;
        this.m_requestIsSuccessful = true;
        this.m_messageFromWebServer = null;
        this.m_exceptionOccured = null;

        this.parse(rawResult);

        //throw exception if exception occurred during parsing
        if (this.m_exceptionOccured != null)
            throw this.m_exceptionOccured;

    }

    /**
     * Gets the parsed results of the request. The maps represent objects with same attributes
     * which were received from the url request.
     * @return An instance of an List with a map for each similar object of the raw result string
     * or null if the parsing was not successful.
     */
    public List<Map<String, String>> resultingList()
    {
        List<Map<String, String>> temp = this.m_requestResultsList;

        this.m_requestResultsList = null;

        return temp;
    }

    /**
     * @return If the request was successful. This value indicates that the parsed result of
     *         the request contains correct values and that there are no faulty results because
     *         of any error.
     */
    public boolean requestIsSuccessful()
    {
        return this.m_requestIsSuccessful;
    }

    /**
     * @return The message from the web server for this request
     */
    public String messageFromWebServer()
    {
        return this.m_messageFromWebServer;
    }

    /**
     * Resets the values of the parser sub class so the next request can use the same object.
     */
    public abstract void resetValues();

    //PROTECTED METHODS///////////////////////////////////////////////////////////////////////////

    /**
     * Just for internal use. Call {@link #parseRawResult(NetworkThread, String)} instead.
     * This method starts the process of parsing in the subclass.
     * @param rawResult The raw result from a network url request.
     */
    protected abstract void parse(String rawResult);

    /**
     * Sets the new session for the network Thread.
     * @param session The new session of the network thread.
     */
    protected void setNetworkThreadSession(String session)
    {
        this.m_networkThread.setSession(session);
    }

    /**
     * Gets the web server url as string.
     * @return The web server url.
     */
    protected String webServerURLString()
    {
        return this.m_networkThread.webServerURLString();
    }
}
