<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

if ( ! function_exists('calculate_next_id')) {

    function calculate_next_id(String $table_id, String $table)
    {
        $ci =& get_instance();
        $ci->db->select($table_id);
        $ci->db->from($table);
        $query = $ci->db->get()->result_array();
        $next_index = 0;
        $allIds = array();

        foreach ($query as $key => $value) {
            $allIds[$value[$table_id]] = $value[$table_id];
        }

        for ($i = 1; $i <= end($allIds); $i++) {
            if (!isset($allIds[$i])) {
                $next_index = $i;
                break;
            }
        }

        $max_id = get_max_id($table_id,$table);

        if ($next_index == 0 && $max_id == null) {
            $next_index = 1;
        } else if ($next_index == 0 && $max_id != null) {
            $next_index = $max_id + 1;
        }

        return $next_index;
    }
}

if ( ! function_exists('get_max_id')) {
    function get_max_id(String $table_id,String $table) {
        $ci =& get_instance();
        $ci->db->select_max($table_id);
        $mResult = $ci->db->get($table)->result_array();
        $data = $mResult[0][$table_id];
        return $data;
    }
}

if ( ! function_exists('show_data')) {
    function show_data(string $id)
    {
        $ci =& get_instance();
        $data = array();
        $ci->db->select('data');
        $ci->db->from('ci_sessions');
        $ci->db->where('id',$id);
        $query = $ci->db->get();

        if ($query->num_rows() > 0) {
            $data = $query->result();
        }

        return $data;
    }
}

if ( ! function_exists('show_all_id')) {
    function show_all_id(string $table,string $id){
        $ci =& get_instance();
        $ci->db->select($id);
        $ci->db->from($table);
        $query = $ci->db->get();

        if ($query->num_rows() > 0) {
            $data = $query->result_array();
        }

        return $data;
    }
}
