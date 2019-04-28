<?php
	
	include('../includes/DbOperations.php');
	
	$db = new DbOperations();

	echo json_encode($db->getJobs());
	
?>