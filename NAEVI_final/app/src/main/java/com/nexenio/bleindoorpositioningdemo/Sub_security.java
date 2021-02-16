package com.nexenio.bleindoorpositioningdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Sub_security extends AppCompatActivity {

    // 선언 구간 //
    Button drive_button, point_button, security_button, car_button, back_button;
    private long lastTimeBackPressed;
    TextView ID_text;
    WebView webView2;

    // 시작 구간 //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        // 버튼 선언 //
        drive_button = (Button) findViewById(R.id.drive_button);
        security_button = (Button) findViewById(R.id.security_button);
        car_button = (Button) findViewById(R.id.car_button);
        back_button = (Button) findViewById(R.id.back_button);
        point_button = (Button) findViewById(R.id.point_button);
        webView2 = (WebView) findViewById(R.id.wv_stream_2);
        ID_text = (TextView)findViewById(R.id.ID_text);

        // ID 불러오기 //
        final Intent ID_intent = getIntent();
        String data = ID_intent.getStringExtra("ID");
        ID_text.setText(data);

        // 레이아웃 이동 구간//

        drive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_security.this, HomeActivity.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_security.this, Sub_car.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        point_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_security.this, Sub_point.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sub_security.this);
                builder.setTitle("로그아웃을 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_security.this, "로그아웃이 정상적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sub_security.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_security.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });


        // 라즈베리파이 웹뷰 카메라 스트리밍 //
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setLoadWithOverviewMode(true);
        webView2.getSettings().setUseWideViewPort(true);

        String url2 = "http://192.168.1.55:8080/stream/video.mjpeg";
        webView2.loadUrl(url2);
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

}
