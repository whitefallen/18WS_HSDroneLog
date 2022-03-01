<?php
/**
 * User: jonas
 * Date: 25.10.2018
 * Time: 14:37
 * Class Fluege_model
 * Handles all the work with the database
 */

class Fluege_model extends CI_Model
{
    /**
     * @var int $flug_id sollte eine Integer Indifikations Zahl enthalten die eine flug_id beschreibt
     */
    private $flug_id;

    /**
     * @var int $pilot_id sollte eine Integer Identifikations Zahl enthalten die ein Pilot beschreibt
     */
    private $pilot_id;

    /**
     * @var int $drohne_id sollte eine Integer Identifikations Zahl enthalten die eine Drohne beschreibt
     */
    private $drohne_id;

    /**
     * @var int $checkliste_id sollte eine Integer Indifikations Zahl enthalten
     */
    private $checkliste_name_id;

    /**
     * @var int $flugdatum beeinhaltet die maximale Flugzeit der Drohne
     */
    private $flugdatum;

    /**
     * @var string $einsatzort_name beeinhaltet den Namen des Einsatzort
     */
    private $einsatzort_name;

    /**
     * @var time $einsatzbeginn beschreibt den Zeitpunkt des Einsatzes
     */
    private $einsatzbeginn;

    /**
     * @var time $einsatzende beschreibt den Zeitpunkt des einsatzendes
     */
    private $einsatzende;

    /**
     * @var time $flugdauer Dauer des Fluges
     */
    private $flugdauer;

    /**
     * @var int $start_und_landungen beschreibt die anzahl der Starte und Landungen
     */
    private $start_und_landungen;

    /**
     * @var string $besondere_vorkommnisse Ist etwas besonderes passiert?
     */
    private $besondere_vorkommnisse;

    /**
     * @var $flug_protokoll protokoll des Fluges als JSON
     */
    private $flug_protokoll;

    /**
     * @var $flugbezeichnung bezeichnung eines Fluges
     */
    private $flugbezeichnung;

    /**
     * @var $laengengrad laengengrad des einsatzortes
     */
    private $laengengrad;

    /**
     * @var $breitengrad breitengrad des einsatzortes
     */
    private $breitengrad;

    /**
     * @param array $flug_data
     * @return
     */
    public function add_flight(array $flug_data){
        $flug_data['flug_id'] = (calculate_next_id('flug_id','Fluege'));
        $this->db->insert('Fluege', $flug_data);
        return $flug_data['flug_id'];
    }

    public function show_a_flight(int $flug_data){
        $data = array();

        $this->db->select('Fluege.flug_id,Fluege.flugdatum,Pilot.vorname,Pilot.nachname,Drohne.drohnen_modell,Fluege.einsatzort_name,
                          Pilot.pilot_id,Drohne.drohne_id,Fluege.einsatzbeginn,Fluege.einsatzende,Checkliste_name.checkliste_name_id,
                          Checkliste_name.bezeichnung,Fluege.flugdauer,Fluege.start_und_landungen,Fluege.besondere_vorkommnisse,
                          Fluege.flugbezeichnung,Drohne.bild,Fluege.laengengrad,Fluege.breitengrad,Pilot.email_adresse,Pilot.studiengang,Pilot.profilbild');
        $this->db->from('Fluege');
        $this->db->join('Pilot', 'Pilot.pilot_id = Fluege.pilot_id');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id','left');
        $this->db->join('Checkliste_name','Checkliste_name.checkliste_name_id = Fluege.checkliste_name_id','left');
        $this->db->where('flug_id', $flug_data);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }
        
        return $data;
    }

    public function show_all_flights(int $offset = 0){

        $data = array();
        $this->db->limit(20,$offset);
        $this->db->select('Fluege.flug_id,Fluege.flugdatum,Fluege.einsatzort_name,Drohne.drohnen_modell,Pilot.vorname,Pilot.nachname,
                           Fluege.einsatzbeginn,Fluege.einsatzende,Fluege.flugbezeichnung,Checkliste_name.bezeichnung,Fluege.breitengrad,
                           Fluege.laengengrad,Pilot.email_adresse,Pilot.studiengang');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id','left');
        $this->db->join('Pilot','Pilot.pilot_id = Fluege.pilot_id','left');
        $this->db->join('Checkliste_name','Checkliste_name.checkliste_name_id = Fluege.checkliste_name_id','left');


        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }

        $this->db->order_by('flugdatum','desc');
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_flight(int $fluege_data){
        $this->db->delete('Fluege', array(
            'flug_id' => $fluege_data
        ));
    }

    public function edit_flight(array $flug_data){
        $this->db->where('flug_id',$flug_data['flug_id']);
        $this->db->update('Fluege',$flug_data);
    }

    public function show_most_used_drones(){
        $data = array();
        $this->db->limit(5);
        $this->db->select('Fluege.drohne_id,Drohne.drohnen_modell,count(Fluege.drohne_id) as total');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id');
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }
        $this->db->group_by('Fluege.drohne_id');
        $this->db->order_by('total','desc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_longest_flight(){
        $data = array();
        $this->db->limit(5);
        $this->db->select('Fluege.flugdauer,Pilot.vorname,Pilot.nachname,Pilot.profilbild,Pilot.pilot_id,sum(Fluege.flugdauer) as sum,
                           count(flug_id) as count');
        $this->db->from('Fluege');
        $this->db->join('Pilot', 'Pilot.pilot_id = Fluege.pilot_id');
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }
        $this->db->group_by('Fluege.pilot_id');
        $this->db->order_by('sum','desc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }



    public function show_flights_before_today(){
        $data = array();
        $this->db->limit(5);
        $date = date('Y-m-d', time());
        $this->db->select('Fluege.flugdatum,Fluege.einsatzort_name,Drohne.drohnen_modell,Fluege.laengengrad,Fluege.breitengrad,
                           Fluege.flugbezeichnung,Fluege.einsatzbeginn,Fluege.einsatzende,Fluege.flug_id');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id');
        $this->db->where('flugdatum <', $date);
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }
        $this->db->order_by('flugdatum','desc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_flights_for_today(){
        $data = array();
        $this->db->limit(5);
        $date = date('Y-m-d', time());
        $this->db->select('Fluege.flugdatum,Fluege.einsatzort_name,Drohne.drohnen_modell,Fluege.einsatzbeginn,Fluege.einsatzende,Fluege.flug_id');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id');
        $this->db->where('flugdatum like', $date);
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }
        $this->db->order_by('flugdatum','desc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_flights_after_today(){
        $data = array();
        $this->db->limit(5);
        $date = date('Y-m-d', time());
        $this->db->select('Fluege.flugdatum,Fluege.einsatzort_name,Drohne.drohnen_modell,Fluege.flug_id');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id');
        $this->db->where('flugdatum >', $date);
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }
        $this->db->order_by('flugdatum','asc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_available_drones(string $date,string $starttime, string $endtime){
        $data = array();
        $this->db->select('Drohne.drohne_id');
        $this->db->from('Fluege');
        $this->db->join('Drohne', 'Drohne.drohne_id = Fluege.drohne_id');
        $this->db->where('Fluege.flugdatum',$date);
        $this->db->where('Fluege.einsatzbeginn <=',$starttime);
        $this->db->where('Fluege.einsatzende >=',$endtime);
        $this->db->group_by('Drohne.drohne_id');
        $subquery = $this->db->get_compiled_select();

        $this->db->select('drohne_id,drohnen_modell');
        $this->db->from('Drohne');
        $this->db->where("`drohne_id` NOT IN ($subquery)", NULL, FALSE);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_flights_id(){
        return show_all_id('Fluege','flug_id');
    }
    
    public function pilot_of_the_week(){
        $monday = date( 'Y-m-d', strtotime( 'monday this week' ) );
        $sunday = date( 'Y-m-d', strtotime( 'sunday this week' ) );
        $data = array();
        $this->db->limit(1);
        $this->db->select('Fluege.flugdauer,Pilot.vorname,Pilot.nachname,Pilot.profilbild,Pilot.pilot_id,sum(Fluege.flugdauer) as sum,count(Fluege.flug_id) as count');
        $this->db->from('Fluege');
        $this->db->join('Pilot', 'Pilot.pilot_id = Fluege.pilot_id');
        $this->db->where('Fluege.flugdatum >=', $monday);
        $this->db->where('Fluege.flugdatum <=', $sunday);
        $this->db->group_by('Fluege.pilot_id');
        $this->db->order_by('sum','desc');

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_flights_without_drones(){
        $data = array();
        $this->db->select('Fluege.flugbezeichnung,Fluege.flug_id');
        $this->db->from('Fluege');
        $this->db->where('Fluege.drohne_id is NULL');
        if(!$_SESSION['users']['rolle'] == 1){
            $this->db->where('Fluege.pilot_id', $_SESSION['users']['pilot_id']);
        }

        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}