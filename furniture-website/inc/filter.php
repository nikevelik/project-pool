<?php
    $filter=[];
    foreach ($_GET as $key=>$val){
        $key=DevHelper::SanitizeData($key);
        $val=DevHelper::SanitizeData($val);
        if($key=='startN' || $key=='endN' || $key=='w') continue;
        $filter[$key] = $val;
    }
?>