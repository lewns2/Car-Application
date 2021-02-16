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

    $statement = mysqli_prepare($con, "INSERT INTO USER VALUES (?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssssssi", $userID, $userPassword, $userName, $userGender, $userPhone, $userBirth, $Point);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);



?>