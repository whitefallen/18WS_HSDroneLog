package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network;

import android.util.Log;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request;

/**
 * This class provides the network requests to get data from a HSDroneLog web server.
 * The data will be requested with POST method of HTTP.
 * To receive the requested data you have to create a {@link NetworkListener}.
 * This class is a singleton and works like an Adapter for the {@link NetworkThread}.
 * It also creates a {@link NetworkHandler} to get the requested data in the UI Thread
 * so you can perform UI modifications.
 * @author René Ebertowski
 *
 */
public class HSDroneLogNetwork
{
    //CLASS VARIABLES/////////////////////////////////////////////////////////////////////////////

    /**The network instance so that you can only have one network. (SINGLETON)*/
    private static HSDroneLogNetwork networkInstance = null;
    /**The url of the web server*/
    private static  String webServerUrl = null;

    //CLASS METHODS///////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a network instance if no one exists. Otherwise it will return the existing
     * network instance. So you can only have one instance of this object. (SINGLETON)<br>
     * Note that you have to set the url of the web server first before calling this method
     * otherwise this method causes an IllegalStateException.
     * @return An instance of the network class or null if no application context was set.
     */
    public static HSDroneLogNetwork networkInstance()
    {
        try {
            if (webServerUrl == null)
                throw new IllegalStateException("Cant create network instance without" +
                        "an web server url!");

            if ( networkInstance == null )
                networkInstance = new HSDroneLogNetwork(webServerUrl);

            return networkInstance;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Sets the url of the web server. Note that this can cause a IllegalStateException if
     * an instance of this object already exists which is currently requesting.
     * @param webServerURL The url of the web server.
     * @throws IllegalStateException if an instance of this object exists which is currently
     * requesting.
     */
    public static void setWebServerUrl(String webServerURL) throws IllegalStateException
    {
        Log.d("setWebServerUrl", webServerURL);
        if (networkInstance == null)
            webServerUrl = webServerURL;
        else if (networkInstance.m_networkThread.isCurrentlyRequesting())
        {
            throw new IllegalStateException("Can not set web server url while network is requesting!");
        }
        else
        {
            webServerUrl = webServerURL;
            networkInstance.closeNetwork();
            networkInstance = new HSDroneLogNetwork(webServerUrl);
        }
    }

    //INSTANCE VARIABLES//////////////////////////////////////////////////////////////////////////

    /**The {@link NetworkThread} instance to gain access to the network functionality.*/
    private NetworkThread m_networkThread;
    /**The Handler for the network thread to gain access to the ui Thread of the app for modifications.*/
    private NetworkHandler m_networkHandler;

    //CONSTRUCTORS////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new Network instance. A Network instance is used to send specific requests to a HSDroneLog
     * server. This objects works like an Adapter Pattern for all parts of the network.
     * Creating the network will also start the Network Thread.
     * @param webServerURL The url of the web server to connect to.
     */
    private HSDroneLogNetwork(String webServerURL)
    {
        Log.d("Server URL", webServerURL);
        this.m_networkThread = new NetworkThread(webServerURL);
        this.m_networkHandler = new NetworkHandler(this.m_networkThread);
        this.m_networkThread.setNetworkHandler(this.m_networkHandler);
        this.m_networkThread.start();

    }

    //PUBLIC METHODS//////////////////////////////////////////////////////////////////////////////

    /**
     * Terminates the Network Thread.<br><br>
     * <b>Note that you can not create another instance of network class.
     * <br>If you close the network you can not reopen it. Therefore you should
     * only call this method if you close your app.</b>
     */
    public void closeNetwork()
    {
        this.m_networkThread.interrupt();
    }

    /**
     * Starts a new custom request. Note that there can only run one request at same time.
     * The received data will be handed over by a {@link NetworkListener}.
     * @param listener The network listener to react to network events or to get the requested data.
     * @param request The Request object for the network request.
     * @throws IllegalStateException If there is running an unfinished request.
     */
    public void startRequest(NetworkListener listener, Request request) throws IllegalStateException
    {
        //set listener for request
        this.setNetworkListenerForNewRequest(listener);

        //requests something
        this.m_networkThread.setNewRequest(request);
    }

    /**
     * If the network is currently requesting any data from a web server.
     * @return true if requesting.
     */
    public boolean isRequesting()
    {
       return this.m_networkThread.isCurrentlyRequesting();
    }

    //PRIVATE METHODS/////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a new Request is valid in the current state of the network and then sets the
     * {@link NetworkListener} for the new request.
     * @param listener The network listener to react to network events or to get the requested data.
     * @throws IllegalStateException If there is a current request.
     */
    private void setNetworkListenerForNewRequest(NetworkListener listener) throws IllegalStateException
    {
        //check if new request is valid
        if (!this.m_networkThread.isCurrentlyRequesting())
        {
            //set listener for request
            this.m_networkHandler.setNetworkListener(listener);
        }
        else
        {
            throw new IllegalStateException("Can not send two Requests at the same time. Cancel last Request if you want to" +
                    "create a new one or wait until the Request has been finished.");
        }
    }

}