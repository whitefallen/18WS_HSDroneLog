<?php
/**
 * Created by PhpStorm.
 * User: tkamm
 * Date: 09.11.2018
 * Time: 13:07
 */
?>
<html lang="de">
    <head>
        <title>Konto Verifizierung für <?php echo "".$nachname.",";  echo $vorname; ?>.</title>
    </head>
    <body>
        <main>
            <section>
                <div>
                    Das Konto: <?php echo $email_adresse; ?>
                </div>
                <div>
                    Kontoinformation: <br>
                    <ul>
                        <li>
                            Vorname : <?php echo $vorname; ?>
                        </li>

                        <li>
                            Nachname : <?php echo $nachname; ?>
                        </li>

                        <li>
                            Email : <?php echo $email_adresse; ?>
                        </li>

                        <li>
                            Studiengang : <?php echo $studiengang; ?>
                        </li>
                    </ul>
                </div>
            </section>
        </main>
    </body>
</html>
