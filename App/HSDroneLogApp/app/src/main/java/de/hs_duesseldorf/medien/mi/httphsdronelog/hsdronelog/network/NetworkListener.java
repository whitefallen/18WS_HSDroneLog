package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network;

import java.util.List;
import java.util.Map;

/**
 * The network listener to listen to events from the network.
 * You will alos get the results of a request as event.
 */
public interface NetworkListener
{

    /**
     * This method is called from the {@link NetworkHandler} when the current request of the {@link NetworkThread} finished successfully.
     * @param results The results from the request in a List of Maps. Each Map
     * represents an object in the data array received from the web server.
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     *                                 to compare values.
     */
    void requestWasSuccessful(List<Map<String, String>> results, String message, String requestIdentificationTag);

    /**
     * This method is called from the {@link NetworkHandler} when the current request of the {@link NetworkThread} was redirected
     * and the user has to log in again.
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     *                                 to compare values.
     */
    void sessionHasExpired(String message, String requestIdentificationTag);

    /**
     * This method is called from the {@link NetworkHandler} when the current request of the {@link NetworkThread} was not successful.
     * @param message The message send from the web server.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     *                                 to compare values.
     */
    void requestWasNotSuccessful(String message, String requestIdentificationTag);

    /**
     * This method is called from the {@link NetworkHandler} when an exception occurred during the current request.
     * @param e The exception instance.
     * @param message The message send from the web server. If no message exists this value is null.
     * @param requestIdentificationTag The identification tag of the specific request type. With this value you can identify
     *                                 the request type. Look public static variables in subclasses of
     *                                 {@link de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.Request}
     *                                 to compare values.
     */
   void exceptionOccurredDuringRequest(Exception e, String message, String requestIdentificationTag);

}
