<html>
<head>
    <title>
        testing Forms
    </title>
</head>
<body>
<form action="<?php echo site_url("akku/edit_akku/{$akku_id}"); ?>" method="post">
<?php
if(isset($akku_id)) {
    foreach ($akku_data as $key=>$value) {
        ?>
        <div>
            <input type="text" name="bezeichnung" value="<?php echo $value->bezeichnung;?>"><br><input type="text" name="anzahl" value="<?php echo $value->anzahl;?>"> <br>
        </div>
    <?php }
}
?>
    <button type="submit">SENDME</button>
</form>
</body>
</html>