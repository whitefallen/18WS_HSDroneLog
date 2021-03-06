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
                        <h3>Profil</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-12 form-group pull-right ">
                            <div class="input-group">
                                <span class="input-group-btn">
                                    <div class="row uneditable" style="display: block;">
                                        <a class="btn btn-success" href="#" onclick="edit();">Editieren</a>
                                        <!--<a class="btn btn-danger" href="<?php echo site_url("user/delete_user/{$current_id}"); ?>">Löschen</a>-->
                                        <button type="button" class="btn btn-danger admin_only" data-toggle="modal" data-target=".bs-example-modal-sm">Löschen</button>


                                        <a class="btn btn-primary"
                                           href="<?php echo site_url("user/index"); ?>">Zurück</a>

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
                                        <a class="btn btn-danger" href="<?php echo site_url("user/delete_user/{$current_id}"); ?>">Löschen</a>
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

                                <?php
                                foreach ($user_data

                                         as $key => $value) {
                                    ?>


                                <div id="uneditable" class="col-sm-12 col-md-12 col-lg-12 uneditable"
                                     style="display: block;">

                                    <div class="col-md-3 col-sm-3 col-xs-12 ">
                                        <p class="text-muted font-13 m-b-30">

                                        </p>
                                        <div class="profile_img">
                                            <div id="crop-avatar">
                                                <!-- Current avatar -->
                                                <img class="img-responsive profile_img" src="<?php echo $value->profilbild?>" alt="Profilbild">
                                                <!--<div class="img-circle profilePicLongestFlight pull-left profile_thumb border-aero" style="background-image: url( <?php echo $this->session->users['profilbild'] ?> )">
                                                </div>--->
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-9 col-sm-9 col-xs-12 ">
                                        <div class="x_title">

                                            <h2><?php echo $value->vorname ?> <?php echo $value->nachname ?></h2>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="col-md-7 col-sm-7 col-xs-10">
                                        <table class="countries_list">
                                            <tr>
                                                <td>Studiengang:</td>
                                                <td><?php echo $value->studiengang?></td>
                                            </tr>
                                            <tr>
                                                <td>E-Mail Addresse:</td>
                                                <td><?php echo $value->email_adresse?></td>
                                            </tr>
                                            <tr>
                                                <td>Rolle:</td>
                                                <td><?php
                                                    if($value->rolle == 1)
                                                        echo "Admin";
                                                    else
                                                        echo "Pilot"?></td>
                                            </tr>
                                            <tr>
                                                <td>Aktivität:</td>
                                                <td>
                                                    <?php
                                                        if($value->aktivitaet == 1)
                                                            echo "Aktiv";
                                                        else
                                                            echo "Inaktiv";
                                                        ?>

                                                </td>
                                            </tr>

                                        </table>
                                        </div>




                                    </div>
                                </div>
                                <?php
                                }
                                ?>


                                <div id="editable" class="editable form-horizontal form-label-left"
                                     style="display: none;">
                                    <div class="x_title">

                                        <h2><?php echo $value->vorname ?> <?php echo $value->nachname ?></h2>
                                        <div class="clearfix"></div>
                                    </div>
                                    <form class="needs-validation" name="edit_drone" id="edit_drone"
                                          action="<?php echo site_url("user/edit_user/{$current_id}") ?>"
                                          method="post" enctype="multipart/form-data">

                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="vorname">Vorname</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="vorname" class="form-control" id="vorname" required value="<?php echo $value->vorname ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="nachname">Nachname </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="nachname" class="form-control" id="nachname" required value="<?php echo $value->nachname ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="email_adresse">eMail</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="email" name="email_adresse" class="form-control" id="email_adresse" required value="<?php echo $value->email_adresse ?>">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="passwort">Passwort</label>

                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="password" name="passwort" class="form-control" id="passwort">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="studiengang">Studiengang</label>

                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="studiengang" class="form-control" id="studiengang" required value="<?php echo $value->studiengang ?>">
                                            </div>
                                        </div>
                                        <div class="form-group admin_only">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="aktivitaet">Aktivität
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12 checkbox">
                                                <label>
                                                    <input type='hidden' value="1" id="aktivitaet" name="aktivitaet"/>
                                                    <input type="checkbox" class="js-switch" id="aktivitaet" name="aktivitaet" value="0" <?php if($value->aktivitaet == 0){echo "checked";} ?>/> Inaktiv
                                                </label>
                                                <!--<select id="rolle" name="rolle" class="form-control col-md-7 col-xs-12" >
                                                    <option value="1">Admin</option>
                                                    <option selected value="0">Pilot</option>

                                                </select>-->
                                            </div>
                                        </div>
                                        <div class="form-group admin_only">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="rolle">Rolle
                                            </label>
                                            <div class="col-md-6 col-sm-6 col-xs-12 checkbox">
                                                <label>
                                                    <input type='hidden' value="0" id="rolle" name="rolle"/>
                                                    <input type="checkbox" class="js-switch" id="rolle" name="rolle" value="1" <?php if($value->rolle == 1){echo "checked";} ?>/> Admin
                                                </label>
                                                <!--<select id="rolle" name="rolle" class="form-control col-md-7 col-xs-12" >
                                                    <option value="1">Admin</option>
                                                    <option selected value="0">Pilot</option>

                                                </select>-->
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="profilbild">Profilbild</label>

                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="file" name="profilbild" class="form-control-file btn" id="profilbild" accept="image/jpeg" value="<?php echo $value->profilbild ?>">
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
