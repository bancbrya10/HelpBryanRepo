<?php

	include('../includes/DbOperations.php');
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		if(isset($_POST['body'])){
				
			$db = new DbOperations();
			
			$result = $db->addComment($_POST['body'], $_POST['jobTitle']);
			if($result == 1){
				$response['error'] = false;
				$response['message'] = "Comment added successfully";
			}
			elseif($result == 2){
				$response['error'] = true;
				$response['message'] = "An error occurred";
			}
			elseif($result == 0){
				$response['error'] = true;
				$response['message'] = "Job does not exist";
			}
		}
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid request";
	}
	
	echo json_encode($response);

?>