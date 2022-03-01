<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Akku
 * Verwaltet alle Requests und Responses für Akku
 */
class Akku extends CI_Controller {

    /**
     * Akku constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Akku_model', 'akku');
        $this->load->model('Drone_model', 'drone');
        $this->load->model('Akkus_zuordnen_model', 'akk_zu');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Akku Aktion für Akku/
     */
    public function index()
    {
        $offset = $this->input->post('offset');
        if(!isset($offset)) {
            $offset = 0;
        }
        $akku_data = $this->show_all($offset);
        $view_data = array('akku_data' => $akku_data);
        if($offset != 0) {
            prepareResponse($akku_data, "", "dashboard/lademehr/mehr_akkus.php", true, $view_data, "");
        } else {
            prepareResponse($akku_data, "", "dashboard/akkus.php", true, $view_data, "");
        }
    }

    /**
     * Gibt die Info zu allen Akkus aus
     * @param int $offset
     * @return array
     */
    private function show_all($offset) {
        return $this->akku->show_all_akkus($offset);
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einem Akku aus
     * @param int $akku_id
     */
    public function show(int $akku_id) {
        $akku_data = $this->akku->show_a_akku($akku_id);
        $current_drone_data = $this->akk_zu->show_all_drones($akku_id);
        $drone_data = $this->drone->show_all_drones(0);

        // Dronen Daten für das Select aufbereiten
        foreach($drone_data as $drone_key => $drone_value) {
            $drone_data[$drone_key]->selected = false;
            foreach($current_drone_data as $current_drone_value) {
                if($drone_value->drohne_id === $current_drone_value->drohne_id) {
                    $drone_data[$drone_key]->selected = true;
                }
            }
        }

        if(empty($akku_data)) {
            prepareResponse($akku_data, "/akku/", "", false, array(), "Kein Akku mit der ID:$akku_id vorhanden", "");
        } else {
            $view_data = array('akku_data' => $akku_data, 'current_id'  => $akku_id, 'drone_data' => $drone_data);
            foreach ($current_drone_data as $d_data) {
                $akku_data[0]->drones[] = $d_data->drohne_id;
            }
            prepareResponse($akku_data, "", "dashboard/akkudetails.php", true, $view_data, "", "");
        }

    }

    /**
     * [API-Schnittstelle]
     * Fügt einen neuen Akku hinzu
     */
    public function add_akku() {
        $bAlreadySent = false;
        $akku_data = array();
        $input_data = $this->input->post();
        $drone_data = $this->drone->show_all_drones();
        $sMessage ="";
        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                if($key != 'session') {
                    if ($key === 'drohnen') {
                        $mMultiple = strpos($value[0], ',');
                        if ($mMultiple !== false) {
                            $nValue = explode(',', $value[0]);
                            $akku_data[$key] = $nValue;
                        } else {
                            $akku_data[$key] = $value;
                        }
                    } else {
                        $akku_data[$key] = $value;
                    }
                }
            }
            $bSuccess = $this->akku->add_akku($akku_data);
            $error = $this->db->error();
            if(!empty($error['code']) || $bSuccess === false) {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_akku_erstellung")[0]->nachricht;
                prepareResponse(array($akku_data), "/akku/", "", false, array(), $sMessage,"");
                exit();
            } else {
                $sMessage = $this->nachrichten->show_a_nachricht("erfolgreiche_akku_erstellung")[0]->nachricht;
            }
            $bAlreadySent = true;
        }
        if(!$bAlreadySent) {
            prepareResponse(array($drone_data), "", "dashboard/addakku.php", true, array('drone_data' => $drone_data), $sMessage, "");
        } else {
            prepareResponse(array($akku_data), "/akku/", "", true, array(), $sMessage, "");
        }
    }

    /**
     * [API-Schnittstelle]
     * Löscht einen bestehenden Akku
     * @param $akku_id
     */
    public function delete_akku($akku_id) {
        $bSuccess = false;
        $sMessage = "Keinen Akku mit der ID:$akku_id vorhanden";
        $mAkkuData = $this->akku->show_a_akku($akku_id);

        if(!empty($mAkkuData)) {
            $this->akku->delete_a_akku($akku_id);
            $error = $this->db->error();
            if(!empty($error['code'])) {
                $bSuccess = false;
                $sMessage = "Der Akku mit der ID:$akku_id konnte nicht gelöscht werden.";
            } else {
                $bSuccess = true;
                $sMessage = "Der Akku mit der ID:$akku_id wurde gelöscht.";
            }
        }
        prepareResponse(array(), "/akku/", "", $bSuccess, array(), $sMessage,"");
    }

    /**
     * [API-Schnittstelle]
     * Editiert einen bereits vorhandenen Akku
     * @param $akku_id
     */
    public function edit_akku($akku_id) {
        if($akku_id != 0) {
            $existingAkkus = $this->akku->show_akkus_id();
            $bIsValid = isValidId($akku_id, $existingAkkus);
            if($bIsValid) {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    foreach($input_data as $key => $value) {
                        if($key != 'session') {
                            if ($key === 'drohnen') {
                                $mMultiple = strpos($value[0], ',');
                                if ($mMultiple !== false) {
                                    $nValue = explode(',', $value[0]);
                                    $data[$key] = $nValue;
                                } else {
                                    $data[$key] = $value;
                                }
                            } else {
                                $data[$key] = $value;
                            }
                        }
                    }
                    $data['akku_id'] = $akku_id;
                    $this->akku->edit_akku($data);
                    prepareResponse(array($data), "/akku/show/".$akku_id, "", true, array(), "","");
                }
            } else {
                prepareResponse(array(), "/akku/", "", false, array(), "Keine Daten für die ID vorhanden","");
            }
        } else {
            prepareResponse(array(), "/akku/", "", false, array(), "","");
        }

    }

}