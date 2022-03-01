<?php if (!isset($this->session->users['email_adresse'])) {
    redirect(base_url());
} ?>
<html lang="en">
<?php $this->load->view("template/head"); ?>

<body class="nav-md ">
<div class="container body">
    <div class="main_container">
        <?php $this->load->view("template/sidebar"); ?>

        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>Flüge</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-4 col-sm-4 col-xs-12 form-group pull-right ">

                      <a class="btn btn-success" href="<?php echo site_url("flight/add_flight") ?>">Flug hinzufügen</a>

                        </div>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">

                        <!-- <div class="x_title">

                             <ul class="nav navbar-right panel_toolbox">
                                 <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                 </li>
                                 <li class="dropdown">
                                     <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                                     <ul class="dropdown-menu" role="menu">
                                         <li><a href="#">Settings 1</a>
                                         </li>
                                         <li><a href="#">Settings 2</a>
                                         </li>
                                     </ul>
                                 </li>
                                 <li><a class="close-link"><i class="fa fa-close"></i></a>
                                 </li>
                             </ul>
                             <div class="clearfix"></div>
                         </div>-->
                        <div class="x_panel">
                        <div class="x_content">
                            <p class="text-muted font-13 m-b-30">

                            </p>
                            <?php
                            if(sizeof($flight_data) != 0) {
                                echo "<div class='table-responsive'>
                            <table id='datatable' class='table table-striped table-bordered'>
                                <thead>
                                <tr>
                                    <th>Bezeichnung</th>
                                    <th>Einsatzdatum</th>

                                    <th>Einsatzort</th>
                                    <th>Drohne</th>
                                    <th>Pilot</th>
                                    <th></th>

                                </tr>
                                </thead>


                                <tbody id='tbody'>";


                                foreach ($flight_data as $key => $value) {
                                    $flugVergangen = new DateTime(date("Y-m-d")) > new DateTime($value->flugdatum);

                                    /*if ($flugVergangen)
                                        echo "<tr style='background-color: lightgrey'>";
                                    else
                                        echo "<tr style='background-color: #e6f6ff'>";
                                    */
                                    if ($flugVergangen)
                                        echo "<tr style='background-color: #F7F7F7'>";
                                    else
                                        echo "<tr style='background-color: white'>";

                                    echo '<td>' . $value->flugbezeichnung . ' </td>';


                                    echo '<td>' . $value->flugdatum . ' </td>';

                                    echo '<td>' . $value->einsatzort_name . '</td>';
                                    echo '<td>' . $value->drohnen_modell . '</td>';

                                    echo '<td>' . $value->vorname . $value->nachname . '</td>';
                                    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='flugdetails($value->flug_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";
                                    echo '</tr>';
                                }



                                echo "</tbody>
                            </table>
                            </div>
                                                        <button type=\"button\" class=\"btn btn-default pull-right\" id=\"lademehr\" ><span class=\"glyphicon-plus glyphicon\"aria-hidden=\"true\"></span> mehr Laden</button>
";} else{
                                echo "<div class='jumbotron'>
                                <h1>Noch kein Flug vorhanden</h1>
                                <p>Erstelle jetzt deinen ersten Flug um zu starten</p>
                                <p><span class=\"glyphicon-arrow-right glyphicon\"aria-hidden=\"true\"></span> <a class=\"btn btn-success\" href= ".site_url("flight/add_flight").">Flug hinzufügen</a><span class=\"glyphicon-arrow-left glyphicon\"aria-hidden=\"true\"></span></p>
                            </div>";
                            }
                            ?>

                        </div>
                    </div>

                    </div>

                </div>
            </div>
        </div>

        <!-- /page content -->

        <!-- footer content -->
        <?php $this->load->view("template/footer"); ?>
        <!-- /footer content -->
    </div>
</div>
<?php $this->load->view("template/scripts"); ?>
<script>
    function flugdetails(id) {
        window.location = "show/"+id;
    }

</script>
<script>
    let variable_offset = 20;
    $("#lademehr").click(function(){
        $.post("<?php echo site_url('flight/index');?>", {offset: variable_offset, append: true}, function(result){
            console.log(result);
            if(!result){
                new PNotify({
                    title: 'keine weiteren Flüge',
                    text: '',
                    type: 'info',
                    styling: 'bootstrap3',
                    addclass: 'dark'
                });
            }
        }).done(function(response){
            $('#datatable > tbody:last-child').append(response);
        });
        variable_offset = variable_offset +20
    });
</script>
</body>
</html>
