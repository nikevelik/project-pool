<?php require_once('init.php') ?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- BLOG SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <div class="row gy-5">
            <div class="col-lg-8">
              <h2 class="text-uppercase lined mb-4">We are here to help you</h2>
              <p class="lead mb-5">Are you curious about something? Do you have some kind of problem with our products? As am hastily invited settled at limited civilly fortune me. Really spring in extent an by. Judge but built gay party world. Of so am he remember although required. Bachelor unpacked be advanced at. Confined in declared marianne is vicinity.</p>
              <p class="text-sm mb-4">Please feel free to contact us, our customer service center is working for you 24/7.</p>
              <!-- CONTACT FORM-->
              <h2 class="lined text-uppercase mb-4">Contact form</h2>
              <form action="system/php/requests.php" method="POST">
                <input type="hidden" value = "contactform" name = "request">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label" for="firstname">First Name</label>
                    <input class="form-control" name = "fname" id="firstname" type="text">
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label" for="lastname">Last Name</label>
                    <input class="form-control" name = "lname"  id="lastname" type="text">
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label" for="emailaddress">Email Address</label>
                    <input class="form-control" name = "email" id="emailaddress" type="email">
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label" for="subject">Subject</label>
                    <input class="form-control" name = "subject" id="subject" type="text">
                  </div>
                  <div class="col-md-12 mb-3">
                    <label class="form-label" for="message">Message</label>
                    <textarea class="form-control" name = "text" id="message" rows="4"></textarea>
                  </div>
                  <div class="col-md-12 text-center">
                    <button class="btn btn-outline-primary" type="submit"><i class="far fa-envelope me-2"></i>Send message</button>
                  </div>
                </div>
              </form>
            </div>
            <!-- CONTACT INFO-->
            <div class="col-lg-4">
              <h3 class="text-uppercase mb-3">Address</h3>
              <p class="text-sm mb-4">13/25 New Avenue<br>New Heaven<br>45Y 73J<br>England, <br><strong>Great Britain</strong></p>
              <h3 class="text-uppercase mb-3">Call center</h3>
              <p class="text-gray-600 text-sm">This number is toll free if calling from Great Britain otherwise we advise you to use the electronic form of communication.</p>
              <p class="text-sm mb-4"><strong>+33 555 444 333</strong></p>
              <h3 class="text-uppercase mb-3">Electronic support</h3>
              <p class="text-gray-600 text-sm">Please feel free to write an email to us or to use our electronic ticketing system.</p>
              <ul class="text-sm mb-0">
                <li><strong><a href="mailto:">info@fakeemail.com</a></strong></li>
                <li><strong><a href="#">Ticketio</a></strong> - our ticketing support platform    </li>
              </ul>
            </div>
          </div>
        </div>
      </section>
      <!-- MAP SECTION-->
      <!-- <div class="border-top border-primary" id="contactMap"></div> -->
      <?php require_once('inc/footer.php'); ?>
    </div>
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>