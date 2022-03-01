<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

if ( ! function_exists('prepareResponse')) {

    /**
     * Funktion zum vorbereiten eines Response entweder zum Web-Frontend oder zur Mobile App
     * @param $_data array
     * @param $_route string
     * @param $_view string
     * @param $_success bool
     * @param $_viewdata array
     * @param $_message string
     * @param $_session string
     * @param $_headercode string
     */
    function prepareResponse(array $_data = array(), string $_route = "", string $_view = "", bool $_success = false, array $_viewdata = array(), string $_message = "", string $_session = "", string $_headercode = "200")
    {
        // User Agent Laden für die Überprüfung
        $ci =& get_instance();
        $ci->load->library('user_agent');

        // Daten für Mobile App vorbereiten
        $_mobiledata['success'] = $_success;
        $_mobiledata['data'] = $_data;
        $_mobiledata['message'] = $_message;
        // Frontend Nachrichten mitgeben
        $_viewdata['message'] = $_message;
        // Mobile die Sitzung mitliefern
        if(!empty($_session)) {
            $_mobiledata['session'] = $_session;
        }
        if(isMobileApp()) {
            handleResponse($_mobiledata,$_route,$_view);
        } else {
            handleResponse($_viewdata,$_route,$_view);
        }
    }
}

if ( ! function_exists('getSession')) {

    /**
     * Funktion zum vorbereiten eines Response entweder zum Web-Frontend oder zur Mobile App
     */
    function getSession() {

        // Controller Inszanz laden, um Codeigniter Librarys zu nutzen
        $ci =& get_instance();
        $ci->load->library('user_agent');

        $sSessionId = "";
        // Prüfen ob es sich um ein Aufruf durch einen Browser oder durch eine App ist
        if(empty($ci->agent->is_browser())) {
            if(!empty($ci->input->post('session'))) {
                $sSessionId = $ci->input->post('session');
            } else {
                prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
                exit();
            }
        } else if($ci->agent->is_browser()) {
            $sSessionId = session_id();
        }

        // Session Information für Mobile vorbereiten
        $sessionData = show_data($sSessionId);
        if(isset($sessionData[0])) {
            session_decode(show_data($sSessionId)[0]->data);
        } else {
            // Cookie entfernen wenn es keine passende Session gibt
            if (isset($_COOKIE['ci_session'])) {
                unset($_COOKIE['ci_session']);
                setcookie('ci_session', '', time() - 3600, '/'); // empty value and old timestamp
            }
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
            exit();
        }
    }
}

if ( ! function_exists('isValidId')) {

    /**
     * Funktion zum Prüfen ob die Angefragte ID in der Tabelle existiert
     * @param $id
     * @param $data
     * @return bool;
     */
    function isValidId(int $id, array $data) {
        // Schauen ob es in der Datenmenge die angefrage ID gibt
        foreach ($data as $data_value) {
            if(in_array($id,$data_value)) {
                return true;
            }
        }
        return false;
    }
}

if ( ! function_exists('isMobileApp')) {
    /**
     * Prüft ob es sich um eine Anfrage von der App oder von einem Browser ist
     * @return bool
     */
    function isMobileApp() {
        $ci =& get_instance();

        if(empty($ci->agent->is_browser())) {
            return true;
        } else {
            return false;
        }
    }
}


if ( ! function_exists('handleResponse')) {

    /**
     * Entscheidet, wie die Antwort gesendet werden soll
     * @param array $_data
     * @param string $_route
     * @param string $_view
     */
    function handleResponse(array $_data, string $_route,string $_view) {
        $ci =& get_instance();
        // Daten an Frontend-Web oder Frontend-App weitergeben
        if ($ci->agent->is_browser()) {
            if (!empty($_route)) {
                redirect($_route, 'location');
            }
            if (!empty($_view)) {
                $ci->load->view($_view, $_data);
            }
            if (empty($_route) && empty($_view)) {
                header('Content-Type: application/json; charset=utf-8',false);
                $jsonData = json_encode($_data);
                echo $jsonData;
            }
        } else if (empty($ci->agent->is_browser())) {
            header('Content-Type: application/json; charset=utf-8', false);
            $jsonData = json_encode($_data);
            echo $jsonData;
        }
    }
}