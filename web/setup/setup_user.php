<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="../assets/vendors/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <title>Konfiguration</title>
</head>
<?php if($_SERVER['HTTPS'] === 'on') {
    $prefix = "https://";
} else {
    $prefix = "http://";
}
?>
<body>
<style>
    body > div.container {
        box-shadow: black 0 -5px 5px 0;
        margin-top: 2vw;
    }
</style>
    <div class="container">
        <h1> Willkommen zu Dronelog</h1>
        <div class="row">
            <div class="col-md-12">
                <p class="text-danger">Sie sehen diese Seite, weil für ihre Installation noch keine Konfiguration hinterlegt ist.</p>
            </div>
        </div>
        <div class="row" style="margin-bottom: 2vw;">
            <div class="col-md-12">
                <p>Bitte füllen sie das Formular auf dieser Seite aus.</p>
            </div>
        </div>
        <form action="setup_user_fin.php" method="post">
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="vorname"> Vorname: </label>
                </div>
                <div class="col-md-9">
                    <input class="form-control" type="text" name="vorname" placeholder="Vorname">
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="nachname"> Nachname: </label>
                </div>
                <div class="col-md-9">
                    <input class="form-control" type="text" name="nachname" placeholder="Nachname">
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="studiengang"> Studiengang: </label>
                </div>
                <div class="col-md-9">
                    <input class="form-control" type="text" name="studiengang" placeholder="Studiengang">
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="email"> Email: </label>
                </div>
                <div class="col-md-9">
                    <input class="form-control" type="text" name="email_adresse" placeholder="E-Mail">
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="passwort"> Passwort: </label>
                </div>
                <div class="col-md-9">
                    <input class="form-control" type="password" name="passwort" placeholder="Passwort">
                </div>
            </div>
            <input type="hidden" name="rolle" value="1">
            <div class="form-group row">
                <div class="col-md-12">
                    <button type="submit" class="btn btn-dark">
                        Absenden
                    </button>
                </div>
            </div>
        </form>
    </div>
</body>
</html>
