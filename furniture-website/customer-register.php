<?php require_once('init.php') ?>
<?php 
  if(isset($_SESSION['in'])){
    header('location: index.php');
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
      <!-- CREDENTIALS SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row g-5">
            <!-- REGISTER BLOCK-->
            <div class="col-lg-6">
              <header class="mb-5">
                <h2 class="text-uppercase mb-4">New account</h2>
                <p class="lead">Not our registered customer yet?</p>
              </header>
              <!-- <p>With registration with us new world of fashion, fantastic discounts and much more opens to you! The whole process will not take you more than a minute!</p> -->
              <p class="text-muted mb-4">If you have any questions, please feel free to <a href="contact2.php">contact us</a>, our customer service center is working for you 24/7.</p>
              <hr class="border-gray-200">
              <!-- REGISTER FORM-->
              <form id="sign_form">
                <div class="form-group mb-3">
                  <label class="form-label" for="username"></label>
                  <input class="form-control" id="username" type="text" name="username" placeholder="Name and Surname">
                </div>
                <div class="form-group mb-3">
                  <label class="form-label" for="useremail"></label>
                  <input class="form-control" id="useremail" type="text" name="useremail" placeholder="Email">
                </div>
                <div class="form-group mb-3">
                  <label class="form-label" for="userpassword"></label>
                  <input class="form-control" id="userpassword" type="password" name="userpassword" placeholder="Password">
                </div>      
                <div class="form-check d-flex">
                  <input class="form-check-input flex-shrink-0" type="radio" name="terms" id = "userterms" checked="true">
                  <label class="cursor-pointer d-block ms-3"><span class="h4 d-block mb-1 text-uppercase">I agree to the terms</span></label>
                </div>
                <div class="form-check d-flex">
                  <input class="form-check-input flex-shrink-0" type="radio" name="privacy" id = "userprivacy" checked="true">
                  <label class="cursor-pointer d-block ms-3"><span class="h4 d-block mb-1 text-uppercase">I agree to the privacy policy</span></label>
                </div>
                <div class="form-check d-flex">
                  <input class="form-check-input flex-shrink-0" type="checkbox" name="newsletter" id = "usernewsletter" checked="true">
                  <label class="cursor-pointer d-block ms-3" for="newsletter"><span class="h4 d-block mb-1 text-uppercase">add me to the newsletter</span></label>
                </div>
                <div class="form-group mb-3 text-center">
                  <button id = "sign_submit_btn" class="btn btn-outline-primary" type="submit"><i class="fas fa-user me-2"></i>Register</button>
                </div>
                 <p class="text-center text-muted small"><span id = 'signup_error_par' style="color: red"></span></p>
              </form>
            </div>
            <!-- LOGIN BLOCK                -->
             <div class="col-lg-6">
              <header class="mb-5">
                <h2 class="text-uppercase mb-4">Login</h2>
                <p class="lead">Already our customer?</p>
              </header>
              <p class="text-muted mb-4">Enter your credentials </p>
              <hr class="border-gray-200"> 
              <!-- LOGIN FORM-->
               <form id="log_form">
                <div class="form-group mb-3">
                  <label class="form-label" for="loginemail"></label>
                  <input class="form-control" id="loginemail" type="text" name="loginemail" placeholder="Enter your email address">
                </div>
                <div class="form-group mb-3">
                  <label class="form-label" for="loginpassword"></label>
                  <input class="form-control" id="loginpassword" type="text" name="loginpassword" placeholder="Enter your password">
                </div>
                <div class="form-group mb-3 text-center">
                  <button class="btn btn-outline-primary" id = "login_form_submit_btn" type="submit"><i class="fas fa-door-open me-2"></i>Log in</button>
                  <p class="text-center text-muted small"><span id = 'login_form_error_par' style="color: red"></span></p>
                  <p class="text-center text-muted small"><a href="#"><strong>Forgotten password?</strong></a></p>
                </div>
              </form>
            </div>
          </div>
        </div>
      </section>
      <script src = "system/js/signup.js"></script>
      <script src = "system/js/login_with_form.js"></script>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>