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
                        <h3>Akku</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-13 form-group pull-right ">
                            <div class="input-group">

                                <span class="input-group-btn">
                      <div class="row uneditable" style="display: block;">
                            <a class="btn btn-success admin_only" href="#" onclick="edit();">Editieren</a>
                            <button type="button" class="btn btn-danger admin_only" data-toggle="modal" data-target=".bs-example-modal-sm">Löschen</button>
                            <a class="btn btn-primary" href="<?php echo site_url("akku/index"); ?>">Zurück</a>

                        </div>
                        <div class="row editable" style="display: none;">
                            <button class="btn btn-success" type="submit" form="edit_akku">Speichern</button>
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
                                        <a class="btn btn-danger" href="<?php echo site_url("akku/delete_akku/{$current_id}"); ?>">Löschen</a>
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
                                foreach($akku_data as $key=>$value) {
                                ?>
                                <h2><?php echo $value->bezeichnung;?></h2>

                                <div class="clearfix"></div>
                            </div>
                            <p class="text-muted font-13 m-b-30">

                            </p>
                            <div id="uneditable" class="col-sm-10 col-md-10 col-lg-10 uneditable" style="display: block;">




                                    <div class="row">
                                        <div class="col-md-3 ">
                                            <span class=details-bezeichner>Anzahl:</span>
                                        </div>
                                        <div class="col-md-6 ">
                                            <p><?php echo $value->anzahl ?></p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3 ">
                                            <span class=details-bezeichner>Drohnen:</span>
                                        </div>
                                        <div class="col-md-6 ">
                                            <table class="countries_list">
                                                <tbody>
                                            <?php
                                            foreach($drone_data as $drone_key=>$drone_value) {

                                                if($drone_value->selected) {
                                                    echo "<tr><td>";
                                                    echo $drone_value->drohnen_modell;
                                                    echo "</td></tr>";
                                                }


                                            }
                                            ?>
                                                    </tbody>
                                                </table>
                                        </div>
                                    </div>


                                    <?php
                                }
                                ?>
                            </div>

                            <div id="editable demo-form2" style="display: none;" class=" editable form-horizontal form-label-left">
                                <form class="needs-validation" name="edit_akku" id="edit_akku" action="<?php echo site_url("akku/edit_akku/{$current_id}") ?>" method="post" >
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="bezeichnung">Bezeichnung</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="text" name="bezeichnung" class="form-control" id="bezeichnung" required value="<?php echo $value->bezeichnung ?>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="anzahl">Anzahl</label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="number" name="anzahl" class="form-control" id="anzahl" required value="<?php echo $value->anzahl ?>">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="drohne"> Drohne
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">


                                                <ul class="to_do">
                                                    <?php
                                                    foreach($drone_data as $drone_key=>$drone_value) {
                                                        ?>
                                                        <li>
                                                            <p>
                                                                <input type="checkbox" value="<?php echo $drone_value->drohne_id; ?>" id="drohne" name="drohnen[]" class="form-control flat col-md-7 col-xs-12" <?php if($drone_value->selected){ echo "checked";}?>> <?php echo $drone_value->drohnen_modell; ?>
                                                            </p>
                                                        </li>

                                                        <?php
                                                    }
                                                    ?>
                                                </ul>

                                        </div>
                                    </div>
                                    <div class="ln_solid"></div>
                                    <div class="form-group">
                                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                            <button type="submit" class="btn btn-success">Speichern</button>
                                            <button class="btn btn-default" type="reset">Zurücksetzen</button>
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
