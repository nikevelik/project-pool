$(document).on('submit', '#add_to_cart_form', ()=>false);
add_to_cart_btn.onclick = ()=>{
    var quantity = $('#product_quantity')[0].value;
    if(logged){
        d = {
            request: "cart",
            product: currentProduct,
            quantity: Number(quantity)
        };
        $.ajax({
            url: 'system/php/requests.php',
            method: 'POST',
            data: d,
            success: response=>{
                if(response.ok=="ok"){
                    location.href = 'shop-basket.php';
                }
            },
            error: response=>{
            },
            dataType: 'json'
        });
    }else{
        pushProductCookie(currentProduct, Number(quantity), "cart");
        location.href='shop-basket.php';
    }
}
add_to_wish_btn.onclick = ()=>{
    if(logged){
        d = {
            request: "wish",
            product: currentProduct
        };
        $.ajax({
            url: 'system/php/requests.php',
            method: 'POST',
            data: d,
            success: response=>{
                console.log(response);
            },
            error: response=>{
                console.log(response);
            },
            dataType: 'json'
        });
    }else{
        pushProductCookie(currentProduct, 0, "wish");
    }
    location.href='customer-wishlist.php';
}