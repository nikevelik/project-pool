<!-- checkout part 4 -->
<!-- CHECKOUT REVIEW TABLE -->
<?php
    if(!isset($_SESSION['in'])){
        $items = json_decode(DevHelper::getCookies("cart"),TRUE);
    }
    $total = 0;
    $shipping=0;
    for($i=0; $i<count($items); $i++){
        $iter_id=$items[$i]['id'];
        $res = DevHelper::select("SELECT * FROM `product` where `id` = '$iter_id'");
        if($res){
            $items[$i]['price'] = $res[0]['price'];
            $items[$i]['dir'] = $res[0]['dir'];
            $items[$i]['name'] = $res[0]['name'];
            $shipping = max($shipping, $defaultShippingTax);
        }
        $total += $items[$i]['price']*$items[$i]['quantity'];
    }
?>
<div class="table-responsive" id ="ckt3">
        <div class="col-lg-3">
            <h3 class="h4 text-uppercase lined mb-4">Summary</h3>
        </div>
        <?php require_once('inc/product_table.php'); ?>
    </div>
    <!-- NAVIAGTION FOOTER-->
    <div class="align-items-center bg-light px-4 py-3 text-center mb-5">
        <div class="row">
            <div class="col-md-6 text-md-end py-1">
                <button class="btn btn-primary my-1" type="submit" id = "submit_order">Place the order<i class="fas fa-angle-right ms-1"></i></button>
                <p class="text-center text-muted small"><span id = 'order_error_par' style="color: red"></span></p>
            </div>
        </div>
        </div>
    </div>