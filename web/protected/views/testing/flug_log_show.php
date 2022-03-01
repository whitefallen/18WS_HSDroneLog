<html lang="de">
<head>
    <title></title>
</head>
<body>
<main>
    <table>
    <?php
    echo "<tr><th>Row#</th><th>latitude</th><th>longitude</th><th>altitude</th><th>homeLatitude</th><th>homeLongitude</th><th>battery_percent</th><th>app_warning</th></tr>";
    foreach ($log_data as $key => $value) {
     echo "<tr><td>{$key}</td><td>{$value->latitude}</td><td>{$value->longitude}</td><td>{$value->altitude}</td><td>{$value->homeLatitude}</td><td>{$value->homeLongitude}</td><td>{$value->battery_percent}</td><td>{$value->app_warning}</td></tr>";
    } ?>
    </table>
</main>
</body>
</html>