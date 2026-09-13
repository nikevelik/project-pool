<?php       
    $f=$startN;
    $l=$endN;  
    $isFirst = ($f==0);
    $isLast  = ($l==0);
    $base =  $_SERVER["REQUEST_URI"];
    $base = preg_replace('/&endN=[0-9]+/', '', $base);
    $base = preg_replace('/&startN=[0-9]+/', '', $base);
    $base = preg_replace('/startN=[0-9]+/', '', $base);
    $base = preg_replace('/endN=[0-9]+/', '', $base);
    $base = (substr($base,-1)!='?')? $base."&" : $base;
    if($f-$products_per_page<=0){
        $previous = $base."startN=0&endN=$products_per_page";
    }else{
        $f1 = $f-$products_per_page;
        $previous = $base."startN=$f1&endN=$f";
    }
    if($l+$products_per_page>=$number){
        $f1 = $number-$products_per_page;
        $next=$base."startN=$f1&endN=$number";
    }else{
        $l1 = $l+$products_per_page;

        $next = $base."startN=$l&endN=$l1";
    }
    function getLink($i){
        global $products_per_page,  $base;
        $f1 = ($i*$products_per_page);
        $l1 = $f1+$products_per_page;
        return $base."startN=$f1&endN=$l1";
    }
?>
<li class="page-item"><a class="page-link" href="<?php echo $previous ?>" aria-label="Previous"><span aria-hidden="true">«</span></a></li>
<?php for($i =0; $i<$number/$products_per_page; $i++){ ?>
    <li class="page-item <?php echo ($l>($i*$products_per_page) && ($products_per_page*$i)>=$f) ? 'active' : '';?>"><a class="page-link" href="<?php echo getLink($i); ?>"><?php echo $i?></a></li>
<?php } ?>
<li class="page-item"><a class="page-link" href="<?php echo $next ?>" aria-label="Next"><span aria-hidden="true">»</span></a></li>