<!-- this initialises the last two sections of the code: get it and the footer -->
<?php require_once('globals.php');?>  
<!--<div class="bg-primary py-5 text-white">
        <div class="container text-center">
          <div class="row">
            <div class="col-lg-8 p-3">
              <h3 class="text-uppercase mb-0">Do you want cool website like this one?</h3>
            </div>
            <div class="col-lg-4 p-3">   <a class="btn btn-outline-light" href="#">Buy this template now</a></div>
          </div>
        </div>
      </div>
      
      
      </div>-->
<footer>
        <!-- MAIN FOOTER-->
        <div class="bg-gray-700 text-white py-5">
          <div class="container py-4">
            <div class="row gy-4">
              <div class="col-lg-3">
                <h4 class="mb-3 text-uppercase">About Us</h4>
                <p class="text-sm text-gray-500">Lock Furniture is a company for product design and flat-pack furniture manufacturing</p>
                <hr>
                <h4 class="h6 text-uppercase">Join Our Monthly Newsletter</h4>
                <form>
                  <div class="input-group border mb-3">
                    <input class="form-control bg-none border-0 shadow-0 text-white" type="email" placeholder="Email address" aria-label="Email address" aria-describedby="button-submit">
                    <button class="btn btn-outline-light bg-none border-0" id="button-submit" type="button"><i class="fas fa-paper-plane"></i></button>
                  </div>
                </form>
              </div>
              <div class="col-lg-3">
                <h4 class="mb-3 text-uppercase">Blog</h4>
                <ul class="list-unstyled">
                  <li class="d-flex align-items-center mb-2"><a <?php if($ftr_blog_target_blank[0]) echo 'target="_blank"';?> href="<?php echo $ftr_blog_link[0]?>"><img class="img-fluid" src="<?php echo $ftr_blog_img[0]; ?>" alt="..." width="40"></a>
                    <div class="ms-2">
                      <h6 class="text-uppercase mb-0"> <a class="text-reset" href="<?php echo $ftr_blog_link[0]?>"><?php echo $ftr_blog_title[0]?></a></h6>
                    </div>
                  </li>
                  <li class="d-flex align-items-center mb-2"><a <?php if($ftr_blog_target_blank[1]) echo 'target="_blank"';?> href="<?php echo $ftr_blog_link[1]?>"><img class="img-fluid" src="<?php echo $ftr_blog_img[1]; ?>" alt="..." width="40"></a>
                    <div class="ms-2">
                      <h6 class="text-uppercase mb-0"> <a <?php if($ftr_blog_target_blank[1]) echo 'target="_blank"';?> class="text-reset" href="<?php echo $ftr_blog_link[1]?>"><?php echo $ftr_blog_title[1]?></a></h6>
                    </div>
                  </li>
                  <li class="d-flex align-items-center mb-2"><a <?php if($ftr_blog_target_blank[2]) echo 'target="_blank"';?> href="<?php echo $ftr_blog_link[2]?>"><img class="img-fluid" src="<?php echo $ftr_blog_img[2]; ?>" alt="..." width="40"></a>
                    <div class="ms-2">
                      <h6 class="text-uppercase mb-0"> <a <?php if($ftr_blog_target_blank[2]) echo 'target="_blank"';?> class="text-reset" href="<?php echo $ftr_blog_link[2]?>"><?php echo $ftr_blog_title[2]?></a></h6>
                    </div>
                  </li>
                </ul>
              </div>
              <div class="col-lg-3">
                <h4 class="mb-3 text-uppercase">Contact</h4>
                <p class="text-uppercase text-sm text-gray-500"><strong>Lock Furniture</strong><br>Positano str. 1<br>Sofia <br><strong>Bulgaria</strong></p><a class="btn btn-primary" href="contact2.php">Go to contact page</a>
              </div>
              <div class="col-lg-3">
                <ul class="list-inline mb-0">
                  <!-- <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare.jpg" alt="..." width="70"></a></li>
                  <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare2.jpg" alt="..." width="70"></a></li>
                  <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare3.jpg" alt="..." width="70"></a></li>
                  <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare3.jpg" alt="..." width="70"></a></li>
                  <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare2.jpg" alt="..." width="70"></a></li>
                  <li class="list-inline-item mb-2 me-2 pb-1"><a href="#"><img class="img-fluid" src="img/detailsquare.jpg" alt="..." width="70"></a></li> -->
                </ul>
              </div>
            </div>
          </div>
        </div>
        <!-- COPYRIGHTS                -->
        <div class="bg-dark py-5">
          <div class="container">
            <div class="row align-items-cenrer gy-3 text-center">
              <div class="col-md-6 text-md-start">
                <p class="mb-0 text-sm text-gray-500">&copy; 2021. Your company / name goes here </p>
                <button class="btn btn-sm btn-link" type="button">Photography</button>
                <button class="btn btn-sm btn-link" type="button">Terms of use</button>
                <button class="btn btn-sm btn-link" type="button">Privacy policy</button>
              </div>
              <div class="col-md-6 text text-md-end">
                <p class="mb-0 text-sm text-gray-500">Template designed by  <a href="https://bootstrapious.com" target="_blank">Bootstrapious</a> &amp; <a href="https://hikershq.com/" target="_blank">HHQ</a> </p>
                <!-- Please do not remove the backlink to us unless you purchase the Attribution-free License at https://bootstrapious.com/attribution-free-license. Thank you.-->
              </div>
            </div>
          </div>
        </div>
      </footer>