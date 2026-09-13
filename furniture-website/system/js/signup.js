$(document).on('submit', '#sign_form', ()=>false);
sign_submit_btn.onclick = ()=>{
	if(useremail.value.replace(/\s/g,'')=='' || userpassword.value.replace(/\s/g,'')==''){
		login_modal_error_par.innerHTML = 'please fill the empty fields';
		return;
	}
	var d = {
		request: 'signup',
		email: useremail.value,
		pass: userpassword.value,
        name: username.value,
        terms: (userterms.checked)? "1" : "0",
        privacy: (userprivacy.checked)? "1" : "0",
        newsletter: (usernewsletter.checked)? "1" : "0"
	};
    if(!d.privacy || !d.terms){
        signup_error_par.innerHTML = 'we cannot process your register request, unless you agree with our terms and policy!';
        return;
    }
	login_modal_error_par.innerHTML = '';
     $.ajax({
	 	url: 'system/php/requests.php',
	 	method: 'POST',
	 	data: d,
	 	success: response=>{
			console.log(response);
	 		if(response.email) signup_error_par.innerHTML = 'invalid or used email!';
	 		else if(response.name) signup_error_par.innerHTML = 'name is invalid!';
	 		else if(response.password) signup_error_par.innerHTML = 'password is not appropriate!';
	 		else if(response.other) signup_error_par.innerHTML = 'invalid inputs!';
			else if(response.txt == '1')location = 'index.php';
			else signup_error_par.innerHTML = 'an error occured. Please try again later';
	 	},
	 	error: response=>{
	 		signup_error_par.innerHTML = 'an error occured. Please try again later';
	 	},
	 	dataType: "json"
	 });
}