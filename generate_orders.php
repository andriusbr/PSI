<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   
  
   
  $sql = "SELECT `id` FROM `order` WHERE `courier_id` IS NULL";
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }
  $order_id_array = array();
  while($row = mysql_fetch_array($retval, MYSQL_ASSOC)) {
  		$order_id_array[] = $row['id'];
  }
  if(empty($order_id_array)){
  	$response["success"] = 0;
    $response["message"] = " All orders are set ";
    die(json_encode($response));
  }

  $sql = "SELECT `id` FROM `courier_login`";
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }
  $courier_id_array = array();
  while($row = mysql_fetch_array($retval, MYSQL_ASSOC)) {
  	$courier_id_array[] = $row['id'];
  }

  if(empty($courier_id_array)){
  	$response["success"] = 0;
    $response["message"] = " There are no couriers ";
    die(json_encode($response));
  }

  $courier_index = 1;
  for($i = 0; $i < sizeof($order_id_array); $i++) {
  	if($courier_index > sizeof($courier_id_array)){
  		$courier_index = 1;
  	}

  	$sql = "UPDATE `order`
  			SET `courier_id` = {$courier_id_array[$courier_index]}
  			WHERE `id` = {$order_id_array[$i]}";
  	$retval = mysql_query( $sql, $conn );
   
  	if(! $retval ) {
    	$response["success"] = 0;
    	$response["message"] = " Error updating data ";
    	die(json_encode($response));
  	}

  	$courier_index++;
  }



  $response["success"] = 1;
  $response["message"] = " Success ";
  die(json_encode($response));
   
  mysql_close();
  exit; ?>