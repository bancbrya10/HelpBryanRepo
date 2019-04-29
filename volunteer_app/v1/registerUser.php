<?php
	include('../includes/DbOperations.php');
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		if(isset($_POST['name']) and
			isset($_POST['userName']) and
			isset($_POST['password']) and
			isset($_POST['birthday'])){
			
			$db = new DbOperations();
			$name = $_POST['name'];
			$userName = $_POST['userName'];
			$password = $_POST['password'];
			$birthday = $_POST['birthday'];
			
			$result = $db->createUser($name,$userName,$password,$birthday);
			
			if($result == 1){
				$response['error'] = false;
				$response['message'] = "User registered successfully";
			}
			elseif($result == 2){
				$response['error'] = true;
				$response['message'] = "An error occurred";
			}
			elseif($result == 0){
				$response['error'] = true;
				$response['message'] = "Username already exists";
			}
		}
		else{
			$response['error'] = true;
			$response['message'] = "Required fields are missing";
		}
		
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid request";
	}
	
	echo json_encode($response);
?>