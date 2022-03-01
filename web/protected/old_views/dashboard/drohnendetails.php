<?php if (!isset($this->session->users['email_adresse'])) {
    redirect(base_url());
} ?>
<?php
//print_r($drone_data); $drone_data enthält alle daten zu den Flügen

?>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>HSDronelog</title>

    <!-- Bootstrap -->
    <link href=<?php echo base_url('/assets/css/bootstrap.css')?> rel="stylesheet">
    <link href=<?php echo base_url('/assets/css/dashboard.css')?> rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">HSDronelog</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2">
            <div class="sidebar">
            <div class="profile">
                <a class="profile-link" href="<?php echo site_url("user/show") ?>">

                    <h2><?php echo $this->session->users['vorname'] ?></h2>
                </a>
            </div>
            <ul class="nav nav-sidebar">
                <li><a href="<?php echo site_url("user/home") ?>"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> Home </a></li>
                <li><a href="<?php echo site_url("flight/index") ?>"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Flüge</a></li>
                <li class="active"><a href="<?php echo site_url("drone/index") ?>"><span class="glyphicon glyphicon-plane" aria-hidden="true"></span> Drohnen <span class="sr-only">(current)</span></a></li>
                <li><a href="<?php echo site_url("checklist/index") ?>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Checklisten </a></li>
                <li><a href="<?php echo site_url("akku/index") ?>"><span class="glyphicon glyphicon-oil" aria-hidden="true"></span> Akkus </a></li>
                <li><a href="<?php echo site_url("user/logout") ?>"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout </a></li>

            </ul>
        </div>
        </div>
</div>
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2">
    <div class="main">
    <h1 class="page-header">
        <?php
        foreach($drone_data as $key=>$value) {

            echo "".$value->drohnen_modell."";

        }
        ?>
    </h1>



    <div class="row">
        <div class="table-wrapper-scroll-y">
        <div id="uneditable" class="col-sm-10 col-md-10 col-lg-10 uneditable" style="display: block;">

        <?php
        foreach($drone_data as $key=>$value) {
            ?>
            <div class="row">
                <div class="col-sm-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>ID:</span>"; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->drohne_id ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Akku:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->bezeichnung ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Fluggewicht in Gramm:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->fluggewicht_in_gramm ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Diagonale Größe in Millimetern:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->diagonale_groesse_in_mm ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Maximale FLugzeit in Minuten:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->flugzeit_in_min ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Maximale FLughöhne in Metern:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->maximale_flueghoehe_in_m ?>
                </div>
            </div>
            <div class="row">
                <div class="col-md-3 drohnen-details">
                    <?php echo "<span class=details-bezeichner>Höchstgeschwindigkeit in km/h:</span> "; ?>
                </div>
                <div class="col-md-6 drohnen-details">
                    <?php echo $value->hoechstgeschwindigkeit_in_kmh ?>
                </div>
            </div>

            <?php
        }
        ?>
        </div>

<div id="editable" class="col-md-6 editable" style="display: none;">
        <form class="needs-validation" name="edit_drone" id="edit_drone" action="<?php echo site_url("drone/edit_drone/{$current_id}") ?>" method="post" >

            <div class="row">
                <label for="drohnen_modell">Drohnen-Modell</label>

                <input type="text" name="drohnen_modell" class="form-control" id="drohnen_modell" required value=" <?php echo $value->drohnen_modell ?>">
            </div>


            <div class="row">
                <label for="fluggewicht_in_gramm">Fluggewicht in Gramm <span class="text-muted"></span></label>
                <input type="text" name="fluggewicht_in_gramm" class="form-control" id="fluggewicht_in_gramm" required value="<?php echo $value->fluggewicht_in_gramm ?>">

            </div>
            <div class="row">
                <label for="flugzeit_in_min">Flugzeit in Min <span class="text-muted"></span></label>
                <input type="text" name="flugzeit_in_min" class="form-control" id="flugzeit_in_min" required value="<?php echo $value->flugzeit_in_min ?>">

            </div>
            <div class="row">
                <label for="diagonale_groesse_in_mm">diagonale Groesse in mm<span class="text-muted"></span></label>
                <input type="text" name="diagonale_groesse_in_mm" class="form-control" id="diagonale_groesse_in_mm" required value="<?php echo $value->diagonale_groesse_in_mm ?>">

            </div>
            <div class="row">
                <label for="maximale_flueghoehe">maximale Flueghoehe in m<span class="text-muted"></span></label>
                <input type="text" name="maximale_flueghoehe_in_m" class="form-control" id="maximale_flueghoehe_in_m" required value="<?php echo $value->maximale_flueghoehe_in_m ?>">

            </div>
            <div class="row">
                <label for="hoechstgeschwindigkeit_in_kmh">Hoechstgeschwindigkeit inkmh <span class="text-muted"></span></label>
                <input type="text" name="hoechstgeschwindigkeit_in_kmh" class="form-control" id="hoechstgeschwindigkeit_in_kmh" required value="<?php echo $value->hoechstgeschwindigkeit_in_kmh ?>">

            </div>


        </form>

</div>
    </div>
        <div class="row uneditable" style="display: block;">
            <a class="btn btn-primary" href="#" onclick="edit();">Editieren</a>
            <a class="btn btn-primary" href="<?php echo site_url("drone/delete_drone/{$current_id}"); ?>">Löschen</a>
        </div>
        <div class="row editable" style="display: none;">
            <button class="btn btn-primary" type="submit" form="edit_drone">Speichern</button>

        </div>

</div>

    </div>
</div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>


<script>
    function edit() {

        $(".uneditable").toggle();
        $(".editable").toggle();
    };

</script>



</body>
</html>