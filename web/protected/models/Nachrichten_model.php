<?php
/**
 * Created by PhpStorm.
 * User: jonas
 * Date: 23.11.2018
 * Time: 09:42
 */

class Nachrichten_model extends CI_Model
{
    /**
     * @var string $bezeichnung beeinhaltet einen Bezeichner für die Nachricht
     */
    private $bezeichnung;

    /**
     * @var string $nachricht beeinhaltet eine Nachricht
     */
    private $nachricht;

    public function add_nachricht(array $nachrichten_data){
        $data = array(
            'bezeichnung' => $nachrichten_data['bezeichnung'],
            'nachricht' => $nachrichten_data['nachricht']
        );

        $this->db->insert('Nachricht', $data);
    }

    public function show_all_nachrichten(){
        $data = array();
        $query = $this->db->get('Nachrichten');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_nachricht(String $nachrichten_data){
        $data = array();
        $query = $this->db->get_where('Nachrichten', array(
            'bezeichnung' => $nachrichten_data
        ));

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function delete_a_nachricht(String $nachrichten_data){
        $this->db->delete('Nachrichten', array(
            'Nachrichten' => $nachrichten_data
        ));
    }

    public function edit_nachricht(array $nachrichten_data){
        $data = array(
            'bezeichnung' => $nachrichten_data['bezeichnung'],
            'nachricht' => $nachrichten_data['nachricht']
        );

        $this->db->where('bezeichnung',$data['bezeichnung']);
        $this->db->update('Nachrichten',$data);
    }
}