package com.example.guswn.allthatlyrics.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.Extension.MyRetrofit;
import com.example.guswn.allthatlyrics.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    // 이메일 정규식
    // public static final Pattern VALID_EMAIL_ADDRESS_REGEX
    // = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    // 이메일 검사
    // public static boolean validateEmail(String emailStr) {
    // Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    // return matcher.find();
    // }
    // 비밀번호 정규식
    // public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM
    // = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$"); // 4자리 ~ 16자리까지 가능
    // 비밀번호 검사
    // public static boolean validatePassword(String pwStr) {
    // Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
    // return matcher.matches();
    // }



    @BindView(R.id.regis_email)
    EditText regis_email;
    @BindView(R.id.regis_password)
    EditText regis_password;
    @BindView(R.id.regis_confirm_password)
    EditText regis_confirm_password;
    @BindView(R.id.regis_username)
    EditText regis_username;
    @BindView(R.id.btn_register)
    Button btn_register;

    String email,password,con_password,username;
    Boolean passwordOK= false;


    RegisterAPI api;

    @OnClick(R.id.btn_register)
    public void registerclick(){
        email = regis_email.getText().toString();
        password = regis_password.getText().toString();
        con_password = regis_confirm_password.getText().toString();
        username = regis_username.getText().toString();

        Log.e("Register_Log",email+"/"+password+"/"+con_password+"/"+username);
        Boolean validate = validate();
        if(validate){
            // 정규식 통과

            Map<String,String> map = new HashMap<>();
            map.put("Password",password);
            map.put("Username",username);
            map.put("Email",email);

            Call<UserValue> call = api.createUser(map);

            call.enqueue(new Callback<UserValue>() {
                @Override
                public void onResponse(Call<UserValue> call, Response<UserValue> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(Register.this,response.code()+"",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UserValue values = response.body();
                    String value = values.getValue();
                    String message = values.getMessage();

                    if(value.equals("1")){
                        //이메일 중복확인 성공
                        Intent intent = new Intent(Register.this,Login.class);
                        startActivity(intent);
                        Toast.makeText(Register.this,message,Toast.LENGTH_SHORT).show();
                    }else {
                        //이미 존재하는 이메일
                        //Toast.makeText(Register.this,message,Toast.LENGTH_SHORT).show();
                        //regis_email.setTextColor(Color.RED);
                        regis_email.setError("이미 존재하는 이메일 입니다");
                    }
                }

                @Override
                public void onFailure(Call<UserValue> call, Throwable t) {
                    Toast.makeText(Register.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //레트로핏
        api = new MyRetrofit().create(RegisterAPI.class);
        //레트로핏
    }

    public boolean validate() {
        boolean valid = true;

        String name = regis_username.getText().toString();
        String email = regis_email.getText().toString();
        String password = regis_password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            regis_username.setError("이름은 적어도 3글자 이상이여야 합니다");
            valid = false;
        } else {
            regis_username.setError(null);
        }

        //비밀번호 확인
        if (password.isEmpty()
                || (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,20}$", password))) {
            regis_password.setError("비밀번호는 영문자와 특수문자 포함 6-20자리이여야 합니다");
            valid = false;
        } else {
            regis_password.setError(null);
            if(!password.equals(con_password)){
                //비밀번호 확인이 일치하지 않은 경우
                //regis_confirm_password.setTextColor(Color.RED);
                valid = false;
                regis_confirm_password.setError("비밀번호가 일치하지 않습니다");
                //Toast.makeText(Register.this,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
            }else {
                //비밀번호 확인이 일치할 경우

                //passwordOK = true;
                //regis_confirm_password.setTextColor(Color.BLACK);
            }
        }




        if (email.isEmpty() || (!Pattern.matches("^[A-z|0-9]([A-z|0-9]*)(@)([A-z]*)(\\.)([A-z]*)$", email))) {
            regis_email.setError("이메일 형식이 올바르지 않습니다");
            valid = false;
        } else {
            regis_email.setError(null);
        }



        return valid;
    }
}
