<html lang="de">
    <head>
        <title></title>
    </head>
    <body>
    <main>
        <form action="<?php echo site_url("flight/upload_log/".$flug_id) ?>" enctype="multipart/form-data" method="post">
            <input type="file" name="log">
            <button type="submit">SEND</button>
        </form>
    </main>
    </body>
</html>