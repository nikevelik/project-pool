$(document).ready(()=>{
	if(!logged) return;
    var arr = [].slice.call($('#checkout_form_0')[0].getElementsByTagName('input'));
    arr.push($('#country')[0]);
    var d = {request: 'prelog'};
    $.ajax({
		url: 'system/php/requests.php',
		method: 'POST',
		data: d,
		success: response=>{
			console.log('inSUCCESS');
			console.log(response);
			if(response.allowed=='0'){
			}else{
				arr.forEach(element => {
                    element.value = response[element.name];   
                });
			}
		},
		error: response=>{
			//console.log('inERROR');
			//console.log(response.responseText);
		},
		dataType: "json"
	});
});