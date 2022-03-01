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
            <form action="<?php echo site_url("index/login_user") ?>" method="post">
              <h1>HSDronelog</h1>
                <?php if(!!$message){
                    echo "<div class='jumbotron'>
                                <h1>Anmeldung Fehlgeschlagen</h1>
                                <p>Passwort oder Email falsch eingegeben, versuche dich erneut einzuloggen.</p>
                            </div>";
                };?>
              <div>
                <input type="text" class="form-control" placeholder="E-Mail Addresse" name="email_adresse" required=""/>
              </div>
              <div>
                <input type="password" class="form-control" placeholder="Passwort" name="passwort" required="" />
              </div>
              <div>
                  <button class="btn btn-default" type="submit" >
                      Anmelden
                  </button>
                <a class="reset_pass" href="#">Passwort vergessen?</a>

              </div>


              <div class="clearfix"></div>

              <div class="separator">
                <p class="change_link">Noch keinen Account?
                  <a href="<?php echo site_url("index/register") ?>" class="to_register"> Account erstellen </a>
                </p>

                <div class="clearfix"></div>
                <br />

                <div>
                  <h1><i class="fa fa-paw"></i> Gentelella Alela!</h1>
                  <p>©2016 All Rights Reserved. Gentelella Alela! is a Bootstrap 3 template. Privacy and Terms</p>
                </div>
              </div>
            </form>
          </section>
        </div>
      </div>
    </div>
  <script>
      function loginfail() {
          new PNotify({
              title: 'Falsche Anmeldedaten',
              text: '',
              type: 'info',
              styling: 'bootstrap3',
              addclass: 'error'
          });
      }
  </script>
  </body>
</html>
