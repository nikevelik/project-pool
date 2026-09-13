<?php require_once('init.php') ?>
<?php
  $items = [];
  $data = [];
  if(!isset($_GET['id'])) header('location: 404.php');
  $orderID = $_GET['id'];
  $res = DevHelper::select("SELECT * FROM  `order_product` WHERE `orderID`=$orderID");
  if($res!='0'){
    for($i=0; $i<count($res); $i++){
      $iter_id = $res[$i]['productID'];
      $res2 =  DevHelper::select("SELECT * FROM  `product` WHERE `id`=$iter_id");
      if($res2!='0'){
        $items[$i]['quantity'] = $res[$i]['quantity'];
        $items[$i]['price'] = $res2[0]['price'];
        $items[$i]['id'] = $res2[0]['id'];
        $items[$i]['dir'] = $res2[0]['dir'];
        $items[$i]['name'] = $res2[0]['name'];
        if($res2[0]['shipping']!=0){
          $shipping=$defaultShippingTax;
        }else{
          $shipping = 0;
        }
      }
      $total += $items[$i]['price']*$items[$i]['quantity'];
    }
  }else{
    header('location: 404.php');
  }
  $res = DevHelper::select("SELECT * from `order_` where `id` = $orderID");
  if($res!='0'){
    $ord_data = $res[0];
    $adr_data;
    $adr_id = $ord_data['addressID'];
    $res2 = DevHelper::select("SELECT * from `address` where `id`=$adr_id");
    if($res2!='0'){
      $adr_data = $res2[0];
    }
  }
  $ord_data['ship_method'] = $shipping_methods[$ord_data['ship_method']];
  $ord_data['pay_method'] = $payment_methods[$ord_data['pay_method']];
?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- ORDER DETAIL INFO-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row gy-5">
            <div class="col-lg-9">
              <p class="lead mb-4">Order #<?php echo $ord_data['id']?> was placed on <?php echo $ord_data['created']?> and <?php echo $orderStatusToText[$ord_data['status']]; ?>.</p>
              <p class="lead text-muted mb-5">If you have any questions, please feel free to <a href="contact2.php">contact us</a>, our customer service center is working for you 24/7.</p>
              <!-- ORDER TABLE-->
              <div>
              <p class="lead text-muted mb-5">Your details</p>
              
              <div class="table-responsive">
                  <table class="table text-nowrap">
                    <thead>
                      <tr class="text-sm">
                        <th class="border-gray-300 border-top py-3">First Name</th>
                        <th class="border-gray-300 border-top py-3">Last Name</th>
                        <th class="border-gray-300 border-top py-3">Address</th>
                        <th class="border-gray-300 border-top py-3">City</th>
                        <th class="border-gray-300 border-top py-3">Country Code</th>
                        <th class="border-gray-300 border-top py-3">Email</th>
                        <th class="border-gray-300 border-top py-3">Phone</th>
                        <th class="border-gray-300 border-top py-3">Shipping Method</th>
                        <th class="border-gray-300 border-top py-3">Payment Method</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr class="text-sm">
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['fname'];?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['lname'];?></a></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $adr_data['address']; ?> </td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $adr_data['city'];?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $adr_data['country'];?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['email']; ?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['phone']; ?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['ship_method']; ?></td>
                        <td class="align-middle border-gray-300 py-3"><?php echo $ord_data['pay_method']; ?></td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <br>
              <p class="lead text-muted mb-5">Products</p>
                <div class="table-responsive">
                  <?php require_once('inc/product_table.php'); ?>
                </div>
                <div>
                <!-- NAVIGATION FOOTER-->
                <div class="row gx-lg-0 align-items-center bg-light px-4 py-3 text-center mb-5">
                  <div class="col-md-6 text-md-start py-1"><a class="btn btn-secondary my-1" href="shop-category.php?all=true"><i class="fas fa-angle-left me-1"></i> Continue shopping</a></div>
                </div>
              </div>
            </div>
             
            </div>
            <div class="col-lg-3">
              <h3 class="h4 text-uppercase lined mb-4">Customer section</h3>
              <nav class="nav flex-column nav-pills"><a class="nav-link text-sm active" href="customer-orders.php"> <i class="me-2 fas fa-list"></i><span>My orders</span></a><a class="nav-link text-sm" href="customer-wishlist.php"> <i class="me-2 fas fa-heart"></i><span>My wishlist</span></a><a class="nav-link text-sm" href="customer-account.php"> <i class="me-2 fas fa-user"></i><span>My account</span></a><a class="nav-link text-sm" href="index.php"> <i class="me-2 fas fa-door-open"></i><span>Logout</span></a>
              </nav>
            </div>
          </div>
        </div>
      </section>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>