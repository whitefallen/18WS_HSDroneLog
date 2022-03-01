<!DOCTYPE html>
<html lang="en">
<head>
	<title>Registrieren</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="<?php echo base_url('/assets/images/drohne_quadrat.png')?>"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/bootstrap/css/bootstrap.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/fonts/font-awesome-4.7.0/css/font-awesome.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/fonts/Linearicons-Free-v1.0.0/icon-font.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/animate/animate.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/css-hamburgers/hamburgers.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/animsition/css/animsition.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/select2/select2.min.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/vendor/daterangepicker/daterangepicker.css')?>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/css/util.css')?>">
    <link rel="stylesheet" type="text/css" href="<?php echo base_url('/assets/css/main.css')?>">
    <!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100" style="background-image: url('<?php echo(base_url('/assets/images/background.jpg'))?>');">
			<div class="wrap-login100 p-t-30 p-b-50">
				<!----<span class="login100-form-title p-b-41">
                    <img src="images/drohe_weiß.png" width="400px">
				</span>--->
				<form class="login100-form validate-form p-b-33 p-t-5" action="<?php echo site_url("user/register_user") ?>" method="post" enctype="multipart/form-data" >

                    <div class="wrap-input100 validate-input" data-validate = "Vornamen eingeben">
						<input class="input100" type="text" name="vorname" placeholder="Vorname">
						<span class="focus-input100" data-placeholder="&#xe82a;"></span>
					</div>
                    <div class="wrap-input100 validate-input" data-validate = "Nachnamen eingeben">
						<input class="input100" type="text" name="nachname" placeholder="Nachname">
						<!---<span class="focus-input100" data-placeholder="&#xe82a;"></span>--->
					</div>
                    <div class="wrap-input100 validate-input" data-validate = "Studiengang eingeben">
						<input class="input100" type="text" name="studiengang" placeholder="Studiengang">
						<!---<span class="focus-input100" data-placeholder="&#xe82a;"></span>--->
					</div>
                    <div class="wrap-input100 validate-input" data-validate = "E-Mail eingeben">
						<input class="input100" type="text" name="email" placeholder="E-Mail">
						<!---<span class="focus-input100" data-placeholder="&#xe82a;"></span>--->
					</div>

                    <!-- <div class="wrap-input100">
                        <input class="input100" type="file" name="profilbild">
                        <span class="focus-input100" data-placeholder="&#xe82a;"></span>
                    </div> --->

					<div class="wrap-input100 validate-input" data-validate="Passwort eingeben">
						<input class="input100" type="password" name="passwort" placeholder="Passwort">
						<span class="focus-input100" data-placeholder="&#xe80f;"></span>
					</div>
                    <input type="hidden" name="rolle" value="0">

					<div class="container-login100-form-btn m-t-32">
						<button class="login100-form-btn" type="submit">
							Registrieren
						</button>

					</div>
                    <div class="container-login100-form-btn m-t-32">
                        <div id="registrieren">
                            <a class="link" href="<?php echo site_url("user/index") ?>">Zurück</a>
                        </div>
                    </div>
				</form>
			</div>
		</div>
	</div>
	

	<div id="dropDownSelect1"></div>

    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/jquery/jquery-3.2.1.min.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/animsition/js/animsition.min.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/bootstrap/js/popper.js')?>"></script>
    <script src="<?php echo base_url('/assets/vendor/bootstrap/js/bootstrap.min.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/select2/select2.min.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/daterangepicker/moment.min.js')?>"></script>
    <script src="<?php echo base_url('/assets/vendor/daterangepicker/daterangepicker.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/vendor/countdowntime/countdowntime.js')?>"></script>
    <!--===============================================================================================-->
    <script src="<?php echo base_url('/assets/js/main.js')?>"></script>

</body>
</html>