<?php
$config_file_path = __DIR__.'/setup';
$config_file_name = '.env';
if(
    !empty($_POST['db_user']) &&
    !empty($_POST['db_host']) &&
    !empty($_POST['env']) &&
    !empty($_POST['base_url'])
) {

    session_start();
    $_SESSION['db_host'] = $_POST['db_host'];
    $_SESSION['db_user'] = $_POST['db_user'];
    $_SESSION['db_pass'] = $_POST['db_pass'];
    $_SESSION['env'] = $_POST['env'];
    $_SESSION['base_url'] = $_POST['base_url'];
    $_SESSION['api_key'] = $_POST['api_key'];
    $_SESSION['upload_size'] = $_POST['upload_size'];
    $_SESSION['img_width'] = $_POST['img_width'];
    $_SESSION['img_height'] = $_POST['img_height'];
    $_SESSION['img_type'] = $_POST['img_type'];
    $_SESSION['upload_path'] = $_POST['upload_path'];
    $handle = fopen(__DIR__.'/../'.$config_file_name, 'w') or die('Cannot open file:  '.__DIR__.'/../'.$config_file_name);

    $file_db_user = 'DB_USER='.$_POST['db_user'];
    $file_db_pass = "\n".'DB_PASS='.$_POST['db_pass'];
    $file_db_host = "\n".'DB_HOST='.$_POST['db_host'];
    $file_env = "\n".'ENVIROMENT='.$_POST['env'];
    $file_base_url = "\n".'BASE_URL='.$_POST['base_url'];
    $file_api_key = "\n".'API_KEY='.$_POST['api_key'];
    $file_upload_size = "\n".'upload_size='.$_POST['upload_size'];
    $file_img_width = "\n".'IMG_WIDTH='.$_POST['img_width'];
    $file_img_height = "\n".'IMG_HEIGHT='.$_POST['img_height'];
    $file_img_type = "\n".'IMG_TYPE='.$_POST['img_type'];
    $file_upload_path = "\n".'UPLOAD_PATH='.(string)$_POST['upload_path'];

    fwrite($handle, $file_db_user);
    fwrite($handle, $file_db_pass);
    fwrite($handle, $file_db_host);
    fwrite($handle, $file_env);
    fwrite($handle, $file_base_url);
    fwrite($handle, $file_api_key);
    fwrite($handle, $file_upload_size);
    fwrite($handle, $file_img_width);
    fwrite($handle, $file_img_height);
    fwrite($handle, $file_img_type);
    fwrite($handle, $file_upload_path);
    fclose($handle);

    header("Location:/setup/setup_sql.php", true,'303');
    die();
}
