<?php
class Checklisten_elemente_model extends CI_Model
{
    /**
     * @var int $element_id sollte eine Identifikationsnummer enthalten
     */
    private $element_id;

    /**
     * @var string $bezeichner sollte eine Bezeichnung des Elements enthalten
     */
    private $bezeichnung;

    /**
     * @var string $erklaerung sollte eine beschreibung des Elements enthalten
     */
    private $erklaerung;

    /**
     * @param array $element_data
     */
    public function add_element(array $element_data){
        $element_data['element_id'] = (calculate_next_id('element_id','Checklisten_elemente'));
        if(isset($element_data['checklists'])){
            $checklists = $element_data['checklists'];
            unset($element_data['checklists']);
        }

        $this->db->insert('Checklisten_elemente', $element_data);

        if(!empty($checklists)) {
            $this->checkliste_zuordnen->add_checklists_to_elements($element_data['element_id'], $checklists);
        }

    }

    /**
     * @return array
     */
    public function show_all_elements(int $offset = 0){
        $data = array();
        $this->db->order_by('element_id','ASC');
        if($offset >= 0) {
            $this->db->limit(20,$offset);
        }
        $query = $this->db->get('Checklisten_elemente');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_element(int $element_data){
        $data = array();
        $query = $this->db->get_where('Checklisten_elemente', array(
            'element_id' => $element_data
        ));

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_element(int $element_data){
        $this->db->delete('Checklisten_elemente', array(
            'element_id' => $element_data
        ));
    }

    /**
     * @param array $element_data
     */
    public function edit_element(array $element_data){
        if(isset($element_data['checklists'])){
            $checklists = $element_data['checklists'];
            unset($element_data['checklists']);
            $this->checkliste_zuordnen->delete_element($element_data['element_id']);
            $this->checkliste_zuordnen->add_checklists_to_elements($element_data['element_id'], $checklists);
        }
        $this->db->where('element_id', $element_data['element_id']);
        $this->db->update('Checklisten_elemente', $element_data);
    }

    public function show_elements_id(){
        return show_all_id('Checklisten_elemente','element_id');
    }
}