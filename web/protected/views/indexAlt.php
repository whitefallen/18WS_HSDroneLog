<!DOCTYPE html>
<html lang="en">
<head>
	<title>HSDronelog - Login</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="<?php echo base_url('/old_assests/images/drohne_quadrat.png')?>"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/bootstrap/css/bootstrap.min.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/fonts/font-awesome-4.7.0/css/font-awesome.min.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/fonts/Linearicons-Free-v1.0.0/icon-font.min.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/animate/animate.css')?>">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/css-hamburgers/hamburgers.min.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/animsition/css/animsition.min.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/select2/select2.min.css')?>">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/vendor/daterangepicker/daterangepicker.css')?>">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/css/util.css')?>">
	<link rel="stylesheet" type="text/css" href="<?php echo base_url('/old_assests/css/main.css')?>">
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100" style="background-image: url('<?php echo(base_url('/old_assests/images/background.jpg'))?>');">
			<div class="wrap-login100 p-t-30 p-b-50">
				<span class="login100-form-title p-b-41">
                    Huhu
                    <!--<img src="images/drohe_wei??.png" width="400px">-->
				</span>
				<form class="login100-form validate-form p-b-33 p-t-5" action="<?php echo site_url("index/login_user") ?>" method="post">

					<div class="wrap-input100 validate-input" data-validate = "E-Mail eingeben">
						<input class="input100" type="text" name="email_adresse" placeholder="E-Mail">
						<span class="focus-input100" data-placeholder="&#xe82a;"></span>
					</div>

					<div class="wrap-input100 validate-input" data-validate="Passwort eingeben">
						<input class="input100" type="password" name="passwort" placeholder="Passwort">
						<span class="focus-input100" data-placeholder="&#xe80f;"></span>
					</div>
                    

					<div class="container-login100-form-btn m-t-32">
						<button class="login100-form-btn" type="submit">
							Anmelden
						</button>
                        
                        
                       <!---- <button class="login100-form-btn">
							Registrieren
						</button>---->
					</div>
                    <div class="container-login100-form-btn m-t-32">
                        <div id="registrieren">
                            <a class="link" href="<?php echo site_url("index/register") ?>">Registrieren</a>
                        </div>
                        <div id="passwort">
                            <a class="link" href="<?php echo site_url("index/password_forgot") ?>">Passwort vergessen?</a>
                        </div>    
                    
                    </div>
                 
					

				</form>
			</div>
		</div>
	</div>
	

	<div id="dropDownSelect1"></div>
	
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/jquery/jquery-3.2.1.min.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/animsition/js/animsition.min.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/bootstrap/js/popper.js')?>"></script>
	<script src="<?php echo base_url('/old_assests/vendor/bootstrap/js/bootstrap.min.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/select2/select2.min.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/daterangepicker/moment.min.js')?>"></script>
	<script src="<?php echo base_url('/old_assests/vendor/daterangepicker/daterangepicker.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/vendor/countdowntime/countdowntime.js')?>"></script>
<!--===============================================================================================-->
	<script src="<?php echo base_url('/old_assests/js/main.js')?>"></script>

</body>
</html>