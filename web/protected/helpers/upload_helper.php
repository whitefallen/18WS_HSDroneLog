<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

if ( ! function_exists('upload_image_from_mobile')) {

    function upload_image_from_mobile(array $_data, string $_needleName)
    {
        $imageNeddle = $_needleName;
        $existingCounter=1;
        $inputStream = file_get_contents('php://input');
        $fileData = substr($inputStream, strpos($inputStream, $imageNeddle)+strlen($imageNeddle));
        $pathToImage = "";
        if(!empty($fileData)) {
            if(!empty($_data['filename'])) {
                $filename =  $_data['filename'];
                $filenameparts = explode(".", $filename);
                while(file_exists('./uploads/'.$filename)){
                    $filename=$filenameparts[0].'-'.$existingCounter.'.'.$filenameparts[1];
                    $existingCounter++;
                }
                $fhandle=fopen('./uploads/'.$filename, 'wb');
                fwrite($fhandle, $fileData);
                fclose($fhandle);
                $pathToImage = "/uploads/".$filename;
            }
        }
        return $pathToImage;
    }
}
