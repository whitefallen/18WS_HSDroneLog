<?php
/**
 * Created by PhpStorm.
 * User: jonas
 * Date: 28.12.2018
 * Time: 13:22
 */

class Akkus_zuordnen_model extends CI_Model
{
    /**
     * @var int enthält eine Identifikationsnummer für Akku
     */
    private $akku_id;

    /**
     * @var int enthält eine Identifikationsnummer für Drohne
     */
    private $drohne_id;

    public function assign_akku_to_drone(int $akku_id,array $drohne_list){

        foreach ($drohne_list as $drohne_id){
            $this->db->insert('Akkus_zuordnen',array(
                'akku_id' => $akku_id,
                'drohne_id' => $drohne_id
            ));
        }
    }

    public function delete(int $akku_id){
        $this->db->where('Akkus_zuordnen.akku_id',$akku_id);
        $this->db->delete('Akkus_zuordnen',array('akku_id' => $akku_id));
    }

    public function show_all_drones(int $akku_id){
        $data = array();
        $this->db->select('Drohne.drohnen_modell,Drohne.drohne_id');
        $this->db->from('Akkus_zuordnen');
        $this->db->join('Drohne', 'Drohne.drohne_id = Akkus_zuordnen.drohne_id');
        $this->db->where('Akkus_zuordnen.akku_id',$akku_id);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}