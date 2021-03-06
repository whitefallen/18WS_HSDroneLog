<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="<?php echo base_url('/assets/images/drohne_quadrat.png')?>"  />


    <title>HSDronelog</title>

    <!-- Bootstrap -->
    <link href=<?php echo base_url('/assets/vendors/bootstrap/dist/css/bootstrap.min.css')?> rel="stylesheet">
    <!-- Font Awesome -->
    <link href=<?php echo base_url('/assets/vendors/font-awesome/css/font-awesome.min.css')?> rel="stylesheet">
    <!-- NProgress -->
    <link href=<?php echo base_url('/assets/vendors/nprogress/nprogress.css')?> rel="stylesheet">
    <!-- iCheck -->
    <link href=<?php echo base_url('/assets/vendors/iCheck/skins/flat/green.css')?> rel="stylesheet">

    <!-- bootstrap-progressbar -->
    <link href=<?php echo base_url('/assets/vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css')?> rel="stylesheet">
    <!-- JQVMap -->
    <link href=<?php echo base_url('/assets/vendors/jqvmap/dist/jqvmap.min.css')?> rel="stylesheet"/>
    <!-- bootstrap-daterangepicker -->
    <link href=<?php echo base_url('/assets/vendors/bootstrap-daterangepicker/daterangepicker.css')?> rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href=<?php echo base_url('/assets/css/custom.min.css')?> rel="stylesheet">
    <link href=<?php echo base_url('/assets/css/custom.css')?> rel="stylesheet">



    <script type="text/javascript" src=<?php echo base_url('/assets/js/openLayers.js')?> ></script>
    <script type="text/javascript" src=<?php echo base_url('/assets/js/OpenStreetMap.js')?>></script>




    <!---- Tom.js Dateien--->
    <script>
        function jumpTo(lon, lat, zoom) {
            var x = Lon2Merc(lon);
            var y = Lat2Merc(lat);
            map.setCenter(new OpenLayers.LonLat(x, y), zoom);
            return false;
        }

        function Lon2Merc(lon) {
            return 20037508.34 * lon / 180;
        }

        function Lat2Merc(lat) {
            var PI = 3.14159265358979323846;
            lat = Math.log(Math.tan( (90 + lat) * PI / 360)) / (PI / 180);
            return 20037508.34 * lat / 180;
        }

        function addMarker(layer, lon, lat, popupContentHTML) {
            lon = parseFloat(lon);
            lat = parseFloat(lat);
            var ll = new OpenLayers.LonLat(Lon2Merc(lon), Lat2Merc(lat));
            var feature = new OpenLayers.Feature(layer, ll);
            feature.closeBox = true;
            feature.popupClass = OpenLayers.Class(OpenLayers.Popup.FramedCloud, {minSize: new OpenLayers.Size(200, 80) } );
            feature.data.popupContentHTML = popupContentHTML;
            feature.data.overflow = "hidden";

            var marker = new OpenLayers.Marker(ll);
            marker.feature = feature;

            var markerClick = function(evt) {
                if (this.popup == null) {
                    //this.popup = this.createPopup(this.closeBox);
                    map.addPopup(this.popup);
                    this.popup.show();
                } else {
                    this.popup.toggle();
                }
                OpenLayers.Event.stop(evt);
            };
            marker.events.register("mousedown", feature, markerClick);

            layer.addMarker(marker);
            //map.addPopup(feature.createPopup(feature.closeBox));
        }

        function getCycleTileURL(bounds) {
            var res = this.map.getResolution();
            var x = Math.round((bounds.left - this.maxExtent.left) / (res * this.tileSize.w));
            var y = Math.round((this.maxExtent.top - bounds.top) / (res * this.tileSize.h));
            var z = this.map.getZoom();
            var limit = Math.pow(2, z);

            if (y < 0 || y >= limit)
            {
                return null;
            }
            else
            {
                x = ((x % limit) + limit) % limit;

                return this.url + z + "/" + x + "/" + y + "." + this.type;
            }
        }
    </script>
    <script type="text/javascript">
        //<![CDATA[

        var map;
        var layer_mapnik;
        var layer_tah;
        var layer_markers;

        function drawmap() {

            OpenLayers.Lang.setCode('de');

            // Position und Zoomstufe der Karte
            var lon = 6; //L??ngengrad
            var lat = 51.2468622;   //Breitengrad
            var zoom = 2;

            map = new OpenLayers.Map('map', {
                projection: new OpenLayers.Projection("EPSG:900913"),
                displayProjection: new OpenLayers.Projection("EPSG:4326"),
                controls: [
                    new OpenLayers.Control.Navigation(),
                    //new OpenLayers.Control.LayerSwitcher(),
                    new OpenLayers.Control.PanZoomBar()],
                maxExtent:
                    new OpenLayers.Bounds(-20037508.34,-20037508.34,
                        20037508.34, 20037508.34),
                numZoomLevels: 18,
                maxResolution: 156543,
                units: 'meters'
            });

            layer_mapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
            layer_markers = new OpenLayers.Layer.Markers("Address", { projection: new OpenLayers.Projection("EPSG:4326"),
                visibility: true, displayInLayerSwitcher: false });

            map.addLayers([layer_mapnik, layer_markers]);





            var flugInformationen = <?php echo json_encode($flight_data, JSON_PRETTY_PRINT); ?>;

            addMarker(layer_markers, flugInformationen[0].laengengrad, flugInformationen[0].breitengrad, "huhu");

            zoom = 10;

            //Aufschnitt der Map, welcher gezeigt werden soll
            jumpTo(Number(flugInformationen[0].laengengrad), Number(flugInformationen[0].breitengrad), 16); //Deutschland Mittelpunkt


        }

        //]]>
    </script>

</head>