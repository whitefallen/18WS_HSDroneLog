<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Hash
 * Verwaltet alle Requests und Responses für das Hash-Tool
 */
class Hash extends CI_Controller {

    /**
     * Hash constructor.
     */
    public function __construct() {
        parent::__construct();
    }

    /**
     * [API-Schnittstelle]
     * Hash Aktion für Hash/
     */
    public function index()
    {
        $passwort = $this->input->post('passwort');
        $passwort = password_hash($passwort, PASSWORD_DEFAULT);
        if(isset($passwort)) {
            echo $passwort;
        }
        $this->load->view('hash_it/hash');
    }


}