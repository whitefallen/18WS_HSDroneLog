<?php
foreach($drone_data as $key=>$value) {
    echo "<tr>";

    echo '<td class="col-md-1 col-sm-1 col-xs-1"><img class="img-responsive " src="'.$value->bild.'"/></td>';
    echo "<td>".$value->drohnen_modell."</td>";
    echo "<td>".$value->flugzeit_in_min." Minuten</td>";
    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='myfunction($value->drohne_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

    echo "</tr>";
}
?>