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
        <form action="setup_env_fin.php" method="post">
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="db_user"> Datenbank Benutzername: </label>
                </div>
                <div class="col-md-9">
                    <input id="db_user" type="text" name="db_user" class="form-control" placeholder="Username" aria-describedby="db_user_help" required/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="db_user_help" class="form-text text-muted">Der Name des Benutzerkontos, mit dem man sich in der Datenbank anmeldet.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="db_pass"> Datenbank Passwort: </label>
                </div>
                <div class="col-md-9">
                    <input id="db_pass" type="text" name="db_pass" class="form-control" placeholder="Passwort" aria-describedby="db_pass_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="db_pass_help" class="form-text text-muted">Das Passwort des Benutzerkontos.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="db_user"> Datenbank Host: </label>
                </div>
                <div class="col-md-9">
                    <input id="db_host" type="text" name="db_host" class="form-control" placeholder="Localhost" aria-describedby="db_host_help" required/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="db_host_help" class="form-text text-muted">Der Name/IP-Addresse ihres Datenbanken Hosts, wo ihre Datenbank liegt.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="env"> Umgebung: </label>
                </div>
                <div class="col-md-9">
                    <select id="env" class="form-control" aria-describedby="env_help" name="env" required>
                        <option value="development">Entwicklung</option>
                        <option value="production" selected>Produktion</option>
                    </select>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="env_help" class="form-text text-muted">Die Umgebung in der die Installation laufen soll (Entwicklung/Produktion).</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="base_url"> Basis URL: </label>
                </div>
                <div class="col-md-9">
                    <input id="base_url" type="text" name="base_url" value="<?php echo $prefix.$_SERVER['SERVER_NAME']; ?>" class="form-control" placeholder="http://meineDomain.de/" aria-describedby="base_url_help" required/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="base_url_help" class="form-text text-muted">Von dieser URL aus werden alle andere Links aufgebaut</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="api_key"> Bing Maps API Schluessel: </label>
                </div>
                <div class="col-md-9">
                    <input id="api_key" type="text" name="api_key" value="" class="form-control" placeholder="ApRuC08jDSxMsdlOdf....." aria-describedby="api_key_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="api_key_help" class="form-text text-muted">Fuer das Anzeigen der Fluege auf der Map werden die Flugorte mithilfe der Bing Geocoder API uebersetzt, <br>
                        diese benötigt ein API-Key den man sich beim anmelden beim <a href="https://www.bingmapsportal.com/">Bing maps Dev Center</a> <i>kostenlos</i> generieren lassen kann. </small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="upload_size"> Maximale Bild-Uploadgroeße in KByte </label>
                </div>
                <div class="col-md-9">
                    <input id="upload_size" type="number" name="upload_size" value="200" class="form-control" placeholder="200" aria-describedby="upload_size_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="upload_size_help" class="form-text text-muted">limitiert Uploads auf den Server, die angabe wird in KiloBytes gemacht. Datein dessen Größe über dem eigestellten maximum liegen <br>
                    werden nicht mehr angenommen.
                    </small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="img_width"> Maximale Bild-Breite </label>
                </div>
                <div class="col-md-9">
                    <input id="img_width" type="number" name="img_width" value="2000" class="form-control" placeholder="2000" aria-describedby="img_width_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="img_width_help" class="form-text text-muted">limitiert Uploads auf den Server, die angabe wird in Pixeln gemacht. Datein dessen Breite über dem eigestellten maximum liegen <br>
                        werden nicht mehr angenommen.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="img_height"> Maximale Bild-Hoehe </label>
                </div>
                <div class="col-md-9">
                    <input id="img_height" type="number" name="img_height" value="2000" class="form-control" placeholder="2000" aria-describedby="img_height_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="img_height_help" class="form-text text-muted">limitiert Uploads auf den Server, die angabe wird in Pixeln gemacht. Datein dessen Hoehe über dem eigestellten maximum liegen <br>
                        werden nicht mehr angenommen.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="img_type"> Erlaubte Bild Typen</label>
                </div>
                <div class="col-md-9">
                    <select id="img_type" name="img_type" class="form-control" aria-describedby="img_type_help">
                        <option value="jpg|jpeg">
                            jpg
                        </option>
                        <option value="jpg|jpeg|png" selected>
                            jpg, png
                        </option>
                        <option value="jpg|jpeg|svg">
                            jpg, svg
                        </option>
                        <option value="jpg|jpeg|png|svg">
                            jpg, png , svg
                        </option>
                        <option value="png">
                            png
                        </option>
                        <option value="png|svg">
                            png , svg
                        </option>
                        <option value="svg">
                            svg
                        </option>
                    </select>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="img_type_help" class="form-text text-muted">limitiert Uploads auf den Server. Datein dessen Dateityp nicht dem erlaubten entsprechen werden nicht mehr angenommen.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-3">
                    <label for="upload_path"> Upload Pfad </label>
                </div>
                <div class="col-md-9">
                    <input id="upload_path" type="text" name="upload_path" value="./uploads/" class="form-control" placeholder="./uploads/" aria-describedby="upload_path_help"/>
                </div>
                <div class="col-md-9 col-md-offset-3">
                    <small id="upload_path_help" class="form-text text-muted">Bestimmt den Upload Pfad für Hochgeladene Datein.</small>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-12">
                    <button type="submit" class="btn btn-primary">
                        Absenden
                    </button>
                </div>
            </div>
        </form>
    </div>
</body>

</html>
