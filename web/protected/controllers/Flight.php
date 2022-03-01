<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Flug
 * Verwaltet alle Requests und Responses für Flug
 */
class Flight extends CI_Controller {

    /**
     * Flug constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Fluege_model', 'flight');
        $this->load->model('Drone_model', 'drone');
        $this->load->model('User_model', 'users');
        $this->load->model('Checkliste_name_model', 'checklist');
        $this->load->model('Checkliste_zuordnen_model', 'ch_zu');
        $this->load->model('Checkliste_werte_model', 'ch_we');
        $this->load->model('Fluglog_model', 'flug_log');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Flug Aktion für Flug/
     */
    public function index()
    {
        $flight_param = $this->input->post('offset');
        if(!isset($flight_param)) {
            $flight_param = 0;
        }
        $flight_data = $this->show_all($flight_param);
        $view_data = array('flight_data' => $flight_data);
        if($flight_param != 0) {
            prepareResponse($flight_data, "", "dashboard/lademehr/mehr_fluege.php", true, $view_data, "","");
        } else {
            prepareResponse($flight_data, "", "dashboard/fluege.php", true, $view_data, "","");
        }
    }

    /**
     * Gibt die Info zu allen Fluegen aus
     * @param int
     * @return array
     */
    public function show_all(int $offset) {
        return $this->flight->show_all_flights($offset);
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einem Flug aus
     * @param int $flight_id
     * @param bool $dev
     */
    public function show(int $flight_id, bool $dev = false) {

        $flight_data = $this->flight->show_a_flight($flight_id);
        $element_data = array();
        $checklist_id = null;
        if(!empty($flight_data)) {
            $checklist_id = $flight_data[0]->checkliste_name_id;
            if(isset($checklist_id)) {
                $element_data = $this->ch_zu->show_all_elements($checklist_id);
            }
        }

        foreach ($element_data as $data_key => $data_value){
            $element_id = $element_data[$data_key]->element_id;
            $werte_data = $this->ch_we->show_a_checkliste_werte($flight_id,$element_id);
            if(!empty($werte_data)) {
                $element_data[$data_key]->angekreuzt = $werte_data[0]->angekreuzt;
                $element_data[$data_key]->kommentar = $werte_data[0]->kommentar;
            } else {
                $element_data[$data_key]->angekreuzt = false;
                $element_data[$data_key]->kommentar = "";
            }
        }

        $checklist_data = $this->checklist->show_all_checklists();
        if(empty($flight_data)) {
            prepareResponse($flight_data, "/flight/", "", false, array(), "","");
        } else {
            $drone_data = $this->drone->show_all_drones();
            $user_data = $this->users->show_all_user();
            $view_data = array(
                'flight_data' => $flight_data,
                'current_id' => $flight_id,
                'drones' => $drone_data,
                'users' => $user_data,
                'element_data'=> $element_data,
                'checklist_data' => $checklist_data
            );
            foreach($element_data as $key => $value) {
                $flight_data[] = $value;
            }
            if($dev) {
                prepareResponse($flight_data, "", "testing/flugdetails.php", true, $view_data, "","");
            } else {
                prepareResponse($flight_data, "", "dashboard/flugdetails.php", true, $view_data, "","");
            }
        }
    }

    /**
     * [API-Schnittstelle]
     * Fügt einen neuen Flug hinzu
     */
    public function add_flight() {
        $session_id = "";
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "", "", false, array(), "Keine Session vorhanden","","401");
        } else {
            $session_id = $_SESSION['users']['pilot_id'];
        }

        $sMessage = "";
        $bAlreadySent = false;
        $input_data = $this->input->post();
        $drone_data = $this->drone->show_all_drones();
        $user_data = $this->users->show_all_user();
        $checklist_data = $this->checklist->show_all_checklists();
        $flight_data = array();

        if(!empty($input_data) && !empty($session_id)) {
            foreach($input_data as $key => $value) {
                if($key != 'session') {
                    $flight_data[$key] = $value;
                }
            }
            //$flight_data['pilot_id'] = $session_id;
            $bValidDates = $this->checkFlightDates($flight_data);
            $this->resetYear($flight_data);
            if($bValidDates == false) {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_flug_erstellung")[0]->nachricht;
                prepareResponse(array($flight_data), "/flight/", "", false, array(), $sMessage,"");
                exit();
            }
            $flight_data['flugdauer'] = $this->calculateFlightDuration($flight_data['einsatzbeginn'],$flight_data['einsatzende']);
            $iId = $this->flight->add_flight($flight_data);
            $error = $this->db->error();
            if(!empty($error['code'])) {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_flug_erstellung")[0]->nachricht;
                prepareResponse(array($flight_data), "/flight/", "", false, array(), $sMessage,"");
                exit();
            } else {
                $sMessage = $this->nachrichten->show_a_nachricht("erfolgreiche_flug_erstellung")[0]->nachricht;
                $coords = $this->locationCodes($flight_data['einsatzort_name']);
                if(!empty($coords)) {
                    $coords['flug_id'] = $iId;
                    $this->flight->edit_flight($coords);
                }
            }
            $bAlreadySent = true;
        }
        if(!$bAlreadySent) {
            prepareResponse(array($drone_data), "", "dashboard/addflug.php", true, array('drone_data' => $drone_data, 'user_data' => $user_data, 'checklist_data' => $checklist_data), $sMessage, "");
        } else {
            prepareResponse(array($flight_data), "/flight/", "", true, array(), $sMessage,"");
        }


    }

    /**
     * [API-Schnittstelle]
     * Löscht einen bestehenden Flug
     * @param $flight_id
     */
    public function delete_flight($flight_id) {
        $flight_data = $this->flight->show_a_flight($flight_id);
        $bSucess = false;
        $sMessage = "Es gibt keinen Flug mit der ID:$flight_id";
        if(!empty($flight_data)) {
            $this->flight->delete_a_flight($flight_id);
            $bSucess = true;
            $sMessage = "Flug mit der ID:$flight_id wurde gelöscht";
        }
        prepareResponse(array(), "/flight/", "", $bSucess, array(), $sMessage,"");
    }

    /**
     * [API-Schnittstelle]
     * Editiert einen Flug
     * @param $flight_id
     */
    public function edit_flight($flight_id) {
        if($flight_id != 0) {
            $existingFlights = $this->flight->show_flights_id();
            $bIsValid = isValidId($flight_id, $existingFlights);
            if($bIsValid) {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    foreach($input_data as $key => $value) {
                        if($key != 'session') {
                            $data[$key] = $value;
                        }
                    }
                    $data['flug_id'] = $flight_id;
                    $bValidDates = $this->checkFlightDates($data);
                    $this->resetYear($data);
                    if($bValidDates == false) {
                        $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_flug_erstellung")[0]->nachricht;
                        prepareResponse(array($data), "/flight/", "", false, array(), $sMessage,"");
                        exit();
                    }
                    $data['flugdauer'] = $this->calculateFlightDuration($data['einsatzbeginn'],$data['einsatzende']);
                    $this->flight->edit_flight($data);

                    if(!empty($data['einsatzort_name'])) {
                        $coords = $this->locationCodes($data['einsatzort_name']);
                        if(!empty($coords)) {
                            $coords['flug_id'] = $flight_id;
                            $this->flight->edit_flight($coords);
                        }
                     }
                    prepareResponse(array($data), '/flight/show/'.$flight_id.'', "", true, array(), "","");
                }
            } else {
                prepareResponse(array(), "/flight/", "", false, array(), "Diese ID gibt es nicht","");
            }
        } else {
            prepareResponse(array(), "/flight/", "", false, array(), "","");
        }
    }

    /**
     * Benutzt die Bing Geocoding API um Orte in Geo-Koordinaten umzuwandeln
     * @param string $einsatzort_name
     * @return array
     */
    public function locationCodes(string $einsatzort_name) {
        $einsatzort_name = cleanNames($einsatzort_name);
        $apiKey = $_ENV['API_KEY'];
        if(!empty($einsatzort_name)) {
            $einsatzort_nameUTF8 = utf8_encode($einsatzort_name);
            $url = "https://dev.virtualearth.net/REST/v1/Locations/DE/{$einsatzort_nameUTF8}?output=json&key=".$apiKey;
            $url = str_ireplace(" ","%20",$url);
            header('Content-Type: application/json; charset=utf-8');
            $curl = curl_init($url);
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            $curl_response = curl_exec($curl);
            if ($curl_response === false) {
                $info = curl_getinfo($curl);
                curl_close($curl);
                die('error occured during curl exec. Additioanl info: ' . var_export($info));
            }
            curl_close($curl);
            $response = $curl_response;
            $myArray = json_decode($response, true);
            if(!empty($myArray["resourceSets"][0]["resources"][0]["point"]["coordinates"])) {
                $coords = ($myArray["resourceSets"][0]["resources"][0]["point"]["coordinates"]);
                $coordinates = array('breitengrad' => $coords[0], 'laengengrad'=> $coords[1]);
                return $coordinates;
            }
        }
    }

    /**
     * [API-Schnittstelle]
     * Ermittelt die Verfügbaren Drohnen
     */
    public function requestAvailableDrones() {
        header('Content-Type: application/json; charset=utf-8');

        $ajaxTime = $this->input->post('startzeit');
        $ajaxDatum = $this->input->post('datum');
        $ajaxEndTime = $this->input->post('endzeit');

        $unusedDrones = $this->flight->show_available_drones($ajaxDatum,$ajaxTime,$ajaxEndTime);
        if(empty($this->agent->is_browser())) {
            prepareResponse($unusedDrones, "", "", true, array(), "","");
        } else {
            echo json_encode($unusedDrones);
        }
    }

    /**
     * Hilsfunktion zum Validieren von Gültigen Tagen/Zeiten
     * @param $flight_data
     * @return bool
     */
    private function checkFlightDates(&$flight_data) {
        $bValidDate = true;

        $minDate = mktime(0,0);
        $maxDate = mktime(23,59);
        $minDate = date("H:i",$minDate);
        $maxDate = date("H:i",$maxDate);
        if(
            $flight_data['einsatzbeginn'] < $minDate ||
            $flight_data['einsatzbeginn'] > $maxDate ||
            $flight_data['einsatzende'] < $minDate ||
            $flight_data['einsatzende'] > $maxDate+1
        ) {
            $bValidDate = false;
        }
        if($bValidDate) {
            if($flight_data['einsatzende'] < $flight_data['einsatzbeginn']) {
                $flight_data['einsatzende'] = $maxDate;
            }
        }
        return $bValidDate;
    }

    private function calculateFlightDuration($start, $end) {
        $endTimeStamp = strtotime($end);
        $startTimeStamp = strtotime($start);
        $duration = ($endTimeStamp-$startTimeStamp);
        return $duration;
    }

    public function checkForAkkus(int $drone_id) {
        // @TODO Funktion um die Akkus einer Drohne zu bekommen
    }

    public function widthdrawAkkus(array $akku_ids) {
        // @TODO Funktion um die Akkus "auszuleihen" für eine Drohne
    }

    /**
     * Verhindert das Dumme User zuweit in die Vergangenheit oder Zukunft planen
     * @param $flight_data
     */
    private function resetYear(&$flight_data) {
        $currentYear = $flight_data['flugdatum'];
        $endYear = date('Y-m-d', strtotime('+2 years'));
        $beginYear = date('Y-m-d',  mktime(0, 0, 0, 12, 30, 2016));

        if($currentYear > $endYear) {
            $flight_data['flugdatum'] = $endYear;
        }
        if($currentYear < $beginYear) {
            $flight_data['flugdatum'] = $beginYear;
        }
    }

    /*
    public function test() {
        $flight_param = $this->input->post('offset');
        if(!isset($flight_param)) {
            $flight_param = 0;
        }
        $flight_data = $this->show_all($flight_param);
        $view_data = array('flight_data' => $flight_data);
        if($flight_param != 0) {
            prepareResponse(array(), "", "testing/more_fluege.php", true, $view_data, "","");
        } else {
            prepareResponse(array(), "", "testing/fluege.php", true, $view_data, "","");
        }
    }
    */

    public function upload_log($flug_id) {
        $csvAsArray = array();
        if(!isMobileApp()) {
            $upload_config['upload_path']          = $_ENV['UPLOAD_PATH'];
            $upload_config['allowed_types']        = 'csv';
            $this->load->library('upload', $upload_config);

            $field_name = "log";
            $this->upload->do_upload($field_name);
            $upload_data = $this->upload->data();
            $csvAsArray = array_map('str_getcsv', file($upload_data['full_path']));
        } else {
            $imageNeddle = "log=";
            $inputStream = file_get_contents('php://input');
            $fileData = substr($inputStream, strpos($inputStream, $imageNeddle)+strlen($imageNeddle));
            $filename =  "temp_csv";
            $fhandle=fopen('./uploads/'.$filename, 'wb');
            fwrite($fhandle, $fileData);
            fclose($fhandle);
            $csvAsArray = array_map('str_getcsv', file('./uploads/'.$filename));
            unlink('./uploads/'.$filename);
        }
        if(!empty($csvAsArray)) {
            $preparedCSV = $this->processCSVData($csvAsArray);
            if(!empty($preparedCSV)) {
                $this->insertLogIntoDB($preparedCSV,$flug_id);
            }
        }
        prepareResponse(array(), "flight/show/".$flug_id, "", true, array(), "","");
    }

    private function processCSVData(array $_csv_data) {
        //Get Headers
        $headers = $this->getCSVHeaders($_csv_data);
        //Remove Header from CSV Data
        unset($_csv_data[0]);
        $preparedCSV = array();
        // Iterate through every row, and save every cell matching the Headers into the new array
        foreach ($_csv_data as $csv_key => $csv_row) {
            foreach($csv_row as $csv_cell_key => $csv_cell) {
                if(isset($headers[$csv_cell_key])) {
                    $preparedCSV[$headers[$csv_cell_key]][] = $csv_cell;
                }
            }
        }
        return $preparedCSV;
    }

    private function getCSVHeaders(array $_csv_data) {
        $maxHeaderInc = sizeof($_csv_data[0]);
        $headers = array();
        for($i = 0; $i < $maxHeaderInc; $i++) {

            if($_csv_data[0][$i] == "Latitude") {
                $headers[$i] = 'latitude';
            }
            if($_csv_data[0][$i] == "Longitude") {
                $headers[$i] = 'longitude';
            }
            if($_csv_data[0][$i] == "Altitude(meters)") {
                $headers[$i] = 'altitude';
            }
            if($_csv_data[0][$i] == "HomeLatitude") {
                $headers[$i] = 'homeLatitude';
            }
            if($_csv_data[0][$i] == "HomeLongitude") {
                $headers[$i] = 'homeLongitude';
            }
            if($_csv_data[0][$i] == "BatteryPower(%)") {
                $headers[$i] = 'battery_percent';
            }
            if($_csv_data[0][$i] == "AppWarning") {
                $headers[$i] = 'app_warning';
            }
        }
        return $headers;
    }

    private function insertLogIntoDB(array $log_data, $flug_id) {
        $index_count = count($log_data['latitude']);
        $latitude = 0;
        $longitude = 0;
        for($index = 0; $index < $index_count; $index++) {
            if($index == 0) {
                $latitude = $log_data['latitude'][$index];
                $longitude = $log_data['longitude'][$index];
            }
            $this->flug_log->add_fluglog(array(
                'flug_id' => $flug_id,
                'latitude' => $log_data['latitude'][$index],
                'longitude' => $log_data['longitude'][$index],
                'altitude' => $log_data['altitude'][$index],
                'homeLatitude' => $log_data['homeLatitude'][$index],
                'homeLongitude' => $log_data['homeLongitude'][$index],
                'battery_percent' => $log_data['battery_percent'][$index],
                'app_warning' => $log_data['app_warning'][$index]
            ));
        }

        if($latitude !=0 && $longitude != 0) {
            $this->flight->edit_flight(array(
                'flug_id' => $flug_id,
                'breitengrad' => $latitude,
                'laengengrad' => $longitude,
            ));
        }
    }

    public function test_log($flug_id) {
        prepareResponse(array(), "", "testing/flight_log.php", true, array('flug_id'=>$flug_id), "","");
    }

    public function show_flight_log($flug_id) {
        $offset = $this->input->post('offset');
        $view = "dashboard/fluglog.php";
        if($offset == null) {
            $offset = 0;
        }
        $data = $this->flug_log->show_a_fluglog($flug_id, $offset);
        $view_data = array('fluglog'=>$data);
        if($offset != 0 ) {
            $view = "dashboard/lademehr/mehr_fluglog.php";
            prepareResponse($data, "", $view, true, $view_data, "","");
        }
        prepareResponse($data, "", $view, true, $view_data, "","");

    }

}