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
                        <h3>Akku hinzufügen</h3>
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
                            <form id="demo-form2" data-parsley-validate action="<?php echo site_url("akku/add_akku") ?>" method="post" class="form-horizontal form-label-left">
                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="bezeichnung">Bezeichnung
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="bezeichnung" name="bezeichnung" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" for="anzahl"> Anzahl
                                    </label>
                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                        <input type="text" id="anzahl" name="anzahl" required="required" class="form-control col-md-7 col-xs-12">
                                    </div>
                                </div>
                                <select multiple name="drohnen[]">
                                    <?php
                                    foreach($drone_data as $key=>$value) {
                                        ?><option value="<?php echo $value->drohne_id; ?>"><?php echo $value->drohnen_modell; ?></option> <?php
                                    }
                                    ?>
                                </select>

                                <div class="ln_solid"></div>
                                <div class="form-group">
                                    <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                        <a class="btn btn-primary"  href="<?php echo site_url("akku/index") ?>">Cancel</a>
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

</script>
</body>
</html>
