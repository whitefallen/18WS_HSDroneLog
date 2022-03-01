<html>
<head>
    <title>
        testing Forms
    </title>
</head>
<body>
<form action="<?php echo site_url("checklist/add_checklist"); ?>" method="post">
    <input type="text" name="bezeichnung"><br>
    <input type="text" name="erklaerung"><br>
    <select multiple name="elements[]">
        <?php
            foreach($element_data as $key=>$value) {
        ?><option value="<?php echo $value->element_id; ?>"><?php echo $value->bezeichnung; ?></option> <?php
            }
        ?>
    </select>
    <button type="submit">SENDME</button>
</form>
</body>
</html>