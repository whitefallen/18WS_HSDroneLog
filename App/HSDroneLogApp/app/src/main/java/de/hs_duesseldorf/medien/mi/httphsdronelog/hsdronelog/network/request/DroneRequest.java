package de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import de.hs_duesseldorf.medien.mi.httphsdronelog.hsdronelog.network.request.parser.JSONRequestParser;

public class DroneRequest extends Request
{
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //public final static request id tags ////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**The identification tag of a allDrones request*/
    public final static String ALL_DRONES_TAG = "allDrones";
    /**The identification tag of a addDrone request*/
    public final static String ADD_DRONE_TAG = "addDrone";
    /**The identification tag of a showDrone request*/
    public final static String SHOW_DRONE_TAG = "showDrone";
    /**The identification tag of a editDrone request*/
    public final static String EDIT_DRONE_TAG = "editDrone";
    /**The identification tag of a deleteDrone request*/
    public final static String DELETE_DRONE_TAG = "deleteDrone";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new DroneRequest instance.
     * @param applicationContext The application Context of the app.
     *                           You can get this value from activity with
     *                           {@link Activity#getApplicationContext()}
     *                           or from fragment you can get the activity and then
     *                           the context with {@link Fragment#getActivity()}.
     */
    public DroneRequest(Context applicationContext)
    {
        super(new JSONRequestParser(applicationContext));
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param offset Gets the next 20 entries from with the offset as first entry. Starting at 0.
     * @throws IllegalStateException If one of the values is null
     */
    public void allDrones(int offset) throws IllegalStateException
    {
        this.setRequestData(true, ALL_DRONES_TAG, "/index.php/drone/",
                this.byteArrayOutputStream("offset=" + offset), false);
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param droneModell The name of the modell of the drone.
     * @param weightInGramm Thei weight of the drone in gramm.
     * @param flightTimeInMinutes The maximum flight time in minutes.
     * @param diagonalSizeInMM The diagonal size of the drone in mm.
     * @param maxFlightHeightInM The maximum flight height of the drone in m.
     * @param maximumSpeedInKmh The maximum speed of the drone in kmh.
     * @param image The image to set as new drone image. Null for no edit
     * @param imageFileName The name of the image file. Null if image is null.
     * @throws IllegalStateException If one of the values is null
     */
    public void addDrone(String droneModell, int weightInGramm, int flightTimeInMinutes, int diagonalSizeInMM,
                         int maxFlightHeightInM, int maximumSpeedInKmh, Bitmap image, String imageFileName) throws IllegalStateException
    {
        if (image != null)
            this.setRequestData(true, ADD_DRONE_TAG, "/index.php/drone/add_drone",
                    this.byteArrayOutputStream("drohnen_modell=" + droneModell + "&fluggewicht_in_gramm=" + weightInGramm
                            + "&flugzeit_in_min=" + flightTimeInMinutes + "&diagonale_groesse_in_mm=" + diagonalSizeInMM
                            + "&maximale_flughoehe_in_m=" + maxFlightHeightInM + "&hoechstgeschwindigkeit_in_kmh=" + maximumSpeedInKmh
                            + "&filename=" + imageFileName + "&bild=", image)
                    , false);
        else
            this.setRequestData(true, ADD_DRONE_TAG, "/index.php/drone/add_drone",
                    this.byteArrayOutputStream("drohnen_modell=" + droneModell + "&fluggewicht_in_gramm=" + weightInGramm
                            + "&flugzeit_in_min=" + flightTimeInMinutes + "&diagonale_groesse_in_mm=" + diagonalSizeInMM
                            + "&maximale_flughoehe_in_m=" + maxFlightHeightInM + "&hoechstgeschwindigkeit_in_kmh=" + maximumSpeedInKmh)
                    , false);
    }

    /**
     * Sets a new all flights request to be send over the network.<br><br>
     * @param droneId The id of the drone to show.
     * @throws IllegalStateException If one of the values is null
     */
    public void showDrone(int droneId) throws IllegalStateException
    {
        this.setRequestData(true, SHOW_DRONE_TAG, "/index.php/drone/show/" + droneId,
                this.byteArrayOutputStream(""), false);
    }

    /**
     * Sets a new edit flights request to be send over the network.<br><br>
     * @param droneId The id of the drone to show.
     * @param droneModell The name of the modell of the drone.
     * @param weightInGramm Thei weight of the drone in gramm.
     * @param flightTimeInMinutes The maximum flight time in minutes.
     * @param diagonalSizeInMM The diagonal size of the drone in mm.
     * @param maxFlightHeightInM The maximum flight height of the drone in m.
     * @param maximumSpeedInKmh The maximum speed of the drone in kmh.
     * @param image The image to set as new drone image. Null for no edit
     * @param imageFileName The name of the image file. Null if image is null.
     * @throws IllegalStateException If one of the values is null
     */
    public void editDrone(int droneId, String droneModell, int weightInGramm, int flightTimeInMinutes, int diagonalSizeInMM,
                          int maxFlightHeightInM, int maximumSpeedInKmh, Bitmap image, String imageFileName) throws IllegalStateException
    {
        if (image != null)
            this.setRequestData(true, EDIT_DRONE_TAG, "/index.php/drone/edit_drone/" + droneId,
                    this.byteArrayOutputStream("drohnen_modell=" + droneModell + "&fluggewicht_in_gramm=" + weightInGramm
                            + "&flugzeit_in_min=" + flightTimeInMinutes + "&diagonale_groesse_in_mm=" + diagonalSizeInMM
                            + "&maximale_flughoehe_in_m=" + maxFlightHeightInM + "&hoechstgeschwindigkeit_in_kmh=" + maximumSpeedInKmh
                            + "&filename=" + imageFileName + "&bild=", image),
                    false);
        else
            this.setRequestData(true, EDIT_DRONE_TAG, "/index.php/drone/edit_drone/" + droneId,
                    this.byteArrayOutputStream("drohnen_modell=" + droneModell + "&fluggewicht_in_gramm=" + weightInGramm
                            + "&flugzeit_in_min=" + flightTimeInMinutes + "&diagonale_groesse_in_mm=" + diagonalSizeInMM
                            + "&maximale_flughoehe_in_m=" + maxFlightHeightInM + "&hoechstgeschwindigkeit_in_kmh=" + maximumSpeedInKmh),
                    false);
    }

    /**
     * Sets a new delete flights request to be send over the network.<br><br>
     * @param droneId The id of the drone to delete.
     * @throws IllegalStateException If one of the values is null
     */
    public void deleteDrone(int droneId) throws IllegalStateException
    {
        this.setRequestData(true, DELETE_DRONE_TAG, "/index.php/drone/delete_drone/" + droneId,
                this.byteArrayOutputStream(""), false);
    }
}
