<script>
    var rolle = <?php echo $this->session->users['rolle']; ?>;
    var p = document.getElementsByClassName("admin_only");
    if(rolle == 0)
    {
        for (let i = 0; i < p.length; i++) {
            console.log(p[i]);
            p[i].style.display = "none";

        }

    }
</script>

<!-- jQuery -->
<script src=<?php echo base_url('/assets/vendors/jquery/dist/jquery.min.js')?>></script>
<!-- Bootstrap -->
<script src=<?php echo base_url('/assets/vendors/bootstrap/dist/js/bootstrap.min.js')?>></script>
<!-- FastClick -->
<script src=<?php echo base_url('/assets/vendors/fastclick/lib/fastclick.js')?>></script>
<!-- NProgress -->
<script src=<?php echo base_url('/assets/vendors/nprogress/nprogress.js')?>></script>
<!-- Chart.js -->
<script src=<?php echo base_url('/assets/vendors/Chart.js/dist/Chart.min.js')?>></script>
<!-- gauge.js -->
<script src=<?php echo base_url('/assets/vendors/gauge.js/dist/gauge.min.js')?>></script>
<!-- bootstrap-progressbar -->
<script src=<?php echo base_url('/assets/vendors/bootstrap-progressbar/bootstrap-progressbar.min.js')?>></script>
<!-- iCheck -->
<script src=<?php echo base_url('/assets/vendors/iCheck/icheck.min.js')?>></script>
<!-- Skycons -->
<script src=<?php echo base_url('/assets/vendors/skycons/skycons.js')?>></script>
<!-- Flot -->
<script src=<?php echo base_url('/assets/vendors/Flot/jquery.flot.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/Flot/jquery.flot.pie.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/Flot/jquery.flot.time.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/Flot/jquery.flot.stack.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/Flot/jquery.flot.resize.js')?>></script>
<!-- Flot plugins -->
<script src=<?php echo base_url('/assets/vendors/flot.orderbars/js/jquery.flot.orderBars.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/flot-spline/js/jquery.flot.spline.min.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/flot.curvedlines/curvedLines.js')?>></script>
<!-- DateJS -->
<script src=<?php echo base_url('/assets/vendors/DateJS/build/date.js')?>></script>
<!-- JQVMap -->
<script src=<?php echo base_url('/assets/vendors/jqvmap/dist/jquery.vmap.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/jqvmap/dist/maps/jquery.vmap.world.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/jqvmap/examples/js/jquery.vmap.sampledata.js')?>></script>
<!-- bootstrap-daterangepicker -->
<script src=<?php echo base_url('/assets/vendors/moment/min/moment.min.js')?>></script>
<script src=<?php echo base_url('/assets/vendors/bootstrap-daterangepicker/daterangepicker.js')?>></script>
<!-- pnotify -->
<script src="<?php echo base_url('/assets/vendors/pnotify/dist/pnotify.js')?>"></script>
<script src="<?php echo base_url('/assets/vendors/pnotify/dist/pnotify.buttons.js')?>"></script>
<script src="<?php echo base_url('/assets/vendors/pnotify/dist/pnotify.nonblock.js')?>"></script>

<script src="<?php echo base_url('/assets/vendors/switchery/dist/switchery.min.js')?>"></script>

<!-- Custom Theme Scripts -->
<script src=<?php echo base_url('/assets/js/custom.js')?>></script>

