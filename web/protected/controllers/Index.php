<?php
defined('BASEPATH') OR exit('No direct script access allowed');
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

/**
 * Class Index
 * Verwaltet alle Requests und Responses für Index
 */
class Index extends CI_Controller {

    /**
     * Index constructor.
     */
    public function __construct() {
        parent::__construct();
        $this->load->model('User_model', 'user_model');
        $this->load->model('Drone_model', 'drone');
        $this->load->model('Fluege_model', 'flight');
        $this->load->model('Email_model', 'email_model');
    }

    /**
     * Index Aktion für Index/
    */
    public function index()
    {
        if ($this->db->table_exists('ci_sessions') )
        {
            if(isset($_SESSION['users']['pilot_id'])) {
                prepareResponse(array(), "dashboard", "", true, array(), "","");
            } else {
                prepareResponse(array(), "/index/login", "", true, array(), "Keine Session vorhanden","");
            }
        }
        else
        {
            header("Location:/setup/setup_env.php", true,'303');
        }
    }

    /**
     * [API-Schnittstelle]
     * Route für das Passwort vergessen
     */
    public function password_forgot() {
        prepareResponse(array(), "", "passwort.php", true, array(), "Funktion derzeit deaktiviert","");
    }

    /**
     * [API-Schnittstelle]
     * Route für das Reseten eines vergessenen Passwortes
     */
    public function password_reset() {
        $bSuccess = false;
        $aData = $this->input->post();
        $pass_data = array();
        $sMessage = "Passwort konnte nicht zurückgesetzt werden";
        if(!empty($aData)) {
            $newPasswort = $this->user_model->generateNewPassword();
            $bSuccess = $this->sendEmail($aData['mail'], 'Passwort Zuruecksetzen', array('passwort' => $newPasswort, 'email_adresse' => $aData['mail']), "reset");
            if($bSuccess) {
                $this->user_model->setNewGeneratedPassword($aData['mail'], $newPasswort);
                $pass_data['passwort'] = $newPasswort;
                $sMessage = "Passwort wurde zurückgesetzt";
            }
        }
        prepareResponse(array($pass_data), "", "passwort_erfolgreich.php", $bSuccess, array(), $sMessage,"");
    }

    /**
     * [API-Schnittstelle]
     * Route für das Registrieren eines neuen Users
     */
    public function register() {
        if(!($this->agent->is_browser)) {
            $this->register_user();
        } else {
            $this->load->view('registrieren');
        }
    }

    /**
     * [API-Schnittstelle]
     * Route für das Login eines neuen Users
     */
    public function login() {
        if(!($this->agent->is_browser)) {
            $this->login_user();
        } else {
            if(isset($_SESSION['users']['pilot_id'])) {
                prepareResponse(array(), "/dashboard/", "", true, array(), "Login war erfolgreich","");
            } else {
                prepareResponse(array(), "", "index.php", true, array(), "","");
            }

        }
    }

    /**
     * [API-Schnittstelle]
     * User Aktion für Index/login
     */
    public function login_user() {
        $responseData = $this->handleUser();
        $bSuccess = $responseData['success'];
        $sMessage = $responseData['message'];
        $sessionId = $responseData['session'];
        $user_data = $responseData['data'];

        if($bSuccess == false) {
            prepareResponse($user_data, "", "index.php", $bSuccess, array(), $sMessage,$sessionId);
        } else {
            prepareResponse($user_data, "/dashboard/", "", $bSuccess, array(), $sMessage,$sessionId);
        }
    }

    private function handleUser() {
        $aData['email_adresse'] = $this->input->post('email_adresse');
        $aData['passwort'] = $this->input->post('passwort');
        $bSuccess = false;
        $user_data = array();
        $sessionId = "";
        $bUserExist = $this->user_model->check_if_user_exist('email_adresse', $aData['email_adresse']);
        if($bUserExist == FALSE) {
        } else {
            $bSuccess = $this->user_model->log_into_existing_user($aData);
        }
        if($bSuccess) {

            $sMessage = $this->nachrichten->show_a_nachricht("erfolgreicher_login")[0]->nachricht;
            if(isset($this->session->users['pilot_id'])) {
                $user_data = $this->user_model->get_user_data($this->session->users['pilot_id']);
                $sessionId = $this->session->session_id;
            }
            $this->user_model->delete_sessions();
        } else {
            $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagener_login")[0]->nachricht;
        }
        $responseData['data'] = $user_data;
        $responseData['session'] = $sessionId;
        $responseData['message'] = $sMessage;
        $responseData['success'] = $bSuccess;
        return $responseData;
    }
    /**
     * [API-Schnittstelle]
     * User Aktion für Index/register
     */
    public function register_user() {
        $aData = $this->input->post();
        $bSuccess = false;
        $aData['rolle'] = 0;
       /*
        $upload_config['upload_path']          = './uploads/';
        $upload_config['allowed_types']        = 'jpg|png';
        $upload_config['max_size']             = 100;
        $upload_config['max_width']            = 1024;
        $upload_config['max_height']           = 768;
        $this->load->library('upload', $upload_config);

        $field_name = "profilbild";
        $this->upload->do_upload($field_name);
        $upload_data = $this->upload->data();

        if(!empty($upload_data['file_name'])) {
            $aData['profilbild'] = $upload_data['full_path'];
        } else {
            $aData['profilbild'] = '/assets/images/muster_pb.png';
        }*/

        $bUserExist = $this->user_model->check_if_user_exist('email_adresse', $aData['email_adresse']);

        if($bUserExist != FALSE) {
            $sMessage = "Benutzer existiert bereits";
            prepareResponse(array(), "index/register", "", $bSuccess, array(), $sMessage, "");
        } else {
            $bSuccess = $this->create_user($aData);
            if($bSuccess) {
                $bSuccess = $this->sendEmail($aData['email_adresse'], 'Konto Verifizierung', $aData, 'verify');
                $sMessage = $this->nachrichten->show_a_nachricht("erfolgreiche_registrierung")[0]->nachricht;
                prepareResponse(array(), "", "registrieren_erfolgreich.php", $bSuccess, array(), $sMessage, "");
            } else {
                $sMessage = $this->nachrichten->show_a_nachricht("fehlgeschlagene_registrierung")[0]->nachricht;
                prepareResponse(array(), "index/register", "", $bSuccess, array(), $sMessage, "");
            }
        }

    }


    /**
     * User Aktion für Index/create_user
     * Erstellt ein neuen neutzer
     * @param array $aData
     * @return mixed
     */
    private function create_user(array $aData) {
        return $this->user_model->create_user_from_form($aData);
    }


    public function sendEmail(string $recipe_email = "", string $subject ="", array $email_data = array(), string $email_type ="") {
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

                //Set who the message is to be sent from
                try {
                    $mail->setFrom($email_setting[0]->email_adresse, 'DroneLog Support');
                } catch (Exception $e) {
                    echo $e->getMessage();
                }
                //Set who the message is to be sent to
                $mail->addAddress($recipe_email);
                //Set the subject line
                $mail->Subject = $subject;
                //Read an HTML message body from an external file, convert referenced images to embedded,
                //convert HTML into a basic plain-text alternative body
                $mail->isHTML(true);
                if (!empty($email_data)) {
                    if($email_type == 'reset') {
                        $mail->Body = $this->load->view('email_reset.php', $email_data, true);
                    } elseif($email_type == 'verify') {
                        $mail->Body = $this->load->view('email_confirm.php', $email_data, true);
                    }
                } else {
                    $mail->Body = $subject;
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

    public function upload_image() {
        $imageNeddle = 'image=';
        $aData = $this->input->post();
        $bSuccess = false;
        $i=1;
        $inputStream = file_get_contents('php://input');
        $fileData = substr($inputStream, strpos($inputStream, $imageNeddle)+strlen($imageNeddle));
        if(!empty($fileData)) {
            if(!empty($aData['filename'])) {
                $filename =  $aData['filename'];
                $filenameparts = explode(".", $filename);
                while(file_exists('./uploads/'.$filename)){
                    $filename=$filenameparts[0].'-'.$i.'.'.$filenameparts[1];
                    $i++;
                }
                $fhandle=fopen('./uploads/'.$filename, 'wb');
                fwrite($fhandle, $fileData);
                fclose($fhandle);
                //$bSuccess = true;
                //$sMessage = "Bild wurde verschickt";
            }
        }
    }
}
