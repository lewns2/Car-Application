<?php
$con=mysqli_connect("localhost","id","password","id");
 
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
 
$userID = $_GET['userID'];
$result = mysqli_query($con,"SELECT Point FROM USER where userID='$userID'");
 
$row = mysqli_fetch_array($result);
$data = $row[0];
 
if($data){
echo $data;
}
mysqli_close($con);
?>