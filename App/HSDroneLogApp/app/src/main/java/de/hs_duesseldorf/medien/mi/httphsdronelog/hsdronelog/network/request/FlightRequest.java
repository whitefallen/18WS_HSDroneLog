package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class FlightRequest extends Request {

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allFlights request*/
    public final static String ALL_FLIGHTS_TAG = "allFlights";
    /**The identification tag of a addFlight request*/
    public final static String ADD_FLIGHT_TAG = "addFlight";
    /**The identification tag of a showFlight request*/
    public final static String SHOW_FLIGHT_TAG = "showFlight";
    /**The identification tag of a editFlight request*/
    public final static String EDIT_FLIGHT_TAG = "editFlight";
    /**The identification tag of a deleteFlight request*/
    public final static String DELETE_FLIGHT_TAG = "deleteFlight";
    /**The identification tag of a avalibleDrones request*/
    public final static String AVAILABLE_DRONES_TAG = "avalibleDrones";
    /**The identification tag of a uploadLog request*/
    public final static String UPLOAD_LOG_TAG = "uploadLog";
    /**The identification tag of a showFlightLog request*/
    public final static String SHOW_FLIGHT_LOG_TAG = "showFlightLog";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new FlightRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public FlightRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param offset Gets the next 20 entries from with the offset as first entry. Starting at 0.
     * @throws IllegalStateException If one of the values is null
     */
    public void allFlights(int offset) throws IllegalStateException
    {
        this.setRequestData(true, ALL_FLIGHTS_TAG, "/index.php/flight/",
                this.byteArrayOutputStream("offset=" + offset), false);
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param pilotId The id of the pilot
     * @param droneId The drone modell
     * @param location The location
     * @param date The date of the flight. format: 2018-12-20
     * @param startTime The start time. format: 10:10:00
     * @param endTime the end time. format: 10:10:00
     * @param checklistId the id of the checklist
     * @param flightDescription The description of the flight.
     * @param specialEvents Special events that occurred during the flight
     * @throws IllegalStateException If one of the values is null
     */
    public void addFlight(int pilotId, int droneId, String location, String date,
                          String startTime, String endTime, int checklistId, String flightDescription,
                          String specialEvents) throws IllegalStateException
    {
        this.setRequestData(true, ADD_FLIGHT_TAG, "/index.php/flight/add_flight",
                this.byteArrayOutputStream("pilot_id=" + pilotId + "&drohne_id=" + droneId + "&flugbezeichnung=" + flightDescription
                        + "&einsatzort_name=" + location + "&flugdatum=" + date + "&einsatzende=" + endTime + "&einsatzbeginn=" + startTime
                        + "&checkliste_name_id=" + checklistId + "&besondere_vorkommnisse=" + specialEvents), false);
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param flightId The id of the flight to show.
     * @throws IllegalStateException If one of the values is null
     */
    public void showFlight(int flightId) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_FLIGHT_TAG, "/index.php/flight/show/" + flightId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit flights request to be send over the network.<br><br>
     * @param flightId The id of the flight to show.
     * @param pilotId The id of the pilot
     * @param droneId The drone id
     * @param location The location
     * @param date The date of the flight. format: 2018-12-20
     * @param startTime The start time. format: 10:10:00
     * @param endTime the end time. format: 10:10:00
     * @param checklistId the id of the checklist
     * @param flightDescription The description of the flight.
     * @param specialEvents Special events that occurred during the flight
     * @throws IllegalStateException If one of the values is null
     */
    public void editFlight(int flightId, int pilotId, int droneId, String location, String date,
                           String startTime, String endTime, int checklistId, String flightDescription,
                           String specialEvents) throws IllegalStateException
    {
        this.setRequestData(true, EDIT_FLIGHT_TAG, "/index.php/flight/edit_flight/" + flightId,
                this.byteArrayOutputStream("pilot_id=" + pilotId + "&drohne_id=" + droneId + "&flugbezeichnung=" + flightDescription
                        + "&einsatzort_name=" + location + "&flugdatum=" + date + "&einsatzende=" + endTime + "&einsatzbeginn=" + startTime
                        + "&checkliste_name_id=" + checklistId + "&besondere_vorkommnisse=" + specialEvents), false);
    }

    /**
     * Sets a new delete flights request to be send over the network.<br><br>
     * @param flightId The id of the flight to delete.
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteFlight(int flightId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_FLIGHT_TAG, "/index.php/flight/delete_flight/" + flightId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new available drones request to be send over the network.<br><br>
     * @param startTime The start Time of the flight to check for available. format: 10:10:00
     * @param endTime The end Time of the flight to check for available. format: 10:10:00
     * @param date The date of the flight to delete. format: 2018-12-20
     * @throws IllegalStateException If one of the values is null
     */
    public void avalibleDrones(String startTime, String endTime, String date) throws IllegalStateException
    {
        this.setRequestData(true, AVAILABLE_DRONES_TAG, "/index.php/flight/requestAvailableDrones",
                this.byteArrayOutputStream("startzeit=" + startTime + "&endzeit=" + endTime + "&datum=" + date), false);
    }

    /**
     * Sets a new available drones request to be send over the network.<br><br>
     * @param flightId The id of the flight to show.
     * @param  logInputStream The input stream of the downloaded flight log zip
     * @throws IOException If one of the values is null or the zip file is invalid
     */
    public void uploadLog(int flightId, InputStream logInputStream) throws IOException
    {
        ByteArrayOutputStream byteArrayStream = this.byteArrayOutputStream("log=");

        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(logInputStream));

        ZipEntry zipEntry = zipInputStream.getNextEntry();
        if (zipEntry != null && zipEntry.getName().startsWith("DJIFlightRecord"))
        {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = zipInputStream.read(buffer, 0, buffer.length)) != -1)
            {
                byteArrayStream.write(buffer, 0, bytesRead);
            }

            zipInputStream.closeEntry();
            zipInputStream.close();

            this.setRequestData(true, UPLOAD_LOG_TAG, "/index.php/flight/upload_log/" + flightId,
                    byteArrayStream, false);
        }
        else
        {
            throw new IllegalArgumentException("Invalid Zip File");
        }
    }

    /**
     * Sets a new showFlightLog request to be send over the network.<br><br>
     * @param flightId The id of the flight to show.
     * @param offset Gets the next 50 entries from with the offset as first entry. Starting at 0.
     * @throws IOException If one of the values is null or the zip file is invalid
     */
    public void showFlightLog(int flightId, int offset)
    {
        this.setRequestData(true, SHOW_FLIGHT_LOG_TAG, "/index.php/flight/show_flight_log/" + flightId,
                this.byteArrayOutputStream("offset=" + offset), false);
    }
}
