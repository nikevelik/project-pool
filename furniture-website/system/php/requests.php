<?php 
    require_once('../../init.php');
    if($_SERVER['REQUEST_METHOD']=='POST'){
        switch ($_POST['request']) {
            case 'login':
                $a = Users::login();
                exit($a);
                return;
                break;
            case 'signup':
                $a = Users::signup();
                exit($a);
                return;
                break;
            case 'change_pass':
                $a = Users::updatePass($_POST['old_pass'], $_POST['new_pass']);
                exit($a);
                return;
                break;
            case 'update':
                $a = Users::updateData();
                exit($a);
                return;
                break;
            case 'prelog':
                $a = Users::getData();
                exit($a);
                return;
                break;
            case 'order':
                $a = Users::placeOrder();
                exit($a);
                return;
                break;
            case 'cart': 
                $a = Users::addToCart();
                exit($a);
                return;
                break;
            case 'update_cart': 
                if(isset($_SESSION['in'])){
                    $a = Users::updateCart();
                    exit($a);
                }
                return;
                break;
            case 'wish':
                $a = Users::addToWishlist();
                exit($a);
                return;
                break;
            case 'contactform': 
                $a = Users::contactForm();
                print_r($a);
                header('location: ../../index.php');
                return;
                break;
            default:
                return;    
                break;
        }
    }
?>