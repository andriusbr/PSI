<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   
  
   
  $sql = 'SELECT id, username, password, first_name, last_name FROM courier_login';
  //mysql_select_db('test_db');
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    die(json_encode($response));
  }

  $ids=array();
  $usernames=array();
  $passwords=array();
  $first_names=array();
  $last_names=array();
   
  while($row = mysql_fetch_array($retval, MYSQL_ASSOC)) {
    array_push($ids, $row['id']);
    array_push($usernames, $row['username']);
    array_push($passwords, $row['password']);
    array_push($first_names, $row['first_name']);
    array_push($last_names, $row['last_name']);
  }

  $response["success"] = 1;
  $response["ids"] = $ids;
  $response["usernames"] = $usernames;
  $response["passwords"] = $passwords;
  $response["first_names"] = $first_names;
  $response["last_names"] = $last_names;
  $response["message"] = " Success ";
  die(json_encode($response));
   
  mysql_close();
  exit; ?>