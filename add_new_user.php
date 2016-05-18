<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   
  
  $firstname=$_POST["firstname"];
  $lastname=$_POST["lastname"];
  $login=$_POST["login"];
   
  if (!empty($_POST)) {
    if (empty($_POST["firstname"]) || empty($_POST["lastname"]) || empty($_POST["login"])) {
      $response["success"] = 0;
      $response["message"] = " One or more fields are empty ";
      die(json_encode($response));
    }

    $query = " SELECT * FROM courier_login WHERE username = '$login'";
    $sql1=mysql_query($query);
    $row = mysql_fetch_array($sql1);

    if(! $row){
      $sql = "INSERT INTO courier_login (username, password, first_name, last_name)
        VALUES ('$login', '$login', '$firstname', '$lastname')";

      //mysql_select_db('test_db');
      $retval = mysql_query( $sql, $conn );

      if(! $retval){
        $response["success"] = 0;
        $response["message"] = " Database error ";//. mysql_error();
        die(json_encode($response));
      }else{
        $response["success"] = 1;
        $response["message"] = " User added successfully ";
        die(json_encode($response));
      }
    }else{
      $response["success"] = 0;
      $response["message"] = " Login already exists ";
      die(json_encode($response));
    }
  }
  else{
    $response["success"] = 0;
    $response["message"] = " One or more fields are empty ";
    die(json_encode($response));
  }
   
  mysql_close();
  exit; ?>