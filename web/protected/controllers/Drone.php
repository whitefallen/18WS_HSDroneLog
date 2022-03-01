<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Drone
 * Verwaltet alle Requests und Responses für Drone
 */
class Drone extends CI_Controller {

    /**
     * Drone constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Drone_model', 'drone');
        $this->load->model('Akku_model', 'akku');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Drone Aktion für Drone/
     */
    public function index()
    {
        $offset = $this->input->post('offset');
        if(!isset($offset)) {
            $offset = 0;
        }
        $drone_data = $this->show_all($offset);
        $view_data = array('drone_data' => $drone_data);
        if($offset != 0 ) {
            prepareResponse($drone_data, "", "dashboard/lademehr/mehr_drohnen.php", true, $view_data, "","");
        } else {
            prepareResponse($drone_data, "", "dashboard/drohnen.php", true, $view_data, "","");
        }
    }

    /**
     * Gibt die Info zu aller Dronen aus
     * @param int $offset
     * @return array
     */
    private function show_all($offset) {
        return $this->drone->show_all_drones($offset);
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einer Drohne aus
     * @param int $drone_id
     */
    public function show(int $drone_id) {
        $drone_data = $this->drone->show_a_drone($drone_id);
        if(empty($drone_data)) {
            prepareResponse($drone_data, "/drone/", "", false, array(), "Keine Drohne mit der ID: {$drone_id} gefunden","");
        } else {
            $akku_data = $this->akku->show_all_akkus();
            $view_data = array('drone_data' => $drone_data, 'current_id' => $drone_id, 'akku_data' => $akku_data);
            prepareResponse($drone_data, "", "dashboard/drohnendetails.php", true, $view_data, "","");
        }

    }

    /**
     * [API-Schnittstelle]
     * Fügt eine neue Drohne hinzu
     */
    public function add_drone() {
        $bAlreadySent = false;
        $input_data = $this->input->post();
        $drone_data = array();
        $sMessage = "";
        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                if(isMobileApp()) {
                    if($key == 'bild') {
                        break;
                    }
                    if($key == 'filename') {
                        $drone_data[$key] = $value;
                    }
                }
                if($key != 'session' && $key != 'filename') {
                    $drone_data[$key] = $value;
                }
            }
            if(isMobileApp() && isset($drone_data['filename'])) {
                $drone_data['bild'] = upload_image_from_mobile($drone_data, "bild=");
                unset($drone_data['filename']);
            } else if(!isMobileApp() && !isset($data['filename'])) {
                $upload_config['upload_path']          = $_ENV['UPLOAD_PATH'];
                $upload_config['allowed_types']        = $_ENV['IMG_TYPE'];
                $upload_config['max_size']             = $_ENV['UPLOAD_SIZE'];
                $upload_config['max_width']            = $_ENV['IMG_WIDTH'];
                $upload_config['max_height']           = $_ENV['IMG_HEIGHT'];
                $this->load->library('upload', $upload_config);

                $field_name = "bild";
                $this->upload->do_upload($field_name);
                $upload_data = $this->upload->data();
                if(!empty($upload_data['file_name']) && empty($this->upload->display_errors())) {
                    $drone_data['bild'] =  '/uploads/'.$upload_data['file_name'];
                } else {
                    $drone_data['bild'] = '/assets/images/drohne_quadrat.png';
                }
            } else {
                $drone_data['bild'] = '/assets/images/drohne_quadrat.png';
            }

            $this->drone->add_drone($drone_data);
            $error = $this->db->error();
            if(!empty($error['code'])) {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_drohnen_erstellung")[0]->nachricht;
                prepareResponse($drone_data, "/drone/", "", false, array(), $sMessage,"");
                exit();
            } else {
                $sMessage = $this->nachrichten->show_a_nachricht("erfolgreiche_drohnen_erstellung")[0]->nachricht;
            }
            $bAlreadySent = true;

        }
        if(!$bAlreadySent) {
            prepareResponse(array(), "", "dashboard/adddrohne.php", true,array(), $sMessage,"");
        } else {
            prepareResponse(array($drone_data), "/drone/", "", true, array(), $sMessage,"");
        }


    }

    /**
     * [API-Schnittstelle]
     * Löscht eine bestehende Drohne
     * @param $drone_id
     */
    public function delete_drone($drone_id) {
        $bSuccess = false;
        $sMessage = "Keine Drohne mit der ID:$drone_id vorhanden";
        $mDroneData = $this->drone->show_a_drone($drone_id);
        if(!empty($mDroneData)) {
            $this->drone->delete_a_drone($drone_id);
            $bSuccess = true;
            $sMessage = "Drohne mit der ID:$drone_id wurde gelöscht.";
        }
        prepareResponse(array(), "/drone/", "", $bSuccess, array(), $sMessage,"");
    }


    /**
     * [API-Schnittstelle]
     * Lässt den derzeit eingeloggten Piloten eine Drohne reservieren
     * @param $drone_id
     */
    public function reservate_drone($drone_id) {
        $iPilot_Id = $this->session->users['pilot_id'];
        $this->drone->reservate_drone($drone_id, $iPilot_Id);
    }

    /**
     * [API-Schnittstelle]
     * Editiert eine bereits vorhandene Drohne
     * @param $drone_id
     */
    public function edit_drone($drone_id) {
        $data = array();
        if($drone_id != 0) {
            $existingDrones = $this->drone->show_drones_id();
            $bIsValid = isValidId($drone_id, $existingDrones);
            if($bIsValid) {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    foreach($input_data as $key => $value) {
                        if(isMobileApp()) {
                            if($key == 'bild') {
                                break;
                            }
                            if($key == 'filename') {
                                $data[$key] = $value;
                            }
                        }
                        if($key != 'session' && $key != 'filename') {
                            $data[$key] = $value;
                        }
                    }

                    if(isMobileApp() && isset($data['filename'])) {
                        $data['bild'] = upload_image_from_mobile($data, "bild=");
                        unset($data['filename']);
                    } else if(!isMobileApp() && !isset($data['filename'])) {
                        $upload_config['upload_path']          = $_ENV['UPLOAD_PATH'];
                        $upload_config['allowed_types']        = $_ENV['IMG_TYPE'];
                        $upload_config['max_size']             = $_ENV['UPLOAD_SIZE'];
                        $upload_config['max_width']            = $_ENV['IMG_WIDTH'];
                        $upload_config['max_height']           = $_ENV['IMG_HEIGHT'];
                        $this->load->library('upload', $upload_config);

                        $field_name = "bild";
                        $this->upload->do_upload($field_name);
                        $upload_data = $this->upload->data();
                        if(!empty($upload_data['file_name']) && empty($this->upload->display_errors())) {
                            $data['bild'] = '/uploads/'.$upload_data['file_name'];
                        }
                    }
                    $data['drohne_id'] = $drone_id;
                    $this->drone->edit_drone($data);
                    prepareResponse(array($data), '/drone/show/'.$drone_id.'', "", true, array(), "","");
                }
            } else {
                prepareResponse(array(), "/drone/", "", false, array(), "Diese ID gibt es nicht","");
            }
        } else {
            prepareResponse(array(), '/drone/', "", false, array(), "","");
        }
    }

}