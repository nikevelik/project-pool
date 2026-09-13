<?php require_once('init.php') ?>
<!DOCTYPE html>
<html>
  <?php require_once('inc/head.php');?>
  <body>
    <div class="wide" id="all">
      <?php require_once('inc/topbar.php');?>
      
      <?php require_once('system/modals/login_modal.php'); ?>
      <?php require_once('inc/navbar.php')?>
      <!-- ABOUT SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <header class="mb-5">
            <h2 class="text-uppercase lined mb-4">About Universal </h2>
            <p class="lead">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.</p>
          </header>
          <div class="row gy-4">
            <div class="col-lg-8">
              <div class="accordion mb-5" id="aboutAccordion">
                      <div class="accordion-item mb-2">
                        <h5 class="accordion-header" id="aboutAccordion-headingOne">
                          <button class="accordion-button text-uppercase fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#aboutAccordion-collapseOne" aria-expanded="true" aria-controls="aboutAccordion-collapseOne">Accordion Item No.1</button>
                        </h5>
                        <div class="accordion-collapse collapse show" id="aboutAccordion-collapseOne" aria-labelledby="aboutAccordion-collapseOne" data-bs-parent="#aboutAccordion">
                          <div class="accordion-body">
                            <div class="row">
                              <div class="col-md-4"><img class="img-fluid" src="assets/img/template-easy-customize.png" alt="..."></div>
                              <div class="col-md-8">
                                <p>One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</p>
                                <p>One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</p>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="accordion-item mb-2">
                        <h5 class="accordion-header" id="aboutAccordion-headingTwo">
                          <button class="accordion-button text-uppercase fw-bold collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#aboutAccordion-collapseTwo" aria-expanded="false" aria-controls="aboutAccordion-collapseTwo">Accordion Item No.2</button>
                        </h5>
                        <div class="accordion-collapse collapse" id="aboutAccordion-collapseTwo" aria-labelledby="aboutAccordion-collapseTwo" data-bs-parent="#aboutAccordion">
                          <div class="accordion-body">
                            <div class="row">
                              <div class="col-md-4"><img class="img-fluid" src="assets/img/template-easy-code.png" alt="..."></div>
                              <div class="col-md-8">
                                <p>One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</p>
                                <p>One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</p>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="accordion-item">
                        <h5 class="accordion-header" id="aboutAccordion-headingThree">
                          <button class="accordion-button text-uppercase fw-bold collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#aboutAccordion-collapseThree" aria-expanded="false" aria-controls="aboutAccordion-collapseThree">Accordion Item No.1 a little too small</button>
                        </h5>
                        <div class="accordion-collapse collapse" id="aboutAccordion-collapseThree" aria-labelledby="aboutAccordion-collapseThree" data-bs-parent="#aboutAccordion">
                          <div class="accordion-body">One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</div>
                        </div>
                      </div>
              </div>
            </div>
            <div class="col-lg-4">
              <div class="ratio ratio-4x3">
                <iframe class="embed-responsive-item" src="//www.youtube.com/embed/upZJpGrppJA"></iframe>
              </div>
            </div>
          </div>
        </div>
      </section>
      <!-- END-->
      <!-- BLOCKS SECTION-->
      <section class="pb-5">
        <div class="container pb-4">
          <div class="row gy-3">
            <div class="col-lg-4">
              <h2 class="lined text-uppercase mb-4">Our skills</h2>
              <div class="progress mb-3" style="height: 20px">
                <div class="progress-bar" role="progressbar" style="width: 85%;" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100">Search Engine Marketing</div>
              </div>
              <div class="progress mb-3" style="height: 20px">
                <div class="progress-bar" role="progressbar" style="width: 90%;" aria-valuenow="90" aria-valuemin="0" aria-valuemax="100">SEO</div>
              </div>
              <div class="progress mb-3" style="height: 20px">
                <div class="progress-bar" role="progressbar" style="width: 100%;" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100">Webdesign</div>
              </div>
            </div>
            <div class="col-lg-4">
              <h2 class="lined text-uppercase mb-4">Our srvices</h2>
              <ul class="list-unstyled">
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
                </li>
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Aliquam tincidunt mauris eu risus.</p>
                </li>
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Vestibulum auctor dapibus neque.</p>
                </li>
              </ul>
            </div>
            <div class="col-lg-4">
              <h2 class="lined text-uppercase mb-4">Our values</h2>
              <ul class="list-unstyled">
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
                </li>
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Aliquam tincidunt mauris eu risus.</p>
                </li>
                <li class="d-flex mb-3">
                  <div class="icon-filled me-2"><i class="fas fa-check"></i></div>
                  <p class="text-sm mb-0">Vestibulum auctor dapibus neque.</p>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>
      <!-- END-->
      <!-- SHOWCASE SECTION-->
      <section class="py-5 bg-pentagon border-top border-bottom border-gray-600">
        <div class="container py-4">
          <!-- Counters-->
          <div class="row gy-4 text-center" id="counterUp">
            <div class="col-lg-3 col-sm-6">
              <div class="text-center text-gray-700">
                <div class="icon-outlined border-gray-600 icon-sm mx-auto mb-3 icon-thin"><i class="fas fa-align-justify"></i></div>
                <h4 class="h1 counter mb-3" data-counter="580">0</h4>
                <p class="text-uppercase fw-bold mb-0">Websites</p>
              </div>
            </div>
            <div class="col-lg-3 col-sm-6">
              <div class="text-center text-gray-700">
                <div class="icon-outlined border-gray-600 icon-sm mx-auto mb-3 icon-thin"><i class="fas fa-users"></i></div>
                <h4 class="h1 counter mb-3" data-counter="100">0</h4>
                <p class="text-uppercase fw-bold mb-0">Satisfied Clients</p>
              </div>
            </div>
            <div class="col-lg-3 col-sm-6">
              <div class="text-center text-gray-700">
                <div class="icon-outlined border-gray-600 icon-sm mx-auto mb-3 icon-thin"><i class="far fa-copy"></i></div>
                <h4 class="h1 counter mb-3" data-counter="320">0</h4>
                <p class="text-uppercase fw-bold mb-0">Projects</p>
              </div>
            </div>
            <div class="col-lg-3 col-sm-6">
              <div class="text-center text-gray-700">
                <div class="icon-outlined border-gray-600 icon-sm mx-auto mb-3 icon-thin"><i class="fas fa-drafting-compass"></i></div>
                <h4 class="h1 counter mb-3" data-counter="923">0</h4>
                <p class="text-uppercase fw-bold mb-0">Magazines and Brochures</p>
              </div>
            </div>
          </div>
        </div>
      </section>
      <!-- END-->
      <!-- OUR TEAM SECTION-->
      <section class="py-5">
        <div class="container py-4">
          <header class="mb-5">
            <h2 class="lined text-uppercase">Meet our team</h2>
          </header>
          <div class="row gy-4">
            <!-- Team member  -->
            <div class="col-lg-3 col-md-6 text-center"><a href="team-member.php"><img class="avatar avatar-xxl p-2 mb-4" src="assets/img/person-1.jpg" alt="Han Solo"></a>
              <h3 class="h4 mb-1 text-uppercase"><a class="text-reset" href="team-member.php">Han Solo</a></h3>
              <p class="text-muted small text-uppercase">Founder</p>
              <ul class="list-inline">
                <li class="list-inline-item"><a class="social-link facebook" href="#"><i class="fab fa-facebook-f"></i></a></li>
                <li class="list-inline-item"><a class="social-link twitter" href="#"><i class="fab fa-twitter"></i></a></li>
                <li class="list-inline-item"><a class="social-link youtube" href="#"><i class="fab fa-youtube"></i></a></li>
                <li class="list-inline-item"><a class="social-link email" href="#"><i class="fas fa-envelope"></i></a></li>
              </ul>
              <p class="small small text-gray-600">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            </div>
            <!-- Team member  -->
            <div class="col-lg-3 col-md-6 text-center"><a href="team-member.php"><img class="avatar avatar-xxl p-2 mb-4" src="assets/img/person-2.jpg" alt="Luke Skywalker"></a>
              <h3 class="h4 mb-1 text-uppercase"><a class="text-reset" href="team-member.php">Luke Skywalker</a></h3>
              <p class="text-muted small text-uppercase">CTO</p>
              <ul class="list-inline">
                <li class="list-inline-item"><a class="social-link facebook" href="#"><i class="fab fa-facebook-f"></i></a></li>
                <li class="list-inline-item"><a class="social-link twitter" href="#"><i class="fab fa-twitter"></i></a></li>
                <li class="list-inline-item"><a class="social-link youtube" href="#"><i class="fab fa-youtube"></i></a></li>
                <li class="list-inline-item"><a class="social-link email" href="#"><i class="fas fa-envelope"></i></a></li>
              </ul>
              <p class="small small text-gray-600">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            </div>
            <!-- Team member  -->
            <div class="col-lg-3 col-md-6 text-center"><a href="team-member.php"><img class="avatar avatar-xxl p-2 mb-4" src="assets/img/person-3.png" alt="Princess Leia"></a>
              <h3 class="h4 mb-1 text-uppercase"><a class="text-reset" href="team-member.php">Princess Leia</a></h3>
              <p class="text-muted small text-uppercase">Team Leader</p>
              <ul class="list-inline">
                <li class="list-inline-item"><a class="social-link facebook" href="#"><i class="fab fa-facebook-f"></i></a></li>
                <li class="list-inline-item"><a class="social-link twitter" href="#"><i class="fab fa-twitter"></i></a></li>
                <li class="list-inline-item"><a class="social-link youtube" href="#"><i class="fab fa-youtube"></i></a></li>
                <li class="list-inline-item"><a class="social-link email" href="#"><i class="fas fa-envelope"></i></a></li>
              </ul>
              <p class="small small text-gray-600">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            </div>
            <!-- Team member  -->
            <div class="col-lg-3 col-md-6 text-center"><a href="team-member.php"><img class="avatar avatar-xxl p-2 mb-4" src="assets/img/person-4.jpg" alt="Jabba Hut"></a>
              <h3 class="h4 mb-1 text-uppercase"><a class="text-reset" href="team-member.php">Jabba Hut</a></h3>
              <p class="text-muted small text-uppercase">Lead Developer</p>
              <ul class="list-inline">
                <li class="list-inline-item"><a class="social-link facebook" href="#"><i class="fab fa-facebook-f"></i></a></li>
                <li class="list-inline-item"><a class="social-link twitter" href="#"><i class="fab fa-twitter"></i></a></li>
                <li class="list-inline-item"><a class="social-link youtube" href="#"><i class="fab fa-youtube"></i></a></li>
                <li class="list-inline-item"><a class="social-link email" href="#"><i class="fas fa-envelope"></i></a></li>
              </ul>
              <p class="small small text-gray-600">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.</p>
            </div>
          </div>
        </div>
      </section>
      <!-- END-->
      <!-- OUR CLIENTS SECTION-->
      <section class="py-5 bg-gray-200">
        <div class="container py-4">
          <header class="mb-5">
            <h2 class="text-uppercase lined lined-center mb-4">Our clients </h2>
          </header>
          <!-- Customer slider-->
          <div class="swiper-container customers-slider">
            <div class="swiper-wrapper">
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-1.png" alt="..." width="140"></div>
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-2.png" alt="..." width="140"></div>
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-3.png" alt="..." width="140"></div>
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-4.png" alt="..." width="140"></div>
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-5.png" alt="..." width="140"></div>
              <div class="swiper-slide h-auto"><img class="img-fluid img-grayscale d-block mx-auto" src="assets/img/customer-6.png" alt="..." width="140"></div>
            </div>
          </div>
        </div>
      </section>
      <!-- END-->
      <!-- CUSTOMERS SECTION-->
      <section class="py-5 bg-pentagon border-top border-gray-600">
        <div class="container py-4">
          <header class="mb-5">
            <h2 class="lined text-uppercase mb-4">What our customers say</h2>
            <p class="lead">We have worked with many clients and we always like to hear they come out from the cooperation happy and satisfied. Have a look what our clients said about us.</p>
          </header>
          <!-- Testimonials slider-->
          <div class="swiper-container testimonials-slider">
            <div class="swiper-wrapper">
              <div class="swiper-slide h-auto mb-5">
                <div class="p-4 bg-white h-100 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <p class="text-sm text-gray-600">One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections.</p>
                    <p class="text-sm text-gray-600"></p>
                  </div>
                  <div class="d-flex align-items-center justify-content-between"><i class="fas fa-quote-left text-primary fa-2x"></i>
                    <div class="d-flex align-items-center ms-3">
                      <div class="me-3 text-end">
                        <h5 class="text-uppercase mb-0">John McIntyre</h5>
                        <p class="small text-muted mb-0">CEO, transTech</p>
                      </div><img class="avatar p-1 flex-shrink-0" src="assets/img/person-1.jpg" alt="John McIntyre" width="60">
                    </div>
                  </div>
                </div>
              </div>
              <div class="swiper-slide h-auto mb-5">
                <div class="p-4 bg-white h-100 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <p class="text-sm text-gray-600">The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. &quot;What's happened to me? &quot; he thought. It wasn't a dream.</p>
                    <p class="text-sm text-gray-600"></p>
                  </div>
                  <div class="d-flex align-items-center justify-content-between"><i class="fas fa-quote-left text-primary fa-2x"></i>
                    <div class="d-flex align-items-center ms-3">
                      <div class="me-3 text-end">
                        <h5 class="text-uppercase mb-0">John McIntyre</h5>
                        <p class="small text-muted mb-0">CEO, transTech</p>
                      </div><img class="avatar p-1 flex-shrink-0" src="assets/img/person-2.jpg" alt="John McIntyre" width="60">
                    </div>
                  </div>
                </div>
              </div>
              <div class="swiper-slide h-auto mb-5">
                <div class="p-4 bg-white h-100 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <p class="text-sm text-gray-600">His room, a proper human room although a little too small, lay peacefully between its four familiar walls.</p>
                    <p class="text-sm text-gray-600">A collection of textile samples lay spread out on the table - Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded frame.</p>
                  </div>
                  <div class="d-flex align-items-center justify-content-between"><i class="fas fa-quote-left text-primary fa-2x"></i>
                    <div class="d-flex align-items-center ms-3">
                      <div class="me-3 text-end">
                        <h5 class="text-uppercase mb-0">John McIntyre</h5>
                        <p class="small text-muted mb-0">CEO, transTech</p>
                      </div><img class="avatar p-1 flex-shrink-0" src="assets/img/person-3.png" alt="John McIntyre" width="60">
                    </div>
                  </div>
                </div>
              </div>
              <div class="swiper-slide h-auto mb-5">
                <div class="p-4 bg-white h-100 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <p class="text-sm text-gray-600">It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull weather. Drops of rain could be heard hitting the pane, which made him feel quite sad.</p>
                    <p class="text-sm text-gray-600"></p>
                  </div>
                  <div class="d-flex align-items-center justify-content-between"><i class="fas fa-quote-left text-primary fa-2x"></i>
                    <div class="d-flex align-items-center ms-3">
                      <div class="me-3 text-end">
                        <h5 class="text-uppercase mb-0">John McIntyre</h5>
                        <p class="small text-muted mb-0">CEO, transTech</p>
                      </div><img class="avatar p-1 flex-shrink-0" src="assets/img/person-4.jpg" alt="John McIntyre" width="60">
                    </div>
                  </div>
                </div>
              </div>
              <div class="swiper-slide h-auto mb-5">
                <div class="p-4 bg-white h-100 d-flex flex-column justify-content-between">
                  <div class="mb-2">
                    <p class="text-sm text-gray-600">It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull weather. Drops of rain could be heard hitting the pane, which made him feel quite sad. Gregor then turned to look out the window at the dull weather. Drops of rain could be heard hitting the pane, which made him feel quite sad.</p>
                    <p class="text-sm text-gray-600"></p>
                  </div>
                  <div class="d-flex align-items-center justify-content-between"><i class="fas fa-quote-left text-primary fa-2x"></i>
                    <div class="d-flex align-items-center ms-3">
                      <div class="me-3 text-end">
                        <h5 class="text-uppercase mb-0">John McIntyre</h5>
                        <p class="small text-muted mb-0">CEO, transTech</p>
                      </div><img class="avatar p-1 flex-shrink-0" src="assets/img/person-1.jpg" alt="John McIntyre" width="60">
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="swiper-pagination"></div>
          </div>
        </div>
      </section>
      <!-- END-->
      <?php require_once('inc/footer.php'); ?>
    </div> 
    <?php require_once ('inc/load_last.php'); ?>
  </body>
</html>