<!-- add nav and BREADCRUMB -->
<?php require_once('globals.php');?>     
  <header class="nav-holder make-sticky">
    <div class="navbar navbar-light bg-white navbar-expand-lg py-0" id="navbar">
      <div class="container py-3 py-lg-0 px-lg-0">
        <!-- Navbar brand--><a class="navbar-brand" href="index.php"><img class="d-none d-md-inline-block" src="assets/img/logo-navbar.png" alt="Lock logo"><img class="d-inline-block d-md-none" src="assets/img/logo-navbar-small.png" alt="Lock logo"><span class="sr-only">Lock - go to homepage</span></a>
        <!-- Navbar toggler-->
        <button class="navbar-toggler text-primary border-primary" type="button" data-bs-toggle="collapse" data-bs-target="#navigationCollapse" aria-controls="navigationCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="sr-only">Toggle navigation</span><i class="fas fa-align-justify"></i></button>
        <!-- Collapsed Navigation    -->
        <div class="collapse navbar-collapse" id="navigationCollapse">
          <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
            
            <!-- products menu-->
            <li class="nav-item dropdown menu-large"><a class="nav-link dropdown-toggle active" id="allPagesMegamenu" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Products</a>
                <ul class="dropdown-menu megamenu p-4" aria-labelledby="allPagesMegamenu">
                <li>
                  <div class="row">
                    <div class="col-md-6 col-lg-3">
                      <h5 class="text-dark text-uppercase pb-2 border-bottom">Series</h5>
                      <ul class="list-unstyled mb-3">
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?series[]=lesse">Lesse</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?series[]=frame">Frame</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?series[]=space">Space</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?series[]=arch">Arch</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?series[]=android">Android </a></li>
                        <!-- <div class="col-lg-6"><img class="img-fluid d-none d-lg-block" src="img/series-nav.jpg" alt=""></div> -->
                      </ul>
                    </div>
                    <div class="col-md-6 col-lg-3">
                      <h5 class="text-dark text-uppercase pb-2 border-bottom">Furniture</h5>
                      <ul class="list-unstyled mb-3">
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?type[]=chair">Seating</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?type[]=table">Tables</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?id[]=27">Sofas</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?type[]=store">Storage</a></li>
                        <!-- <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Other</a></li> -->
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?">All products</a></li>  
                      </ul>
                      
                    </div>
                    <div class="col-md-6 col-lg-3">
                      <h5 class="text-dark text-uppercase pb-2 border-bottom">Acessories</h5>
                      <ul class="list-unstyled mb-3">
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Vases</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Stands</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Trees</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Candlesticks</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="#">Other</a></li>
                        <!-- <div class="col-lg-6"><img class="img-fluid d-none d-lg-block" src="img/accessories-nav.jpg" alt=""></div> -->
                      </ul>
                    </div>
                    <div class="col-md-6 col-lg-3">
                      <h5 class="text-dark text-uppercase pb-2 border-bottom">Lightning</h5>
                      <ul class="list-unstyled mb-3">
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?type[]=lamp">Hanging Lamps</a></li>
                        <li class="nav-item"><a class="nav-link-sub py-2 text-uppercase" href="shop-category-left.php?type[]=lamp">Floor Lamps</a></li>
                        <!-- <div class="col-lg-6"><img class="img-fluid d-none d-lg-block" src="img/lightning-nav.jpg" alt=""></div> -->
                      </ul>
                    </div>
                  </div>
                </li>
              </ul> 
            </li>
            <!-- Blog button -->
            <!-- <a class="nav-link" id="featuresMegamenu" href="blog-medium.php">Blog</a> -->
            <!-- contact-page -->
            <a class="nav-link" id="featuresMegamenu" href="contact2.php">Contact</a>
            <!-- <li class="nav-item dropdown"><a class="nav-link dropdown-toggle" id="hpDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">About us</a>
                <ul class="dropdown-menu" aria-labelledby="hpDropdown">
                <li><a class="dropdown-item text-uppercase border-bottom" href="about.php">About us</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-wishlist.php">Our team</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="shop-basket.php">Partners</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-orders.php">Why to choose us?</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="contact2.php">Contact us</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-orders.php">FAQ</a></li>
              </ul>
            </li> -->
            <a class="nav-link" id="contactMegamenu" href="shop-basket.php">Shopping Cart</a>
            <?php 
            echo '
            <li class="nav-item dropdown"><a class="nav-link dropdown-toggle" id="hpDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Profile</a>
                <ul class="dropdown-menu" aria-labelledby="hpDropdown">
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-account.php">My Account</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-wishlist.php"> My Whishlist</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="customer-orders.php">My orders</a></li>
                <li><a class="dropdown-item text-uppercase border-bottom" href="logout.php">Log out</a></li>
              </ul>
            </li>';
            ?>
          </ul>
        </div>
      </div>
    </div>
  </header>

<?php 
  if($_SERVER["PHP_SELF"]!=$pages_assoc_with_name["index"]){ ?>
  <div class="wide" id="all">
    <!-- HEADING BREADCRUMB-->
    <section class="bg-pentagon py-4">
            <div class="container py-3">
              <div class="row d-flex align-items-center gy-4">
                <div class="col-md-7">
                  <h1 class="h2 mb-0 text-uppercase"><?php echo $title;?></h1>
                </div>
                <div class="col-md-5">
                  <!-- Breadcrumb-->
                  <ol class="text-sm justify-content-start justify-content-lg-end mb-0 breadcrumb undefined">
                    <li class="breadcrumb-item"><a class="text-uppercase" href="index.html">Home</a></li>
                    <li class="breadcrumb-item text-uppercase active"><?php echo $title ?></li>
                  </ol>
                </div>
              </div>
            </div>
      </section>
<?php } ?>