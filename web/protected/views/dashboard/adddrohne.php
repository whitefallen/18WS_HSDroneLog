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


                             <div class="clearfix"></div>
                         </div>-->

                            <div class="x_content">
                                <br />
                                <form id="demo-form2" data-parsley-validate action="<?php echo site_url("drone/add_drone") ?>" method="post" class="form-horizontal form-label-left" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="drohnen_modell">Bezeichnung
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="text" id="drohnen_modell" name="drohnen_modell" required="required" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                   <!-- <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="akku_id">Akku
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">

                                            <select id="akku_id" name="akku_id" class="form-control col-md-7 col-xs-12" required>
                                                <?php

                                                foreach($drone_data as $key=>$value) {

                                                    echo "<option value='$value->akku_id'>".$value->bezeichnung."</option>";

                                                }

                                                ?>
                                            </select>
                                        </div>
                                    </div>-->
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="fluggewicht_in_gramm">Fluggewicht in Gramm
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="number" id="fluggewicht_in_gramm" name="fluggewicht_in_gramm" required="required" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="flugzeit_in_min" class="control-label col-md-3 col-sm-3 col-xs-12">Flugzeit in Min</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="flugzeit_in_min" class="form-control col-md-7 col-xs-12" type="number" name="flugzeit_in_min" required="required">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="maximale_flughoehe_in_m" class="control-label col-md-3 col-sm-3 col-xs-12">maximale Flughoehe</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="maximale_flughoehe_in_m" class="form-control col-md-7 col-xs-12" type="number" name="maximale_flughoehe_in_m" required="required">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="diagonale_groesse_in_mm" class="control-label col-md-3 col-sm-3 col-xs-12">diagonale Groesse in mm</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="diagonale_groesse_in_mm" class="form-control col-md-7 col-xs-12" type="number" name="diagonale_groesse_in_mm" required="required">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="hoechstgeschwindigkeit_in_kmh" class="control-label col-md-3 col-sm-3 col-xs-12">Hoechstgeschwindigkeit in kmh</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="hoechstgeschwindigkeit_in_kmh" class="form-control col-md-7 col-xs-12" type="number" name="hoechstgeschwindigkeit_in_kmh" required="required">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="bild" class="control-label col-md-3 col-sm-3 col-xs-12">Bild</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input id="bild" class="form-control-file btn  col-md-7 col-xs-12" type="file" name="bild">
                                        </div>
                                    </div>
                                    <div class="ln_solid"></div>
                                    <div class="form-group">
                                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                            <a class="btn btn-primary"  href="<?php echo site_url("drone/index") ?>">Zur??ck</a>
                                            <button class="btn btn-default" type="reset">Leeren</button>
                                            <button type="submit" class="btn btn-success">Speichern</button>
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

</script>
</body>
</html>
