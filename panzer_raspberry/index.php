<?php

    if ($_GET["control"]) {
        $command = $_GET["control"];
        $out = shell_exec("python3 /home/pi/panzerControl.py $command 2>&1");
        echo json_encode($out);
    }
    else {
        $out = "GET REQUEST";
        echo json_encode($out);
    }

?>
