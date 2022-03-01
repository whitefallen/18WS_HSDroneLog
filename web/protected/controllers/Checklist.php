<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Checklist
 * Verwaltet alle Requests und Responses für Checklisten
 */
class Checklist extends CI_Controller {

    /**
     * Checklist constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Checkliste_name_model', 'checklist');
        $this->load->model('Checklisten_elemente_model', 'ch_el');
        $this->load->model('Checkliste_zuordnen_model', 'ch_zu');
        $this->load->model('Checkliste_werte_model', 'ch_we');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Checklist Aktion für Checklist/
     */
    public function index() {
        $offset = $this->input->post('offset');
        if(!isset($offset)) {
            $offset = 0;
        }
        $checklist_data = $this->show_all($offset);
        $element_data = $this->ch_el->show_all_elements();
        $view_data = array('checklist_data' => $checklist_data,'element_data' => $element_data);
        if($offset != 0) {
            prepareResponse($checklist_data, "", "dashboard/lademehr/mehr_checklisten.php", true, $view_data, "","");
        } else {
            prepareResponse($checklist_data, "", "dashboard/checklisten.php", true, $view_data, "","");
        }
    }

    /**
     * Gibt die Info zu allen Checklisten aus
     * @param int $offset
     * @return array
     */
    private function show_all($offset) {
        return $this->checklist->show_all_checklists($offset);
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einer Checkliste aus
     * @param int $checklist_id
     */
    public function show(int $checklist_id) {
        $checklist_data = $this->checklist->show_a_checklist($checklist_id);
        $element_data = $this->ch_zu->show_all_elements($checklist_id);
        foreach ($element_data as $e_data) {
            $checklist_data[0]->elements[] = $e_data->element_id;
        }
        $element_all_data = $this->ch_el->show_all_elements(-1);
        // Element Daten für das Select aufbereiten
        foreach($element_all_data as $element_key => $element_value) {
            $element_all_data[$element_key]->selected = false;
            foreach($element_data as $current_element_value) {
                if($element_value->element_id === $current_element_value->element_id) {
                    $element_all_data[$element_key]->selected = true;
                }
            }
        }
        $view_data = array('checklist_data' => $checklist_data,'current_id' => $checklist_id, 'element_data' => $element_all_data);
        if(empty($checklist_data)) {
            prepareResponse($checklist_data, "/checklist/", "", true, array(), "","");
        } else {
            prepareResponse($checklist_data, "", "dashboard/checklistendetails.php", true, $view_data, "","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Fügt eine neue Checkliste hinzu
     */
    public function add_checklist() {

        $input_data = $this->input->post();
        $bAlreadySent = false;
        $ch_ele = $this->ch_el->show_all_elements(-1);
        $checklist_data = array();
        $sMessage = "";
        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                if($key != 'session') {
                    if ($key === 'elements') {
                        $mMultiple = strpos($value[0], ',');
                        if($mMultiple !== false) {
                            $nValue =  explode(',', $value[0]);
                            $checklist_data[$key] = $nValue;
                        } else {
                            $checklist_data[$key] = $value;
                        }
                    } else {
                        $checklist_data[$key] = $value;
                    }
                }
            }
            $this->checklist->add_checklist($checklist_data);
            $error = $this->db->error();
            if(!empty($error['code'])) {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_checklisten_erstellung")[0]->nachricht;
                prepareResponse(array($checklist_data), "/checklist/", "", false, array(), $sMessage,"");

                exit();
            } else {
                $sMessage = $this->nachrichten->show_a_nachricht("erfolgreiche_checklisten_erstellung")[0]->nachricht;
            }
            $bAlreadySent = true;
        }
        if(!$bAlreadySent) {
            prepareResponse(array(), "", "dashboard/addchecklist.php", true, array('element_data' => $ch_ele), $sMessage,"");
        } else {
            prepareResponse(array($checklist_data), "/checklist/", "", true, array(), $sMessage,"");
        }
    }

    /**
     * [API-Schnittstelle]
     * Löscht eine bestehende Checkliste
     * @param $checklist_id
     */
    public function delete_checklist($checklist_id) {
        if($_SESSION['users']['rolle'] == 1)  {
            $bSuccess = false;
            $sMessage = "Es gibt keine Checkliste mit der ID:$checklist_id .";
            $checklistData = $this->checklist->show_a_checklist($checklist_id);
            if(!empty($checklistData)) {
                $this->checklist->delete_a_checklist($checklist_id);
                $bSuccess = true;
                $sMessage = "Checkliste mit der ID:$checklist_id wurde gelöscht.";
            }
            prepareResponse(array(), "/checklist/", "", $bSuccess, array(), $sMessage,"");
        } else {
            prepareResponse(array(), "/checklist/", "", false, array(), "Keine Berechtigung","");
        }

    }

    /**
     * [API-Schnittstelle]
     * Editiert eine bereits vorhandene Checkliste
     * @param $checklist_id
     */
    public function edit_checklist($checklist_id) {
        if($_SESSION['users']['rolle'] == 1)  {
            $mChecklist = $this->checklist->show_a_checklist($checklist_id);
            if(!empty($mChecklist)) {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    foreach($input_data as $key => $value) {
                        if($key != 'session') {
                            if ($key === 'elements') {
                                $mMultiple = strpos($value[0], ',');
                                if($mMultiple !== false) {
                                    $nValue =  explode(',', $value[0]);
                                    $checklist_data[$key] = $nValue;
                                } else {
                                    $checklist_data[$key] = $value;
                                }
                            } else {
                                $checklist_data[$key] = $value;
                            }
                        }
                    }

                    $checklist_data['checkliste_name_id'] = $checklist_id;
                    $this->checklist->edit_checklist($checklist_data);
                    $error = $this->db->error();
                    if(!empty($error['code'])) {
                        prepareResponse(array($checklist_data), "/checklist/show/".$checklist_id, "", false, array(), "Fehler beim bearbeiten der Checkliste","");
                    } else {
                        prepareResponse(array($checklist_data), "/checklist/show/".$checklist_id, "", true, array(), "Checkliste Erfolgreich bearbeitet.","");
                    }
                }
            } else {
                prepareResponse(array(), "/checklist/", "", false, array(), "Es gibt keine Daten zu dieser ID","");
            }
        } else {
            prepareResponse(array(), "/checklist/", "", false, array(), "Keine Berechtigung","");
        }
    }

    public function checklist_state($flight_id) {
        $input_data = $this->input->post();

        $flight_checklist_data = array();
        $flight_checklist_data['flug_id'] = $flight_id;
        foreach ($input_data as $checklist_data_key => $checklist_data) {
            $flight_checklist_data[$checklist_data_key] = $checklist_data;
        }
        //$this->unsetUncheckedValue($flight_checklist_data);
        $this->ch_we->add_checkliste_werte($flight_checklist_data);
        prepareResponse(array(), "flight/show/".$flight_id, "", true, array(), "","");
    }

    private function unsetUncheckedValue(&$data)
    {
        foreach ($data['elements'] as $element_key => &$element_value) {
            if(!isset($element_value['wert'])) {
                unset($element_value['kommentar']);
            }
        }
        $data['elements'] = array_filter($data['elements']);
    }

}