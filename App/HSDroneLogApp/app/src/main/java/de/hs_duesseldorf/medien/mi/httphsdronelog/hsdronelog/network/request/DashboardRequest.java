package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class DashboardRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a mostUsedDrones request*/
    public final static String MOST_USED_DRONES_TAG = "mostUsedDrones";
    /**The identification tag of a flightsWithoutDrones request*/
    public final static String FLIGHTS_WITHOUT_DRONES_TAG = "flightsWithoutDrones";
    /**The identification tag of a amountOfDrones request*/
    public final static String AMOUNT_OF_DRONES_TAG = "amountOfDrones";
    /**The identification tag of a longestFlight request*/
    public final static String LONGEST_FLIGHT_TAG = "longestFlight";
    /**The identification tag of a flightData request*/
    public final static String FLIGHT_DATA_TAG = "flightData";
    /**The identification tag of a flightDataBefore request*/
    public final static String FLIGHT_DATA_BEFORE_TAG = "flightDataBefore";
    /**The identification tag of a flightDataToday request*/
    public final static String FLIGHT_DATA_TODAY_TAG = "flightDataToday";
    /**The identification tag of a flightDataAfter request*/
    public final static String FLIGHT_DATA_AFTER_TAG = "flightDataAfter";
    /**The identification tag of a pilotOfTheWeek request*/
    public final static String PILOT_OF_THE_WEEK_TAG = "pilotOfTheWeek";


    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new DashboardRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public DashboardRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new amount of drones request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void mostUsedDrones() throws IllegalStateException
    {
        this.setRequestData(true, MOST_USED_DRONES_TAG, "/index.php/dashboard/most_used_drones/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new flights without drones request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void flightsWithoutDrones() throws IllegalStateException
    {
        this.setRequestData(true, FLIGHTS_WITHOUT_DRONES_TAG, "/index.php/dashboard/flights_without_drones/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new amount of drones request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void amountOfDrones() throws IllegalStateException
    {
        this.setRequestData(true, AMOUNT_OF_DRONES_TAG, "/index.php/dashboard/amount_drones/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new longest flight request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void longestFlight() throws IllegalStateException
    {
        this.setRequestData(true, LONGEST_FLIGHT_TAG, "/index.php/dashboard/longest_flight/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new flight data request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void flightData() throws IllegalStateException
    {
        this.setRequestData(true, FLIGHT_DATA_TAG, "/index.php/dashboard/flight_data/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new flight data before request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void flightDataBefore() throws IllegalStateException
    {
        this.setRequestData(true, FLIGHT_DATA_BEFORE_TAG, "/index.php/dashboard/flight_data_before/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new flight data today request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void flightDataToday() throws IllegalStateException
    {
        this.setRequestData(true, FLIGHT_DATA_TODAY_TAG, "/index.php/dashboard/flight_data_today/",
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new flight data after request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void flightDataAfter() throws IllegalStateException
    {
        this.setRequestData(true, FLIGHT_DATA_AFTER_TAG, "/index.php/dashboard/flight_data_after/",
                this.byteArrayOutputStream(""), false);
    }


    /**
     * Sets a new pilotOfTheWeek request to be send over the network.<br><br>
     * @throws IllegalStateException If one of the values is null
     */
    public void pilotOfTheWeek()
    {
        this.setRequestData(true, PILOT_OF_THE_WEEK_TAG, "/index.php/dashboard/pilot_of_the_week/",
                this.byteArrayOutputStream(""), false);
    }
}
