 <!-- checkout part 3 -->
 <!-- PAYMENT METHOD FORM-->
 <form class="py-4" id ="ckt2">
        <div class="col-lg-3">
            <h3 class="h4 text-uppercase lined mb-4">Payment</h3>
        </div>
        <div class="row mb-4 gy-4">
            <?php for($i=0; $i<count($shipping_methods); $i++){?>
            <div class="col-md-6">
            <div class="bg-light p-4 p-xl-5">
                <div class="form-check d-flex align-items-center">
                <input class="form-check-input flex-shrink-0" id="payment<?php echo $i; ?>" type="radio" name="payment">
                <label class="cursor-pointer d-block ms-3" for="payment<?php echo $i; ?>"><span class="h4 d-block mb-1 text-uppercase"><?php echo $payment_methods[$i]; ?></span><span class="text-sm d-block mb-0 text-muted"><?php echo $payment_methods_desc[$i]; ?></span></label>
                </div>
            </div>
            </div>
            <?php } ?>
        </div>
    </form>