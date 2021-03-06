<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Checklist_Element
 * Verwaltet alle Requests und Responses für Checklisten
 */
class Checklistelement extends CI_Controller {

    /**
     * Checklist_Element constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Checklisten_elemente_model', 'c_element');
        $this->load->model('Checkliste_name_model', 'checklist');
        $this->load->model('Checkliste_zuordnen_model', 'ch_zu');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Checklist Aktion für Checklist_Element/
     */
    public function index() {
        $element_data = $this->show_all();
        $view_data = array('element_data' => $element_data);
        prepareResponse($element_data, "", "dashboard/element.php", true, $view_data, "", "");
    }

    /**
     * Gibt die Info zu allen Checklist_Elemente aus
     */
    private function show_all() {
        return $this->c_element->show_all_elements();
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einer Checklist_Element aus
     * @param int $checklist_element_id
     */
    public function show(int $checklist_element_id) {
        $element_data = $this->c_element->show_a_element($checklist_element_id);
        $checklist_data = $this->ch_zu->show_all_checklists($checklist_element_id);
        $all_checklist_data = $this->checklist->show_all_checklists(-1);
        foreach ($checklist_data as $e_data) {
            $element_data[0]->checklists[] = $e_data->checkliste_name_id;
        }
        // Checklisten Daten für das Select aufbereiten
        foreach($all_checklist_data as $checklist_key => $checklist_value) {
            $all_checklist_data[$checklist_key]->selected = false;
            foreach($checklist_data as $current_checklist_value) {
                if($checklist_value->checkliste_name_id === $current_checklist_value->checkliste_name_id) {
                    $all_checklist_data[$checklist_key]->selected = true;
                }
            }
        }
        if(empty($element_data)) {
            prepareResponse($element_data, "/checklist/", "", false, array(), "Kein Element mit der ID: {$checklist_element_id} vorhanden","");
        } else {
            $view_data = array('element_data' => $element_data, 'current_id' => $checklist_element_id, 'checklist_data' => $all_checklist_data);
            prepareResponse($element_data, "", "dashboard/checklistelementdetails.php", true, $view_data, "","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Fügt ein neues Checklist_Element hinzu
     */
    public function add_element() {
        $element_data = array();
        $input_data = $this->input->post();
        $bAlreadySent = false;

        $checklist_data = $this->checklist->show_all_checklists(-1);
        $view_data = array('checklist_data' => $checklist_data);
        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                if($key != 'session') {
                    if ($key === 'checklists') {
                        $mMultiple = strpos($value[0], ',');
                        if($mMultiple !== false) {
                            $nValue =  explode(',', $value[0]);
                            $element_data[$key] = $nValue;
                        } else {
                            $element_data[$key] = $value;
                        }
                    } else {
                        $element_data[$key] = $value;
                    }
                }
            }
            $this->c_element->add_element($element_data);
            $error = $this->db->error();
            if(!empty($error['message'])) {
                prepareResponse(array($element_data), "/checklist/", "", false, array(), $error['message'],"");
                exit();
            }
            $bAlreadySent = true;
        }

        if(!$bAlreadySent) {
            prepareResponse($checklist_data, "", "dashboard/addchecklistelement.php", true, $view_data, "","");
        } else {
            prepareResponse(array($element_data), "/checklist/", "", true, array(), "","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Löscht ein bestehendes Checklist_Element
     * @param $checklist_element_id
     */
    public function delete_element($checklist_element_id) {
        $bSuccess = false;
        if($_SESSION['users']['rolle'] == 1)  {
            $sMessage = "Es gibt kein Element mit der ID:$checklist_element_id.";
            $mElementData = $this->c_element->show_a_element($checklist_element_id);
            if(!empty($mElementData)) {
                $this->c_element->delete_a_element($checklist_element_id);
                $bSuccess = true;
                $sMessage = "Element mit der ID:$checklist_element_id wurde gelöscht.";
            }
            prepareResponse(array($checklist_element_id), '/checklist/', "", $bSuccess, array(), $sMessage,"");
        } else {
            prepareResponse(array($checklist_element_id), '/checklist/', "", $bSuccess, array(), "Keine Berechtigung","");
        }


    }

    /**
     * [API-Schnittstelle]
     * Editiert ein bereits vorhandenes Checklist_Element
     * @param $checklist_element_id
     */
    public function edit_element(int $checklist_element_id) {
        if($_SESSION['users']['rolle'] == 1)  {
            $sMessage = "Es gibt kein Element mit der ID:$checklist_element_id.";
            $mElementData = $this->c_element->show_a_element($checklist_element_id);
            if(!empty($mElementData)) {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    foreach($input_data as $key => $value) {
                        if($key != 'session') {
                            if ($key == 'checklists') {
                                $mMultiple = strpos($value[0], ',');
                                if($mMultiple !== false) {
                                    $nValue =  explode(',', $value[0]);
                                    $element_data[$key] = $nValue;
                                } else {
                                    $element_data[$key] = $value;
                                }

                            } else {
                                $element_data[$key] = $value;
                            }
                        }
                    }
                    $element_data['element_id'] = $checklist_element_id;
                    $this->c_element->edit_element($element_data);
                    $sMessage = "Element mit der ID:$checklist_element_id. bearbeitet";
                    prepareResponse(array($element_data), '/checklistelement/show/'.$checklist_element_id.'', "", true, array(), $sMessage,"");
                }
            } else {
                prepareResponse(array(), '/checklist', "", false, array(), $sMessage,"");
            }
        } else {
            prepareResponse(array(), '/checklist', "", false, array(), "Keine Berechtigung","");
        }


    }
}