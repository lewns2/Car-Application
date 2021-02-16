package com.nexenio.bleindoorpositioningdemo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PointPlusRequest extends StringRequest{

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://wkths22.dothome.co.kr/putpoint.php";
    private Map<String, String> map;

    public PointPlusRequest(String userID, int Point, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("Point", Point + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
