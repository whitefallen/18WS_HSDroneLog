<?php if (!isset($this->session->users['email_adresse'])) {
    redirect(base_url());
} ?>
<html lang="en">
   <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>HSDronelog</title>

    <!-- Bootstrap -->
    <link href=<?php echo base_url('/assets/css/bootstrap.css')?> rel="stylesheet">
    <link href=<?php echo base_url('/assets/css/dashboard.css')?> rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->


     <!---Script für Map---->
       <script type="text/javascript" src="http://www.openlayers.org/api/OpenLayers.js"></script>
       <script type="text/javascript" src="http://www.openstreetmap.org/openlayers/OpenStreetMap.js"></script>
       <script type="text/javascript" src="tom.js"></script>



       <script type="text/javascript">
           //<![CDATA[

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

               var ll = new OpenLayers.LonLat(Lon2Merc(lon), Lat2Merc(lat));
               var feature = new OpenLayers.Feature(layer, ll);
               feature.closeBox = true;
               feature.popupClass = OpenLayers.Class(OpenLayers.Popup.FramedCloud, {minSize: new OpenLayers.Size(300, 180) } );
               feature.data.popupContentHTML = popupContentHTML;
               feature.data.overflow = "hidden";

               var marker = new OpenLayers.Marker(ll);
               marker.feature = feature;

               var markerClick = function(evt) {
                   if (this.popup == null) {
                       this.popup = this.createPopup(this.closeBox);
                       map.addPopup(this.popup);
                       this.popup.show();
                   } else {
                       this.popup.toggle();
                   }
                   OpenLayers.Event.stop(evt);
               };
               marker.events.register("mousedown", feature, markerClick);

               layer.addMarker(marker);
               map.addPopup(feature.createPopup(feature.closeBox));
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














           var map;
           var layer_mapnik;
           var layer_tah;
           var layer_markers;

           function drawmap() {
               // Popup und Popuptext mit evtl. Grafik
               var popuptext="<font color=\"black\"><b>Hochschule Düsseldorf</b><p><img src=\"test.jpg\" width=\"180\" height=\"113\"></p></font>";

               OpenLayers.Lang.setCode('de');

               // Position und Zoomstufe der Karte
               var lon = 6.791686900000059;
               var lat = 51.2468622;
               var zoom = 15;

               map = new OpenLayers.Map('map', {
                   projection: new OpenLayers.Projection("EPSG:900913"),
                   displayProjection: new OpenLayers.Projection("EPSG:4326"),
                   controls: [
                       new OpenLayers.Control.Navigation(),
                       new OpenLayers.Control.LayerSwitcher(),
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
               jumpTo(lon, lat, zoom);

               // Position des Markers
               addMarker(layer_markers, lon, lat, popuptext);

           }

           //]]>
       </script>



   </head>
 <body onload="drawmap();">

 <?php $this->load->view("template/navbar"); ?>

    <div class="container-fluid">
        <?php $this->load->view("template/sidebar"); ?>
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2">
        <div class="main">
          <h1 class="page-header">Dashboard</h1>



            <div id="map">

            </div>


    </div>
    </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>



</body>
</html>