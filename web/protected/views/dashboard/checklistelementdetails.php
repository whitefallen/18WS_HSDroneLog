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
                        <h3>Checklisten Element</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-13 form-group pull-right ">
                            <div class="input-group">

                                <span class="input-group-btn">
                      <div class="row uneditable" style="display: block;">
                        <a class="btn btn-success" href="#" onclick="edit();">Editieren</a>
                          <button type="button" class="btn btn-danger admin_only" data-toggle="modal" data-target=".bs-example-modal-sm">Löschen</button>

                        <a class="btn btn-primary" href="<?php echo site_url("checklist/index"); ?>">Zurück</a>
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
                                        <a class="btn btn-danger" href="<?php echo site_url("checklistelement/delete_element/{$current_id}"); ?>">Löschen</a>
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
                                    foreach($element_data as $key=>$value) {
                                    ?>
                                    <h2><span class="glyphicon glyphicon-check" aria-hidden="true"></span> <?php echo $value->bezeichnung ?></h2>

                                    <div class="clearfix"></div>
                                </div>
                                <p class="text-muted font-13 m-b-30">

                                </p>
                                <div id="uneditable" class="row uneditable" style="display: block;">
                                    <div class="col-md-12 col-sm-12 col-xs-12">
                                        <div class="row">
                                            <div class="col-md-4 col-sm-4 col-xs-12">
                                                <div class="x_title">
                                                    <h3>Beschreibung</h3>
                                                    <div class="clearfix"></div>
                                                </div>
                                                <div class="x_content">
                                                    <blockquote>
                                                    <p><?php echo $value->erklaerung ?></p>
                                                        </blockquote>
                                                </div>
                                            </div>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <div class="x_title">
                                                    <h3>Befindet sich in Checklisten</h3>
                                                    <div class="clearfix"></div>
                                                </div>
                                                <div class="x_content">


                                                        <div class="accordion" id="accordion" role="tablist"
                                                             aria-multiselectable="true">
                                                        <?php
                                                        $first = true;
                                                        foreach($checklist_data as $checklist_key=>$checklist_value) {
                                                            if ($checklist_value->selected) {
                                                                if($first) {
                                                                    ?>

                                                                        <div class="panel">
                                                                            <a class="panel-heading"
                                                                               role="tab"
                                                                               id="heading<?php echo $checklist_value->checkliste_name_id ?>"
                                                                               data-toggle="collapse"
                                                                               data-parent="#accordion"
                                                                               href="#collapse<?php echo $checklist_value->checkliste_name_id ?>"
                                                                               aria-expanded="true"
                                                                               aria-controls="collapse<?php echo $checklist_value->checkliste_name_id ?>">
                                                                                <h4 class="panel-title"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> <?php echo $checklist_value->bezeichnung ?></h4>
                                                                            </a>
                                                                            <div id="collapse<?php echo $checklist_value->checkliste_name_id ?>"
                                                                                 class="panel-collapse collapse in"
                                                                                 role="tabpanel"
                                                                                 aria-labelledby="heading<?php echo $checklist_value->checkliste_name_id ?>">
                                                                                <div class="panel-body">
                                                                                    <p><strong>Beschreibung</strong>
                                                                                    </p>
                                                                                    <?php echo $checklist_value->erklaerung ?>
                                                                                    <div class="pull-right"><button class="btn btn-default" onclick='myfunction(<?php echo $checklist_value->checkliste_name_id ?> );'><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></button></div>
                                                                                </div>
                                                                            </div>
                                                                        </div>


                                                                    <?php $first = false;
                                                                }else{
                                                                    ?>

                                                            <div class="panel">
                                                                <a class="panel-heading collapsed" role="tab" id="heading<?php echo $checklist_value->checkliste_name_id?>" data-toggle="collapse" data-parent="#accordion" href="#collapse<?php echo $checklist_value->checkliste_name_id?>" aria-expanded="false" aria-controls="collapse<?php echo $checklist_value->checkliste_name_id?>">
                                                                    <h4 class="panel-title"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> <?php echo $checklist_value->bezeichnung ?></h4>
                                                                </a>
                                                                <div id="collapse<?php echo $checklist_value->checkliste_name_id?>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading<?php echo $checklist_value->checkliste_name_id?>">
                                                                    <div class="panel-body">
                                                                        <p><strong>Beschreibung</strong>
                                                                        </p>
                                                                        <?php echo $checklist_value->erklaerung?>
                                                                        <div class="pull-right"><button class="btn btn-default" onclick='myfunction(<?php echo $checklist_value->checkliste_name_id ?> );'><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></button></div>
                                                                    </div>
                                                                </div>
                                                            </div>


                                                            <?php
                                                            }}
                                                        }
                                                        ?>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>


                                    <?php
                                    }
                                    ?>
                                </div>

                                <div id="editable demo-form2" style="display: none;" class="editable form-horizontal form-label-left">
                                    <form class="needs-validation" name="edit_element" id="edit_element" action="<?php echo site_url("checklistelement/edit_element/{$current_id}") ?>" method="post" >

                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="bezeichnung">Bezeichnung</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="bezeichnung" class="form-control" id="bezeichnung" required value="<?php echo $value->bezeichnung ?>">
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="erklaerung">Erklaerung</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="erklaerung" class="form-control" id="erklaerung" required value="<?php echo $value->erklaerung ?>">
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="checklist"> Checklisten
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <ul class="to_do">
                                                    <?php
                                                    foreach($checklist_data as $checklist_key=>$checklist_value) {
                                                        ?>
                                                        <li>
                                                            <p>
                                                                <input type="checkbox" value="<?php echo $checklist_value->checkliste_name_id; ?>" id="checklist" name="checklists[]" class="form-control flat col-md-7 col-xs-12" <?php if($checklist_value->selected){ echo "checked";}?>> <?php echo $checklist_value->bezeichnung; ?>
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
    function myfunction(id) {
        window.location = "../../checklist/show/"+id;
    }
</script>
</body>
</html>
