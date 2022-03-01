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
                        <h3>Einstellungen</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-4 col-sm-4 col-xs-12 form-group pull-right ">



                            <a class="btn btn-success admin_only" href="<?php echo site_url("") ?>">Einstellungen</a>

                        </div>
                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>Mail</h2>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <p class="text-muted font-13 m-b-30">
                                </p>
                                <?php

                                foreach($email_data as $key=>$value) {
                                    ?>
                                <form id="demo-form2" data-parsley-validate action="<?php echo site_url("settings/add_email") ?>" method="post" class="form-horizontal form-label-left" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="email_adresse">E-Mail
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="email" id="email_adresse" name="email_adresse"  required="required" value="<?php echo $value->email_adresse?>" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="passwort">Passwort
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="password" id="passwort" name="passwort" required="required" value="<?php echo $value->passwort?>" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="smtp_server">STMP-Server
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="text" id="smtp_server" name="smtp_server" required="required"  value="<?php echo $value->smtp_server?>" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="port">Port
                                        </label>
                                        <div class="col-md-6 col-sm-6 col-xs-12">
                                            <input type="number" id="port" name="port" required="required" value="<?php echo $value->port?>" class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>

                                    <div class="ln_solid"></div>
                                    <div class="form-group">
                                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                            <button class="btn btn-default" type="reset">Leeren</button>
                                            <button type="submit" class="btn btn-success">Speichern</button>
                                        </div>
                                    </div>
                                </form>
                                <?php } ?>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>Messages</h2>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                            <p class="text-muted font-13 m-b-30">
                            </p>
                                <div class="table-responsive">
                            <table id="datatable" class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>Bezeichnung</th>
                                    <th>Nachricht</th>
                                    <th></th>

                                </tr>
                                </thead>


                                <tbody>
                                <?php
                                foreach($message_data as $key=>$value) {

                                    echo "<tr >";

                                    echo "<td>".$value->bezeichnung."</td>";
                                    echo "<td>" .$value->nachricht. "</td>";
                                    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='myfunction(\"$value->bezeichnung\");'><span  class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span></button></td>";


                                    echo "</tr>";

                                }
                                ?>

                                </tbody>
                            </table>
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
    function myfunction(id) {
        window.location = "../settings/show/"+id;
    }

</script>
<script>
    function enable(id) {
        console.log(id+"+icon")
        var input = document.getElementById(id);
        var button = document.getElementById(id+"+icon");
        if(input.disabled) {
            input.disabled = false;


             button.classList.remove('glyphicon-edit');
             button.classList.add('glyphicon-save');
        }
        else {
            input.disabled = true;
            button.classList.remove('glyphicon-save');
            button.classList.add('glyphicon-edit');
        }
    }
</script>

</body>
</html>
