<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
</head>
<body>

<div id="container">
    <h1>Dronelog - Password Hash</h1>
    <div id="body">
        <form action="<?php echo site_url("hash") ?>" method="post">
            <input name="passwort" type="password" title="passwort"/>
            <button type="submit">SEND ME</button>
        </form>
    </div>
</div>
</body>
</html>