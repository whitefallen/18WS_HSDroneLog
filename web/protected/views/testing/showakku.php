<html>
<head>
    <title>
        testing Forms
    </title>
</head>
<body>
<?php

if(isset($akku_id)) {
    foreach ($akku_data as $key=>$value) { ?>
        <div>
            <span><?php echo $value->bezeichnung; ?>  :  </span><span><?php echo $value->anzahl;?> </span> <br>
        </div>
    <?php }
} else {
    foreach ($akku_data as $key=>$value) { ?>
        <div>
            <span><?php echo $value->bezeichnung; ?>  :  </span><span><?php echo $value->anzahl;?> </span> <br>
        </div>
    <?php }
}
?>
</body>
</html>