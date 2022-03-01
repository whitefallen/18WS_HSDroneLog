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
                        <h3>Nachricht</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-6 col-sm-6 col-xs-12 form-group pull-right ">
                            <div class="input-group">

                                <span class="input-group-btn">

                        <div class="row" >
                            <button class="btn btn-success" type="submit" form="edit_akku">Speichern</button>
                            <a class="btn btn-primary" href="<?php echo site_url("dashboard/settings") ?>">Zurück</a>
                        </div>
                                </span>
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
                                    foreach($message_data as $key=>$value) {
                                    ?>
                                    <h2><?php echo "".$value->bezeichnung."";?></h2>

                                    <div class="clearfix"></div>
                                </div>
                                <p class="text-muted font-13 m-b-30">

                                </p>


                                <div id="demo-form2"  class="form-horizontal form-label-left">
                                    <form class="needs-validation" name="edit_message" id="edit_message" action="<?php echo site_url("message/edit_message/{$current_id}") ?>" method="post" >
                                        <div class="form-group">
                                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="nachricht">Nachricht</label>
                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                <input type="text" name="nachricht" class="form-control" id="nachricht" required value="<?php echo $value->nachricht ?>">
                                            </div>
                                        </div>


                                        <div class="ln_solid"></div>
                                        <div class="form-group">
                                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                                <button type="submit" class="btn btn-success">Speichern</button>
                                                <button class="btn btn-default" type="reset">Zurücksetzen</button>
                                                <a class="btn btn-primary" href="<?php echo site_url("dashboard/settings") ?>">Zurück</a>
                                            </div>
                                        </div>


                                    </form>
                                    <?php } ?>
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