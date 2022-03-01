<?php
/**
 * Created by PhpStorm.
 * User: jonas
 * Date: 25.03.2019
 * Time: 14:24
 */

class Fluglog_model extends CI_Model
{
    /**
     * @var int Identifikationsnummer für Flüge
     */
    private $flug_id;

    /**
     * @var String latitude
     */
    private $latitude;

    /**
     * @var String longitude
     */
    private $longitude;

    /**
     * @var String altitude
     */
    private $altitude;

    /**
     * @var String homeLatitude
     */
    private $homeLatitude;

    /**
     * @var String homeLongitude
     */
    private $homeLongitude;

    /**
     * @var Strimg battery_percent
     */
    private $battery_percent;

    /**
     * @var String app_warning
     */
    private $app_warning;

    /**
     * @param array $fluglog_data
     */
    public function add_fluglog(array $fluglog_data){
        $this->db->insert('Fluglog', $fluglog_data);
    }

    /**
     * @param int $flug_id
     * @return array
     */
    public function show_a_fluglog(int $flug_id,int $offset =0){
        $data = array();
        $this->db->limit(50,$offset);
        $this->db->select('*');
        $this->db->from('Fluglog');
        $this->db->where('flug_id', $flug_id);
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}