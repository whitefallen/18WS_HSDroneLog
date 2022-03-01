<?php
foreach($element_data as $key=>$value) {
    echo "<tr >";

    echo "<td>".$value->bezeichnung."</td>";
    echo "<td>".$value->erklaerung."</td>";
    echo "<td class=\"col-md-1 col-sm-1 col-xs-1\"> <button class=\"btn btn-default\" onclick='elementdetail($value->element_id );'><span class=\"glyphicon glyphicon-eye-open\" aria-hidden=\"true\"></span></button></td>";

    echo "</tr>";
}
?>