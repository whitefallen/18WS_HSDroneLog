<?php
class Checkliste_name_model extends CI_Model
{
    /**
     * @var int $checkliste_name_id
     */
     private $checkliste_name_id;

    /**
     * @var string $bezeichner beeinhaltet den namen der checkliste
     */
    private $bezeichnung;

    /**
     * @var string $erklaerung beeinhaltet eine erklärung der Checkliste
     */
    private $erklaerung;

    public function __construct(){
        parent::__construct();
        $this->load->model('Checkliste_zuordnen_model', 'checkliste_zuordnen');
    }

    /**
     * @param array $checklist_data
     * @return
     */
    public function add_checklist(array $checklist_data){

        $checklist_data['checkliste_name_id'] = (calculate_next_id('checkliste_name_id', 'Checkliste_name'));
        if(isset($checklist_data['elements'])) {
            $elements = $checklist_data['elements'];
            unset($checklist_data['elements']);
        }
        $this->db->insert('Checkliste_name', $checklist_data);

        if(!empty($elements)) {
            $this->checkliste_zuordnen->add_elements_to_checklist($checklist_data['checkliste_name_id'], $elements);
        } else{
            return false;
        }
    }

    /**
     * @return array
     */
    public function show_all_checklists(int $offset = 0){
        $data = array();
        $this->db->order_by('checkliste_name_id','ASC');
        if($offset >= 0) {
            $this->db->limit(20,$offset);
        }
        $query = $this->db->get('Checkliste_name');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_checklist(int $checklist_data){
        $data = array();
        $query = $this->db->get_where('Checkliste_name', array(
            'checkliste_name_id' => $checklist_data
        ));

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_checklist(int $checklist_data){
        $this->db->delete('Checkliste_name', array(
            'checkliste_name_id' => $checklist_data
        ));
    }

    public function edit_checklist(array $checklist_data){
        if(isset($checklist_data['elements'])) {
            $elements = $checklist_data['elements'];
            unset($checklist_data['elements']);
            $this->checkliste_zuordnen->delete_checklist($checklist_data['checkliste_name_id']);
            $this->checkliste_zuordnen->add_elements_to_checklist($checklist_data['checkliste_name_id'], $elements);
        }
        $this->db->where('checkliste_name_id', $checklist_data['checkliste_name_id']);
        $this->db->update('Checkliste_name', $checklist_data);
    }

    public function show_checkliste_name_id(){
        return show_all_id('Checkliste_name','checkliste_name_id');
    }

    public function show_related_elements(int $checkliste_name_id){
        $data = array();
        $this->db->select('Checklisten_elemente.bezeichnung');
        $this->db->from('Checkliste_name');
        $this->db->join('Checkliste_zuordnen', 'Checkliste_zuordnen.cehckliste_name_id = Checkliste_name.checkliste_name_id');
        $this->db->join('Checklisten_elemente', 'Checklisten_elemente.element_id = Checkliste_zuordnen.element_id');
        $this->db->where('Akkus_zuordnen.drohne_id',$checkliste_name_id);

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}