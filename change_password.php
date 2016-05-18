<?php
$host="mysql8.000webhost.com";
$user="a2316779_ab";
$pass="qazwsx3";
$dbname="a2316779_kp";
  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);

  $username=$_POST["username"];
  $old_password=$_POST["old_password"];
  $new_password=$_POST["new_password"];
   

   $response["success"] = 0;
  if (!empty($_POST)) {
    if(empty($_POST['username'])){
      $response["message"] = "Authentication failure.";
      die(json_encode($response));
    }
  if (empty($_POST['old_password']) || empty($_POST['new_password'])) {
  // Create some data that will be the JSON response 
          $response["message"] = "One or more fields are empty1.";
          
          //die is used to kill the page, will not let the code below to be executed. It will also
          //display the parameter, that is the json data which our android application will parse to be //shown to the users
          die(json_encode($response));
      }
      
$sql = "UPDATE courier_login SET password='$new_password' WHERE username='$username'";

$retval = mysql_query( $sql, $conn );
  if(! $retval )
  {
    $response["message"] = 'Could not update data: ' . mysql_error();//"Invalid password ";//
    die(json_encode($response));
  }
  else{
    $response["success"] = 1;
    $response["message"] = "Password changed";
  die(json_encode($response));
  }
      
   
   
  }
  else{
    $response["message"] = " One or more fields are empty2.";
    die(json_encode($response));
  }
   
  mysql_close($conn);
  ?>