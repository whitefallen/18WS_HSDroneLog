<?php

/**
 * Class Drone_model
 * Handles all the work with the Database
 */
class Drone_model extends CI_Model {

    /**
     * @var int $drohne_id sollte eine Integer Indifikations Zahl enthalten
     */
    private $drohne_id;

    /**
     * @var int $akku_id sollte eine Integer Identifikations Zahl enthalten
     */
    private $akku_id;

    /**
     * @var string $drohnen_modell sollte ein Drohnen Modell zu einer Drohne enthalten
     */
    private $drohnen_modell;

    /**
     * @var int $fluggewicht_in_gramm sollte das Gewicht einer Drohne enthalten
     */
    private $fluggewicht_in_gramm;

    /**
     * @var int $flugzeit_in_min beeinhaltet die maximale Flugzeit der Drohne
     */
    private $flugzeit_in_min;

    /**
     * @var int $anzahl_akkus beeinhaltet die anzahl der Akkus im Bestand
     */
    private $diagonale_groesse_in_mm;

    /**
     * @var int $maximale_flueghoehe_in_m maximale Flughoehe der Drohne ueber den Meeresspiegel
     */
    private $maximale_flughoehe_in_m;

    /**
     * @var int $hoechstgeschwindigkeit_in_km/h hoechstgeschwindigkeit der Drohne
     */
    private $hoechstgeschwindigkeit_in_kmh;

    /**
     * @var string $bild Bild der Drohne
     */
    private $bild;

    /**
     * @param array $drone_data
     */
    public function add_drone(array $drone_data){
        $drone_data['drohne_id'] = (calculate_next_id('drohne_id','Drohne'));
        $this->db->insert('Drohne', $drone_data);
    }

    /**
     * @return array
     */
    public function show_all_drones(int $offset=0){
        $data = array();
        $this->db->limit(20,$offset);
        $this->db->select('Drohne.drohne_id,Drohne.bild,Drohne.drohnen_modell,Drohne.flugzeit_in_min');
        $this->db->from('Drohne');
        $query = $this->db->order_by('drohne_id','ASC');
        $query = $this->db->get();


        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_drone(int $drone_data,bool $admin = true,int $offset=0){
        $data = array();
        $this->db->limit(20,$offset);
        $this->db->select('Drohne.drohne_id,Drohne.fluggewicht_in_gramm,Drohne.diagonale_groesse_in_mm,
                           Drohne.flugzeit_in_min,Drohne.maximale_flughoehe_in_m,Drohne.hoechstgeschwindigkeit_in_kmh,Drohne.drohnen_modell,Drohne.bild');
        $this->db->from('Drohne');
        $this->db->where('drohne_id', $drone_data);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_drone(int $drone_data){
        $this->db->delete('Drohne', array(
            'drohne_id' => $drone_data
        ));
    }

    public function delete_all_drones(){
        $this->db->empty('Drohne');
    }

    public function edit_drone(array $drone_data){
        $this->db->where('drohne_id',$drone_data['drohne_id']);
        $this->db->update('Drohne',$drone_data);
    }

    public function show_drones_id(){
        return show_all_id('Drohne','drohne_id');
    }

    public function show_related_akkus(int $drohne_id){
        $data = array();
        $this->db->select('Akku.bezeichnung');
        $this->db->from('Drohne');
        $this->db->join('Akkus_zuordnen', 'Akkus_zuordnen.drohne_id = Drohne.drohne_id');
        $this->db->join('Akku', 'Akku.akku_id = Akkus_zuordnen.akku_id');
        $this->db->where('Akkus_zuordnen.drohne_id',$drohne_id);

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}