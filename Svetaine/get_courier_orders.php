<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   


  $retrieved_username=$_POST["_username"];

  $sql = "SELECT id
  FROM courier_login
  WHERE username = '$retrieved_username'";
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }

  $row = mysql_fetch_array($retval, MYSQL_ASSOC);
  if(empty($row)){
  	$response["success"] = 0;
    $response["message"] = " Error parsing data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }

  $retrieved_id=$row['id'];
  
  

  $sql = "SELECT id, address, product_price,
  first_name, last_name
  FROM `order`
  WHERE courier_id = '$retrieved_id'";
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }

  $ids=array();
  $addresses=array();
  $products=array();
  $prices=array();
  $first_names=array();
  $last_names=array();
   
  while($row = mysql_fetch_array($retval, MYSQL_ASSOC)) {
    array_push($ids, $row['id']);
    array_push($prices, $row['product_price']);
    array_push($addresses, $row['address']);
    array_push($first_names, $row['first_name']);
    array_push($last_names, $row['last_name']);


    $order_id=$row['id'];
    $sql2 = "SELECT product_name FROM products WHERE order_id = '$order_id'";
    $retval2 = mysql_query( $sql2, $conn );
   
    if(! $retval2 ) {
      $response["success"] = 0;
      $response["message"] = " Error retrieving data ";
      die(json_encode($response));
    }
    $product_names=array();
    while($row_product_name = mysql_fetch_array($retval2, MYSQL_ASSOC)){
        array_push($product_names, $row_product_name['product_name']);
    }

    array_push($products, $product_names);

  }

  $response["success"] = 1;
  $response["ids"] = $ids;
  $response["products"] = $products;
  $response["prices"] = $prices;
  $response["addresses"] = $addresses;
  $response["first_names"] = $first_names;
  $response["last_names"] = $last_names;
  die(json_encode($response));
   
  mysql_close();
  exit; ?>