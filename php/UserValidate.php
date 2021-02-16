<?php
    $con=mysqli_connect("localhost","id","password","id");
    $userID = $_POST["userID"];
    $statement = mysqli_prepare($con, "SELECT userID FROM USER WHERE userID = ?");
    //������ *�� �ϸ� mysqli_stmt_bind_result���� ������ ���� ������
    mysqli_stmt_bind_param($statement, "s", $userID);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);//����� Ŭ���̾�Ʈ�� ������
    mysqli_stmt_bind_result($statement, $userID);//����� $userID�� ���ε���
    $response = array();
    $response["success"] = true;
    while(mysqli_stmt_fetch($statement)){
    $response["success"] = false;//ȸ�����ԺҰ��� ��Ÿ��
    $response["userID"] = $userID;
    }
    //�����ͺ��̽� �۾��� ���� Ȥ�� �����Ѱ��� �˷���
    echo json_encode($response);
?>