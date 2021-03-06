<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Dashboard
 * Verwaltet alle Requests und Responses für Dashboard
 */
class Dashboard extends CI_Controller {

    /**
     * Dashboard constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('User_model', 'user_model');
        $this->load->model('Drone_model', 'drone');
        $this->load->model('Fluege_model', 'flight');
        $this->load->model('Nachrichten_model', 'messages');
        getSession();
    }

    /**
     * Dashboard Aktion für Dashboard/
    */
    public function index()
    {
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "index/login", "", false, array(), "Keine Session vorhanden","");
        } else {
            if(!isMobileApp()) {
                $pilot_of_the_week = $this->pilot_of_the_week();
                $most_used_drones = $this->most_used_drones();
                $amount_drones = $this->amount_drones();
                $longest_flight = $this->longest_flight();
                $flight_data = $this->flight_data();
                $flight_data_before = $this->flight_data_before();
                $flight_data_today = $this->flight_data_today();
                $flight_data_after = $this->flight_data_after();
                $flight_data_without_drones = $this->flights_without_drones();
                $view_data = array(
                    'most_used_drones' => $most_used_drones,
                    'amount_drones' => $amount_drones,
                    'longest_flight' => $longest_flight,
                    'flight_data_before' => $flight_data_before,
                    'flight_data_today' => $flight_data_today,
                    'flight_data_after' => $flight_data_after,
                    'flight_data' => $flight_data,
                    'pilot_of_the_week' => $pilot_of_the_week,
                    'flight_data_without_drones' => $flight_data_without_drones,
                    'current_id' => $_SESSION['users']['pilot_id']
                );
                prepareResponse($view_data, "", "dashboard/index.php", true, $view_data, "","");
            } else {
                prepareResponse(array(), "", "", true, array(), "Keine Daten fürs Dashbord vorhanden","");
            }
        }
    }

    /**
     * User Aktion für Dashboard/home
     */
    public function home()
    {
        prepareResponse(array(), "dashboard", "", true, array(), "","");
    }

    /**
     * [API-Schnittstelle]
     * Gibt die meist genutzten Drohnen zurück
     * @return mixed
     */
    public function most_used_drones() {
        $most_used_drones = $this->flight->show_most_used_drones();
        if(isMobileApp()) {
            prepareResponse($most_used_drones, "", "", true, array(), "","");
        }
        return $most_used_drones;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die anzahl der Drohnen zurück
     * @return int
     */
    public function amount_drones() {
        $amount_drones = count($this->drone->show_all_drones());
        if(isMobileApp()) {
            prepareResponse(array("amount_drones" =>$amount_drones), "", "", true, array(), "","");
        }
        return $amount_drones;
    }

    /**
     * [API-Schnittstelle]
     * Gibt den Längsten Flug zurück
     * @return mixed
     */
    public function longest_flight() {
        $longest_flight = $this->flight->show_longest_flight();
        if(isMobileApp()) {
            prepareResponse($longest_flight, "", "", true, array(), "","");
        }
        return $longest_flight;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Flug daten zurück
     * @return mixed
     */
    public function flight_data() {
        $flight_data = $this->flight->show_all_flights();
        if(isMobileApp()) {
                prepareResponse($flight_data, "", "", true, array(), "","");
        }
        return $flight_data;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die vergangenen Flüge zurück
     * @return mixed
     */
    public function flight_data_before() {
        $flight_data_before = $this->flight->show_flights_before_today();
        if(isMobileApp()) {
            prepareResponse($flight_data_before, "", "", true, array(), "","");
        }
        return $flight_data_before;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Heutigen Flüge zurück
     * @return mixed
     */
    public function flight_data_today() {
        $flight_data_today = $this->flight->show_flights_for_today();
        if(isMobileApp()) {
            prepareResponse($flight_data_today, "", "", true, array(), "","");
        }
        return $flight_data_today;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die geplanten Flüge zurück
     * @return mixed
     */
    public function flight_data_after() {
        $flight_data_after = $this->flight->show_flights_after_today();
        if(isMobileApp()) {
            prepareResponse($flight_data_after, "", "", true, array(), "","");
        }
        return $flight_data_after;
    }

    /**
     * [API-Schnittstelle]
     * Gibt den Pilot der Woche zurück
     * @return mixed
     */
    public function pilot_of_the_week() {
        $pilot_of_the_week = $this->flight->pilot_of_the_week();
        if(isMobileApp()) {
            prepareResponse($pilot_of_the_week, "", "", true, array(), "","");
        }
        return $pilot_of_the_week;
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Fluege zurueck die keine Drohne haben
     * @return mixed
     */
    public function flights_without_drones() {
        $flight_data = $this->flight->show_flights_without_drones();
        if(isMobileApp()) {
            prepareResponse($flight_data, "", "", true, array(), "","");
        }
        return $flight_data;
    }

}
