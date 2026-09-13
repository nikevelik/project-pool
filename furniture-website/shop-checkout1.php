<?php require_once('init.php') ?>
<!DOCTYPE html>
<?php
  $islogged = isset($_SESSION['in']);
  $nocookies = empty(json_decode(DevHelper::getCookies("cart")));
  $nodatabase;
  if(!$islogged){
    if($nocookies)header('location: shop-basket.php');
  }else{
        $items = [];
        $cid = $_SESSION['id'];
        $res = DevHelper::select("SELECT `id` from `cart` where `userID` = '$cid'");
        if($res){
            $cart_id = $res[0]['id'];
            $res1 = DevHelper::select("SELECT * from `cart_product` where `cartID` = '$cart_id'");
            if($res1!=0){
                for($i=0; $i<count($res1); $i++){
                    $items[$i]['id'] = $res1[$i]['productID'];
                    $items[$i]['quantity'] = $res1[$i]['quantity'];
                }
            }else{
                header('location: shop-basket.php');
            }
        }
    }
?>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      <?php require_once('inc/topbar.php');?>
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- CHECKOUT STEP 1 SECTION-->
      <section class="py-5">
        <div class="container py-4">
            <div class="row gy-5">
                <div class="col-lg-9">
                    <?php require_once('inc/checkout1.php'); ?>
                    <?php require_once('inc/checkout2.php'); ?>
                    <?php require_once('inc/checkout3.php'); ?>
                    <?php require_once('inc/checkout4.php'); ?>
                </div>
            </div>
        </div>
      </section>
      <script src = 'system/js/order.js'> </script>
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>