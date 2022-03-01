<html>
    <head>
        <title></title>
    </head>
    <style>
        body {
            background: linear-gradient(135deg, #000000 0%, #333333 100%) fixed;
            color: #FFFFFF;
        }

        .stage {
            position: fixed;
            top: 60%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .icon {
            display: inline-block;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            text-align: center;
            font-size: 80px;
            cursor: pointer;
            color: red;
            line-height: 50px;
        }

        .icon:after {
            pointer-events: none;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border-radius: 50%;
            content: '';
            box-shadow: inset 0 0 0 3px #FFF;
            transition: transform 0.2s, opacity 0.2s;
        }

        .icon:hover:after {
            transform: scale(2.45);
            opacity: 0.8;
        }

        .pulsing:before {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border-radius: 50%;
            content: '';
            box-shadow: inset 0 0 0 3px #FFF;
            transition: transform 0.2s, opacity 0.2s;
            animation: pulsing 2.7s infinite;
        }

        div > p {
            text-align:center;
            color:white;
            font-size: 30px;
        }
        
        div > p > a {
            text-decoration: none;
            color: white;
            cursor: default;
            height: 200px;
            width: 500px;
            position: fixed;
            top: 50%;
            left: 50%;
            margin-top: -100px; /* Negative half of height. */
            margin-left: -250px; /* Negative half of width. */
        }

        div > div > i.fas {
            top: 15px;
            position: fixed;
            left: 10px;
        }

        @keyframes pulsing {
            0% {
                transform: scale(1);
                opacity: 1;
            }
            50% {
                transform: scale(2);
                opacity: 0;
            }
            100% {
                transform: scale(1);
                opacity: 0;
            }
        }
    </style>
    <body>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">

        <div class="stage">
            <div class="icon pulsing"><i class="fas fa-heart"></i></div>
        </div>
        <div>
            <p><a href="<?php echo site_url("index/login") ?>">Wir sind im Umbau....</a></p>
        </div>
        <script>
            $(function() {
                $(".pulse").hover(function() {
                    $(this).removeClass("pulse");
                }, function() {
                    $(this).addClass("pulse");
                });
            });
        </script>
    </body>
</html>

