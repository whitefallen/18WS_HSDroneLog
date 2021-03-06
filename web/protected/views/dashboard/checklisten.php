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
                        <h3>Checklisten</h3>
                    </div>

                    <div class="title_right">
                        <div class="col-md-7 col-sm-7 col-xs-12 form-group pull-right ">
                            <div class="input-group">

                                <span class="input-group-btn">



                    </span>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">

                        <!-- <div class="x_title">


                             <div class="clearfix"></div>
                         </div>--><div class="col-md-6 col-sm-6 col-xs-12">
                        <div class="x_panel">
                        <div class="x_content">
                            <div class="x_title">
                                <h2>Checkliste</h2>
                                <ul class="nav navbar-right panel_toolbox">

                                        <a class="btn btn-success" href="<?php echo site_url("checklist/add_checklist"); ?>">Checkliste hinzufügen</a>

                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="table-responsive">
                                <p class="text-muted font-13 m-b-30">

                                </p>
                            <table id="datatable_checkliste" class="table table-striped table-bordered ">
                                <thead>
                                <tr>

                                    <th>Name</th>

                                    <th>Beschreibung</th>
                                    <th></th>
                                </tr>
                                </thead>


                                <tbody>

                                 <?php
                foreach($checklist_data as $key=>$value) {
                    echo "<tr >";

                    echo "<td>".$value->bezeichnung."</td>";
                    echo "<td>".$value->erklaerung."</td>";
                    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='myfunction($value->checkliste_name_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

                    echo "</tr>";
                }
                ?>
                                </tbody>
                            </table>
                            </div>
                            <button type="button" class="btn btn-default pull-right" id="lademehr_checkliste" ><span class="glyphicon-plus glyphicon"aria-hidden="true"></span> mehr Laden</button>

                        </div>
                        </div>
                        </div>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                            <div class="x_panel">
                                <div class="x_content">
                                    <div class="x_title">
                                        <h2>Elemente</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                                <a class="btn btn-success" href="<?php echo site_url("checklistelement/add_element"); ?>">Element hinzufügen</a>

                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <p class="text-muted font-13 m-b-30">

                                    </p>
                                    <div class="table-responsive">
                            <table id="datatable_element" class="table table-striped table-bordered ">
                                <thead>
                                <tr>

                                    <th>Name</th>

                                    <th>Beschreibung</th>
                                    <th></th>
                                </tr>
                                </thead>


                                <tbody>

                                <?php
                                foreach($element_data as $key=>$value) {
                                    echo "<tr >";

                                    echo "<td>".$value->bezeichnung."</td>";
                                    echo "<td>".$value->erklaerung."</td>";
                                    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='elementdetail($value->element_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

                                    echo "</tr>";
                                }
                                ?>
                                </tbody>
                            </table>

                            </div>
                                    <button type="button" class="btn btn-default pull-right" id="lademehr_element" ><span class="glyphicon-plus glyphicon"aria-hidden="true"></span> mehr Laden</button>

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
        window.location = "show/"+id;
    }
    function elementdetail(id) {
        window.location = "../checklistelement/show/"+id;
    }
</script>
<script>
    let variable_offset = 20;
    $("#lademehr_checkliste").click(function(){
        $.post("<?php echo site_url('checklist/index');?>", {offset: variable_offset, append: true}, function(result){
            console.log(result);
            if(!result){
                new PNotify({
                    title: 'keine weiteren Checklisten',
                    text: '',
                    type: 'info',
                    styling: 'bootstrap3',
                    addclass: 'dark'
                });
            }
        }).done(function(response){
            $('#datatable_checkliste > tbody:last-child').append(response);
        });
        variable_offset = variable_offset +20
    });
</script>
<script>
    let variable_offset = 20;
    $("#lademehr_element").click(function(){
        $.post("<?php echo site_url('checklist/index');?>", {offset: variable_offset, append: true}, function(result){
            console.log(result);
            if(!result){
                new PNotify({
                    title: 'keine weiteren Elemente',
                    text: '',
                    type: 'info',
                    styling: 'bootstrap3',
                    addclass: 'dark'
                });
            }
        }).done(function(response){
            $('#datatable_element > tbody:last-child').append(response);
        });
        variable_offset = variable_offset +20
    });
</script>
</body>
</html>
