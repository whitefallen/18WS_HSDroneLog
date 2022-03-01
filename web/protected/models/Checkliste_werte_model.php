<?php
class Checkliste_werte_model extends CI_Model
{
    /**
     * @var int $flug_id sollte eine Identifikationsnummer enthalten
     */
    private $flug_id;

    /**
     * @var int $element_id sollte eine Identifikationsnummer enthalten
     */
    private $element_id;

    /**
     * @var bool $angekreuzt wurde es angekreuzt?
     */
    private $angekreuzt;

    /**
     * @var string $kommentar sollte ein Kommentar enthalten
     */
    private $kommentar;

    /**
     * @return array
     */
    public function show_all_checkliste_werte(bool $admin = true){
        $data = array();
        $this->db->order_by('flug_id','ASC');
        $query = $this->db->get('Checkliste_flug');

        if($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }

    public function show_a_checkliste_werte(int $checkliste_werte_data, int $element_id){
        $data = array();
        $query = $this->db->get_where('Checkliste_werte', array(
            'flug_id' => $checkliste_werte_data,
            'element_id' => $element_id
        ));

        if($query->num_rows() > 0) {
            $data = $query->result();
        }
        return $data;
    }

    public function delete_a_checkliste_werte(int $checkliste_werte_data){
        $this->db->delete('Checkliste_werte', array(
            'flug_id' => $checkliste_werte_data
        ));
    }

    public function edit_checkliste_werte(array $checkliste_werte_data){
        $this->db->where('flug_id',$checkliste_werte_data['flug_id']);
        $this->db->update('Checkliste_werte',$checkliste_werte_data);
    }

    // Checklisten-Flug Ankreuzen testen
    public function add_checkliste_werte(array $checkliste_werte_data) {
        $flug_id = $checkliste_werte_data['flug_id'];
        $this->delete_a_checkliste_werte($flug_id);
        //$checkliste_id = $checkliste_flug_data['checklist_id'];
        $elements = $checkliste_werte_data['elements'];
        foreach ($elements as $element_key => $element_wert) {
            $element_comment = $element_wert['kommentar'];
            if($element_wert['wert'] == '1') {
                $element_wert = true;
            } else {
                $element_wert = false;
            }
            $this->db->insert('Checkliste_werte',array(
                'flug_id' => $flug_id,
                'element_id' => $element_key,
                'angekreuzt' => $element_wert,
                'kommentar' => $element_comment
            ));
        }
    }
}