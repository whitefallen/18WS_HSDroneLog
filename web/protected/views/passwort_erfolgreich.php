<!DOCTYPE html>
<html lang="en">
<?php $this->load->view("template/head"); ?>

<body class="login">
<div>
    <a class="hiddenanchor" id="signup"></a>
    <a class="hiddenanchor" id="signin"></a>

    <div class="login_wrapper">
        <div class="animate form login_form">
            <section class="login_content">
                    <h1>HSDronelog</h1>
                    <div>
                        Ihr Passwort wird zurückgesetzt.<br>
                        Sie bekommen ihr neues Passwort per E-Mail zugeschickt.
                    </div>


                    <div class="clearfix"></div>

                    <div class="separator">
                        <p class="change_link">Schon einen Account?
                            <a href="<?php echo site_url("user/index") ?>" class="to_register"> zum Login </a>
                        </p>

                        <div class="clearfix"></div>
                        <br />

                        <div>
                            <h1><i class="fa fa-paw"></i> Gentelella Alela!</h1>
                            <p>©2016 All Rights Reserved. Gentelella Alela! is a Bootstrap 3 template. Privacy and Terms</p>
                        </div>
                    </div>
            </section>
        </div>
    </div>
</body>
</html>
