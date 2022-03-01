<?php


session_start();
$servername = $_SESSION['db_host'];
$username = $_SESSION['db_user'];
$password = $_SESSION['db_pass'];

if(
    !empty($_POST['start_sql'])
) {

    $conn = new mysqli($servername, $username, $password, 'dronelog');
    // Check connection
    if ($conn->connect_error) {
        header("Location:/", true,'303');
        die("Connection failed: " . $conn->connect_error);
    }
    $_SESSION['db_conn'] = $conn;
    $sql_tbl = file_get_contents('dronelog_tbl_schema_dump.sql');
    /* execute multi query */
    $conn->multi_query($sql_tbl);
    // Finish processing multi query before executing the next one
    while(mysqli_next_result($conn)){;}
    if(isset($_POST['bsp_data'])) {
        $sql_bsp = file_get_contents('dronelog_bsp_data_dump.sql');
        $conn->multi_query($sql_bsp);
    }
    header("Location:/setup/setup_user.php", true,'303');
    die();
}
