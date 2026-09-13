
$(document).on('submit', '#log_modal_form', ()=>false);
login_modal_submit_btn.onclick = ()=>{
	if(email_modal.value.replace(/\s/g,'')=='' || password_modal.value.replace(/\s/g,'')==''){
		login_modal_error_par.innerHTML = 'please fill the empty fields';
		return;
	}
	var d = {
		request: 'login',
		email: email_modal.value,
		pass: password_modal.value
	};
	login_modal_error_par.innerHTML = '';
    $.ajax({
		url: 'system/php/requests.php',
		method: 'POST',
		data: d,
		success: response=>{
			if(response.allowed=='0'){
				login_modal_error_par.innerHTML = 'incorrect credentials!';
			}else{
				location = 'index.php';
			}
		},
		error: response=>{
			login_modal_error_par.innerHTML = 'an error occured. Please try again later';
		},
		dataType: "json"
	});
}