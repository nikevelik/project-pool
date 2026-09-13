console.log('ok, in');
$(document).on('submit', '#log_form', ()=>false);
login_form_submit_btn.onclick = ()=>{
	if(loginemail.value.replace(/\s/g,'')=='' || loginpassword.value.replace(/\s/g,'')==''){
		login_form_error_par.innerHTML = 'please fill the empty fields';
		return;
	}
	var d = {
		request: 'login',
		email: loginemail.value,
		pass: loginpassword.value
	};
	login_form_error_par.innerHTML = '';
    $.ajax({
		url: 'system/php/requests.php',
		method: 'POST',
		data: d,
		success: response=>{
			if(response.allowed=='0'){
				login_form_error_par.innerHTML = 'incorrect credentials!';
			}else{
				console.log('login successful');
				location = 'index.php';
			}
		},
		error: response=>{
			//console.log('inERROR');
			//console.log(response.responseText);
			login_form_error_par.innerHTML = 'an error occured. Please try again later';
		},
		dataType: "json"
	});
}