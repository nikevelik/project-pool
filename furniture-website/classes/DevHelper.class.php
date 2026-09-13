<?php
	class DevHelper{
		const dbname="lockdb";
		const svname="localhost"; 
		const usname="root"; 
		const ps=""; 
		static function select($query){
			$conn=new mysqli(self::svname, self::usname, self::ps, self::dbname);
			$result = $conn->query($query);
			if(!($conn->query($query)==TRUE)) return '0';
			$data=[];
			if($result->num_rows>0){
				for($i=0;$row=$result->fetch_assoc(); $i++) $data[$i]=$row;
				//echo $row;
				$conn->close();
				return $data;
			}else{
				$conn->close();
				return '0';
			}
		}
		static function insert($table, $assocArr){
			$conn=new mysqli(self::svname, self::usname, self::ps, self::dbname);
			$querySTART = "INSERT INTO $table (";
			$queryEND = "VALUES(";
			foreach( $assocArr as $key=>$val){
				$querySTART.=$key.",";
				$queryEND.="'".$val."',";
			}
			$querySTART[strlen($querySTART)-1]=')';
			$queryEND[strlen($queryEND)-1]=')';
			$query = $querySTART." ".$queryEND;
			if($conn->query($query) === TRUE){
				#echo 'ok';
			}else{
				#echo "Error creating table: " . $conn->error;
			}
			$conn->close();
		}
		static function insertNormally($query){
			$conn=new mysqli(self::svname, self::usname, self::ps, self::dbname);
			if($conn->query($query) === TRUE){
				#echo 'ok';
			}else{
				#echo "Error creating table: " . $conn->error;
			}
			$conn->close();
		}
		static function deleteRow($sql){
			$conn=new mysqli(self::svname, self::usname, self::ps, self::dbname);
			if ($conn->query($sql) === TRUE) {
				#echo 'ok';
			 } else {
				 //echo "Error deleting record: " . $conn->error;
			 }
		 
			 $conn->close();
		}
		static function update($table, $assocArr, $field, $value){
			$conn=new mysqli(self::svname, self::usname, self::ps, self::dbname);
			$query = "UPDATE $table SET ";
			foreach( $assocArr as $key=>$val){
				$query.="$key = '$val', ";
			}
			$query[strlen($query)-2]=' ';
			$query.="WHERE $field = '$value';";
			//echo "$query";
			if($conn->query($query) === TRUE){
				//echo 'ok';
			}else{
				//echo "Error creating table: " . $conn->error;
			}
			$conn->close();
		}

		static function checkPass($pass){
			if(strlen($pass)<5) return '0';
			$big = '0';
			$small='0';
			$num='0';
			for ( $i = 0; $i<strlen($pass); $i++){
				if($pass[$i]>=chr(48) && $pass[$i]<=chr(57)) $num='1';
				if($pass[$i]>=chr(65) && $pass[$i]<=chr(90)) $big='1';
				if($pass[$i]>=chr(97) && $pass[$i]<=chr(122)) $small='1';
			}
			if($num=='1' && $big=='1' && $small='1'){
				if(self::checkForNotAcceptableChars($pass)=='1'){
					return '1';
				}
			}
			return '0';
		}
		static function hashPass($pass){
			return (md5($pass));
		}
		static function checkForNotAcceptableChars($str){
			$arr = str_split($str);
			$i=0;
			foreach ($arr as $j){
				$i = ord($j);
				if($i>32 && $i<127){
					//if($i==46 || ($i>=48 && $i<=57) || ($i>=65 && $i<=90)  || $i==95 || ($i>=97 && $i<=122)){
					//}else return '0';
				}else return '0';
			}
			return '1';
		}
		static function  test_input($data) {
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			return $data;
		}
		static function checkEmail($email){
			$res = self::select("SELECT `id` from `user` where `email` = '$email'");
			if($res!='0') return false;
			return filter_var($email, FILTER_VALIDATE_EMAIL);
		}
		static function checkPhone($phone){
			return (preg_match('/^[0-9]{10}+$/', $phone));
		}
		#src: проектът на Ники, телеграм
		static function clean_name($string) {
			$string = str_replace(' ', '-', self::test_input($string)); // Replaces all spaces with hyphens.
			return str_replace('/[^A-Za-zĂÂÎȘȚăâîșț\u0400-\u04FF\-]/', '', $string); // Removes special chars.
		}
		//FILTERING THE NAME FOR SPECIAL CHARACTERS
		static function SanitizeName($name){    
			$name = self::clean_name($name);
			return filter_var($name, FILTER_SANITIZE_STRING);
		}
		static function clean($string) {
			return str_replace('/[^A-Za-z0-9ĂÂÎȘȚăâîșț\u0400-\u04FF\-@\.:\/№\_°%]/', '', self::test_input($string)); // Removes special chars.
		}
		//FILTERING THE INPUTED DATA FOR SPECIAL CHARACTERS
		static function SanitizeData($input){
			//getting the type of the input
			$input_type = gettype($input);
			//If input is not array
			if($input_type == "string"){
				//removing all special characters
				$input = self::clean($input);
				return $input;
			}
			//switch for the type of the input
			switch ($input_type) {
				case 'string':
				//if string
					//sanitizing the input
					$input = filter_var($input, FILTER_SANITIZE_STRING);
					return $input;
					break;
				case 'integer':
				//if int
					//sanitizing the input
					$input = filter_var($input, FILTER_SANITIZE_NUMBER_INT);
					return $input;
					break;
				case 'double':
				//if float
					//sanitizing the input
					$input = filter_var($input, FILTER_SANITIZE_NUMBER_FLOAT);
					return $input;
					break;
				case 'bool':
				//if bool
					//sanitizing the input
					$input = filter_var($input, FILTER_VALIDATE_BOOLEAN);
					return $input;
					break;
				case "array":
					$return_input = array();
					foreach ($input as $key) {
						$return_input[] = self::SanitizeData($key);
					}
					return $return_input;
					break;
				default:
				//if none of the above
					return false;    
					break;
			}
		}
		static function getCookies($cart_or_wishlist){
			$i = 0;
			$items = [];
			while(true){
				$cookie = DevHelper::SanitizeData($_COOKIE[$cart_or_wishlist.$i]);
				if($cookie=="")break;
				if("cart"==$cart_or_wishlist){
					$iter_pr = (int)(substr($cookie, 0, strpos($cookie, 'x')));
					$iter_qtt = (int)(substr($cookie, strpos($cookie, 'x')+1, strlen($cookie)));
					$items[$i] = ["id"=>$iter_pr, "quantity"=>$iter_qtt, "cookie"=>$i];
				}else{
					$iter_pr = (int)($cookie);
					$items[$i] = ["id"=>$iter_pr, "cookie"=>$i];
				}
				$i++;
			}
			return json_encode($items);
		}
		static function deleteAllProductCookies($cart_or_wishlist){
			foreach ($_COOKIE as $key=>$val){
				if(substr($key, 0, 4)==$cart_or_wishlist){
					unset($_COOKIE[$key]);
					setcookie($key, null, -1, '/');
				}
 			 }
		}
		static function deleteAllCookies(){
			foreach ($_COOKIE as $key=>$val){
				if(substr($key, 0, 9)=='PHPSESSID')continue;
				unset($_COOKIE[$key]);
				setcookie($key, null, -1, '/');
 			 }
		}
		static function clean_everything(){
			foreach($_POST as $key=>$val){
				$_POST[$key] = DevHelper::SanitizeData($val);
			}
		}
		static function cleanCookies($cart_or_wishlist){
			$regex = ($cart_or_wishlist=="cart") ? '/([0-9]+x[0-9]+)/':'/([0-9]+)/';
			foreach ($_COOKIE as $key=>$val){
				if(substr($key, 0, 4)==$cart_or_wishlist){
					if(!preg_match($regex, $val)){
						unset($_COOKIE[$key]);
						setcookie($key, null, -1, '/');
					}
				}
			}
		}
		static function getOrderCookies(){
			$arr = [];
			$i=0;
			foreach($_COOKIE as $key=>$val){
				$val = DevHelper::SanitizeData($val);
				if(substr($key, 0, 1)=='o'){
					$firstSplit = strpos($val, '|' );
					$secondSplit = strpos($val, '|', $firstSplit+1);
					$length = strlen($val);
					$arr[$i]["id"] = substr($key, 5);
					$arr[$i]["total"] = substr($val, 0, $firstSplit);
					$arr[$i]["status"] = substr($val, $firstSplit+1, $secondSplit-$firstSplit-1);
					$arr[$i]["created"] = substr($val, $secondSplit+1, $length-$secondSplit-1);
					$i++;
				}
			}
			return json_encode($arr);
		}
	}
?>	