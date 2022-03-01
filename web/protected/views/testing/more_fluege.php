<?php
foreach($flight_data as $key=>$value) {
    $flugVergangen = new DateTime(date("Y-m-d"))>new DateTime($value->flugdatum);

    if ($flugVergangen)
        echo "<tr style='background-color: lightgrey'>";
    else
        echo "<tr style='background-color: #e6f6ff'>";

    echo '<td>' . $value->flugbezeichnung.' </td>';


    echo '<td>' . $value->flugdatum.' </td>';

    echo '<td>' . $value->einsatzort_name . '</td>';
    echo '<td>' . $value->drohnen_modell . '</td>';

    echo '<td>' . $value->vorname  . $value->nachname .  '</td>';
    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='flugdetails($value->flug_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";
    echo '</tr>';
}