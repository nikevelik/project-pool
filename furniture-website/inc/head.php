<!-- this file initialises the head tag of every other file -->
<?php require_once('globals.php');?>  
<!DOCTYPE html>
<html>
<head>
    <?php if($isThisPageRestricted) header('location: customer-register.php'); ?>
    <title><?php echo $title;?></title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="all,follow">
    <!-- Google fonts-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,700">
    <!-- Swiper slider-->
    <link rel="stylesheet" href="assets/vendor/swiper/swiper-bundle.min.css">
    <!-- Choices.js [Custom select]-->
    <link rel="stylesheet" href="assets/vendor/choices.js/public/assets/styles/choices.css">
    <!-- Theme stylesheet-->
    <link rel="stylesheet" href="assets/css/style.default.css" id="theme-stylesheet">
    <link rel="stylesheet" href="assets/css/style.marsala.css">
    <!-- Custom stylesheet - for your changes-->
    <link rel="stylesheet" href="assets/css/custom.css">
    <!-- Favicon and apple touch icons-->
    <link rel="shortcut icon" href="assets/img/favicon.ico" type="image/x-icon">
    <link rel="apple-touch-icon" href="img/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="57x57" href="img/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="72x72" href="img/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="img/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="img/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="img/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="img/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="img/apple-touch-icon-152x152.png">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <?php echo ($titles_assoc_with_php_self[$_SERVER["PHP_SELF"]]=='PRODUCT')? 
        '<script type="module" src="https://unpkg.com/@google/model-viewer/dist/model-viewer.min.js"></script>
        <script nomodule src="https://unpkg.com/@google/model-viewer/dist/model-viewer-legacy.js"></script>': 
        '';
    ?>
    <!-- Tweaks for older IEs--><!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script><![endif]-->
</head>