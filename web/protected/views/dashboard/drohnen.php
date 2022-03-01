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
                        <h3>Drohnen</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-4 col-sm-4 col-xs-12 form-group pull-right ">



                      <a class="btn btn-success admin_only" href="<?php echo site_url("drone/add_drone") ?>">Drohne hinzufügen</a>

                        </div>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">

                        <div class="x_panel">
                            <div class="x_content">
                                <p class="text-muted font-13 m-b-30">

                                </p>
                                <div class="table-responsive">
                                <table id="datatable" class="table table-striped table-bordered">
                                    <thead>
                                    <tr>

                                        <th>Bild</th>
                                        <th>Bezeichnung</th>
                                        <th>Flugzeit</th>
                                        <th></th>
                                    </tr>
                                    </thead>


                                    <tbody>
                                    <?php
                                    foreach($drone_data as $key=>$value) {
                                        echo "<tr class='entry'>";

                                        echo '<td class="col-md-1 col-sm-1 col-xs-1"><img class="img-responsive " src="'.$value->bild.'"/></td>';
                                        echo "<td>".$value->drohnen_modell."</td>";
                                        echo "<td>".$value->flugzeit_in_min." Minuten</td>";
                                        echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='myfunction($value->drohne_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

                                        echo "</tr>";
                                    }
                                    ?>
                                    </tbody>
                                </table>

                                </div>
                                <button type="button" onclick="load()" class="btn btn-default pull-right" id="lademehr"><span class="glyphicon-plus glyphicon"aria-hidden="true"></span> mehr Laden</button>

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
    let variable_offset = 20;
    $("#lademehr").click(function(){
        $.post("<?php echo site_url('drone/index');?>", {offset: variable_offset, append: true}, function(result){
            console.log(result);
            if(!result){
                new PNotify({
                    title: 'keine weiteren Drohnen',
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
