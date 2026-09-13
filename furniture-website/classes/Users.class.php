<?php
	require_once('DevHelper.class.php');
	class Users{
		static function login(){
			$response = ["allowed"=>""];
			DevHelper::clean_everything();
			$email = $_POST['email'];
			$psp = DevHelper::hashPass($_POST['pass']);
			$query = DevHelper::select("SELECT `id` FROM `user` where `email` = '$email' and `pass`='$psp'");
			if ($query!='0') {
				session_start();
				$_SESSION['in']=true;
				$_SESSION['id'] = $query[0]['id'];
				$response["allowed"] ='1';
			} else {
				$response["allowed"] = '0';
				$response["myErrorMessage"]='USER NOT FOUND';
			}
			DevHelper::deleteAllCookies();
			return json_encode($response);
		}
		#deletes the session
		static function logout(){
			session_start();
			unset($_SESSION['in']);
			session_destroy();
			header('Location: index.php');
			exit();
		}
		#create new user, adress, cart and whishlist rows in database
		static function signup(){
			$response=["email"=>false, 'password'=>false, "other"=>false, "txt"=>"0", "name"=>false];
			$formdata = ["fname"=>"", "lname"=>"", "email"=>"", "pass"=>"", "privacy"=>"", "terms"=>"", "newsletter"=>""];
			DevHelper::clean_everything();
			//creates first name and lastname from (and deletes) the name field
			$arr = explode(" ",$_POST["name"], 2);
			if(!empty($arr)){
				$_POST["fname"] =$arr[0];
				$_POST["lname"] =$arr[1];
				unset($_POST["name"]);
			}else{
				$response['name']= true;
				$response['myErrorMessage']='Name not written correctly';
				return json_encode($response);
			}
			//get tinyint from terms, privacy and newsletter
			$_POST['terms']=($_POST['terms']=="1")? 1: 0;
			$_POST['privacy']=($_POST['privacy']=="1")? 1: 0;
			$_POST['newsletter']=($_POST['newsletter']=="1")? 1: 0;
			//check if password is at least 6characters, including uppercase and lowercase letter, number and special character
			if(DevHelper::checkPass($_POST['pass'])=='0'){
				$response['password']= true;
				$response['myErrorMessage']='PASSWORD NOT SECURE';
				return json_encode($response);
			}
			//check if email is correctly written and if it is not already connected to a user.
			if(!DevHelper::checkEmail($_POST['email'])){
				$response['email']= true;
				$response['myErrorMessage']='INVALID EMAIL';
				return json_encode($response);
			}
			foreach($formdata as $key=>$val){	
				$formdata[$key] = $_POST[$key];
				if(!$formdata[$key]){
					$response['other']= true;
					$response['MyErrorMessage']='UNKNOWN ERROR';
					return json_encode($response);
				}
			}
			$email = $formdata['email'];//define variable to simplify query
			$formdata["pass"]=DevHelper::hashPass($formdata["pass"]);
			DevHelper::insert("user", $formdata);//create the user
			$cid = DevHelper::select("SELECT `id` from `user` where `email` = '$email'")[0]["id"];//get new user's id
			//create an empty row with for their address, cart and wishlist (update it later..)
			DevHelper::insert("address", ["userID"=>"$cid"]);
			DevHelper::insert("cart", ["userID"=>"$cid"]);
			DevHelper::insert("whishlist", ["userID"=>"$cid"]);
			$response["txt"] = '1';
			session_start();
			$_SESSION['in']=true;
			$_SESSION['id'] = $cid;
			DevHelper::deleteAllCookies("cart");
			return json_encode($response);
		}
		static function updateData(){
			if(isset($_SESSION['id']))$cid = $_SESSION['id'];
			else return;
			$response=["allowed" => "0"];
			$formdata = ["fname"=>"", "lname"=>"", "email"=>"", "phone"=>""];
			$adr_assoc = ["userID"=>"","address"=>"","city"=>"","country"=>""];
			DevHelper::clean_everything();
			//check if mail is correct and doesnt match other mails
			if(isset($_POST['email'])){
				if(!filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)){
					$response['email']= true;
					$response['myErrorMessage']='INVALID EMAIL';
					return json_encode($response);
				}
				$email = $_POST['email'];
				$res = DevHelper::select("SELECT `id` FROM `user` where `email` = '$email'");
				if($res){if($res[0]['id']!=$cid){
					$response['email']= true;
					$response['myErrorMessage']='USED EMAIL';
					return json_encode($response);
				}}
			}
			//phone check
			if(!DevHelper::checkPhone($_POST['phone'])){
				$response['phone']= true;
				$response['myErrorMessage']='INVALID PHONE';
				return json_encode($response);
			}
			//creates first name and lastname from (and deletes) the name field
			$arr = explode(" ",$_POST["name"], 2);
			if(!empty($arr)){
				$_POST["fname"] =$arr[0];
				$_POST["lname"] =$arr[1];
				unset($_POST["name"]);
			}else{
				$response['name']= true;
				$response['myErrorMessage']='Name not written correctly';
				return json_encode($response);
			}
			foreach($formdata as $key=>$val){	//setting user row
				$formdata[$key] = $_POST[$key];
				if(!$formdata[$key]) unset($formdata[$key]);
			}
			$_POST['userID']=$cid;//define it for easier setting
			foreach($adr_assoc as $key=>$value){//setting address row
				$adr_assoc[$key] =$_POST[$key];
				if(!$adr_assoc[$key])unset($adr_assoc[$key]);
			}	
			DevHelper::update('user', $formdata, 'id', $_SESSION['id']);
			DevHelper::update('address', $adr_assoc, 'userID', $_SESSION['id']);
			$response["allowed"]='1';
			return json_encode($response);
		}
		static function getData(){
			$cid=$_SESSION['id'];
			$result = ["allowed"=>'0', 'userStatus'=>'not found'];
			if(!isset($_SESSION['in'])) {
				$result['userStatus'] = 'not logged';
				return json_encode($result);
			}
			$query_user_result=DevHelper::select("SELECT `fname`,`lname`,`phone`,`email` FROM `user` WHERE `id` = $cid");
			if($query_user_result!=0){
				$result = $query_user_result[0];
				$result['name'] = $result['fname'].' '.$result['lname'];
				$query_address_result=DevHelper::select("SELECT `address`, `city`, `country` FROM `address` where `userID` = $cid");
				if($query_address_result!=0){
					$result+=$query_address_result[0];
					$result["allowed"]="1";
					$result["userStatus"]="found";
					return json_encode($result);
				}
			}
			return json_encode($result);
			#SELECT `fname`,`lname`,`phone`,`email` FROM `user` WHERE `id` = '$cid';
			#SELECT `address`, `city`, `country` FROM `address` where `userID` = 2;
		}
		static function updatePass($oldpass, $pass){
			$response=["allowed"=>"0"];
			if(DevHelper::checkPass($pass)=='0'){
				$response['new_pass']= true;
				$response['myErrorMessage']='NEW PASSWORD NOT SECURE';
				return json_encode($response);
			}
			$oldpass = DevHelper::hashPass(DevHelper::SanitizeData($oldpass));
			$pass = DevHelper::hashPass(DevHelper::SanitizeData($pass));
			$d = ["pass"=>$pass];
			$cid = $_SESSION['id'];
			$res = DevHelper::select("SELECT `pass` from `user` where `pass`='$oldpass' and `id`='$cid'");
			if($res!='0'){
				if($oldpass==$res[0]["pass"]){
					DevHelper::update('user', $d, 'id', $_SESSION['id']);
					$response["allowed"]="1";
					return json_encode($response);
				}
			}
			$response["old_pass"]=true;
			$response['myErrorMessage']='OLD PASSWORD DOES NOT MATCH';
			return json_encode($response);
		}
		static function placeOrder(){
			$response =["allow"=>"0"];
			DevHelper::clean_everything();
			if(isset($_SESSION['in'])){
				$products_in_cart = [];
				$cid = $_SESSION['id'];
				$res = DevHelper::select("SELECT `id` from `cart` where `userID` = '$cid'");
				if($res){
					$cart_id = $res[0]['id'];
					$res1 = DevHelper::select("SELECT * from `cart_product` where `cartID` = '$cart_id'");
					if($res1){
						for($i=0; $i<count($res1); $i++){
							$products_in_cart[$i]['id'] = $res1[$i]['productID'];
							$products_in_cart[$i]['quantity'] = $res1[$i]['quantity'];
						}
					}
				}
			}else{
				$products_in_cart = json_decode(DevHelper::getCookies("cart"),true);
				if(count($products_in_cart)==0) return json_encode($response);
			}
			$adr_assoc = ["address"=>"", "city"=>"", "country"=>""];
			$method_values = ['0','1','2'];
			//shipping method check
			if((!isset($_POST['ship_method'])) || (! in_array($_POST['ship_method'], $method_values))){
				$response["ship"] = true;
				$response['myErrorMessage']='Please choose delivery method	';
				return json_encode($response);
			}
			//payment method check
			if(!isset($_POST['pay_method']) || (! in_array($_POST['pay_method'], $method_values))){
				$response["pay"] = true;
				return json_encode($response);
			}
			//name check
			$arr = explode(" ",$_POST["name"], 2);
			if(!empty($arr)){
				$_POST["fname"] =$arr[0];
				$_POST["lname"] =$arr[1];
				unset($_POST["name"]);
			}else{
				$response['name']= true;
				$response['myErrorMessage']='Name not written correctly';
				return json_encode($response);
			}
			//email check
			if(!isset($_POST['email'])){
				$response['email']= true;
				$response['myErrorMessage']='INVALID EMAIL';
				return json_encode($response);
			}else if(!filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)){
				$response['email']= true;
				$response['myErrorMessage']='INVALID EMAIL';
				return json_encode($response);
			}
			//phone check
			if(!DevHelper::checkPhone($_POST['phone'])){
				$response['phone']= true;
				$response['myErrorMessage']='INVALID Phone';
				return json_encode($response);
			}
			foreach($adr_assoc as $key=>$val){
				if((!isset($_POST[$key]))){
					$response['other']= true;
					$response['myErrorMessage']="An unknown error occurred. Check $key";
					return json_encode($response);
				}
				$adr_assoc[$key] = $_POST[$key];
			}
			if(isset($_SESSION['in'])){
				$adr_assoc['userID'] = $_SESSION['id'];
			}
			DevHelper::insert('address', $adr_assoc);
			$adr_cid = DevHelper::select("SELECT * FROM `address` ORDER BY `id` DESC LIMIT 1")[0]['id'];//get address id we jsut inserted
			$order = ["total"=>0, "status"=>0, "addressID"=>$adr_cid, "ship_method"=>$_POST['ship_method'], "pay_method"=>$_POST['pay_method'], "email"=>$_POST['email'], "phone"=>$_POST['phone'], "fname"=>$_POST['fname'], "lname"=>$_POST["lname"]];
			for($i =0; $i<count($products_in_cart); $i++){
				$id = $products_in_cart[$i]["id"];
				$res = DevHelper::select("SELECT * from `product` where `id` = $id");
				if($res){
					$order["total"]+=$res[0]['price'];
				}
			}
			if(isset($_SESSION['in'])){
				$order['userID'] =$_SESSION['id'];
			}
			DevHelper::insert('order_', $order);
			$res = DevHelper::select("SELECT * FROM `order_` ORDER BY `id` DESC LIMIT 1")[0];
			$order_cid = $res['id'];
			$time = $res['created'];
			$status = $res['status'];
			for($i =0; $i<count($products_in_cart); $i++){
				
				DevHelper::insert("order_product", [
					"orderID"=>$order_cid, 
					"productID"=>$products_in_cart[$i]['id'], 
					"quantity"=>$products_in_cart[$i]['quantity']
				]);
				DevHelper::deleteRow("DELETE FROM `whishlist_product` WHERE `cartID` = $cart_id");
			}
			
			DevHelper::deleteAllProductCookies("cart");
			$response["allow"]="1";
			$response["orderID"]=$order_cid;
			$response["total"] = $order["total"];
			$response["time"] = $time;
			$response["status"] = $status;
			return json_encode($response);
		}
		static function addToCart(){
			DevHelper::clean_everything();
			$cid = $_SESSION['id'];
			$res = DevHelper::select("SELECT `id` from `cart` where `userID` = $cid");
			if($res){
				$cart_id = $res[0]['id'];
				$res1 = DevHelper::select("SELECT * from `cart_product` where `cartID` = '$cart_id'");
				if($res1){
					for($i=0; $i<count($res1); $i++){
						if($res1[$i]['productID']==$_POST['product']){
							DevHelper::update('cart_product', ["quantity"=>$_POST['quantity']+$res1[$i]['quantity']], 'id', $res1[$i]['id']);
							return json_encode(["ok"=>"ok"]);
						}
					}
				}
				DevHelper::insert('cart_product', ["productID"=>$_POST['product'], "quantity"=>$_POST['quantity'], "cartID"=>$cart_id]);
				return json_encode(["ok"=>"ok"]);
			}
		}
		static function addToWishlist(){
			if(!isset($_SESSION['id'])){
				return json_encode(["ok"=>"nook"]);
			}
			DevHelper::clean_everything();
			$cid = $_SESSION['id'];
			$res = DevHelper::select("SELECT `id` from `whishlist` where `userID` = $cid");
			if($res!=0){
				$whishlistID = $res[0]['id'];
				$res1 = DevHelper::select("SELECT * from `whishlist_product` where `whishlistID` = '$whishlistID'");
				if($res1){
					for($i=0; $i<count($res1); $i++){
						if($res1[$i]['productID']==$_POST['product']){
							return json_encode(["ok"=>"ok"]);
						}
					}
				}
				DevHelper::insert('whishlist_product', ["productID"=>$_POST['product'], "whishlistID"=>$whishlistID]);
				return json_encode(["ok"=>"ok"]);
			}
		}
		static function updateCart(){
			DevHelper::clean_everything();
			$cid = $_SESSION['id'];
			$res = DevHelper::select("SELECT `id` from `cart` where `userID` = $cid");
			if($res){
				$cart_id = $res[0]['id'];
				$res1 = DevHelper::select("SELECT * from `cart_product` where `cartID` = '$cart_id'");
				if($res1){
					for($i=0; $i<count($res1); $i++){
						$res1_id = $res1[$i]['id'];
						for($j=0; $j<count($_POST['ids']); $j++){
							if($_POST['inps'][$j] != false){
								if($_POST['ids'][$j] == $res1[$i]['productID']){
									DevHelper::update('cart_product', ["quantity"=>$_POST['inps'][$j]], 'id', $res1_id);
								}
							}
							if($_POST['inps'][$j] == 0){
								if($_POST['ids'][$j] == $res1[$i]['productID']){								
									DevHelper::deleteRow("DELETE FROM `cart_product` WHERE `id` = $res1_id");
								}
							}
						}
					}
					return json_encode(["ok"=>"ok"]);
				}
			}
			return json_encode(["ok"=>"nook"]);
		}
		static function contactForm(){
			DevHelper::clean_everything();
			if(isset($_POST["subject"]) &&isset($_POST["text"]) &&isset($_POST["fname"]) &&isset($_POST["lname"]) &&isset($_POST["email"])){
				$s = $_POST["subject"];$t = $_POST["text"];$f= $_POST["fname"];$l = $_POST["lname"];$e = $_POST["email"]; 
				DevHelper::insertNormally("INSERT INTO `contactform` (`subject`, `text`, `fname`, `lname`, `email`) VALUES ('$s', '$t', '$f', '$l', '$e')");
				return json_encode(["msg"=>"valid"]);
			}else{
				return json_encode(["msg"=>"invalid"]);
			}
		}
	}
?>