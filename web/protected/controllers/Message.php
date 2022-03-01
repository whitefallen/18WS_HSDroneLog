<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class Message
 * Verwaltet alle Requests und Responses für Messages
 */
class Message extends CI_Controller {

    /**
     * Message constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('Nachrichten_model', 'message');
        $this->load->model('Email_model', 'email_model');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","");
        }
    }

    /**
     * [API-Schnittstelle]
     * Message Aktion für Message/
     */
    public function index() {
        $message_data = $this->show_all();
        $email_setting = $this->email_model->show_email_settings();
        if(empty($email_setting)) {
            $email_setting[0] = new stdClass();
            $email_setting[0]->port ="";
            $email_setting[0]->passwort = "";
            $email_setting[0]->smtp_server = "";
            $email_setting[0]->email_adresse ="";
        }
        $view_data = array('message_data' => $message_data, 'email_data' => $email_setting);
        prepareResponse($message_data, "", "dashboard/settings.php", true, $view_data, "","");
    }

    /**
     * Gibt die Info zu allen Message aus
     */
    private function show_all() {
        return $this->message->show_all_nachrichten();
    }

    /**
     * [API-Schnittstelle]
     * Gibt die Infos zu einer message aus
     * @param string $message_key
     */
    public function show(string $message_key) {
        $message_data = $this->message->show_a_nachricht($message_key);
        if(empty($message_data)) {
            if(!is_numeric($message_key)) {
                $message = "Keine Nachricht für den Identifier '$message_key' gefunden";
            } else {
                $message = "Es wird einen Identifier-String erwartet z.b erfolgreicher_login";
            }
            prepareResponse($message_data, "/message/", "", false, array(), "$message","");
        } else {
            $view_data = array('message_data' => $message_data, 'current_id' => $message_key);
            prepareResponse($message_data, "", "dashboard/settingsdetails.php", true, $view_data, "","");
        }

    }

    /**
     * [API-Schnittstelle]
     * Editiert eine bereits vorhandene Message
     * @param $message_key
     */
    public function edit_message($message_key) {
        if(!empty($message_key)) {
            $input_data = $this->input->post();
            if(!empty($input_data)) {
                foreach($input_data as $key => $value) {
                    if($key != 'session') {
                        $message_data[$key] = $value;
                    }
                }
                $message_data['bezeichnung'] = $message_key;
                $this->message->edit_nachricht($message_data);
                prepareResponse(array($message_data), '/settings/show/'.$message_key.'', "", true, array(), "","");
            }
        } else {
            prepareResponse(array(), "/message/", "", false, array(), "","");
        }

    }


}