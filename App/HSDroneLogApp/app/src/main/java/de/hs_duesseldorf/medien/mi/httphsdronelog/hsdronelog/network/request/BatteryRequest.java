package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class BatteryRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allBatteries request*/
    public final static String ALL_BATTERIES_TAG = "allBatteries";
    /**The identification tag of a addBattery request*/
    public final static String ADD_BATTERY_TAG = "addBattery";
    /**The identification tag of a showBattery request*/
    public final static String SHOW_BATTERY_TAG = "showBattery";
    /**The identification tag of a editBattery request*/
    public final static String EDIT_BATTERY_TAG = "editBattery";
    /**The identification tag of a deleteBattery request*/
    public final static String DELETE_BATTERY_TAG = "deleteBattery";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new BatteryRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public BatteryRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all batteries request to be send over the network.<br><br>
     * @param offset Gets the next 20 entries from with the offset as first entry. Starting at 0.
     * @throws IllegalStateException If one of the values is null
     */
    public void allBatteries(int offset) throws IllegalStateException
    {
        this.setRequestData(true, ALL_BATTERIES_TAG, "/index.php/akku/",
                this.byteArrayOutputStream("offset=" + offset), false);
    }

    /**
     * Sets a new add battery request to be send over the network.<br><br>
     * @param designation The designation of the battery.
     * @param amount The amount of the battery.
     * @param drones The drones which can use this battery separated by ,. format: 2,3,4
     * @throws IllegalStateException If one of the values is null
     */
    public void addBattery(String designation, int amount, String drones) throws IllegalStateException
    {
        this.setRequestData(true, ADD_BATTERY_TAG, "/index.php/akku/add_akku",
                this.byteArrayOutputStream("bezeichnung=" + designation + "&anzahl=" + amount + "&drohnen[]=" + drones), false);
    }

    /**
     * Sets a new show battery request to be send over the network.<br><br>
     * @param batteryId The id of the battery.
     * @throws IllegalStateException If one of the values is null
     */
    public void showBattery(int batteryId) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_BATTERY_TAG, "/index.php/akku/show/" + batteryId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit battery request to be send over the network.<br><br>
     * @param batteryId The id of the battery.
     * @param designation The designation of the battery.
     * @param amount The amount of the battery.
     * @param drones The drones which can use this battery separated by ,. format: 2,3,4
     * @throws IllegalStateException If one of the values is null
     */
    public void editBattery(int batteryId, String designation, int amount, String drones) throws IllegalStateException
    {
        this.setRequestData(true, EDIT_BATTERY_TAG, "/index.php/akku/edit_akku/" + batteryId,
                this.byteArrayOutputStream("bezeichnung=" + designation + "&anzahl=" + amount + "&drohnen[]=" + drones)
                , false);
    }

    /**
     * Sets a new delete battery request to be send over the network.<br><br>
     * @param batteryId The id of the battery.
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteBattery(int batteryId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_BATTERY_TAG, "/index.php/akku/delete_akku/" + batteryId,
                this.byteArrayOutputStream(""), false);
    }
}
