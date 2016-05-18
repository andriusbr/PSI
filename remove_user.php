<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   
  
  $id=$_POST["id"];
   
  $sql = "DELETE FROM courier_login WHERE id='$id'";

  $retval = mysql_query( $sql, $conn );
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }else{
    $response["success"] = 1;
    $response["message"] = " User deleted succesfully ";
    die(json_encode($response));
  }
  
  mysql_close();
  exit; ?>