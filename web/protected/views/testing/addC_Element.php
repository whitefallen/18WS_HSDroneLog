<html>
<head>
    <title>
        testing Forms
    </title>
</head>
<body>
<form action="<?php echo site_url("checklistelement/add_element"); ?>" method="post">
    <input type="text" name="bezeichnung"><br>
    <input type="text" name="erklaerung">
    <button type="submit">SENDME</button>
</form>
</body>
</html>