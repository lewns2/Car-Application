package com.nexenio.bleindoorpositioningdemo;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Sub_car extends AppCompatActivity {
    BatteryProgressView progress;
    int count=0;
    float pers;
    android.os.Handler handler2;
    // 선언 구간 //
    Button drive_button, point_button, security_button, car_button, back_button, BluetoothON, BluetoothConnect;
    private long lastTimeBackPressed;
    TextView ID_text, battery_total, battery_time, BluetoothStatus, voltage;

    // 블루투스 선언 //
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // 시작 구간 //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        progress= (BatteryProgressView)findViewById(R.id.progress);
        progress.setProgress(66);
        handler2=new android.os.Handler();

        // 버튼 선언 //
        drive_button = (Button) findViewById(R.id.drive_button);
        security_button = (Button) findViewById(R.id.security_button);
        car_button = (Button) findViewById(R.id.car_button);
        back_button = (Button) findViewById(R.id.back_button);
        point_button = (Button) findViewById(R.id.point_button);
        BluetoothON = (Button) findViewById(R.id.BluetoothON);
        BluetoothConnect = (Button) findViewById(R.id.BluetoothConnect);
        ID_text = (TextView) findViewById(R.id.ID_text);
        battery_total = (TextView) findViewById(R.id.battery_total);
        battery_time = (TextView) findViewById(R.id.battery_time);
        BluetoothStatus = (TextView) findViewById(R.id.BluetoothStatus);
        voltage = (TextView) findViewById(R.id.voltage) ;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothConnect.setEnabled(false);

        // ID 불러오기 //
        final Intent ID_intent = getIntent();
        String data = ID_intent.getStringExtra("ID");
        ID_text.setText(data);

        // 레이아웃 이동 구간//

        drive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_car.this, HomeActivity.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        security_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_car.this, Sub_security.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        point_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sub_car.this, Sub_point.class);
                intent.putExtra("ID", "" + ID_text.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sub_car.this);
                builder.setTitle("로그아웃을 하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_car.this, "로그아웃이 정상적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sub_car.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Sub_car.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });

        // 블루투스 버튼 활성화 //
        BluetoothON.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                bluetoothOn();
            }
        });

        BluetoothConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                listPairedDevices();
            }
        });

        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    battery_total.setText(readMessage);
                    voltage.setText("V");
                    pers = Float.parseFloat(readMessage);
                    pers = pers * 20;

                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(pers);
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setProgress(pers);

                                }
                            }, 1000);
                        }
                    }, 2000);
                    if (Float.parseFloat(battery_total.getText().toString()) > 0) {
                        float batterytime = Float.parseFloat(battery_total.getText().toString()) * 20;
                        float hour = batterytime / 60;
                        String hour_s = String.format("%.2f",hour);
                        battery_time.setText(hour_s + "시간");
                    }
                }
            }
        };
    }

    // 블루투스 함수 //
    void bluetoothOn() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                BluetoothConnect.setEnabled(true);
                BluetoothStatus.setText("블루투스 ON || Connect를 눌러주세요.");
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
                BluetoothConnect.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    BluetoothStatus.setText("활성화완료");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
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
