package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network;

import android.os.Handler;
import android.os.Message;

/**
 * The Network Handler to get access to the UI Thread of android.
 * The Handler is used to get a message from a network Thread and then passes on the
 * message to the network listener so that you get this message in the ui Thread.
 * In the UI thread you can modify all views from android layout.
 */
class NetworkHandler extends Handler
{
    /**The network thread instance to get the resulting map from the request.*/
    private NetworkThread m_networkThread;
    /**The {@link NetworkListener} to listen to network events e.g. when the request finished.*/
    private NetworkListener m_networkListener;

    /**
     * Creates a new Network Handler so that you get the resulting map of the request in the ui Thread.
     * @param networkThread The network Thread instance.
     */
    NetworkHandler(NetworkThread networkThread)
    {
        this.m_networkThread = networkThread;
    }

    /**
     * Sets the network listener for a request. You have to set the listener each time before sending a
     * new request!
     * @param networkListener An istance of a Network Listener
     * @throws IllegalArgumentException If the networkListener is null
     */
    public void setNetworkListener(NetworkListener networkListener) throws IllegalArgumentException
    {
        try {
            if (networkListener == null)
                throw new IllegalArgumentException("networkListener is not allowed to be null!");

            this.m_networkListener = networkListener;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
        try {
            if(this.m_networkListener == null)
                throw new IllegalStateException("You have to set a Network Listener to get the events from a request.");

            //open network for new connection
            this.m_networkThread.openForNewRequest();

            //check if request was successfully
            if (msg.what == NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_SUCCESSFUL)
            {
                //pass result to listener via UI Thread
                this.m_networkListener.requestWasSuccessful(
                        this.m_networkThread.resultingList(),
                        this.m_networkThread.messageFromWebServer(),
                        this.m_networkThread.requestIdentificationTag()
                );
            }
            else if(msg.what == NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_REDIRECTED)
            {
                //pass result to listener via UI Thread
                this.m_networkListener.sessionHasExpired(
                        this.m_networkThread.messageFromWebServer(),
                        this.m_networkThread.requestIdentificationTag()
                );
            }
            else if(msg.what == NetworkThread.MESSAGEOBJECT_WHAT_REQUEST_NOT_SUCCESSFUL)
            {
                //pass result to listener via UI Thread
                this.m_networkListener.requestWasNotSuccessful(
                        this.m_networkThread.messageFromWebServer(),
                        this.m_networkThread.requestIdentificationTag()
                );
            }
            else if(msg.what == NetworkThread.MESSAGEOBJECT_WHAT_EXCEPTION_OCCURED)
            {
                this.m_networkListener.exceptionOccurredDuringRequest(
                        this.m_networkThread.exceptionOccured(),
                        this.m_networkThread.messageFromWebServer(),
                        this.m_networkThread.requestIdentificationTag()
                );
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
