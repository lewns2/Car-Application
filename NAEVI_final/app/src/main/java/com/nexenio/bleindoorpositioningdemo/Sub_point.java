package com.nexenio.bleindoorpositioningdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class Sub_point extends AppCompatActivity {

    // 선언 구간 //
    Button drive_button, point_button, security_button, car_button, back_button, plus_button, point_list;
    TextView point_result, ID_text;
    private long lastTimeBackPressed;
    phpdo task;

    // 시작 구간 //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        // 버튼 선언 //
        drive_button = (Button) findViewById(R.id.drive_button);
        security_button = (Button) findViewById(R.id.security_button);
        car_button = (Button) findViewById(R.id.car_button);
        back_button = (Button) findViewById(R.id.back_button);
        point_button = (Button) findViewById(R.id.point_button);
        plus_button = (Button) findViewById(R.id.plus_button);
        point_list = (Button) findViewById(R.id.point_list);
        point_result = (TextView) findViewById(R.id.point_result);
        ID_text = (TextView)findViewById(R.id.ID_text);
        final String userID = ID_text.getText().toString();

        // ID 불러오기 //
        final Intent ID_intent = getIntent();
        String data = ID_intent.getStringExtra("ID");
        ID_text.setText(data);

        // 포인트 값을 얻는데 쓰이는 아이디 호출 //
        String id = ID_text.getText().toString();
        task = new phpdo();
        task.execute(id);

        // 레이아웃 이동 구간//

        drive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_point.this, HomeActivity.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        security_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_point.this, Sub_security.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_point.this, Sub_car.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sub_point.this);
                builder.setTitle("로그아웃을 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_point.this, "로그아웃이 정상적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sub_point.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_point.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });

        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sub_point_plus.class);
                intent.putExtra("point_result", point_result.getText());
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivityForResult(intent,3000);
            }
        });
    }

    // 포인트 가져오기 //
    private class phpdo extends AsyncTask<String,Void,String> {
        protected void onPreExecute(){

        }
        @Override
        protected String doInBackground(String... arg0) {

            try{
                String id = arg0[0];
                String link = "http://wkths22.dothome.co.kr/getpoint.php?userID=" + id;

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null){
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } catch (Exception e){
                return new String("Exception : " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //txtview.setText("Login Successful");
            point_result.setText(result);
        }
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
