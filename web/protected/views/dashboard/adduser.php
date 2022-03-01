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
                        <h3>Pilot hinzufügen</h3>
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
                            <form id="demo-form2" data-parsley-validate action="<?php echo site_url("user/add_user") ?>" method="post" class="form-horizontal form-label-left" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="vorname">Vorname
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="vorname" name="vorname" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="nachname">Nachname
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="nachname" name="nachname" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="passwort">Passwort
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="password" id="passwort" name="passwort" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="email_adresse">E-Mail
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="email_adresse" name="email_adresse" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="studiengang">Studiengang
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="studiengang" name="studiengang" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group admin_only">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="aktivitaet">Aktivität
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12 checkbox">
                                        <label>
                                            <input type='hidden' value="1" id="aktivitaet" name="aktivitaet"/>
                                            <input type="checkbox" class="js-switch" id="aktivitaet" name="aktivitaet" value="0" /> Inaktiv
                                        </label>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="rolle">Rolle
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12 checkbox">
                                        <label>
                                            <input type='hidden' value="0" id="rolle" name="rolle"/>
                                            <input type="checkbox" class="js-switch" id="rolle" name="rolle" value="1"/> Admin
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
                                        <input type="file" name="profilbild" class="form-control-file btn" id="profilbild" accept="image/jpeg">
                                    </div>
                                </div>

                                <div class="ln_solid"></div>
                                <div class="form-group">
                                    <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                        <a class="btn btn-primary"  href="<?php echo site_url("user/index") ?>">Zurück</a>
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
