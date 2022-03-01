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

        <div class="col-sm-3 col-md-2 sidebar">

            <div class="profile">
                <a class="profile-link" href="<?php echo site_url("user/show") ?>">

                    <h2><?php echo $this->session->users['vorname'] ?></h2>
                </a>
            </div>

            <ul class="nav nav-sidebar">

                <li><a href="<?php echo site_url("user/home") ?>"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> Home </a></li>
                <li><a href="<?php echo site_url("flight/index") ?>"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Flüge</a></li>
                <li><a href="<?php echo site_url("drone/index") ?>"><span class="glyphicon glyphicon-plane" aria-hidden="true"></span> Drohnen <span class="sr-only">(current)</span></a></li>
                <li class="active"><a href="<?php echo site_url("checklist/index") ?>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Checklisten </a></li>
                <li><a href="<?php echo site_url("akku/index") ?>"><span class="glyphicon glyphicon-oil" aria-hidden="true"></span> Akkus </a></li>

                <li><a href="<?php echo site_url("user/logout") ?>"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout </a></li>
            </ul>
        </div>



    </div>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2">
        <div class="main">
        <h1 class="page-header">Checklisten</h1>

        <div class="table-wrapper-scroll-y">


            <table class="table table-hover">

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Bild</th>
                    <th>Bezeichnung</th>
                </tr>
                </thead>
                <tbody>
                <?php
                foreach($checklist_data as $key=>$value) {
                    echo "<tr class='anklickbar' onclick='myfunction($value->checklist_id );'>";
                    echo "<td>".$value->checkliste_name_id."</td>";
                    echo "<td>".$value->bezeichner."</td>";
                    echo "<td>".$value->erklaerung."</td>";

                    echo "</tr>";
                }
                ?>
                </tbody>
            </table>
        </div>
        <a class="btn btn-default" href="<?php echo site_url("checklist/add_checklist") ?>" role="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Checkliste hinzufügen</a>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<script>
    function myfunction(id) {
        window.location = "show/"+id;
    }

</script>


</body>
</html>