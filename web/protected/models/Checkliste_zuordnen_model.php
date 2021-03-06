<?php
/**
 * Created by PhpStorm.
 * User: jonas
 * Date: 14.11.2018
 * Time: 14:56
 */

class Checkliste_zuordnen_model extends CI_Model
{
    /**
     * @var int $checkliste_name_id beeinahltet eine Identifikationsnummer
     */
    private $checkliste_name_id;

    /**
     * @var int $element_id beeinhaltet eine Identifikationsnummer
     */
    private $element_id;

    public function add_elements_to_checklist(int $checklist_id,array $element_list){

        foreach ($element_list as $element_id){
            $this->db->insert('Checkliste_zuordnen',array(
                'checkliste_name_id' => $checklist_id,
                'element_id' => $element_id
            ));
        }
    }

    public function add_checklists_to_elements(int $element_id,array $checklist_list){
        foreach ($checklist_list as $checklist_id){
            $this->db->insert('Checkliste_zuordnen',array(
                'checkliste_name_id' => $checklist_id,
                'element_id' => $element_id
            ));
        }
    }

    public function show_all_elements(int $checkliste_id){
        $data = array();
        $this->db->select('Checklisten_elemente.bezeichnung,Checklisten_elemente.element_id');
        $this->db->from('Checkliste_zuordnen');
        $this->db->join('Checklisten_elemente', 'Checklisten_elemente.element_id = Checkliste_zuordnen.element_id');
        $this->db->where('Checkliste_zuordnen.checkliste_name_id',$checkliste_id);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_all_checklists(int $element_id){
        $data = array();
        $this->db->select('Checkliste_name.bezeichnung,Checkliste_name.checkliste_name_id');
        $this->db->from('Checkliste_zuordnen');
        $this->db->join('Checkliste_name', 'Checkliste_name.checkliste_name_id = Checkliste_zuordnen.checkliste_name_id');
        $this->db->where('Checkliste_zuordnen.element_id',$element_id);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_checklist(int $checkliste_name_id){
        $this->db->delete('Checkliste_zuordnen',array('checkliste_name_id' => $checkliste_name_id));
    }

    public function delete_element(int $element_id){
        $this->db->delete('Checkliste_zuordnen',array('element_id' => $element_id));
    }
}