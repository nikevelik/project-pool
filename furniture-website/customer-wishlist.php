
<?php require_once('init.php') ?>
<!DOCTYPE html>
<html>
  <?php 
    $str = '';
    if(!isset($_SESSION['in'])){
      $items = json_decode(DevHelper::getCookies("wish"), TRUE);
      foreach($items as $current_item){
        print_r($current_item);
        $id = $current_item['id'];
        $str.="&id[]=$id";
        //echo "$str<br>";
      }
    }else{
      $items = [];
      $cid = $_SESSION['id'];
      $res = DevHelper::select("SELECT `id` from `whishlist` where `userID` = '$cid'");
      if($res){
          $wish_id = $res[0]['id'];
          $res1 = DevHelper::select("SELECT * from `whishlist_product` where `whishlistID` = '$wish_id'");
          if($res1){
                 //$items[$i]['id'] = $res1[$i]['productID'];
                foreach($res1 as $current_item){
                  print_r($current_item);
                  $id = $current_item['productID'];
                  $str.="&id[]=$id";
                  // echo "$str<br>";
                }
          }
      }
  }
    $str = 'shop-category-left.php?w=1&'.substr($str,1);
    //echo $str;
    header("location: $str");
  /*
  require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- WISHLIST SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row g-5">
            <div class="col-lg-9">
              <p class="lead mb-5">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
              <div class="row gy-5 align-items-stretch">
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product1.jpg" alt="Fur coat with very but very very long name"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">Fur coat with very but very very long name</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product2.jpg" alt="White Blouse Armani"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">White Blouse Armani</a></h3>
                      <p class="mb-0">
                        <del class="text-gray-500 me-2">$200.00</del>$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                      <li class="mb-1">
                        <div class="ribbon sale ribbon-primary">SALE</div>
                      </li>
                      <li class="mb-1">
                        <div class="ribbon new ribbon-info">NEW</div>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product3.jpg" alt="Black Blouse Versace"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">Black Blouse Versace</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product4.jpg" alt="Black Blouse Versace"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">Black Blouse Versace</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product3.jpg" alt=" White Blouse Armani"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php"> White Blouse Armani</a></h3>
                      <p class="mb-0">
                        <del class="text-gray-500 me-2">$200.00</del>$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                      <li class="mb-1">
                        <div class="ribbon sale ribbon-primary">SALE</div>
                      </li>
                      <li class="mb-1">
                        <div class="ribbon new ribbon-info">NEW</div>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product4.jpg" alt="White Blouse Versace"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">White Blouse Versace</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                      <li class="mb-1">
                        <div class="ribbon new ribbon-info">NEW</div>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product2.jpg" alt="White Blouse Versace"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">White Blouse Versace</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                      <li class="mb-1">
                        <div class="ribbon new ribbon-info">NEW</div>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="col-lg-3 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="shop-detail.php"><img class="img-fluid" src="assets/img/product1.jpg" alt="Fur coat"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="shop-detail.php">Fur coat</a></h3>
                      <p class="mb-0">$143.00
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                    </ul>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-lg-3">
              <h3 class="h4 text-uppercase lined mb-4">Customer section</h3>
              <nav class="nav flex-column nav-pills"><a class="nav-link text-sm" href="customer-orders.php"> <i class="me-2 fas fa-list"></i><span>My orders</span></a><a class="nav-link text-sm active" href="customer-wishlist.php"> <i class="me-2 fas fa-heart"></i><span>My wishlist</span></a><a class="nav-link text-sm" href="customer-account.php"> <i class="me-2 fas fa-user"></i><span>My account</span></a><a class="nav-link text-sm" href="index.php"> <i class="me-2 fas fa-door-open"></i><span>Logout</span></a>
              </nav>
            </div>
          </div>
        </div>
      </section>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>*/