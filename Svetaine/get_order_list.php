<?php
  $host="mysql8.000webhost.com";
  $user="a2316779_ab";
  $pass="qazwsx3";
  $dbname="a2316779_kp";

  $conn=mysql_connect($host,$user,$pass);
  $db= mysql_select_db($dbname);   
  
   
  $sql = "SELECT * FROM `order`";
  $retval = mysql_query( $sql, $conn );
   
  if(! $retval ) {
    $response["success"] = 0;
    $response["message"] = " Error retrieving data ";
    //$response["message"] = " Error ". mysql_error();
    die(json_encode($response));
  }

  $ids=array();
  $quantities=array();
  $products=array();
  $prices=array();
  $couriers=array();
  $addresses=array();
   
  while($row = mysql_fetch_array($retval, MYSQL_ASSOC)) {
    $id=$row['id'];
    array_push($ids, $id);
    //array_push($quantities, $row['product_qty']);
    array_push($prices, $row['product_price']);
    array_push($addresses, $row['address']);
    $courier_id=$row['courier_id'];

    $sql2 = "SELECT username FROM courier_login WHERE id = '$courier_id'";
    $result = mysql_query($sql2);
   
    if(! $result ) {
      $response["success"] = 0;
      $response["message"] = " Error retrieving data ";
      die(json_encode($response));
    }
    $courier = mysql_result($result,0);
    array_push($couriers, $courier);


    $sql3 = "SELECT product_name FROM products WHERE order_id = '$id'";
    $retval3 = mysql_query( $sql3, $conn );
   
    if(! $retval3 ) {
      $response["success"] = 0;
      $response["message"] = " Error retrieving data ";
      die(json_encode($response));
    }
    $product_names=array();
    while($row_product_name = mysql_fetch_array($retval3, MYSQL_ASSOC)){
        array_push($product_names, $row_product_name['product_name']);
    }

    array_push($products, $product_names);
  }

  $response["success"] = 1;
  $response["ids"] = $ids;
  //$response["quantities"] = $quantities;
  $response["products"] = $products;
  $response["prices"] = $prices;
  $response["couriers"] = $couriers;
  $response["addresses"] = $addresses;
  die(json_encode($response));
   
  mysql_close();
  exit; ?>