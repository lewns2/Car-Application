package com.nexenio.bleindoorpositioningdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;
    private TextView ID_text, point_result;
    phpdo task;
    Button point_button, security_button, car_button, back_button;
    private long lastTimeBackPressed;

    public  static int signal=0;
    //draw 신호함수

    // 소켓 관련 //
    private String convoMessage;
    private Handler handler = new Handler();
    private ServerConnection serverConnection;
    Button connect, findline, funend, findline1, findline2;

    // 본문 시작 //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        security_button = (Button) findViewById(R.id.security_button);
        car_button = (Button) findViewById(R.id.car_button);
        back_button = (Button) findViewById(R.id.back_button);
        point_button = (Button) findViewById(R.id.point_button);
        connect = (Button)findViewById(R.id.connect);
        findline = (Button)findViewById(R.id.findline);
        findline1 = (Button)findViewById(R.id.findline1);
        findline2 = (Button)findViewById(R.id.findline2);
        funend = (Button)findViewById(R.id.funend);
        point_result = (TextView) findViewById(R.id.point_result);
        ID_text = (TextView)findViewById(R.id.ID_text);

        // ID 불러오기 //
        Intent ID_intent = getIntent();
        String data = ID_intent.getStringExtra("ID");
        ID_text.setText(data);

        // 포인트 값을 얻는데 쓰이는 아이디 호출 //
        String id = ID_text.getText().toString();
        task = new phpdo();
        task.execute(id);

        // 레이아웃 이동 구간//
        security_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Sub_security.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Sub_car.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        point_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Sub_point.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("로그아웃을 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeActivity.this, "로그아웃이 정상적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeActivity.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });


        // setup UI
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        // 소켓 부분 //
        connect.setEnabled(true);
        connect.setOnClickListener(this);
        findline.setEnabled(false);
        findline.setOnClickListener(this);
        funend.setEnabled(false);
        funend.setOnClickListener(this);
        findline1.setEnabled(false);
        findline1.setOnClickListener(this);
        findline2.setEnabled(false);
        findline2.setOnClickListener(this);
    }
    @Override
    public  void finish(){
        signal=0;
        super.finish();
    }
    @Override
    public void onClick(View v) {
        if (v == findline) {
            convoMessage = "FindLine";
            back_point_purchase(250);
            findline.setEnabled(false);

        }
        if (v == findline1) {
            convoMessage = "FindFirst";
            findline.setEnabled(true);
            point_purchase(250, 1);
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    signal = 1;
                }
            },2000);
        }
        if (v == findline2) {
            convoMessage = "FindSecond";
            findline.setEnabled(true);
            point_purchase(250, 2);
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    signal = 2;
                }
            },2000);

        }
        if (v == funend) {
            convoMessage = "funEnd";
            findline1.setEnabled(true);
            findline2.setEnabled(true);
            sendToServer(convoMessage);
        }
        if (v == connect) {
            connect.setEnabled(false);
            findline1.setEnabled(true);
            findline2.setEnabled(true);
            funend.setEnabled(true);
            new ServerConnectionTask().execute();
        }
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

    // 포인트 잔량 + 포인트 결제 함수 + 포인트 수정 //
    void point_purchase(final int point, final int number) {
        final int point_total = Integer.parseInt(point_result.getText().toString());
        final int result = point_total - point;
        String id = ID_text.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    final boolean success = jsonResponse.getBoolean("success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle(number + "번 충전소 포인트 선결제");
                    builder.setMessage(point_total + "포인트 남아있으며, " + point + "포인트 만큼 결제됩니다. 결제하시겠습니까");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (point_total < point) {
                                        Toast.makeText(HomeActivity.this, "포인트 충전이 필요합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (success) {
                                            Toast.makeText(HomeActivity.this, point + "포인트 결제를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                            sendToServer(convoMessage);
                                            findline.setEnabled(true);
                                            log("Me: " + convoMessage);
                                        } else {
                                            Toast.makeText(HomeActivity.this, point + "포인트 결제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    builder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(HomeActivity.this, point + "포인트 결제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PointPlusRequest earnRequest = new PointPlusRequest(id, result, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(earnRequest);
    }

    // 포인트 잔량 + 포인트 결제 함수 + 포인트 수정 //
    void back_point_purchase(final int point) {
        final int point_total = Integer.parseInt(point_result.getText().toString());
        final int result = point_total + point;
        String id = ID_text.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    final boolean success = jsonResponse.getBoolean("success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("포인트 환물");
                    builder.setMessage(point + "포인트 환불 받으신 후 충전을 취소하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (success) {
                                        Toast.makeText(HomeActivity.this, point + "포인트 환불을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        sendToServer(convoMessage);
                                        log("Me: " + convoMessage);
                                    } else {
                                        Toast.makeText(HomeActivity.this, point + "포인트 환불을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    builder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(HomeActivity.this, point + "포인트 환불을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PointPlusRequest earnRequest = new PointPlusRequest(id, result, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(earnRequest);
    }

    // 소켓 관련 내용 시작 //

    public void log(String msg) {
        final String line = msg + "\n";
        handler.post(new Runnable() {
            @Override
            public void run() {
                // textView.setText(textView.getText() + line);
                //textView.invalidate();
            }
        });
    }

    void sendToServer(String msg) {
        final String lineFromUser = msg;
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnection.sendToServer(lineFromUser);
            }
        }).start();
    }

    void disconnectToServer() {
        serverConnection.disconnectToServer();
    }

    public void disconnected() {
        serverConnection = null;
        connect.setEnabled(true);
        findline.setEnabled(false);
        funend.setEnabled(false);
    }

    private class ServerConnectionTask extends AsyncTask<Void, Void, ServerConnection> {


        @Override
        protected ServerConnection doInBackground(Void... _) {
            String host = "192.168.1.48";
            int port = Integer.valueOf("10223");
            serverConnection = new ServerConnection(HomeActivity.this, host, port);
            serverConnection.connect();
            return serverConnection;
        }

        @Override
        protected void onPostExecute(ServerConnection serverConnection) {
            HomeActivity.this.serverConnection = serverConnection;

            new Thread(serverConnection).start();
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
