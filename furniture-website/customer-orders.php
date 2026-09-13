<?php require_once('init.php') ?>
<?php
  if(isset($_SESSION['in'])){
    $user_id = $_SESSION['id'];
    $orders = DevHelper::select("Select * from `order_` where `userID` = '$user_id'");
    if(!$orders){
      $orders = [];
    }
  }else {$orders = json_decode(DevHelper::getOrderCookies(),true);}
?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      <?php require_once('inc/topbar.php');?>
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- CUSTOMERS ORDERS SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row gy-5">
            <div class="col-lg-9">
              <p class="text-muted lead mb-5">If you have any questions, please feel free to <a href="contact.php">contact us</a>, our customer service center is working for you 24/7.</p>
              <!-- ORDERS TABLE-->
              <div class="table-responsive">
                <table class="table table-hover text-nowrap">
                  <thead>
                    <tr class="text-sm">
                      <th class="border-gray-300 border-top py-3">Order</th>
                      <th class="border-gray-300 border-top py-3">Date</th>
                      <th class="border-gray-300 border-top py-3">Total</th>
                      <th class="border-gray-300 border-top py-3">Status</th>
                      <th class="border-gray-300 border-top py-3">More:</th>
                    </tr>
                  </thead>
                  <tbody>
                    <?php for($i=0; $i<count($orders); $i++){?>
                    <tr class="text-sm">
                      <th class="align-middle py-3">#<?php echo $orders[$i]["id"]; ?></th>
                      <td class="align-middle py-3"><?php echo $orders[$i]["created"]; ?></td>
                      <td class="align-middle py-3">€<?php echo $orders[$i]["total"]; ?></td>
                      <td class="align-middle py-3"><span class="badge fw-light text-uppercase <?php echo $orderStatusStyle[$orders[$i]["status"]]; ?>"><?php echo $orderStatusToTextShort[$orders[$i]["status"]]; ?></span></td>
                      <td class="align-middle py-3"><a class="btn btn-outline-primary btn-sm" href="customer-order.php?id=<?php echo $orders[$i]["id"]; ?>">View</a></td>
                    </tr>
                    <?php } ?>
                  </tbody>
                </table>
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