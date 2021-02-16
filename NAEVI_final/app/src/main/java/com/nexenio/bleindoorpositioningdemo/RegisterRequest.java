package com.nexenio.bleindoorpositioningdemo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import  com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://wkths22.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword, String userName, String userGender, String userPhone, String userBirth, int Point, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userGender", userGender);
        map.put("userPhone", userPhone);
        map.put("userBirth", userBirth);
        map.put("Point", Point + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}