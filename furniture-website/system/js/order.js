var _SHIP_METHOD;
var _PAY_METHOD;
submit_order.onclick = ()=>{
    for(let i=0; i<3; i++){
        if($('#delivery'+i)[0].checked) _SHIP_METHOD = ''+i;
        if($('#payment'+i)[0].checked) _PAY_METHOD = ''+i;    
    }
    d = {
            request: 'order',
            name: name_user_data.value,
            city: city_user_data.value,
            country: country.value,
            ship_method: _SHIP_METHOD,
            pay_method: _PAY_METHOD,
            email: email_user_data.value,
            phone: phone_user_data.value,
            address: address_user_data.value
        }
    $.ajax({
        url: 'system/php/requests.php',
        method: 'POST',
        data: d,
        dataType: 'json',
        method: 'POST',
        success: (r)=>{
            console.log(r, "aaa");
            if(r.allow == "1"){
                if(!logged)setCookie(`order${r.orderID}`, `${r.total}|${r.status}|${r.time}`, 365);
                location.href = `customer-order.php?id=${r.orderID}`;
            }else{
                if(r.ship || r.pay || r.email || r.name || r.other || r.phone){
                    order_error_par.innerHTML = r.myErrorMessage;
                }
            }
        },
        error: (res)=>{
            console.log(res.responseText, res);
        }
    });
}