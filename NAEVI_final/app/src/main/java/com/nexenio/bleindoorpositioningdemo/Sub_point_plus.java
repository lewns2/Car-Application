package com.nexenio.bleindoorpositioningdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Sub_point_plus extends AppCompatActivity {

    // 선언 구간 //
    Button back_button, point_plus_100, point_plus_200, point_plus_300, point_plus_500, point_plus_1000, point_plus_2000;
    TextView point_count, ID_text;

    // 시작 구간 //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_plus);

        // 버튼 선언 //
        back_button = (Button) findViewById(R.id.back_button);
        point_plus_100 = (Button) findViewById(R.id.point_plus_100);
        point_plus_200 = (Button) findViewById(R.id.point_plus_200);
        point_plus_300 = (Button) findViewById(R.id.point_plus_300);
        point_plus_500 = (Button) findViewById(R.id.point_plus_500);
        point_plus_1000 = (Button) findViewById(R.id.point_plus_1000);
        point_plus_2000 = (Button) findViewById(R.id.point_plus_2000);
        point_count = (TextView) findViewById(R.id.point_count);
        ID_text = (TextView)findViewById(R.id.ID_text);

        // ID 불러오기 //
        final Intent ID_intent = getIntent();
        String data1 = ID_intent.getStringExtra("ID");
        ID_text.setText(data1);

        // 포인트 총량 불러오기//
        final Intent intent = getIntent();
        String data = intent.getStringExtra("point_result");
        point_count.setText(data);

        // 레이아웃 이동 구간//

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sub_point_plus.this);
                builder.setTitle("로그아웃을 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_point_plus.this, "로그아웃이 정상적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sub_point_plus.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_point_plus.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });

        // 포인트 충전 구간 //
        point_plus_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 100;
                point_purchase(point);
            }
        });

        point_plus_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 200;
                point_purchase(point);
            }
        });

        point_plus_300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 300;
                point_purchase(point);
            }
        });

        point_plus_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 500;
                point_purchase(point);
            }
        });

        point_plus_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 1000;
                point_purchase(point);
            }
        });

        point_plus_2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 2000;
                point_purchase(point);
            }
        });
    }

    // 포인트 잔량 + 포인트 충전 연산 함수 + 포인트 수정하기 //
    void point_purchase(final int point) {
        int point_total = Integer.parseInt(point_count.getText().toString());
        final int result = point_total + point;
        String id = ID_text.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    final boolean success = jsonResponse.getBoolean("success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Sub_point_plus.this);
                    builder.setTitle("포인트 결제 확인");
                    builder.setMessage(point + "포인트를 결제하시겠습니까? (" + point*100 + "원)");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (success) {
                                        Toast.makeText(Sub_point_plus.this, point + "포인트 결제를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Sub_point_plus.this, Sub_point.class);
                                        intent.putExtra("ID", "" + ID_text.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Sub_point_plus.this, point + "포인트 결제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    builder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Sub_point_plus.this, point + "포인트 결제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PointPlusRequest earnRequest = new PointPlusRequest(id, result, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Sub_point_plus.this);
        queue.add(earnRequest);
    }
}
