<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Email extends CI_Controller {

    /**
     * Dashboard constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('User_model', 'user_model');
        $this->load->model('Nachrichten_model', 'messages');
        $this->load->model('Email_model', 'email_model');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    public function index() {
        $email_setting = $this->email_model->show_email_settings();
        $view_data = array('email_data' => $email_setting);
        prepareResponse($email_setting, "dashboard/settings", "", true, $view_data, "","");
    }

    public function add_email() {
        $missingProperty = false;
        $aData = $this->input->post();
        if(!empty($aData)) {
            foreach ($aData as $key=>$data) {
                if(empty($data)) {
                    $missingProperty = true;
                }
            }
        }
        if($missingProperty) {
            prepareResponse($aData, "dashboard/settings","", false, array(), "","");
        } else {
            $aData['pilot_id'] = 1;
            if($aData['session']) {
                unset($aData['session']);
            }
            $this->email_model->add_email_settings($aData);
            prepareResponse($aData, "dashboard/settings", "", true, array(), "","");
        }
    }
}