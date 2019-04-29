<?php
	class DbOperations{
		
		private $con;
		
		function __construct(){
				
			require_once dirname(__FILE__).'/DbConnect.php';
			
			$db = new DbConnect();
			
			$this->con = $db->connect();
				
		}
		
		public function createUser($name,$userName,$password,$birthday){
			if($this->isUserExist($userName)){
				return 0;
			}
			else{
				$password = md5($password);
				$rating = 3.0;
				$stmt = $this->con->stmt_init();
				if($stmt = $this->con->prepare("INSERT INTO user (id,name,userName,password,birthday,rating) VALUES (NULL,?,?,?,?,?);")){				
					$stmt->bind_param("ssssd",$name,$userName,$password,$birthday,$rating);
					if($stmt->execute()){
						return 1;
					}
					else{
						return 2;
					}
				}
			}	
		}
		
		public function userLogin($userName, $pass){
			$password = md5($pass);
			$stmt = $this->con->prepare("SELECT id FROM user WHERE userName = ? AND password = ?");
			$stmt->bind_param("ss",$userName,$password);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}
		
		public function getUserByUsername($userName){
			$stmt = $this->con->stmt_init();
			$stmt = $this->con->prepare("SELECT * FROM user WHERE userName = ?");
			$stmt->bind_param("s",$userName);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}
		
		private function isUserExist($userName){
			$stmt = $this->con->prepare("SELECT id FROM user WHERE userName = ?");
			$stmt->bind_param("s", $userName);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}
		
		public function getJobs(){
			$stmt = $this->con->query("SELECT * FROM job");
			
			if($stmt->num_rows > 0){
				$response['jobs'] = array();
				
				while($row = $stmt->fetch_assoc()){
					$job = array();
					$job['jobTitle'] = $row['jobTitle'];
					$job['jobDescription'] = $row['jobDescription'];
					$job['datePosted'] = $row['datePosted'];
					$job['dateOfJob'] = $row['dateOfJob'];
					$job['startTime'] = $row['startTime'];
					$job['endTime'] = $row['endTime'];
					$job['latitude'] = $row['latitude'];
					$job['longitude'] = $row['longitude'];
					$job['postedBy'] = $row['postedBy'];
					$job['comments'] = $row['comments'];
					
					array_push($response['jobs'], $job);
				}
				$response['error'] = false;
			}
			else{
				$response['error'] = true;
				$response['message'] = "No jobs found";
			}
			
			return $response;
		}
		
		public function getComments($jobTitle){
			$stmt = $this->con->stmt_init();
			$stmt = $this->con->prepare("SELECT comments FROM job WHERE jobTitle = ?");
			$stmt->bind_param("s",$jobTitle);
			$stmt->execute();
			$response = $stmt->get_result()->fetch_assoc();
			
			return $response;
			
		}
		
		public function addPostedJob($userName, $jobTitle){
			$stmt = $this->con->stmt_init();
			$stmt = $this->con->prepare("SELECT currentPostedJobs FROM user WHERE userName = ?");
			$stmt->bind_param("s",$userName);
			$stmt->execute();
			$result = $stmt->get_result()->fetch_assoc();
			if(!$result['currentPostedJobs']){
				$currentPostedJobs = $jobTitle;
			}
			else{
				$currentPostedJobs = $jobTitle.", ".$result['currentPostedJobs'];
			}
			
			if($stmt = $this->con->prepare("UPDATE user SET currentPostedJobs = ? WHERE userName = ?")){
				$stmt->bind_param("ss",$currentPostedJobs,$userName);
				if($stmt->execute()){
					return true;
				}				
			}
			return false;
		}
		
		public function addComment($body, $jobTitle){
			if($this->isJobExist($jobTitle)){
				$result = $this->getComments($jobTitle);
				if(!$result['comments']){
					$comments['comments'] = $body;
				}
				else{
					$comments['comments'] = $body.", ".$result['comments'];
				}
				
				$stmt = $this->con->stmt_init();
				if($stmt = $this->con->prepare("UPDATE job SET comments = ? WHERE jobTitle = ?")){
					$stmt->bind_param("ss", $comments['comments'], $jobTitle);
					if($stmt->execute()){
						return 1; //Success
					}
					else{
						return 3;
					}
				}
				else{
					return 2; //Prepare statement error
				}
				
			}
			else{
				return 0; //Job does not exist
			}
		}
		
		public function createJob($jobTitle, $jobDescription, $datePosted, $dateOfJob, $startTime, $endTime, $latitude, $longitude, $postedBy){
			if($this->isJobExist($jobTitle)){
				return 0;
			}
			else{
				$start = date($startTime);
				$end = date($endTime);
				$stmt = $this->con->stmt_init();
				if($stmt = $this->con->prepare("INSERT INTO job (id,jobTitle,jobDescription,datePosted,dateOfJob,startTime,endTime,latitude,longitude,postedBy) VALUES (NULL,?,?,?,?,?,?,?,?,?);")){				
					$stmt->bind_param("ssssssdds", $jobTitle, $jobDescription, $datePosted, $dateOfJob, $start, $end, $latitude, $longitude, $postedBy);
					if($stmt->execute()){
						if($this->addPostedJob($postedBy,$jobTitle)){
							return 1;
						}
						else{
							return 3;
						}
					}
					else{
						return 2;
					}
				}
			}
		}
		
		private function isJobExist($jobTitle){
			$stmt = $this->con->prepare("SELECT id FROM job WHERE jobTitle = ?");
			$stmt->bind_param("s", $jobTitle);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}
	}
?>