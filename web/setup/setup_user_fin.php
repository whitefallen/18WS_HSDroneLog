<?php

    session_start();
    $servername = $_SESSION['db_host'];
    $username = $_SESSION['db_user'];
    $password = $_SESSION['db_pass'];
    $bSuccess = false;
    $conn = new mysqli($servername, $username, $password, 'dronelog');

    $hashedPassword = password_hash($_POST['passwort'], PASSWORD_DEFAULT);

    $sql = "INSERT INTO `Pilot` (`pilot_id`,`vorname`,`nachname`,`studiengang`,`email_adresse`,`passwort`,`rolle`,`loeschberechtigung`,`aktivitaet`)
    VALUES (66,'{$_POST['vorname']}','{$_POST['nachname']}','{$_POST['studiengang']}','{$_POST['email_adresse']}','{$hashedPassword}',1,1,1);";

    if ($conn->query($sql) === TRUE) {
        echo "Admin User Successfully created";
        $bSuccess = true;
    } else {
        printf("User could not be created - Errormessage: %s\n", $conn->connect_error);
    }
    $conn->close();

    if($bSuccess) {
        header("Location:/", true,'303');
    }
