<?php 
    //this file initialises variables to simplify and shorten further code in many files
    $php_selves = ["/about.php", "/blog-medium.php", "/blog-post.php",  "/blog-small.php",  "/contact2.php",  "/customer-account.php",  "/customer-order.php",  "/customer-orders.php",  "/customer-register.php",  "/customer-wishlist.php",  "/faq.php",  "/index.php",  "/portfolio-3.php",  "/services.php",  "/shop-basket.php",  "/shop-category-full.php",  "/shop-category-left.php",  "/shop-checkout1.php",  "/shop-checkout2.php",  "/shop-checkout3.php",  "/shop-checkout4.php",  "/shop-detail.php",  "/team.php", "/404.php", '/system/php/requests.php', '/checkout-done.php'];
    $titles = ["ABOUT US", "BLOG", "TITLE", "BLOG", "CONTACT US", "MY ACCOUNT", "MY ORDER", "MY ORDERS", "REGISTER", "MY WISHLIST", "FAQ", "HOME", "PARTNERSHIPS", "WHY CHOOSE US", "MY CART", "PRODUCTS", "PRODUCTS", "CHECKOUT STEP", "CHECKOUT STEP 2", "CHECKOUT STEP 3", "CHECKOUT STEP 4", "PRODUCT", "OUR TEAM", "NOT FOUND", 'request', "Order Summary"];
    $page_names = ["about", "blog-medium", "blog-post", "blog-small", "contact", "customer-account", "customer-order", "customer-orders", "customer-register", "customer-wishlist", "faq", "index", "portfolio", "services", "shop-backet", "shop-category-full", "shop-category-left", "shop-checkout-1", "shop-checkout-2", "shop-checkout-3", "shop-checkout-4", "shop-detail", "team", "404", 'request', "order-summary"];
    $restricted_for_guests = ["customer-account"];
    $titles_assoc_with_name = [""=>""];
    $titles_assoc_with_php_self= [""=>""];
    $pages_assoc_with_name = [""=>""];
    $names_assoc_with_php_self=[""=>""];
    for($i=0; $i< sizeof($php_selves); $i++){
        $php_selves[$i]="/nikola-academy-room".$php_selves[$i];
        $titles_assoc_with_name[$page_names[$i]] = $titles[$i];
        $pages_assoc_with_name[$page_names[$i]] = $php_selves[$i];
        $titles_assoc_with_php_self[$php_selves[$i]] = $titles[$i];
        $names_assoc_with_php_self[$php_selves[$i]]=$page_names[$i];
    }
    $ftr_blog_img = ["assets/img/media-kapital.jpg","assets/img/media-dnevnik.jpg","assets/img/puzzle.png"];
    $ftr_blog_link = ["http://www.capital.bg/light/neshta/2016/01/22/2690202_mlad_konstruktor/", "http://www.dnevnik.bg/razvlechenie/2016/08/10/2809118_ne_vseki_stol_e_s_chetiri_kraka/", "#"];
    $ftr_blog_title = ["Млад конструктор","Не всеки стол е на 4 крака","Interlocking and flat pack"];
    $ftr_blog_target_blank = [TRUE, TRUE, FALSE];
    $shipping_methods = ["USPS", "POST", "PICK UP"];
    $payment_methods = ["PAYPAL", "GATEWAY", "ON DELIVERY"];
    $payment_methods_desc = ["PAYPAL", "GATEWAY", "ON DELIVERY"];
    $shipping_methods_desc = ["USPS", "POST", "PICK UP"];
    #root path and ect.
    $title = $titles_assoc_with_php_self[$_SERVER["PHP_SELF"]];
    $orderStatusToText = ["is currently being prepared" ,"is shipped", "is deliverd", "is complete", "is currently on hold"];
    $orderStatusToTextShort = ["being prepared" ,"shipped", "deliverd", "complete", "on hold", "cancelled"];
    $orderStatusStyle = ["bg-info", "bg-primary", "bg-success", "bg-danger", "bg-warning", "bg-danger", "bg-secondary"];
    $defaultShippingTax = 10;
    for($j=0; $j<count($restricted_for_guests) && !isset($_SESSION['in']); $j++){
        if($names_assoc_with_php_self[$_SERVER["PHP_SELF"]]==$restricted_for_guests[$j]){
            $isThisPageRestricted = true;
            break;
        } 
    }
    $valids = ["type"=>[], "serie"=>[], "color"=>[], "all"=>[]];
    $valids["type"] = ['table', 'chair', 'lamp', 'store', 'stool', 'barstool'];
    $valids["serie"] = ['lesse', 'frame', 'space', 'android', 'arch',  'pendant', 'stack'];
    $valids["color"] = ['natural', 'stain', 'black', 'red'];
    $products_per_page = 6;
?>