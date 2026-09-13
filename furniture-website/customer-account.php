<?php require_once('init.php') ?>
<?php  if(!isset($_SESSION['in'])) header('location: customer-register.php'); ?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- CUSTOMER ACCOUNT SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row gy-5">
            <div class="col-lg-9">
              <p class="lead mb-4">Change your personal details or your password here.</p>
              <!-- <p class="text-muted mb-5">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p> -->
              <!-- CHANGE PASSWORD FORM-->
              <form class="py-4 border-top border-bottom mb-5" id = "change_pass">
                <div class="row">
                  <div class="col-12 mb-4">
                    <h3 class="text-uppercase lined">Change password</h3>
                  </div>
                  <div class="col-lg-6 mb-3">
                    <label class="form-label" for="password_old">Old password</label>
                    <input class="form-control" id="password_old" type="password" name="password_old">
                  </div>
                  <div class="col-lg-6 mb-3">
                    <label class="form-label" for="password_new">New password</label>
                    <input class="form-control" id="password_new" type="password" name="password_new">
                  </div>
                </div>
                <div class="row">
                  <div class="col-lg-12 text-center">
                    <button id = "change_pass_btn" class="btn btn-outline-primary" type="button"> <i class="fas fa-save me-2"></i>Save new password</button>
                    <p class="text-center text-muted small"><span id = 'change_pass_error_par' style="color: red"></span></p>
                  </div>
                </div>
              </form>
              <!-- PROFILE DETAIL FORM-->
              <form class="py-4" action="#" id = "user_data_from">
                  <div class="col-12 mb-4">
                    <h3 class="text-uppercase lined">Profile detail</h3>
                  </div>
                  <?php include 'inc/form.php'; ?>
                  <button class="btn btn-outline-primary" id = "user_data_form_btn"type="button"> <i class="fas fa-save me-2"></i>Save changes</button>
                  <p class="text-center text-muted small"><span id = 'user_data_form_error_par' style="color: red"></span></p>
              </form>
            </div>
            <div class="col-lg-3">
              <h3 class="h4 text-uppercase lined mb-4">My profile</h3>
              <nav class="nav flex-column nav-pills">
                <a class="nav-link text-sm" href="customer-orders.php"><i class="me-2 fas fa-list"></i><span>My orders</span></a>
                <a class="nav-link text-sm" href="customer-wishlist.php"> <i class="me-2 fas fa-heart"></i><span>My wishlist</span></a>
                <a class="nav-link text-sm active" href="customer-account.php"> <i class="me-2 fas fa-user"></i><span>My account</span></a>
                <a class="nav-link text-sm" href="logout.php"> <i class="me-2 fas fa-door-open"></i><span>Logout</span></a>
              </nav>
            </div>
          </div>
        </div>
      </section>
      <script src = "system/js/update_data.js"></script>
      <script src = "system/js/change_pass.js"></script>
      
      <?php require_once('inc/footer.php'); ?>
   </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>