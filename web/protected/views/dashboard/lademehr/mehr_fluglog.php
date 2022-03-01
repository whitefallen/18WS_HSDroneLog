<?php
foreach ($fluglog as $key=>$value){
    echo "<tr>";

    echo "<td>" . $value->latitude . "</td>";
    echo "<td>" . $value->longitude . "</td>";
    echo "<td>" . $value->altitude . "</td>";
    echo "<td>" . $value->homeLatitude . "</td>";
    echo "<td>" . $value->homeLongitude . "</td>";
    echo "<td>" . $value->battery_percent . "</td>";
    echo "<td>" . $value->app_warning . "</td>";
    echo "</tr>";
};
