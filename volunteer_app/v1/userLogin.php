<?php
	include('../includes/DbOperations.php');
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST'){
		if(isset($_POST['userName']) and isset($_POST['password'])){
			$db = new DbOperations();
			
			if($db->userLogin($_POST['userName'], $_POST['password'])){
				$user = $db->getUserByUsername($_POST['userName']);
				$response['error'] = false;
				$response['id'] = $user['id'];
				$response['name'] = $user['name'];
				$response['userName'] = $user['userName'];
				$response['birthday'] = $user['birthday'];
				$response['previousJobs'] = $user['previousJobs'];
				$response['currentPostedJobs'] = $user['currentPostedJobs'];
				$response['rating'] = $user['rating'];
			}
			else{
				$response['error'] = true;
				$response['message'] = "Invalid username or password";
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