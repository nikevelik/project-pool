<?php 
    session_start();
    require_once('inc/globals.php');
    #src: проектът на Ники, телеграм
    ini_set('default_charset', 'utf-8');
    ini_set('error_reporting', E_ALL & ~E_NOTICE  & ~E_WARNING & ~E_DEPRECATED);
    ini_set('display_errors', 'On');  //On or Off
    define('ROOT_PATH', $_SERVER['DOCUMENT_ROOT']);
    require_once('classes/classes.php');
    #require(ROOT_PATH . '/conf/db.conf.php');
    date_default_timezone_set('Europe/Sofia');
?>
<?php if($_SERVER['PHP_SELF']!=$pages_assoc_with_name["request"]){ ?>
<script>
    var logged=false;
    <?php if(isset($_SESSION['in'])){ ?>
        logged = true;        
    <?php } ?>
    function setCookie(cname, cvalue, exdays) {
        const d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        let expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
        console.log(document.cookie);
    }  
    function getCookie(cname) {
        let name = cname + "=";
        let ca = document.cookie.split(';');
        for(let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
    function deleteCookie(cname){
        document.cookie = cname+"=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        console.log(document.cookie);
    }
    function reorderProductCookies(cartOrWishList, lastIdx){
        if(cartOrWishList){ <?php DevHelper::cleanCookies("cart"); ?>;}
        else {<?php DevHelper::cleanCookies("wish"); ?>;}
        for(let i=0; i<lastIdx; i++){
            if(getCookie(cartOrWishList+i)==""){
                for(let j=lastIdx; j>=0; j--){          
                    let iter_cookie = getCookie(cartOrWishList+j);
                    if(iter_cookie!=""){
                        setCookie(cartOrWishList+i, iter_cookie, 365);
                        deleteCookie(cartOrWishList+j);
                        lastIdx--;
                        break;
                    }
                }
            }
        }
    }
    function pushProductCookie(target_product, target_quantity, cartOrWishList="cart"){
        if(target_quantity%1!=0 || (target_quantity<1 && cartOrWishList=="cart"))return;
        var i=0;
        while (true){
            var cookie = getCookie(cartOrWishList+i);
            if(cookie=="") break;
            if(cartOrWishList=='cart'){
                var iterated_product = Number(cookie.substring(0, cookie.indexOf('x')));
                if(iterated_product == target_product){
                    var iterated_quantity = Number(cookie.substring(cookie.indexOf('x')+1, cookie.length));
                    console.log(target_quantity+2);
                    target_quantity+=iterated_quantity;
                    break;
                }
            }else if(cookie==target_product){
                console.log("already in whishlist");
                break;
            }
            i++;
        }
        if(cartOrWishList=="cart"){
            setCookie(cartOrWishList+i,target_product+"x"+target_quantity,365);
        }else{
            setCookie(cartOrWishList+i,target_product,365);
        }
        <?php DevHelper::cleanCookies("cart"); ?>
    } 
</script>
<?php }
?>