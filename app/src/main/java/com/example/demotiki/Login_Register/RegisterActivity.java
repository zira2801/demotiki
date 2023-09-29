package com.example.demotiki.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demotiki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout layoutpass;
    TextInputEditText edtpass,edtemail,edtrepass;

    ProgressBar process;
    Button trove,dangky;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        layoutpass = (TextInputLayout) findViewById(R.id.textinPass);
        edtemail = (TextInputEditText) findViewById(R.id.edt_email);
        edtpass = (TextInputEditText) findViewById(R.id.edt_password);
        edtrepass = (TextInputEditText) findViewById(R.id.edt_re_password);
        trove = (Button) findViewById(R.id.btn_trolai);
        dangky = (Button) findViewById(R.id.btn_dangky);
        process = (ProgressBar) findViewById(R.id.progressDK);
        edtpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = charSequence.toString();
                if(pass.length() >= 8){
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(pass);
                    boolean isPwContains = matcher.find();
                    if(isPwContains){
                        layoutpass.setHelperText("Mật khẩu mạnh");
                        layoutpass.setError("");
                    }
                    else{
                        layoutpass.setHelperText("");
                        layoutpass.setError("Mật khẩu quá yếu! phải có ít nhất 1 ký tự đặc biệt");
                    }
                }
                else{
                    layoutpass.setHelperText("Mật khẩu phải từ nhất 8 - 12 ký tự");
                    layoutpass.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* startActivity(new Intent(RegisterActivity.this,LoginActivity.class));*/
                finish();
            }
        });

        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,repass;
                email = String.valueOf(edtemail.getText());
                pass = String.valueOf(edtpass.getText());
                repass = String.valueOf(edtrepass.getText());

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(repass)){
                    Toast.makeText(RegisterActivity.this,"Bạn chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!repass.equals(pass)){
                        Toast.makeText(RegisterActivity.this,"Mật khẩu nhập lại không đúng",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        process.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        process.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this,"Đăng ký tài khoản thành công",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this,"Email đã tồn tại",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}