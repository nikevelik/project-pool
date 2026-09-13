<?php require_once('init.php') ?>
<?php 
  $pg_desc;
  $filter = [];
  require_once('inc/filter.php');
  $hrefs = [];
  $dir = [];
  $img = [];
  $names = [];
  $price = [];
  $oldPrice = [];
  $isFreeShipping = [];
  $number = 0;
  $startN = 0;
  $endN = $products_per_page;
  if(isset($_GET['startN'])){
    $startN = (int)$_GET['startN'];
    if(isset($_GET['endN'])){
      $endN = (int)$_GET['endN'];
    }else {
      $endN = $startN+$products_per_page;
    }
  }
  $res = DevHelper::select("SELECT * FROM `product`");
  if($res){
    $number = count($res);
    $filtered_res =[];
    $addThese = [];
    for($i=0; $i<$number; $i++){
      $should_i_add_it = true;
      foreach ($filter as $key=>$val){
          $doesItMatchAnyCurrentFilter = false;
          foreach ($val as $current_filter){
            //echo "$key $current_filter<br>";
            if($res[$i][$key]==$current_filter){
              $doesItMatchAnyCurrentFilter = true; 
              break;
            }
          }
          $a = ($doesItMatchAnyCurrentFilter) ? 'TRUE': 'FALSE';
          //echo $res[$i]['name']." $a "."<br>";
          if(!$doesItMatchAnyCurrentFilter){
            $should_i_add_it = false;
            break;
          }
      }
      if($should_i_add_it){
        $filtered_res[] = $res[$i];
      }
    }
    $res = $filtered_res;
    //print_r($res[0]);
    $number = count($res);
    for($i=0; $i<$number; $i++){
      $id = $res[$i]['id'];
      $oldPrice[$i] = $res[$i]['oldprice'];
      $price[$i] = $res[$i]['price'];
      $names[$i]= $res[$i]['name'];
      $dir[$i] = $res[$i]['dir'];
      $img[$i] = $dir[$i].'/'.'main.jpg';
      $isFreeShipping[$i]= ($res[$i]['shipping']==0);
      $hrefs[$i] = "shop-detail.php?id=$id";
    }
  }else{
    $endN=-1;
  }
  if($number<$products_per_page) {
      $startN = 0;
      $endN = $products_per_page;
  }
  $endN = ($endN>$number)?$number : $endN;
  if(isset($_GET['w'])){
    if($_GET['w']==1){
      $title= 'MY WHISHLIST';
    }
  }
  if(empty($filter)){
    $title = 'all products';
  }
?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- SHOP SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row g-5">
            <!-- SHOP SIDEBAR-->
            <?php require_once('shop-sidebar.php'); ?>
            <!-- SHOP LISTING-->
            <div class="col-lg-9">
              <p class="text-muted lead text-center mb-5"><?php echo $pg_desc ?></p>
              <div class="row gy-5 align-items-stretch">
              <?php for($i=$startN; $i<$endN; $i++){ ?> 
                <div class="col-lg-4 col-md-6">
                  <!-- Product-->
                  <div class="product h-100">
                    <div class="product-image"><a href="<?php echo "$hrefs[$i]";?>"><img class="img-fluid" src="<?php echo $img[$i];?>" alt="<?php echo 'alt'?>"></a></div>
                    <div class="py-4 px-3 text-center">
                      <h3 class="h5 text-uppercase mb-3"><a class="reset-link" href="<?php echo $hrefs[$i];?>"><?php echo $names[$i]?></a></h3>
                      <p class="mb-0">
                        <del class="text-gray-500 me-2"><?php echo ($oldPrice[$i]) ? $oldPrice[$i]."€" : '';?></del><?php echo $price[$i]."€"?>
                      </p>
                    </div>
                    <ul class="list-unstyled p-0 ribbon-holder mb-0">
                     <?php if($oldPrice[$i]){?> 
                        <li class="mb-1">
                          <div class="ribbon sale ribbon-primary"><?php echo 'Sale';?></div>
                        </li>
                        <?php } if($isFreeShipping[$i]){?>
                        <li class="mb-1">
                          <div class="ribbon new ribbon-info"><?php echo 'FREE SHIP';?></div>
                        </li>
                      <?php } ?>
                    </ul>
                  </div>
                </div>
              <?php } ?>
              </div>
              <!-- <a class="d-block text-center py-4"><img class="img-fluid" src="assets/img/banner2.jpg" alt="banner"></a> -->
              <?php if($number>6){ ?>
              <!-- <div class="text-center mb-5"><a class="btn btn-outline-primary" href="#"><i class="fas fa-angle-down me-2"></i>Load more</a></div> -->
              <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                  <?php require_once('inc/make_pagination.php') ?>
                </ul>
              </nav> 
              <?php } ?>
            </div>
          </div>
        </div>
      </section>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>