<?php require_once('init.php') ?>
<?php
  if(!isset($_SESSION['in'])){
        $items = json_decode(DevHelper::getCookies("cart"),TRUE);
    }else{
        $items = [];
        $cid = $_SESSION['id'];
        $res = DevHelper::select("SELECT `id` from `cart` where `userID` = '$cid'");
        if($res){
            $cart_id = $res[0]['id'];
            $res1 = DevHelper::select("SELECT * from `cart_product` where `cartID` = '$cart_id'");
            if($res1){
                for($i=0; $i<count($res1); $i++){
                    $items[$i]['id'] = $res1[$i]['productID'];
                    $items[$i]['quantity'] = $res1[$i]['quantity'];
                }
            }
        }
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
<script> 
  var numberproducts = <?php echo count($items); ?>;
  var items;
  if(numberproducts>0){
    items = (<?php echo json_encode($items); ?>);
  }
</script>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- SHOP BASKET SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <p class="text-muted lead mb-5">You currently have <?php echo count($items); ?> item(s) in your cart.</p>
          <div class="row">
            <div class="col-lg-9">
              <!-- CART PRODUCTS TABLE-->
              <form id = "cart_form">
                <div class="table-responsive">
                    <?php require_once('inc/product_table.php');?>
                </div>
                <!-- NAVIGATION FOOTER-->
                <div class="row gx-lg-0 align-items-center bg-light px-4 py-3 text-center mb-5">
                  <div class="col-md-6 text-md-start py-1"><a class="btn btn-secondary my-1" href="shop-category.php?all=true"><i class="fas fa-angle-left me-1"></i> Continue shopping</a></div>
                  <div class="col-md-6 text-md-end py-1">
                    <button id = "update_cart_btn" class="btn btn-secondary my-1"><i class="fas fa-sync-alt me-1"></i> Update cart</button>
                    <button id = "proceed_to_checkout_btn" class="btn btn-outline-primary my-1" type="submit">Proceed to checkout <i class="fas fa-angle-right ms-1"></i></button>
                  </div>
                </div>
              </form>
            </div>
            <script src = "system/js/cart.js"></script>
            <!-- CHECKOUT SIDEBAR [ORDER SUMMARY]-->
            <div class="col-lg-3">
              <div class="mb-5">
                <div class="p-4 bg-gray-200">
                  <h3 class="text-uppercase mb-0">Order summary</h3>
                </div>
                <div class="bg-light py-4 px-3">
                  <p class="text-muted">
                    <?php echo ($shipping!='0') ? 'Please note that €10 is the shipping cost for orders to Europe we will contact you for the shipping fees after you place your order': 'Free shipping for all of the products! ';?>
                  </p>
                  <div class="table-responsive">
                    <table class="table mb-0">
                      <tbody class="text-sm">
                        <tr>
                          <th class="text-muted"> <span class="d-block py-1 fw-normal">Order subtotal</span></th>
                          <th> <span class="d-block py-1 fw-normal text-end">€<?php echo $total; ?></span></th>
                        </tr>
                        <tr>
                          <th class="text-muted"> <span class="d-block py-1 fw-normal">Shipping and handling</span></th>
                          <th> <span class="d-block py-1 fw-normal text-end">€<?php echo $shipping; ?></span></th>
                        </tr>
                        <!-- <tr>
                          <th class="text-muted"> <span class="d-block py-1 fw-normal">Tax</span></th>
                          <th> <span class="d-block py-1 fw-normal text-end">$0.00</span></th>
                        </tr> -->
                        <tr class="total">
                          <td class="py-3 border-bottom-0 text-muted"> <span class="lead fw-bold">Total</span></td>
                          <th class="py-3 border-bottom-0"> <span class="lead fw-bold text-end">€<?php echo $shipping+$total; ?></span></th>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              
            </div>
          </div>
        </div>
      </section>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>