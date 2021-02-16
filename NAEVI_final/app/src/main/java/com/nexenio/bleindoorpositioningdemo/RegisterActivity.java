package com.nexenio.bleindoorpositioningdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    // 선언 //
    private EditText e_mail, password, name, password_confirm, phone_second, phone_third;
    private long lastTimeBackPressed;
    private Button register_button, back_button, ID_check;
    private ArrayAdapter adapter_phone, adapter_year, adapter_month, adapter_day;
    private Spinner phone_first, birth_year, birth_month, birth_day;
    private RadioGroup gender_group;
    private TextView year_note, month_note, day_note, phone_first_note;
    private boolean validate = false;
    private AlertDialog dialog;

    // 시작 //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        // 역할 //
        e_mail = (EditText) findViewById(R.id.e_mail);
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.password_confirm);
        name = (EditText) findViewById(R.id.name);
        register_button = (Button) findViewById(R.id.register_button);
        back_button = (Button) findViewById(R.id.back_button);
        ID_check = (Button) findViewById(R.id.ID_check);
        birth_year = (Spinner) findViewById(R.id.birth_year);
        birth_month = (Spinner) findViewById(R.id.birth_month);
        birth_day = (Spinner) findViewById(R.id.birth_day);
        phone_first = (Spinner) findViewById(R.id.phone_first);
        phone_second = (EditText) findViewById(R.id.phone_second);
        phone_third = (EditText) findViewById(R.id.phone_third);
        gender_group = (RadioGroup) findViewById(R.id.gender_group);
        year_note = (TextView) findViewById(R.id.year_note);
        month_note = (TextView) findViewById(R.id.month_note);
        day_note = (TextView) findViewById(R.id.day_note);
        phone_first_note = (TextView) findViewById(R.id.phone_first_note);

        // 스피너 생년월일 //
        adapter_phone = ArrayAdapter.createFromResource(this, R.array.phone, android.R.layout.simple_spinner_dropdown_item);
        phone_first.setAdapter(adapter_phone);
        adapter_year = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_dropdown_item);
        birth_year.setAdapter(adapter_year);
        adapter_month = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_dropdown_item);
        birth_month.setAdapter(adapter_month);
        adapter_day = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_dropdown_item);
        birth_day.setAdapter(adapter_day);

        phone_first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phone_first_note.setText("" + adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birth_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 year_note.setText("" + adapterView.getItemAtPosition(i));
             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        birth_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month_note.setText("" + adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birth_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_note.setText("" + adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 아이디 검증 시작 //
        ID_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = e_mail.getText().toString();
                if (validate) {
                    return;// 검증 완료 //
                }
                // ID 값을 입력하지 않았다면 //
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요!")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 아이디 검증시작 //
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success & (4<e_mail.getText().toString().length()) & (13>e_mail.getText().toString().length())) { //사용할 수 있는 아이디라면 //
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("사용할 수 있는 아이디입니다.");
                                builder.setMessage("id : " + e_mail.getText().toString() + "을 사용하시겠습니까?");
                                builder.setPositiveButton("예",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(RegisterActivity.this, "중복 체크를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                                e_mail.setEnabled(false);// 아이디값을 바꿀 수 없도록 함 //
                                                validate = true;// 검증완료 //
                                                e_mail.setBackgroundColor(getResources().getColor(R.color.md_red_50));
                                                ID_check.setBackgroundColor(getResources().getColor(R.color.md_red_50));
                                            }
                                        });
                                builder.setNegativeButton("아니요",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(RegisterActivity.this, "중복 체크를 다시 해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                builder.show();
                            } else {// 사용할 수 없는 아이디라면 //
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }// Response.Listener 완료 //
                };

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        // 비밀번호 일치 검사 //
        password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userPassword = password.getText().toString();
                String userPasswordConfirm = password_confirm.getText().toString();

                if (userPassword.equals(userPasswordConfirm)) {
                    password.setBackgroundColor(Color.GREEN);
                    password_confirm.setBackgroundColor(Color.GREEN);
                } else {
                    password.setBackgroundColor(Color.RED);
                    password_confirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 회원 가입 //
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = e_mail.getText().toString();
                String userPassword = password.getText().toString();
                String userName = name.getText().toString();
                String userPhone = phone_first_note.getText().toString() + "-" + phone_second.getText().toString() + "-" + phone_third.getText().toString();
                String userBirth = year_note.getText().toString() + month_note.getText().toString() + day_note.getText().toString();

                //ID 중복체크를 했는지 확인함
                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디 중복체크를 하세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 성별 감지 //
                final int gender = gender_group.getCheckedRadioButtonId();
                final RadioButton gender_button = (RadioButton) findViewById(gender);
                String userGender = gender_button.getText().toString();

                // 입력 확인 //

                if(name.getText().toString().length() <2) {
                    Toast.makeText(RegisterActivity.this, "성함을 2자 이상 입력하세요!", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }

                if(phone_second.getText().toString().length() <3) {
                    Toast.makeText(RegisterActivity.this, "전화번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                    phone_second.requestFocus();
                    return;
                }

                if(phone_second.getText().toString().length() >4) {
                    Toast.makeText(RegisterActivity.this, "전화번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                    phone_second.requestFocus();
                    return;
                }

                if(phone_third.getText().toString().length() <3) {
                    Toast.makeText(RegisterActivity.this, "전화번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                    phone_third.requestFocus();
                    return;
                }

                if(phone_third.getText().toString().length() >4) {
                    Toast.makeText(RegisterActivity.this, "전화번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                    phone_third.requestFocus();
                    return;
                }

                if(e_mail.getText().toString().length() < 5) {
                    Toast.makeText(RegisterActivity.this, "아이디를 5~12자 내외로 입력하세요!", Toast.LENGTH_SHORT).show();
                    e_mail.requestFocus();
                    return;
                }

                if(e_mail.getText().toString().length() > 12) {
                    Toast.makeText(RegisterActivity.this, "아이디를 5~12자 내외로 입력하세요!", Toast.LENGTH_SHORT).show();
                    e_mail.requestFocus();
                    return;
                }

                if(password.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 6자 이상 입력하세요!", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                if(password_confirm.getText().toString().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    password_confirm.requestFocus();
                    return;
                }

                if(!password.getText().toString().equals(password_confirm.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    password_confirm.setText("");
                    password.requestFocus();
                    return;
                }

                // 서버 통신 //
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // 성공여부 확인 //
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "회원가입을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, Main_Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // 예외처리, e로 데이터 이동//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // 서버로 요청을 함. //
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userGender, userPhone, userBirth, 10, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }

        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Main_Login.class);
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

}
