<div class="col-lg-3">
              <!-- Categories-->
              <!--<h3 class="h4 lined text-uppercase mb-4">Categories</h3>
              <ul class="nav flex-column nav-pills mb-4">
                <li class="nav-item"><a class="nav-link" href="shop-category.php"> 
                    <div class="d-flex align-items-center justify-content-between"><span class="fw-bold text-uppercase">Men</span>
                      <div class="badge bg-secondary">42</div>
                    </div></a>
                  <ul class="list-unstyled text-sm text-muted mb-0">
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">T-shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Pants</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Accessories</span></a></li>
                  </ul>
                </li>
                <li class="nav-item"><a class="nav-link active" href="shop-category.php"> 
                    <div class="d-flex align-items-center justify-content-between"><span class="fw-bold text-uppercase">Ladies</span>
                      <div class="badge bg-white text-primary">123</div>
                    </div></a>
                  <ul class="list-unstyled text-sm text-muted mb-0">
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">T-shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Pants</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Accessories</span></a></li>
                  </ul>
                </li>
                <li class="nav-item"><a class="nav-link" href="shop-category.php"> 
                    <div class="d-flex align-items-center justify-content-between"><span class="fw-bold text-uppercase">Kds</span>
                      <div class="badge bg-secondary">11</div>
                    </div></a>
                  <ul class="list-unstyled text-sm text-muted mb-0">
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">T-shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Shirts</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Pants</span></a></li>
                    <li><a class="nav-link ps-4 text-muted letter-spacing-1" href="shop-category.php"> <span class="ps-2">Accessories</span></a></li>
                  </ul>
                </li>
              </ul>-->
              <!-- Brands        -->
              <div class="d-flex align-items-center justify-content-between mb-3">
                <h3 class="h4 lined text-uppercase">Products</h3>
                <button class="btn btn-sm btn-danger" type="button"><i class="fas fa-times-circle me-2"></i>Clear</button>
              </div>
              <form class="mb-4"  action = "" method = "GET">
                <?php for($i =0; $i<count($valids["color"]); $i++){$current_type = $valids['type'][$i]; ?>
                  <div class="form-check">
                    <input class="form-check-input" id="<?php echo "type_$i"?>" name = "type[]" value =<?php echo "$current_type"?> type="checkbox" 
                    <?php echo in_array($current_type, $filter["type"]) ?  'checked': '';?>>
                    <label class="ps-2 form-check-label" for="<?php echo "type_$i"?>"><?php echo "$current_type"?></label>
                  </div>
                <?php } ?>
                <button class="btn btn-sm btn-outline-primary mt-3" type="submit"><i class="fas fa-pencil-alt me-2"></i>Apply</button>
              </form>
              <!-- Colors-->
              <div class="d-flex align-items-center justify-content-between mb-3">
                <h3 class="h4 lined text-uppercase">Colors</h3>
                <button class="btn btn-sm btn-danger" type="button" id = "clearColors"><i class="fas fa-times-circle me-2"></i>Clear</button>
                <script> 
                    clearColors.onclick = ()=>{
                      for(let i=0; i< <?php echo count($valids["color"])?>; i++){
                          $(`#color_${i}`)[0].checked = false;
                      }
                    }
                </script>
              </div>
              <form class="mb-4" id = "colors_filter_form" action = "" method = "GET">
                <?php for($i =0; $i<count($valids["color"]); $i++){$current_col = $valids['color'][$i]; ?>
                  <div class="form-check">
                    <input class="form-check-input color_input" id="<?php echo "color_$i"?>" name = "color[]" value =<?php echo "$current_col"?> type="checkbox" 
                    <?php echo in_array($current_col, $filter["color"]) ?  'checked': '';?>>
                    <label class="ps-2 form-check-label d-flex align-items-center" for="<?php echo "color_$i"?>"><span><?php echo "$current_col"?></span></label>
                  </div>
                <?php } ?>
                <button class="btn btn-sm btn-outline-primary mt-3" type="submit"><i class="fas fa-pencil-alt me-2"></i>Apply</button>
              </form>
              <!-- <a class="d-inline-block" href="#"><img class="img-fluid" src="assets/img/banner.jpg" alt=""></a> -->
            </div>