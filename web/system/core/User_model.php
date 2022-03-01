<?php

/**
 * Class user_model
 * Handles all the work with the Database
 */
class user_model extends CI_Model {

    /**
     * @var string $username should contain a username
     */
    private $username;

    /**
     * @var string $password should contain a password
     */
    private $password;

    /**
     * @return bool creates a user from the form
     */
    public function create_user_from_form() {
        if(
            !empty($this->input->post('username')) &&
            !empty($this->input->post('password'))
        ) {
            $success = $this->create_user();
            return $success;
        } else {
            return false;
        }
    }

    /**
     * @return bool logs a user into the backend
     */
    public function log_into_existing_user() {
        if(
            !empty($this->input->post('username')) &&
            !empty($this->input->post('password'))
        ) {
            $success = $this->login_user();
            return $success;
        } else {
            return false;
        }
    }

    /**
     * removes the session on logout
     */
    public function logout_user() {
        $this->session->sess_destroy();
    }

    /**
     * @return bool inserts a new user
     */
    private function create_user() {
        
        $this->set_userdata_from_input();

        if(!$this->check_if_username_exist()) {
            if(!empty($this->username) && !empty($this->password)) {
                $this->db->insert('users', array(
                    'username' => $this->username,
                    'password' => $this->password
                ));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @return bool writes the user in the session if exists
     */
    private function login_user() {

        $this->set_userdata_from_input();

        $query = $this->check_if_user_exist();
        if($query != false) {
            foreach($query->result() as $user_data) {
                $this->session->users = array(
                    'id' => $user_data->id,
                    'username' => $user_data->username,
                    'logged_in' => true
                );
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return bool Checks if the username exist in the Database
     */
    private function check_if_username_exist() {

        $query = $this->db->get_where('users', array(
            'username' => $this->username
        ));

        if ($query->num_rows() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return mixed if user exist return query else false
     */
    private function check_if_user_exist() {

        $query = $this->db->get_where('users', array(
            'username' => $this->username,
            'password' => $this->password
        ));

        if ($query->num_rows() > 0) {
            return $query;
        } else {
            return false;
        }
    }

    /**
     * sets the form data into the model
     */
    private function set_userdata_from_input() {
        $this->username = $this->input->post('username');
        $this->password = $this->input->post('password');
    }
}