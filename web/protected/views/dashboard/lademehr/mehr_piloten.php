<?php
foreach($user_data as $key=>$value) {
    echo "<tr >";
    echo '<td class="col-md-1 col-sm-1 col-xs-1"><img class="img-responsive " src="'.$value->profilbild.'"/></td>';
    echo '<td>' . $value->vorname . '</td>';
    echo '<td>' . $value->nachname.' </td>';
    echo '<td>' . $value->email_adresse . '</td>';
    echo '<td>' . $value->letzter_login . '</td>';
    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='myfunction($value->pilot_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

    echo '</tr>';
}
?>