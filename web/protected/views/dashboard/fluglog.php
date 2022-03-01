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
                <div class="clearfix"></div>

                <div class="row">



                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel tile fixed_height_80">
                            <div class="row x_title">
                                <div class="col-md-6">
                                    <h3>Fluglog</h3>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                            <div class="x_content">
                                <?php
                                if ($fluglog == null){
                                    echo "Es ist noch kein Fluglog hinterlegt!";
                                }
                                else{
                                    echo "<div class=\"table-responsive \">";
                                    echo '<table id="datatable" class="table table-striped table-bordered">';
                                    echo '<thead><tr>
                             
                                            <td>Latitude</td>
                                            <td>Longitude</td>
                                            <td>Altitude</td>
                                            <td>HomeLatitude</td>
                                            <td>HomeLongitude</td>
                                            <td>Battery_Percent</td>
                                            <td>App_Warning</td>
                                           </tr>
                                           </thead>
                                           <tbody>
                                    ';

                                    foreach ($fluglog as $key=>$value){

                                        echo "<tr>";
                                       
                                        echo "<td>" . $value->latitude . "</td>";
                                        echo "<td>" . $value->longitude . "</td>";
                                        echo "<td>" . $value->altitude . "</td>";
                                        echo "<td>" . $value->homeLatitude . "</td>";
                                        echo "<td>" . $value->homeLongitude . "</td>";
                                        echo "<td>" . $value->battery_percent . "</td>";
                                        echo "<td>" . $value->app_warning . "</td>";
                                        echo "</tr>";
                                    };
                                    echo "</tbody>";
                                    echo "</table>";
                                    echo "</div>";
                                    echo "<button type=\"button\" class=\"btn btn-default pull-right\" id=\"lademehr\" ><span class=\"glyphicon-plus glyphicon\"aria-hidden=\"true\"></span> mehr Laden</button>";
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
    function myfunction(id) {
        window.location = "show/"+id;
    }

</script>
<script>
    let variable_offset = 50;
    $("#lademehr").click(function(){
        $.post("<?php echo site_url('flight/show_flight_log/'.$fluglog[0]->flug_id.'');?>", {offset: variable_offset, append: true}, function(result){
            console.log(result);
            if(!result){
                new PNotify({
                    title: 'keine weiteren Daten',
                    text: '',
                    type: 'info',
                    styling: 'bootstrap3',
                    addclass: 'dark'
                });
            }
        }).done(function(response){
            $('#datatable > tbody:last-child').append(response);
        });
        variable_offset = variable_offset +50
    });
</script>
</body>
</html>
