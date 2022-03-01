<?php if (!isset($this->session->users['email_adresse'])) {
    redirect(base_url());
} ?>
<html lang="en">
<?php $this->load->view("template/headFlugdetails"); ?>

<body class="nav-md" onload=drawmap();>
<div class="container body">
    <div class="main_container">
        <?php $this->load->view("template/sidebar"); ?>

        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>Flug</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-13 form-group pull-right ">
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <div class="row uneditable" style="display: block;">
                                        <a class="btn btn-success" href="#" onclick="edit();">Editieren</a>
                                        <button type="button" class="btn btn-danger" data-toggle="modal" data-target=".bs-example-modal-sm">Löschen</button>
                                        <a class="btn btn-primary" href="<?php echo site_url("flight/index"); ?>">Zurück</a>
                                    </div>

                                    <div class="row editable" style="display: none;">
                                        <button class="btn btn-success" type="submit" form="edit_flight">Speichern</button>
                                        <a class="btn btn-primary" onclick="edit()">Zurück</a>
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
                                        <a class="btn btn-danger" href="<?php echo site_url("flight/delete_flight/{$current_id}"); ?>">Löschen</a>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="clearfix"></div>

                <?php
                foreach($flight_data as $key=>$value) {
                ?>

                <div id="uneditable" class="row uneditable">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel">
                            <div class="x_content">
                                <div class="x_title">
                                    <h2><?php echo $value->flugbezeichnung ?></h2>
                                    <div class="clearfix"></div>
                                </div>
                                <div class="row">
                                    <div class="col-md-4 col-sm-4 col-xs-12">
                                        <div class="row">
                                            <div class="col-md-12 col-sm-12 col-xs-12">
                                                <div class="x_title">
                                                    <h3>Flugdetails</h3>
                                                    <div class="clearfix"></div>
                                                </div>
                                                <div class="x_content">
                                                    <table class="countries_list">
                                                        <tr>
                                                            <td>Einsatzort:</td>
                                                            <td><?php echo $value->einsatzort_name ?></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Längengrad:</td>
                                                            <td><?php echo $value->laengengrad ?></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Breitengrad:</td>
                                                            <td><?php echo $value->breitengrad ?></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Flugdatum:</td>
                                                            <td><?php echo $value->flugdatum ?></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Einsatzbeginn:</td>
                                                            <td><?php echo $value->einsatzbeginn ?> Uhr</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Einsatzende:</td>
                                                            <td><?php echo $value->einsatzende ?> Uhr</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Flugdauer:</td>
                                                            <td><?php echo ($value->flugdauer / 60) ?> min</td>
                                                        </tr>
                                                        <!--<tr>
                                                            <td>Anzahl Starts und Landungen:</td>
                                                            <td><?php echo $value->start_und_landungen ?></td>
                                                        </tr>-->
                                                        <tr>
                                                            <td>Besondere Vorkommnisse:</td>
                                                            <td><?php echo $value->besondere_vorkommnisse ?></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">


                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <!----<div class="x_panel">--->
                                    <div class="x_content">
                                        <form action="<?php echo site_url("checklist/checklist_state/".$current_id) ?>" method="post" id="checklist_form" name="checklist_form">

                                        <div class="x_title">
                                            <h2>Checkliste</h2>
                                            <ul class="nav navbar-right panel_toolbox">
                                                <li><button class="btn-sm btn-success" type="submit">Speichern</button>
                                                </li>
                                            </ul>
                                            <div class="clearfix"></div>
                                        </div>
                                        <ul class="to_do">

                                            <?php
                                            foreach($element_data as $element_key=>$element_value) {
                                                ?>
                                            <li>
                                                <p>
                                                    <input type="hidden" name="elements[<?php echo $element_value->element_id?>][wert]" value="0">
                                                    <input type='checkbox' class='flat' name='elements[<?php echo $element_value->element_id?>][wert]' value="1" <?php if ($element_value->angekreuzt) echo "checked"?>>
                                                    <?php echo $element_value->bezeichnung?>
                                                    <input type='text' class="form-control flugdetailsCheckliste" name='elements[<?php echo $element_value->element_id?>][kommentar]' value="<?php echo $element_value->kommentar?>">
                                                </p>
                                            </li>
                                            <?php
                                            }
                                            ?>
                                        </ul>

                                        </form>

                                    </div>
                                <!---</div>--->
                            </div>







                        </div>
                    </div>


                    <div class="col-md-3 col-sm-3 col-xs-12">
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <!---<div class="x_panel tile fixed_height_80">--->
                                    <div class="x_title">
                                        <h3>Drohne</h3>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <ul class="list-unstyled  scroll-view">
                                            <li class="media event">
                                                <div class="img-circle profilePicLongestFlight pull-left profile_thumb border-aero" style="background-image: url(<?php echo $value->bild ?>)">
                                                </div>
                                                <div class="media-body">
                                                    <!---<a class="title longestFlightTitle"><?php echo $value->drohnenmodell ?> <?php echo $value->nachname ?>
                                                           </a><p><strong>Flugzeit vergangener Woche: </strong></p>
                                                           <p> <small>In</small>
                                                           </p>--->
                                                    <a class="title longestFlightTitle"><?php echo $value->drohnen_modell ?></a>
                                                </div>
                                            </li>
                                        </ul>
                                    <!---</div>-->
                                </div>
                            </div>
                        </div>



                            <div class="col-md-12 col-sm-12 col-xs-12">
                                    <div class="x_title">
                                        <h3>Pilot</h3>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <ul class="list-unstyled  scroll-view">
                                            <li class="media event">
                                                <div class="img-circle profilePicLongestFlight pull-left profile_thumb border-aero" style="background-image: url( <?php echo $value->profilbild ?> )">
                                                </div>
                                                <div class="media-body">
                                                    <a class="title longestFlightTitle"><?php echo $value->vorname ?> <?php echo $value->nachname ?>
                                                    <!---<h3><?php echo $value->vorname ?> <?php echo $value->nachname ?></h3>-->
                                                </div>
                                            </li>
                                        </ul>
                                </div>

                            </div>




                        </div>



                                    <div class="col-md-5 col-sm-5 col-xs-12">



                                        <div class="row">

                                        <div class="col-md-12 col-sm-12 col-xs-12">
                                            <div class="x_panel">
                                                <div class="x_content">

                                                    <div id="map" style="height: 55%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        </div>
                                        <div class="row">

                                        <div class="col-md-12 col-sm-12 col-xs-12">
                                            <div class="x_title">
                                                <h3>Fluglog hochladen</h3>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div class="x_content">
                                                <ul class="list-unstyled  scroll-view">
                                                    <li class="media event">
                                                        <div class="media-body">
                                                            <form id="demo-form2" data-parsley-validate action="<?php echo site_url("flight/upload_log/".$current_id) ?>" method="post" class="form-horizontal form-label-left" enctype="multipart/form-data">
                                                            <input type="file" name="log" class="form-control-file btn" id="log" accept="">

                                                                <hr>

                                                            <button class="btn btn-default" type="submit">Hochladen</button>
                                                                <a href="<?php echo site_url("flight/show_flight_log/".$current_id) ?>">Fluglog anzeigen</a>
                                                            </form>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>

                                        </div>

                                        </div>

                    </div>







                </div>



                </div>
                </div>

                        </div>
                    </div>



            <?php
            }
            ?>

                <div class="row editable" id="editable" style="display: none;">
                    <div class="col-md-12 col-sm-12 col-xs-12">

                        <div class="x_panel">
                            <div class="x_content">
                                <div class="x_title">
                                    <?php
                                    foreach($flight_data as $key=>$value) {
                                    ?>
                                    <h2><?php echo $value->flugbezeichnung ?></h2>
                                    <div class="clearfix"></div>
                                </div>



                                <div id=" demo-form2"    class="  form-horizontal form-label-left">
                                    <form class="needs-validation" name="edit_flight" id="edit_flight" action="<?php echo site_url("flight/edit_flight/{$current_id}") ?>" method="post" >
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="flugbezeichnung" >Flugname
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" id="flugbezeichnung" name="flugbezeichnung" required="required" value="<?php echo $value->flugbezeichnung ?>" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>
                                        <div class="form-group admin_only">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="pilot_id">Pilot</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <select id="pilot_id" name="pilot_id" class="form-control col-md-7 col-xs-12" required>

                                                    <?php
                                                    foreach($users as $user_key=>$user_value) {
                                                        if($flight_data[0]->pilot_id == $user_value->pilot_id) {
                                                            echo "<option selected value='$user_value->pilot_id'>".$user_value->vorname." ".$user_value->nachname."</option>";
                                                        } else {
                                                            echo "<option value='$user_value->pilot_id'>".$user_value->vorname." ".$user_value->nachname."</option>";
                                                        }
                                                    }

                                                    ?>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="einsatzort_name" >Einsatzort
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" id="einsatzort_name" name="einsatzort_name" required="required" value="<?php echo $value->einsatzort_name ?>" class="form-control col-md-7 col-xs-12">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="flugdatum" class="control-label col-md-3 col-sm-3 col-xs-12" >Flugdatum</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input id="flugdatum" class="form-control col-md-7 col-xs-12" type="date" name="flugdatum" value="<?php echo $value->flugdatum ?>" required="required">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="einsatzbeginn" class="control-label col-md-3 col-sm-3 col-xs-12" >Einsatzbeginn</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input id="einsatzbeginn" class="form-control col-md-7 col-xs-12" type="time" value="<?php echo $value->einsatzbeginn ?>" name="einsatzbeginn" required="required">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="einsatzende" class="control-label col-md-3 col-sm-3 col-xs-12">Einsatzende</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input id="einsatzende" class="form-control col-md-7 col-xs-12" type="time" value="<?php echo $value->einsatzende ?>" name="einsatzende" required="required">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="drohnen_modell">Drohne
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">

                                                <select id="drohnen_modell" name="drohne_id" class="form-control col-md-7 col-xs-12" required>
                                                    <?php
                                                    foreach($drones as $drone_key=>$drone_value) {
                                                        if($flight_data[0]->drohne_id == $drone_value->drohne_id) {
                                                            echo "<option selected value='$drone_value->drohne_id'>".$drone_value->drohnen_modell."</option>";
                                                        } else {
                                                            echo "<option value='$drone_value->drohne_id'>".$drone_value->drohnen_modell."</option>";
                                                        }
                                                    }

                                                    ?>
                                                </select>
                                            </div>

                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="drohnen_modell">checkliste
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">

                                                <select id="checkliste" name="checkliste_name_id" class="form-control col-md-7 col-xs-12">
                                                    <?php
                                                    foreach($checklist_data as $checkliste_key=>$checkliste_value) {
                                                        if($flight_data[0]->checkliste_name_id == $checkliste_value->checkliste_name_id) {
                                                            echo "<option selected value='$checkliste_value->checkliste_name_id'>".$checkliste_value->bezeichnung."</option>";
                                                        } else {
                                                            echo "<option value='$checkliste_value->checkliste_name_id'>".$checkliste_value->bezeichnung."</option>";
                                                        }
                                                    }

                                                    ?>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="besondere_vorkommnisse" class="control-label col-md-3 col-sm-3 col-xs-12">Besondere Vorkommnisse</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <textarea id="besondere_vorkommnisse" class="form-control col-md-7 col-xs-12"   name="besondere_vorkommnisse" ><?php echo $value->besondere_vorkommnisse ?></textarea>
                                            </div>
                                        </div>

                                        <div class="ln_solid"></div>
                                        <div class="form-group">
                                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                                <button type="button" id="formbutton" onclick="oof()" class="btn btn-success">Speichern</button>
                                                <button class="btn btn-default" type="reset">Leeren</button>
                                                <a class="btn btn-primary" href="#" onclick="edit();">Zurück</a>
                                            </div>
                                        </div>
                                        <?php } ?>









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
<script>
    $("#einsatzende").keyup(function(){
        var einsatzende = $("#einsatzende").val();
        var einsatzstart = $("#einsatzbeginn").val();
        var flugdatum = $("#flugdatum").val();
        $.post("<?php echo site_url('flight/requestAvailableDrones');?>", {startzeit: einsatzstart, endzeit: einsatzende, datum: flugdatum}, function(result){
            console.log(result);
            var $dropdown = $("#drohnen_modell");
            $('option', $dropdown).remove();
            $.each(result, function() {
                $dropdown.append($("<option />").val(this.drohne_id).text(this.drohnen_modell));
            });
        });
    });
</script>
<script>
    function oof(){
        let einsatzbeginnn = document.getElementById("einsatzbeginn");
        let einsatzende = document.getElementById("einsatzende");
        if(einsatzbeginnn.value < einsatzende.value){
            document.getElementById("formbutton").type = "submit";
        } else{
            document.getElementById("formbutton").type = "button";

            new PNotify({
                title: 'Gib eine Gültige Uhrzeit ein',
                text: '',
                type: 'info',
                styling: 'bootstrap3',
                addclass: 'dark'
            });

        }


    }
</script>
</body>
</html>
