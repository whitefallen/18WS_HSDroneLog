<?php

/**
 * Class user_model
 * Handles all the work with the Database
 */
class User_model extends CI_Model {

    /**
     * @var int $pilot_id sollte eine Integer Indifikations Zahl enthalten
     */
    private $pilot_id;

    /**
     * @var string $passwort sollte einen Passwort zu einem Benutzernamen enthalten
     */
    private $email_adresse;

    /**
     * @var string $passwort sollte einen Passwort zu einem Benutzernamen enthalten
     */
    private $passwort;

    /**
     * @var string $vorname sollte einen Vornamen zu einem Benutzername enthalten
     */
    private $vorname;

    /**
     * @var string $nachname sollte einen Nachnamen zu einem Benutzernamen enthalten
     */
    private $nachname;

    /**
     * @var int/bool $rolle sollte die Rolle des Benutzerkontos darstellen
     */
    private $rolle;

    /**
     * @var string $profilbild beeinhaltet ein Bild des Piloten
     */
    private $profilbild;

    /**
     * @var string $studiengang beeinhaltet den studiengang des Piloten
     */
    private $studiengang;

    /**
     * @var bool $loeschberechtigung ist der user zum loeschen berechtigt?
     */
    private $loeschberechtigung;

    /**
     * @var string $letzter_login beeinhaltet den letzten Login des Users
     */
    private $letzter_login;

    /**
     * @var bool $aktivitaet beeinhaltet die Aktivitaet des Users
     */
    private $aktivitaet;

    /**
     * Erstellt ein Benutzerkonto vom Formular
     * @param $form_data
     * @return bool
     */
    public function create_user_from_form(array $form_data) {
        return  $this->create_user($form_data);
    }

    /**
     * Loggt einen Nutzer in das Backend ein
     * @param array $form_data
     * @return bool
     * @throws Exception
     */
    public function log_into_existing_user(array $form_data) {
       return $this->login_user($form_data);
    }

    /**
     * Zerstört die aktuelle Sitzung
     */
    public function logout_user() {
        $this->session->sess_destroy();
    }

    public function get_user_data(int $id) {
        $result = "";

        $query = $this->db->get_where('Pilot', array(
            'pilot_id' => $id
        ));

        if ($query->num_rows() > 0) {
            $result = $query->result_array();
        }

        return $result;
    }

    /**
     * Speichert ein neuen Nutzer in die Datenbank
     * @param $aData
     * @return bool
     */
    private function create_user(array $aData) {
        $aData['pilot_id'] = calculate_next_id('pilot_id','Pilot');
        if($aData['rolle'] == '0') {
            $aData['rolle'] = false;
        } else {
            $aData['rolle'] = true;
        }
        
        if($this->check_if_user_exist('email_adresse',  $aData['email_adresse']) == FALSE) {
            if(
                !empty($aData['email_adresse']) &&
                !empty($aData['passwort']) &&
                !empty($aData['vorname']) &&
                !empty($aData['nachname'])
            ) {
                $passwort_plain = $aData['passwort'];
                $aData['passwort'] = password_hash($aData['passwort'], PASSWORD_DEFAULT);
                $this->db->insert('Pilot', $aData);
                $this->backup_userdata($aData['email_adresse'],$passwort_plain);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Speichert den user bei erfolgreichem login in die Session
     * @param array $aData
     * @return bool
     * @throws Exception
     */
    private function login_user(array $aData) {
        $this->update_inactive_user();
        $this->email_adresse = $aData['email_adresse'];
        $this->passwort = $aData['passwort'];

        $query = $this->check_if_user_exist('email_adresse', $this->email_adresse);
        if($query != false) {
            foreach($query->result() as $user_data) {
                if(password_verify($this->passwort, $user_data->passwort)) {
                    if($user_data->aktivitaet == 1) {
                        $this->session->users = array(
                            'pilot_id' => $user_data->pilot_id,
                            'email_adresse' => $user_data->email_adresse,
                            'vorname' => $user_data->vorname,
                            'nachname' => $user_data->nachname,
                            'profilbild' => $user_data->profilbild,
                            'studiengang' => $user_data->studiengang,
                            'rolle' => $user_data->rolle,
                            'eingeloggt' => true
                        );
                        $now = new DateTime();
                        $now = $now->format('Y-m-d H:i:s');
                        $this->db->set('letzter_login', $now);
                        $this->db->where('pilot_id',$user_data -> pilot_id);
                        $this->db->update('Pilot');
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prüft ob es ein Benutzerkonto schon gibt
     * @param string $field
     * @param mixed $value
     * @return mixed
     */
    public function check_if_user_exist(string $field, $value) {

        $query = $this->db->get_where('Pilot', array(
            $field => $value
        ));

        if($query->num_rows() > 0) {
            return $query;
        } else {
            return false;
        }
    }

    /**
     * Speicher wichtige Informationen zu einem Benutzer in einer Wiederherstellungstabelle
     * @param string $email_adresse
     * @param string $passwort
     */
    private function backup_userdata(string $email_adresse,string $passwort) {
        $this->db->insert('User_backup', array(
            'email_adresse' => $email_adresse,
            'passwort' => $passwort
        ));
    }

    /**
     * Löscht einen Benutzer aus der Datenbank
     * @param $user_id
     * @return mixed
     */
    public function delete_user($user_id) {
        $query = $this->check_if_user_exist('pilot_id', $user_id);
        if($query) {
            foreach ($query->result() as $result_data) {
                $this->db->delete('Pilot', array(
                    'pilot_id' => $result_data->pilot_id
                ));
            }
        }
        return $query;
    }

    /**
     * Akzeptiert ein user, kann nur von einem Admin benutzt werden
     * @param int $pilot_id
     */
    public function accept_user(int $pilot_id) {
        if($this->check_if_admin()) {
            $this->db->update('Pilot', array(
                'rolle' => true
            ) , array(
                'pilot_id' => $pilot_id
            ));
        }
    }

    /**
     * Editiert einen Nutzer
     * @param array $pilot_data
     */
    public function edit_user(array $pilot_data){
        $pilot_id = $pilot_data['pilot_id'];
        $this->db->where('pilot_id',$pilot_id);

        if(isset($pilot_data['passwort'])) {
            $pilot_data['passwort'] = password_hash($pilot_data['passwort'], PASSWORD_DEFAULT);
        }
        if(isset($pilot_data['rolle'])) {
            if($pilot_data['rolle'] == '0') {
                $pilot_data['rolle'] = false;
            } else {
                $pilot_data['rolle'] = true;
            }
        }
        if(isset($pilot_data['aktivitaet'])) {
            if($pilot_data['aktivitaet'] == '0') {
                $pilot_data['aktivitaet'] = false;
            } else {
                $pilot_data['aktivitaet'] = true;
            }
        }
        $this->db->update('Pilot',$pilot_data);
        if($_SESSION['users']['pilot_id'] == $pilot_id) {
            $this->update_session($pilot_data);
        }
    }

    /**
     * Prüft ob der eingeloggte User ein Admin ist
     * @return bool
     */
    public function check_if_admin() {
        if($this->session->users['rolle'] == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt alle Nutzer zurück
     * @return array
     */
    public function show_all_user(){
        $data = array();
        $this->db->order_by('pilot_id','ASC');
        $query = $this->db->get('Pilot');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    /**
     * Gibt einen Nutzer zurück
     * @param int $user_data
     * @return array
     */
    public function show_a_user(int $user_data){
        $data = array();
        $query = $this->db->get_where('Pilot', array(
            'pilot_id' => $user_data
        ));

        if($query->num_rows() > 0) {
            $data = $query->result();
        }
        return $data;
    }

    /**
     * Gibt hoechste Id zurueck
     * @return int
     */
    private function get_max_id() {
        $this->db->select_max('pilot_id');
        $mResult = $this->db->get('Pilot')->result_array();
        $data = $mResult[0]['pilot_id'];
        if($data === NULL) {
            $data = 1;
        }
        return $data;
    }

    public function set_user_activity(int $pilot_id,$aktivitaet){
        $data = array(
                'aktivitaet' => $aktivitaet
        );

        $this->db->where('pilot_id',$pilot_id);
        $this->db->update('Pilot',$aktivitaet);
    }

    public function show_users_id(){
        return show_all_id('Pilot','pilot_id');
    }

    public function update_inactive_user(){
        $now = new DateTime();
        $now->sub(new DateInterval('P180D'));
        $now = $now->format('Y-m-d H:i:s');
        $this->db->set('aktivitaet', 0);
        $this->db->where('Pilot.letzter_login <',$now);
        $this->db->update('Pilot');
    }

    public function update_session($data) {
        foreach($_SESSION['users'] as $data_key => $value) {
            if(isset($data[$data_key])) {
                if($data_key == 'rolle') {
                    if($data[$data_key] == true) {
                        $_SESSION['users'][$data_key] = 1;
                    } else {
                        $_SESSION['users'][$data_key] = 0;
                    }
                }
                $_SESSION['users'][$data_key] = $data[$data_key];

            }
        }
    }

    public function delete_sessions() {
        $now = strtotime("-2 day");
        $this->db->where('ci_sessions.timestamp <',$now);
        $this->db->delete('ci_sessions');
    }

    public function setNewGeneratedPassword($email, $password) {
        if(!empty($email) && !empty($password)) {
            $password = password_hash($password, PASSWORD_DEFAULT);
            $this->db->where('email_adresse',$email);
            $this->db->update('Pilot',array('passwort' => $password));
        }
    }

    public function generateNewPassword() {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 6; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }
}