
$(document).on('submit', '#user_data_from', ()=>false);
user_data_form_btn.onclick = ()=>{
    var d = {};
    var arr = [].slice.call($('#user_data_from')[0].getElementsByTagName('input'));
    arr.push($('#country')[0]);
    arr.forEach(el => {
        if(el.value.replace(/\s/g,'')!='') d[el.name] = el.value;
    });
    d.request ='update';
    user_data_form_error_par.style.color='red';
	user_data_form_error_par.innerHTML = '';
    $.ajax({
		url: 'system/php/requests.php',
		method: 'POST',
		data: d,
		success: response=>{
			if(response.allowed=='0'){
				if(response.email || response.phone || response.name){
					user_data_form_error_par.innerHTML = response.myErrorMessage;
				}else {
					user_data_form_error_par.innerHTML = "An error occured. Please try again later";
				}
			}else{
                user_data_form_error_par.innerHTML = 'successfull!';
                user_data_form_error_par.style.color = 'green';
				user_data_form_btn.remove();
			}
		},
		error: response=>{
			//console.log('inERROR');
			//console.log(response.responseText);
			user_data_form_error_par.innerHTML = 'An error occured. Please try again later';
		},
		dataType: "json"
	});
}