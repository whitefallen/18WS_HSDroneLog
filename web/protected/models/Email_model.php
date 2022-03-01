<?php
/**
 * Created by PhpStorm.
 * User: jonas
 * Date: 28.03.2019
 * Time: 10:38
 */

class Email_model  extends CI_Model
{

    /**
     * @var int pilot_id
     */
    private $pilot_id;
    /**
     * @var String E-mail Addresse
     */
    private $email_adresse;

    /**
     * @var String smtp_server
     */
    private $smtp_server;

    /**
     * @var String passwort
     */
    private $passwort;

    /**
     * @var int port
     */
    private $port;

    /**
     * @param array $email_data
     */
    public function add_email_settings(array $email_data){
        $this->db->truncate('Email');
        $this->db->insert('Email', $email_data);
    }

    public function show_email_settings(){
        $data = array();

        $this->db->select('*');
        $this->db->from('Email');
        $query = $this->db->get();

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }


}