<?php
class Akku_model extends CI_Model
{
    /**
     * @var int $akku_id sollte eine Identifikationsnummer enthalten
     */
    private $akku_id;

    /**
     * @var string $bezeichnung sollte einen Namen für den Akku beeinhalten
     */
    private $bezeichnung;

    /**
     * @var int $anzahl sollte die Anzahl der Akkus beeinhalten
     */
    private $anzahl;

    public function __construct(){
        parent::__construct();
        $this->load->model('Akkus_zuordnen_model', 'akkus_zuordnen');
    }

    /**
     * @param array $akku_data
     * @return
     */
    public function add_akku(array $akku_data)
    {
        $drohnen = $akku_data['drohnen'];
        unset($akku_data['drohnen']);
        if (!empty($drohnen)) {
            $akku_data['akku_id'] = (calculate_next_id('akku_id', 'Akku'));
            $this->db->insert('Akku', $akku_data);
            $this->akkus_zuordnen->assign_akku_to_drone($akku_data['akku_id'], $drohnen);
        }else{
            return false;
        }
    }

    /**
     * @return array
     */
    public function show_all_akkus(int $offset=0){
        $data = array();
        $this->db->limit(20,$offset);
        $this->db->order_by('akku_id','ASC');
        $query = $this->db->get('Akku');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_akku(int $akku_id){
        $data = array();
        $this->db->select('Akku.bezeichnung,Akku.anzahl,Akku.akku_id');
        $this->db->from('Akku');
        $this->db->where('Akku.akku_id',$akku_id);

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_akku(int $akku_data){
        $this->db->delete('Akku', array(
            'akku_id' => $akku_data
        ));
        $this->akkus_zuordnen->delete($akku_data);
    }

    public function edit_akku(array $akku_data){
        $drohnen = $akku_data['drohnen'];
        unset($akku_data['drohnen']);
        $this->db->where('akku_id',$akku_data['akku_id']);
        $this->db->update('Akku',$akku_data);
        $this->akkus_zuordnen->delete($akku_data['akku_id']);
        $this->akkus_zuordnen->assign_akku_to_drone($akku_data['akku_id'],$drohnen);
    }

    public function show_akkus_id(){
        return show_all_id('Akku','akku_id');
    }
}