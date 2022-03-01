<?php

use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Class User
 * Verwaltet alle Requests und Responses für User
 */
class User extends CI_Controller {

    /**
     * User constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('User_model', 'user_model');
        $this->load->model('Email_model', 'email_model');
        getSession();
        if(!isset($_SESSION['users']['pilot_id'])) {
            prepareResponse(array(), "/", "", false, array(), "Keine Session vorhanden","","401");
        }
    }

    /**
     * [API-Schnittstelle]
     * User Aktion für user/
    */
    public function index()
    {
        if(isset($_SESSION['users']['pilot_id'])) {
            if($_SESSION['users']['rolle'] == 1) {
                $user_data = $this->user_model->show_all_user();
                $view_data = array('user_data' => $user_data);
                prepareResponse($user_data, "", "dashboard/piloten.php", true, $view_data, "","");
            } else {
                prepareResponse(array(), "user/show/".$_SESSION['users']['pilot_id'], "", true, array(), "Keine Befugniss","");
            }

        } else {
            prepareResponse(array(), "/", "", true, array(), "Keine Session vorhanden","");
        }
    }
    /**
     * [API-Schnittstelle]
     * Anzeigen eines users
     * @param $user_id
     */
    public function show($user_id) {
        $session_user_id = $_SESSION['users']['pilot_id'];
        $view_data = array();
        $success = false;
        // Ist der Nutzer kein Admin darf er nur sein Profil sehen
        if($session_user_id != $user_id && $_SESSION['users']['rolle'] != 1) {
            $user_id = $session_user_id;
        }
        $current_user_data = $this->user_model->show_a_user($user_id);
        if(!empty($current_user_data)) {
            $view_data = array('user_data' => $current_user_data, 'current_id' => $user_id);
            $success = true;
        }
        prepareResponse($current_user_data, "", "dashboard/user.php", $success, $view_data,  "", "");
    }

    /**
     * [API-Schnittstelle]
     * Editieren eines Users basierend einer ID
     * @param $pilot_id
     */
    public function edit_user($pilot_id) {
        $data = array();
        $sMessage ="";
        if($pilot_id != 0) {
            if($_SESSION['users']['pilot_id'] != $pilot_id && $_SESSION['users']['rolle'] != 1) {
                prepareResponse(array(), "user/index", "", false, array(),  "Keine Befugniss", "");
            } else {
                $input_data = $this->input->post();
                if(!empty($input_data)) {
                    if($_SESSION['users']['rolle'] != 1 || $_SESSION['users']['pilot_id'] == $pilot_id) {
                        unset($input_data['rolle']);
                    }
                    if($_SESSION['users']['rolle'] != 1 && $_SESSION['users']['pilot_id'] != $pilot_id)  {
                        unset($input_data['passwort']);
                    }
                    if($_SESSION['users']['pilot_id'] == $pilot_id) {
                        unset($input_data['aktivitaet']);
                    }
                    foreach($input_data as $key => $value) {
                        if(isMobileApp()) {
                            if($key == 'profilbild') {
                                break;
                            }
                            if($key == 'filename') {
                                $data[$key] = $value;
                            }
                        }
                        if($key != 'session' && $key != 'filename') {
                            $data[$key] = $value;
                        }
                        if($key == 'passwort' && empty($value)) {
                            unset($input_data['passwort']);
                            unset($data['passwort']);
                        }
                    }
                    
                    if(isMobileApp() && isset($data['filename'])) {
                        $data['profilbild'] = upload_image_from_mobile($data, "profilbild=");
                        unset($data['filename']);
                    } else if(!isMobileApp() && !isset($data['filename'])) {
                        $upload_config['upload_path']          = $_ENV['UPLOAD_PATH'];
                        $upload_config['allowed_types']        = $_ENV['IMG_TYPE'];
                        $upload_config['max_size']          = $_ENV['UPLOAD_SIZE'];
                        $upload_config['max_width']            = $_ENV['IMG_WIDTH'];
                        $upload_config['max_height']           = $_ENV['IMG_HEIGHT'];
                        $this->load->library('upload', $upload_config);

                        $field_name = "profilbild";
                        $this->upload->do_upload($field_name);
                        $upload_data = $this->upload->data();

                        if(!empty($upload_data['file_name']) && empty($this->upload->display_errors())) {
                            $data['profilbild'] = '/uploads/'.$upload_data['file_name'];
                        }
                        if($this->upload->display_errors()) {
                            $sMessage = $this->upload->display_errors();
                        }
                    }

                    $data['pilot_id'] = $pilot_id;
                    $current_user_data = $this->user_model->show_a_user($pilot_id);
                    $this->user_model->edit_user($data);
                    if(isset($input_data['aktiviteat'])) {
                        $this->notifyUserState($data, $current_user_data);
                    }
                    prepareResponse($data, "user/show/".$pilot_id, "", true, array(),  $sMessage, "");
                }
            }
        } else {
            prepareResponse(array(), "user/index", "", false, array(),  "", "");
        }
    }
    /**
     * [API-Schnittstelle]
     * User Aktion für user/register
     */
    public function add_user() {
        $input_data = $this->input->post();
        $bSuccess = false;
        $aData = array();
        $upload_message ="";
        if(!empty($input_data)) {
            foreach($input_data as $key => $value) {
                if(isMobileApp()) {
                    if($key == 'profilbild') {
                        break;
                    }
                    if($key == 'filename') {
                        $aData[$key] = $value;
                    }
                }
                if($key != 'session' && $key != 'filename') {
                    $aData[$key] = $value;
                }
            }
            if(isMobileApp() && isset($aData['filename'])) {
                $aData['profilbild'] = upload_image_from_mobile($aData, "profilbild=");
                unset($aData['filename']);
            } else if(!isMobileApp() && !isset($data['filename'])) {
                    $upload_config['upload_path']          = $_ENV['UPLOAD_PATH'];
                    $upload_config['allowed_types']        = $_ENV['IMG_TYPE'];
                    $upload_config['max_size']             = $_ENV['UPLOAD_SIZE'];
                    $upload_config['max_width']            = $_ENV['IMG_WIDTH'];
                    $upload_config['max_height']           = $_ENV['IMG_HEIGHT'];
                    $this->load->library('upload', $upload_config);
                    $field_name = "profilbild";
                    $this->upload->do_upload($field_name);
                    $upload_data = $this->upload->data();

                    if(!empty($upload_data['file_name']) && empty($this->upload->display_errors())) {
                        $aData['profilbild'] = '/uploads/'.$upload_data['file_name'];
                    } else {
                        $aData['profilbild'] = '/assets/images/muster_pb.png';
                    }
                    if($this->upload->display_errors()) {
                        $upload_message = $this->upload->display_errors();
                    }
            } else {
                $aData['profilbild'] = '/assets/images/muster_pb.png';
            }

            $bUserExist = $this->user_model->check_if_user_exist('email_adresse', $aData['email_adresse']);
            if($bUserExist != FALSE) {
                $message = "Benutzer existiert bereits";
                prepareResponse(array(), "user/add_user", "", $bSuccess, array(), $message, "");
            } else {
                $bSuccess = $this->create_user($aData);
                if($bSuccess) {
                    $message = "Registrieren war erfolgreich";
                    if($upload_message) {
                        $message = $upload_message;
                    }
                    prepareResponse(array($aData), "/user/", "", $bSuccess, array(),$message, "");
                } else {
                    $message = "Registrieren war nicht erfolgreich";
                    if($upload_message) {
                        $message = $upload_message;
                    }
                    prepareResponse(array(), "user/add_user", "", $bSuccess, array(), $message, "");
                }
            }
        } else {
            prepareResponse(array(), "", "dashboard/adduser.php", true, array(), "", "");
        }
    }

    /**
     * User Aktion für user/create_user
     * Erstellt ein neuen neutzer
     * @param array $aData
     * @return mixed
     */
    private function create_user(array $aData) {
        return $this->user_model->create_user_from_form($aData);
    }

    /**
     * [API-Schnittstelle]
     * User Aktion für user/logout
     * Loggt ein Benutzter aus dem System aus
     */
    public function logout() {
        $this->user_model->logout_user();
        prepareResponse(array(), "index/login", "", true, array(),  "Erfolgreich ausgeloggt", "");
    }

    /**
     * [API-Schnittstelle]
     * Ein nicht-admin Pilot muss von einem Admin akzeptiert werden
     * @param $user_id
     */
    public function accept_user($user_id) {
        $this->user_model->accept_user($user_id);
    }

    /**
     * [API-Schnittstelle]
     * Löscht einen Piloten aus der Datenbank
     * @param $user_id
     */
    public function delete_user($user_id) {
        $bSuccess = false;
        $sMessage = "Es gibt keinen Benutzer mit der ID:$user_id.";
        $result = $this->user_model->get_user_data($user_id);
        if(!empty($result)) {
            $query = $this->user_model->delete_user($user_id);
            if($query) {
                $bSuccess = true;
                $sMessage = "User mit der ID:$user_id gelöscht";
            } else {
                $bSuccess = false;
                $sMessage = "User konnte nicht gelöscht werden";
            }
        }
        prepareResponse(array(), "user/index", "", $bSuccess, array(),  $sMessage, "");
    }

    private function notifyUserState(array $edit_data, array $old_data) {
        if($old_data[0]->aktivitaet != $edit_data['aktivitaet']) {
            $this->sendNotificationEmail($edit_data);
        }
    }

    public function sendNotificationEmail(array $edit_data) {
        $email_setting = $this->email_model->show_email_settings();
        if(!empty($email_setting)) {
            if(
                !empty($email_setting[0]->smtp_server) &&
                !empty($email_setting[0]->port) &&
                !empty($email_setting[0]->email_adresse) &&
                !empty($email_setting[0]->passwort)
            ) {
                $mail = new PHPMailer();
                $mail->IsSMTP();
                $mail->Host = $email_setting[0]->smtp_server;
                $mail->Port = $email_setting[0]->port;
                $mail->SMTPAuth = true;
                $mail->Username = $email_setting[0]->email_adresse;
                $mail->Password = $email_setting[0]->passwort;
                $mail->SMTPSecure = 'tls';
                $mail->CharSet = 'UTF-8';
                $mail->Encoding = 'base64';
                try {
                    $mail->setFrom($email_setting[0]->email_adresse, 'DroneLog Support');
                } catch (Exception $e) {
                    echo $e->getMessage();
                }
                $mail->addAddress($edit_data['email_adresse']);
                $mail->isHTML(true);
                if ($edit_data['aktivitaet'] == 1) {
                    $mail->Body = "Ihr Konto wurde Aktiviert";
                    $mail->Subject = "Dronelog-Konto Aktivierung";
                } else {
                    $mail->Body = "Ihr Konto wurde Deaktiviert";
                    $mail->Subject = "Dronelog-Konto Deaktivierung";
                }
                if (!$mail->send()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }


}
