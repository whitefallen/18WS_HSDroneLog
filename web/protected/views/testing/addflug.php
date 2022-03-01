<?php if (!isset($this->session->users['email_adresse'])) {
    redirect(base_url());
} ?>
<html lang="en">
<?php $this->load->view("template/head"); ?>

<body class="nav-md footer_fixed">
<div class="container body">
    <div class="main_container">
        <?php $this->load->view("template/sidebar"); ?>

        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>Flug hinzufügen</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right ">
                            <div class="input-group">


                            </div>
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

                        <div class="x_content">
                            <br />
                            <form id="demo-form2" data-parsley-validate action="<?php echo site_url("flight/add_flight") ?>" method="post" class="form-horizontal form-label-left">
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="flugbezeichnung">Flugname
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="flugbezeichnung" name="flugbezeichnung" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="pilot_id">Pilot</label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                    <select id="pilot_id" name="pilot_id" class="form-control col-md-7 col-xs-12" required>

                                        <?php
                                        foreach($user_data as $user_key=>$user_value) {

                                                echo "<option value='$user_value->pilot_id'>".$user_value->vorname." ".$user_value->nachname."</option>";

                                        }

                                        ?>
                                    </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="flugdatum" class="control-label col-md-3 col-sm-3 col-xs-12">Flugdatum</label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input id="flugdatum" class="form-control col-md-7 col-xs-12" type="date" name="flugdatum" required="required">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="einsatzbeginn" class="control-label col-md-3 col-sm-3 col-xs-12">Einsatzbeginn</label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input id="einsatzbeginn" class="form-control col-md-7 col-xs-12" type="time" name="einsatzbeginn" required="required">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="einsatzende" class="control-label col-md-3 col-sm-3 col-xs-12">Einsatzende</label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input id="einsatzende" class="form-control col-md-7 col-xs-12" type="time" name="einsatzende" required="required">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="einsatzort_name">Einsatzort
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="einsatzort_name" name="einsatzort_name" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="drohnen_modell">Drohne
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">

                                        <select id="drohnen_modell" name="drohne_id" class="form-control col-md-7 col-xs-12" required>
                                            <option> Drohne Wählen...</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="ln_solid"></div>
                                <div class="form-group">
                                    <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                        <a class="btn btn-primary"  href="<?php echo site_url("flight/index") ?>">Cancel</a>
                                        <button class="btn btn-primary" type="reset">Reset</button>
                                        <button type="submit" class="btn btn-success">Submit</button>
                                    </div>
                                </div>

                            </form>
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

    $("#einsatzende").keyup(function(){
        var einsatzende = $("#einsatzende").val();
        var einsatzstart = $("#einsatzstart").val();
        var flugdatum = $("#flugdatum").val();
        $.post("<?php echo site_url('flight/requestAvailableDrones')?>", {startzeit: einsatzstart, endzeit: einsatzende, date: flugdatum}, function(result){
           console.log(result);
            var $dropdown = $("#drohnen_modell");
            $('option', $dropdown).remove();
            $.each(result, function() {
                $dropdown.append($("<option />").val(this.drohne_id).text(this.drohnen_modell));
            });
        });
    });

</script>
</body>
</html>
