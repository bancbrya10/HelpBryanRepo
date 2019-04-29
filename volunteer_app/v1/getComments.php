<?php
	
	include('../includes/DbOperations.php');
	
	$db = new DbOperations();
	$response = $db->getComments($_POST['jobTitle']);
	
	echo json_encode($response['comments']);
	
?>