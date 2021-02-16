package com.nexenio.bleindoorpositioningdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Main_Login extends AppCompatActivity {

    // 선언 구간 //
    Button login_button, register_button;
    private long lastTimeBackPressed;
    private EditText e_mail, password;
    private CheckBox auto_login;
    private boolean saveLoginData;
    private String id, pwd;
    private SharedPreferences appData;

    // 시작 구간 //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        // 버튼 선언 //
        login_button = (Button) findViewById(R.id.login_button);
        register_button = (Button) findViewById(R.id.register_button);
        e_mail = (EditText) findViewById(R.id.e_mail);
        password = (EditText) findViewById(R.id.password);
        auto_login = (CheckBox) findViewById(R.id.auto_login);

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            e_mail.setText(id);
            password.setText(pwd);
            auto_login.setChecked(saveLoginData);
        }

        // 레이아웃 이동 구간//
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = e_mail.getText().toString();
                final String userPassword = password.getText().toString();

                // 로그인 서버통신 //
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // 성공여부 확인 //
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                id = userID;
                                pwd = userPassword;
                                save();
                                Intent intent = new Intent(Main_Login.this, HomeActivity.class);
                                intent.putExtra("ID", "" + id);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // 예외처리, e로 데이터 이동//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Main_Login.this);
                queue.add(loginRequest);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Login.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //뒤로가기 버튼 2번 눌렀을 때 종료 ********************************************************************************//
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            super.onBackPressed();
        }
        Toast.makeText(getApplicationContext(), "뒤로 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
    //뒤로가기 버튼 2번 눌렀을 때 종료 ********************************************************************************//

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", auto_login.isChecked());
        editor.putString("ID", e_mail.getText().toString().trim());
        editor.putString("PWD", password.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        pwd = appData.getString("PWD", "");
    }

}
