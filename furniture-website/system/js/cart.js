$(document).on('submit', '#cart_form', ()=>false);
var inps = [];
var rows = [];
var btns = [];
var cookiesIdxs = [];
var deletedIDs = [];
for(let i =0; i<numberproducts; i++){
    rows[i] = $(`#row_cart_${i}`)[0];
    btns[i] = $(`#btn_delete_row_cart_${i}`)[0];
    inps[i] = $(`.modify_quantity`)[i];
    cookiesIdxs[i] = items[i].cookie;
    btns[i].addEventListener("click", ()=>{
        if(!logged){
            deleteCookie("cart"+items[i].cookie);
            cookiesIdxs.splice(i,1);
        }else deletedIDs[i] = Number(inps[i].id.substring(1));
        rows[i].remove();
        inps[i]=false;
    });
}
update_cart_btn.onclick =()=>{
    update_cart();
    location.href = 'shop-basket.php';
}
proceed_to_checkout_btn.onclick =()=>{
    update_cart();
    location.href="shop-checkout1.php";
}
function update_cart(){
    if(logged){
        let d={
            request: "update_cart",
            inps: [],
            ids: []
        }
        for(let i=0; i<inps.length; i++){
            if(inps[i]==false){
                d.inps[i] = false;
                d.ids[i] = deletedIDs[i];
            }
            if(inps[i].value!=undefined){
                d.inps[i] = inps[i].value;
                d.ids[i] = Number(inps[i].id.substring(1));
            }
        }
        console.log(d);
        $.ajax({
            method: 'POST',
            url: 'system/php/requests.php',
            dataType: 'json',
            data: d,
            success: response=>{
                console.log(response);
            },
            error: response=>{
                console.log(response);
            }
        })
        
        return;
    }
    for(let i=0; i<inps.length; i++){
        if(!inps[i]) break;
        if(typeof(Number(inps[i].value)) != 'number' || inps[i].value %1 != 0 || inps[i].value <0) {
            continue;
        }
        let cookieIdx = inps[i].id.substring(inps[i].id.indexOf('_')+1, inps[i].id.length);
        let oldCookie = getCookie("cart"+cookieIdx);
        setCookie("cart"+cookieIdx, oldCookie.substring(0, oldCookie.indexOf('x'))+'x'+inps[i].value);
    }
    if(cookiesIdxs.length>0){
        reorderProductCookies("cart", Math.max(...cookiesIdxs));
    }
}