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
 <?php $this->load->view("template/navbar"); ?>



          <?php $this->load->view("template/sidebar"); ?>

          <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

           <h1 class="page-header">Drohnen</h1>

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
            foreach($drone_data as $key=>$value) {
                echo "<tr class='anklickbar' onclick='myfunction($value->drohne_id );'>";
                echo "<td>".$value->drohne_id."</td>";
                echo "<td>".$value->bild."</td>";
                echo "<td>".$value->drohnen_modell."</td>";

                echo "</tr>";
                }
            ?>
            </tbody> 
        </table>
        </div>
         <a class="btn btn-default" href="<?php echo site_url("drone/add_drone") ?>" role="button"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Drohne hinzufügen</a>

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