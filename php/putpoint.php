<?php 
    $con=mysqli_connect("localhost","id","password","id");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userName = $_POST["userName"];
    $userGender = $_POST["userGender"];
    $userPhone = $_POST["userPhone"];
    $userBirth = $_POST["userBirth"];
    $Point = $_POST["Point"];

    mysqli_query($con,"UPDATE USER SET Point='$Point' WHERE userID='$userID'");


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);



?>

