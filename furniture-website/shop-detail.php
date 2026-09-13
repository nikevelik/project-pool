<?php require_once('init.php') ?>
<?php  
  
  $id;
  $page_dsc;
  $dir;
  $isShippingFree;
  $product_data = [];
  $materialsHTML;
  $dimensionsHTML; 
  $weightHTML;
  $d3;
  $i1;
  $i2;
  $i3;
  $_3rd_source_display_html;
  $_3rd_icon_display_html;
  $d3_icon = 'assets/img/3dicon.png';
  if($_GET['id']){
    $id = $_GET['id'];
    $res = DevHelper::select("SELECT * from `product` WHERE `id` = $id");
    if($res){
      $product_data = $res[0];
      $isShippingFree = ($product_data['shipping']==0);
      $title = $res[0]['name'];
      $page_dsc = $res[0]['name']. ' is ...';
      $details = file_get_contents($product_data['dir']."/desc.txt");
      $materialsHTML = '<li>'.str_replace(',',' </li> <li> ', $product_data['material']).' </li>';
      $weightHTML = 'weight: '. $product_data['weight'];
      $dimensionsHTML = 'dimensions: '. $product_data['dimensions'];
      $d3 =$product_data['dir']."/3d.glb";
      $i1 =$product_data['dir']."/main.jpg";
      $i2 =$product_data['dir']."/0.jpg";
      $i3 =$product_data['dir']."/1.jpg";
      if(file_exists($d3)){
          $_3rd_source_display_html="
            <div class='container'>
                <div id='aSide'>
                    <model-viewer src='$d3' alt='LESSE CHAIR' auto-rotate camera-controls ar ios-src='$d3'>
                    </model-viewer>
                </div>
            </div>
          ";
          $_3rd_icon_display_html = "
           <img class='img-fluid' src='$d3_icon' alt='...'>
          ";
      }else{
        $_3rd_source_display_html="
          <img class='img-fluid' src='$i3' alt='...'>
        ";
        $_3rd_icon_display_html = "
           <img class='img-fluid' src='$i3' alt='...'>
          ";
      }
    }
  }else{ 
    header('location: 404.php');
  }
?>
<!DOCTYPE html>
<script>
  var currentProduct = <?php echo "$id"; ?>;
</script>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- SHOP DETAIL SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row g-5">
            <div class="col-lg-9">
              <p class="lead mb-4"><?php echo $page_dsc;?></p>
              <!-- <div class="text-center mb-5"><a class="text-muted text-center small text-uppercase text-decoration-underline" href="#details">Scroll to product details, material &amp; care and sizing</a></div> -->
              <div class="row gy-5 align-items-stretch">
                <!-- PRODUCTS SLIDER-->
                <div class="col-lg-6">
                  <div class="swiper-container shop-detail-slider">
                    <div class="swiper-wrapper">
                      <div class="swiper-slide"><img class="img-fluid" src="<?php echo $i1;?>" alt="..."></div>
                      <div class="swiper-slide"><img class="img-fluid" src="<?php echo $i2;?>" alt="..."></div>
                      <div class="swiper-slide"><?php echo $_3rd_source_display_html;?></div>
                    </div>
                  </div>
                </div>
                <!-- FPRODUCT INFO-->
                <div class="col-lg-6 d-flex flex-column justify-content-between">
                  <div class="p-4 p-lg-5 border mb-5 bg-light">
                    <form id="add_to_cart_form">
                       <h3 class="text-center mb-3">Quantity</h3>
                      <select id = "product_quantity" class="form-select js-sizes mb-5" data-customclass="bg-white rounded-0 border-2 text-uppercase border-gray-200">
                        <?php for($i=1; $i<100; $i++){ echo "<option>$i</option>";} ?>
                      </select> 
                      <p class="h1 text-muted mb-4 text-center fw-normal"> <del class="text-gray-500 me-2"><?php echo ($product_data['oldprice']) ? '€'.$product_data['oldprice']:'';?></del><?php echo '€'.$product_data['price']; ?></p>
                      <p class="h1 text-muted mb-4 text-center fw-normal"> <?php echo ($isShippingFree) ? 'Free Shipping' : ''; ?></p>
                      <p class="text-center">
                        <button id = "add_to_cart_btn" class="btn btn-outline-primary" type="submit"><i class="fas fa-shopping-cart"></i> Add to cart</button>
                        <button id = "add_to_wish_btn" class="btn btn-secondary" type="submit" data-bs-toggle="tooltip" data-placement="top" title="Add to wishlist"><i class="far fa-heart"></i></button>
                      </p>
                    </form>
                  </div>
                  <script src = "system/js/add_to_cart_and_whishlist.js"></script>
                  <!-- SLIDERS THUMBS -->
                  <div class="swiper-container shop-detail-slider-thumbs w-100">
                    <div class="swiper-wrapper">
                      <div class="swiper-slide swiper-slide-thumb flex-fill"><img class="img-fluid" src="<?php echo $product_data['dir']."/main.jpg";?>" alt="..."></div>
                      <div class="swiper-slide swiper-slide-thumb flex-fill"><img class="img-fluid" src="<?php echo $product_data['dir']."/0.jpg";?>" alt="..."></div>
                      <div class="swiper-slide swiper-slide-thumb flex-fill"><?php echo $_3rd_icon_display_html; ?>
                    </div>
                  </div>
                </div>
              </div>
              <!-- PRODUCT INFO-->
              <div id="details">
                <div class="border-top border-bottom py-4 px-0 px-lg-4 my-5">
                  <h4>Product details</h4>
                  <p><?php echo $details;?></p>
                  <h4>Material &amp; care</h4>
                  <ul>
                    <?php echo $materialsHTML; ?>
                  </ul>
                  <h4>Size &amp; Fit</h4>
                  <ul class="mb-4">
                    <li><?php echo $dimensionsHTML;?></li>
                    <li><?php echo $weightHTML;?></li>
                  </ul>
                  <h4><?php echo ($isShippingFree) ? 'FREE SHIPPING WORLDWIDE' : 'Standart €10 shipping to Europe'; ?></h4>
                  <?php echo ($isShippingFree) ? '' : '<ul class="mb-4"><li>contact us for orders elsewhere</li></ul>' ?>
                  <!-- <figure class="p-4 bg-light border-start border-4 border-primary">
                    <blockquote class="blockquote">
                      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.</p>
                    </blockquote>
                    <figcaption class="blockquote-footer mb-0">Someone famous in 
                      <cite title="Source Title">Source Title</cite>
                    </figcaption>
                  </figure> -->
                </div>
                <?php #require_once('inc/suggestions_after_product.php'); ?>
              </div>
            </div>
            <!-- CHECKOUT SIDEBAR [ORDER SUMMARY]                                        -->
            <?php #require_once('inc/product_sidebar.php'); ?>
          </div>
        </div>
          
        </div>
      </section>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>