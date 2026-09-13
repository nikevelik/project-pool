console.log('inPASS.JS');
$(document).on('submit', '#change_pass', ()=>false);
change_pass_btn.onclick = ()=>{
	if(password_old.value.replace(/\s/g,'')=='' || password_new.value.replace(/\s/g,'')==''){
		change_pass_error_par.innerHTML = 'please fill the empty fields!';
		return;
	}
	var d = {
		request: 'change_pass',
		old_pass: password_old.value,
		new_pass: password_new.value
	};
	change_pass_error_par.innerHTML = '';
	change_pass_error_par.style.color = 'red';
    $.ajax({
		url: 'system/php/requests.php',
		method: 'POST',
		data: d,
		success: response=>{
			if(response.allowed=='0'){
                if(response.new_pass){
				    change_pass_error_par.innerHTML = 'insecure new password!';
                }else if(response.old_pass){
                    change_pass_error_par.innerHTML = 'incorrect password!';
                }
			}else{
				change_pass_error_par.innerHTML = 'successfull!';
				change_pass_error_par.style.color = 'green';
				password_new.value='';
				password_old.value='';
				change_pass_btn.remove();
			}
		},
		error: response=>{
			change_pass_error_par.innerHTML = 'an error occured. Please try again later';
		},
		dataType: "json"
	});
}