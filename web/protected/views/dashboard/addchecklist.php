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
                        <h3>Checkliste hinzufügen</h3>
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
                            <form id="demo-form2" data-parsley-validate action="<?php echo site_url("checklist/add_checklist") ?>" method="post" class="form-horizontal form-label-left">
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="bezeichnung">Bezeichnung
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="bezeichnung" name="bezeichnung" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="erklaerung">Erklärung
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="erklaerung" name="erklaerung" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="elements"> Elemente
                                    </label>

                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <ul class="to_do">
                                            <?php
                                            foreach($element_data as $element_key=>$element_value) {
                                                ?>
                                                <li>
                                                    <p>
                                                        <input type="checkbox" value="<?php echo $element_value->element_id; ?>" id="elements" name="elements[]" class="form-control flat col-md-7 col-xs-12"> <?php echo $element_value->bezeichnung; ?>
                                                    </p>
                                                </li>

                                                <?php
                                            }
                                            ?>
                                        </ul>
                                    </div>


                                    <!---<div class="col-md-6 col-sm-6 col-xs-12">
                                    <select multiple id="elements" name="elements[]" required="required" class="form-control col-md-12 col-xs-12">

                                        <?php
                                        foreach($element_data as $element_key=>$element_value) {

                                            if($element_value->selected) {
                                                echo "<option selected value='$element_value->element_id'>".$element_value->bezeichnung."</option>";
                                            } else {
                                                echo "<option value='$element_value->element_id'>".$element_value->bezeichnung."</option>";
                                            }
                                        }

                                        ?>

                                    </select>
                                    </div>--->

                                </div>

                                <div class="ln_solid"></div>
                                <div class="form-group">
                                    <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                        <a class="btn btn-primary"  href="<?php echo site_url("checklist/index") ?>">Zurück</a>
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
