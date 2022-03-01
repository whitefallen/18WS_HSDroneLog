<!--
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2">
            <div class="sidebar">
                <div class="profile">
                    <a class="profile-link" href="<?php echo site_url("user/show") ?>">

                        <h2><?php echo $this->session->users['vorname'] ?></h2>
                    </a>
                </div>
                <ul class="nav nav-sidebar">
                    <li><a href="<?php echo site_url("user/home") ?>"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> Home </a></li>
                    <li><a href="<?php echo site_url("flight/index") ?>"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Flüge</a></li>
                    <li><a href="<?php echo site_url("drone/index") ?>"><span class="glyphicon glyphicon-plane" aria-hidden="true"></span> Drohnen</a></li>
                    <li><a href="<?php echo site_url("checklist/index") ?>"><span class="glyphicon glyphicon-list" aria-hidden="true"></span> Checklisten </a></li>
                    <li><a href="<?php echo site_url("akku/index") ?>"><span class="glyphicon glyphicon-oil" aria-hidden="true"></span> Akkus </a></li>
                    <li><a href="<?php echo site_url("user/logout") ?>"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Logout </a></li>

                </ul>
            </div>
        </div>
    </div>
</div>
-->
<div class="col-md-3 left_col">

    <div class="left_col scroll-view">
        <div class="navbar nav_title" style="border: 0;">
            <a href="<?php echo site_url("dashboard") ?>" class="site_title"><i width="60px"><img src="<?php echo base_url('/assets/images/drohne_weiss.png')?>" class="sidebarLogo"></i> <!---<i class="fa fa-plane"></i>--><span>HSDronelog</span></a>
        </div>

        <div class="clearfix"></div>

        <!-- menu profile quick info -->
        <div class="profile clearfix">
            <a class="profile-link" href="<?php echo site_url("user/show")."/".$this->session->users['pilot_id'] ?>">
                <div class="profile_pic">
                    <div class="img-circle profilePicNavbar" style="background-image: url(<?php echo $this->session->users['profilbild'] ?>)"></div>
                <!----<img src="<?php echo $this->session->users['profilbild'] ?>" alt="..." class="img-circle profile_img">-->
            </div>
            <div class="profile_info">


                    <h2 class="profilname"><?php echo $this->session->users['vorname'] ?>
                        <?php echo $this->session->users['nachname'] ?></h2>
                    <h2><?php echo substr($this->session->users['email_adresse'],0,11) ?>...</h2>
                    <h2><?php
                        if($this->session->users['rolle'] == 1)
                            echo "(Admin)";
                        else
                            echo "(Pilot)"?></h2>



            </div>
            </a>
        </div>
        <!-- /menu profile quick info -->

        <br />

        <!-- sidebar menu -->
        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
            <div class="menu_section">
                <h3>Navigation</h3>
                <ul class="nav side-menu">
                    <li><a href="<?php echo site_url("dashboard") ?>"><i class="fa fa-home"></i> Dashboard </a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li><a href="<?php echo site_url("flight/index") ?>"><i class="fa fa-edit"></i> Flüge </a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li><a href="<?php echo site_url("drone/index") ?>"><i class="fa fa-plane"></i> Drohnen </a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li><a href="<?php echo site_url("checklist/index") ?>"><i class="fa fa-list"></i> Checklisten </a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li><a href="<?php echo site_url("akku/index") ?>"><i class="fa fa-battery-full"></i> Akkus</a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li class="admin_only"><a href="<?php echo site_url("user/index") ?>"><i class="fa fa-user"></i>Nutzer</a>
                        <ul class="nav child_menu"></ul>
                    </li>
                    <li><a href="<?php echo site_url("user/logout") ?>"><i class="fa fa-backward"></i>Logout</a>
                        <ul class="nav child_menu"></ul>
                    </li>

                </ul>
            </div>

        </div>
        <!-- /sidebar menu -->

        <!-- /menu footer buttons -->
        <div class="sidebar-footer hidden-small">

           <div class="impressum">Impressum</div>
        </div>
        <!-- /menu footer buttons -->
    </div>
</div>

<!-- top navigation -->
<div class="top_nav">
    <div class="nav_menu">
        <nav>
            <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
            </div>

            <ul class="nav navbar-nav navbar-right">
                <li class="closed">
                    <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <img src="<?php echo $this->session->users['profilbild'] ?>" alt=""><?php echo $this->session->users['vorname'] ?> <?php echo $this->session->users['nachname'] ?>
                        <span class=" fa fa-angle-down"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-usermenu pull-right">
                        <li><a href="<?php echo site_url("flight/add_flight") ?>">Flug hinzufügen</a></li>
                        <li><a href="<?php echo site_url("user/show")."/".$this->session->users['pilot_id'] ?>">Profil anzeigen</a></li>
                        <li class="admin_only"><a href="<?php echo site_url("dashboard/settings") ?>">Einstellungen</a></li>
                        <!---<li><a href="login.html"><i class="fa fa-sign-out pull-right"></i>Logout</a></li>-->
                    </ul>
                </li>


            </ul>
        </nav>
    </div>
</div>
<!-- /top navigation -->