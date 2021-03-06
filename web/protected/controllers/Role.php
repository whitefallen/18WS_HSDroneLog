<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Role
 * Verwaltet alle Requests und Responses für Roles
 */
class Role extends CI_Controller {

    /**
     * Role constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Rollen_model', 'role');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse("", "/", "", false, "", "Keine Session vorhanden","");
        }
    }

    /**
     * Message Aktion für Role/
     */
    public function index() {
        $this->load->view('dashboard/role.php', array('role_data' => $this->role->show_all_roles()));
    }

    /**
     * Gibt die Infos zu einer Rolle aus
     * @param int $role_id
     */
    public function show(int $role_id) {
        $role_data = $this->role->show_a_role($role_id);
        $this->load->view('dashboard/role.php', array('role_data' => $role_data));
    }

    /**
     * Fügt eine neue Rolle hinzu
     */
    public function add_role() {

        $input_data = $this->input->post();

        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                $role_data[$key] = $value;
            }
            $this->role->add_role($role_data);
        }
        $this->load->view('dashboard/role.php');
    }

    /**
     * Löscht eine bestehende Rolle
     * @param $role_id
     */
    public function delete_role($role_id) {
        $this->role->delete_a_role($role_id);
        redirect('/role/', 'location');
    }

    /**
     * Editiert eine bereits vorhandene Rolle
     * @param $role_id
     */
    public function edit_role($role_id) {
        if($role_id != 0) {
            $input_data = $this->input->post();
            if(!empty($input_data)) {
                foreach($input_data as $key => $value) {
                    $role_data[$key] = $value;
                }
                $role_data['role_id'] = $role_id;
                $this->role->edit_role($role_data);
            }
        }
    }


}