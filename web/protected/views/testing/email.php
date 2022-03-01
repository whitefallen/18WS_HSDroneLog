<html>
<head>
    <title></title>
</head>
<body>
<form action="<?php echo site_url('email/add_email')?>" method="post">
    <label>Email</label><input name="email_adresse" type="text">
    <label>smtp_server</label><input name="smtp_server" type="text">
    <label>passwort</label><input name="passwort" type="text">
    <label>port</label><input name="port" type="text">
    <button type="submit">SEND ME</button>
</form>
<div>
    <?php var_dump($data); ?>
</div>
</body>
</html>