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
                Als nächster Schritt müssen die Datenbank Tabellen erstellt werden, die die Anwendung benötigt.
            </div>
        </div>
        <form action="setup_sql_fin.php" method="post">
            <input name="start_sql" type="hidden" value="1">
            <div class="form-group row">
                <div class="col-md-4">
                    <label for="bsp_data">Möchten sie Beispiel-Daten einfügen?</label>
                </div>
                <div class="col-md-2">
                    <input type="checkbox" id="bsp_data" name="bsp_data" value="1"/>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-12">
                    <button type="submit" class="btn btn-dark">
                        Tabellen erstellen
                    </button>
                </div>
            </div>
        </form>
    </div>
</body>

</html>
