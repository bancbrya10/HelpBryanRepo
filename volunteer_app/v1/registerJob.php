<?php
	include('../includes/DbOperations.php');
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		if(isset($_POST['jobTitle']) and
			isset($_POST['jobDescription']) and
			isset($_POST['datePosted']) and
			isset($_POST['dateOfJob']) and
			isset($_POST['startTime']) and
			isset($_POST['endTime']) and
			isset($_POST['latitude']) and
			isset($_POST['longitude']) and
			isset($_POST['postedBy'])){
				
			$db = new DbOperations();
			$jobTitle = $_POST['jobTitle'];
			$jobDescription = $_POST['jobDescription'];
			$datePosted = $_POST['datePosted'];
			$dateOfJob = $_POST['dateOfJob'];
			$startTime = $_POST['startTime'];
			$endTime = $_POST['endTime'];
			$latitude = $_POST['latitude'];
			$longitude = $_POST['longitude'];
			$postedBy = $_POST['postedBy'];
			
			$result = $db->createJob($jobTitle, $jobDescription, $datePosted, $dateOfJob, $startTime, $endTime, $latitude, $longitude, $postedBy);
			if($result == 1){
				$response['error'] = false;
				$response['message'] = "Job registered successfully";
			}
			elseif($result == 2){
				$response['error'] = true;
				$response['message'] = "An error occurred";
			}
			elseif($result == 0){
				$response['error'] = true;
				$response['message'] = "Job already exists please use a unique title";
			}
			elseif($result == 3){
				$response['error'] = true;
				$response['message'] = "Unable to add job to user posted job list";
			}
		}
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid request";
	}
	
	echo json_encode($response);
?>