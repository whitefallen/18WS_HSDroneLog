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
                        <h3>Drohne</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-13 form-group pull-right">
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <div class="row uneditable " style="display: block;">
                                        <a class="btn btn-success admin_only" href="#" onclick="edit();">Editieren</a>
                                        <!--<a class="btn btn-danger"
                                           href="<?php echo site_url("drone/delete_drone/{$current_id}"); ?>">Löschen</a>-->
                                        <button type="button " class="btn btn-danger admin_only" data-toggle="modal" data-target=".bs-example-modal-sm">Löschen</button>

                                        <a class="btn btn-primary"
                                           href="<?php echo site_url("drone/index"); ?>">Zurück</a>

                                    </div>
                                    <div class="row editable" style="display: none;">
                                        <button class="btn btn-success" type="submit" form="edit_drone">Speichern</button>
                                        <a class="btn btn-primary" href="#" onclick="edit();">Zurück</a>
                                    </div>
                    </span>
                            </div>
                        </div>
                        <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-hidden="true">
                            <div class="modal-dialog modal-sm">
                                <div class="modal-content">

                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
                                        </button>
                                        <h4 class="modal-title" id="myModalLabel2">Lösch Warnung</h4>
                                    </div>
                                    <div class="modal-body">
                                        <h4>Löschen?</h4>
                                        <p>Wollen sie den Eintrag wirklich löschen?</p>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-primary" data-dismiss="modal">Schließen</button>
                                        <a class="btn btn-danger" href="<?php echo site_url("drone/delete_drone/{$current_id}"); ?>">Löschen</a>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">

                        <div class="x_panel">
                            <div class="x_content">
                                <div class="x_title">
                                    <?php
                                    foreach ($drone_data

                                    as $key => $value) {
                                    ?>
                                    <h2><?php echo $value->drohnen_modell ?></h2>
                                    <div class="clearfix"></div>
                                </div>


                                    <div id="uneditable" class="col-sm-10 col-md-10 col-lg-10 uneditable"
                                         style="display: block;">
                                        <div class="col-md-4 col-sm-4 col-xs-12 pull-right">
                                            <div class="profile_img">
                                                <div id="crop-avatar">
                                                    <!-- Current avatar -->
                                                    <!--<div class="img-circle profilePicLongestFlight pull-left profile_thumb border-aero" style="background-image: url(<?php echo $value->bild ?>)">

                                                    </div>-->
                                                    <img class="img-responsive avatar-view" src="<?php echo $value->bild?>" alt="Drohne">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <h3>Technische Details</h3>
                                            <table class="countries_list">
                                                <tbody>
                                                <tr>
                                                    <td>Fluggewicht:</td>
                                                    <td class="fs15 fw700 text-right"><?php echo $value->fluggewicht_in_gramm ?> Gramm</td>
                                                </tr>
                                                <tr>
                                                    <td>Diagonale Größe:</td>
                                                    <td class="fs15 fw700 text-right"><?php echo $value->diagonale_groesse_in_mm ?> Millimeter</td>
                                                </tr>
                                                <tr>
                                                    <td>Maximale Flugzeit:</td>
                                                    <td class="fs15 fw700 text-right"><?php echo $value->flugzeit_in_min ?> Minuten</td>
                                                </tr>
                                                <tr>
                                                    <td>Maximale Flughöhe:</td>
                                                    <td class="fs15 fw700 text-right"><?php echo $value->maximale_flughoehe_in_m ?> Meter</td>
                                                </tr>
                                                <tr>
                                                    <td>Höchstgeschwindigkeit:</td>
                                                    <td class="fs15 fw700 text-right"><?php echo $value->hoechstgeschwindigkeit_in_kmh ?> km/h</td>
                                                </tr>
                                                </tbody>
                                            </table>


                                        </div>
                                    </div>
                                    <?php
                                    }
                                    ?>


                                <div id="editable" class="editable form-horizontal form-label-left"
                                     style="display: none;">
                                    <form class="needs-validation" name="edit_drone" id="edit_drone"
                                          action="<?php echo site_url("drone/edit_drone/{$current_id}") ?>"
                                          method="post" enctype="multipart/form-data">

                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="drohnen_modell">Drohnen-Modell</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="drohnen_modell" class="form-control"
                                                       id="drohnen_modell" required
                                                       value="<?php echo $value->drohnen_modell ?>">
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="fluggewicht_in_gramm">Fluggewicht in Gramm <span
                                                        class="text-muted"></span></label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="fluggewicht_in_gramm" class="form-control"
                                                       id="fluggewicht_in_gramm" required
                                                       value="<?php echo $value->fluggewicht_in_gramm ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="flugzeit_in_min">Flugzeit in Min <span
                                                        class="text-muted"></span></label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="flugzeit_in_min" class="form-control"
                                                       id="flugzeit_in_min" required
                                                       value="<?php echo $value->flugzeit_in_min ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="diagonale_groesse_in_mm">diagonale Groesse in mm<span
                                                        class="text-muted"></span></label>

                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="diagonale_groesse_in_mm" class="form-control"
                                                       id="diagonale_groesse_in_mm" required
                                                       value="<?php echo $value->diagonale_groesse_in_mm ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="maximale_flughoehe">maximale Flueghoehe in m<span
                                                        class="text-muted"></span></label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="maximale_flughoehe_in_m" class="form-control"
                                                       id="maximale_flughoehe_in_m" required
                                                       value="<?php echo $value->maximale_flughoehe_in_m ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="hoechstgeschwindigkeit_in_kmh">Hoechstgeschwindigkeit in kmh
                                                <span class="text-muted"></span></label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="hoechstgeschwindigkeit_in_kmh"
                                                       class="form-control" id="hoechstgeschwindigkeit_in_kmh" required
                                                       value="<?php echo $value->hoechstgeschwindigkeit_in_kmh ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                   for="bild">Bild</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12" >
                                                <input type="file" name="bild"
                                                       class="form-control-file btn" id="bild"
                                                       value="<?php echo $value->bild ?>">
                                            </div>
                                        </div>
                                        <div class="ln_solid"></div>
                                        <div class="form-group">
                                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                                <button type="submit" class="btn btn-success">Speichern</button>
                                                <button class="btn btn-default" type="reset">Leeren</button>
                                                <a class="btn btn-primary" href="#" onclick="edit();">Zurück</a>
                                            </div>
                                        </div>

                                    </form>

                                </div>

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
    function edit() {

        $(".uneditable").toggle();
        $(".editable").toggle();
    };

</script>
</body>
</html>