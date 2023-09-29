package com.example.demotiki.Login_Register;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demotiki.MainActivity;
import com.example.demotiki.MainActivity2;
import com.example.demotiki.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    ImageView anhgg;
    ImageView anhface;
    FirebaseAuth  firebaseAuth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;
    CallbackManager mCallbackManager;
    FirebaseDatabase database;
    int RC_SIGN_IN = 20;
    TextInputEditText edtEmail,edtPass;
    Button nut_dn,trove;
    ProgressDialog progressDialog;
    BeginSignInRequest beginSignInRequest;

    TextView taotaikhoan;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        anhgg = (ImageView) findViewById(R.id.anhgg);
        anhface = (ImageView) findViewById(R.id.anhfb);
        edtEmail = (TextInputEditText) findViewById(R.id.edt_email_dn);
        edtPass = (TextInputEditText) findViewById(R.id.edt_matkhau_dn);
        nut_dn = (Button) findViewById(R.id.btn_dangnhap);
        taotaikhoan = (TextView) findViewById(R.id.taotk);
        trove = (Button) findViewById(R.id.btn_trove);

        trove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        taotaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        //Dang nhap bang email
        nut_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass;
                email = String.valueOf(edtEmail.getText());
                pass = String.valueOf(edtPass.getText());

                if(TextUtils.isEmpty(email)){
                    makeText(LoginActivity.this,"Bạn chưa nhập Email!", LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    makeText(LoginActivity.this,"Bạn chưa nhập Mật khẩu!", LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.show();
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user == null){
                                        return;
                                    }
                                    String userId = user.getUid();
                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                // Thêm dữ liệu vào database vì người dùng chưa có dữ liệu trong database
                                                usersRef.child("id").setValue(user.getUid());
                                                usersRef.child("name").setValue(user.getDisplayName());
                                                usersRef.child("email").setValue(user.getEmail());
                                                usersRef.child("SoDT").setValue("");
                                                usersRef.child("GioiTinh").setValue("");
                                                usersRef.child("NgaySinh").setValue("");
                                                usersRef.child("DiaChi").setValue("");
                                                usersRef.child("QuocTich").setValue("");
                                                usersRef.child("profile").setValue(user.getPhotoUrl());
                                                usersRef.child("AnhBia").setValue("");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công", LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this,"Tài khoản không tồn tại", LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(LoginActivity.this.getApplication());
        //Đăng nhập bằng google
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,options);
        database = FirebaseDatabase.getInstance();
        anhgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = googleSignInClient.getSignInIntent();
                startActivityForResult(i,RC_SIGN_IN);
            }
        });

        //Đăng nhập bằng facebook
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                    }
                });

        anhface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert  user != null;
                            makeText(LoginActivity.this, "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity2.class));
                            DatabaseReference usersRef = database.getReference().child("users").child(user.getUid());
                            usersRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        // Thêm dữ liệu vào database vì người dùng chưa có dữ liệu trong database
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("id", user.getUid());
                                        map.put("name", user.getDisplayName());
                                        map.put("profile", user.getPhotoUrl().toString());
                                        map.put("SoDT", "");
                                        map.put("email", user.getEmail());
                                        map.put("GioiTinh", "");
                                        map.put("NgaySinh", "");
                                        map.put("DiaChi", "");
                                        map.put("QuocTich", "");
                                        map.put("AnhBia", "");
                                        usersRef.setValue(map);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            startActivity(new Intent(LoginActivity.this,MainActivity2.class));
            finish();
        }
    }
    private void googleSignIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        /*startActivityForResult(intent,RC_SIGN_IN);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }catch (ApiException e){
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuth(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            DatabaseReference usersRef = database.getReference().child("users").child(user.getUid());
                            usersRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        // Thêm dữ liệu vào database vì người dùng chưa có dữ liệu trong database
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("id", user.getUid());
                                        map.put("name", user.getDisplayName());
                                        map.put("profile", user.getPhotoUrl().toString());
                                        map.put("SoDT", "");
                                        map.put("email", user.getEmail());
                                        map.put("GioiTinh", "");
                                        map.put("NgaySinh", "");
                                        map.put("DiaChi", "");
                                        map.put("QuocTich", "");
                                        map.put("AnhBia", "");
                                        usersRef.setValue(map);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            makeText(LoginActivity.this,"Đăng nhập thành công",LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                        }
                        else{
                            makeText(LoginActivity.this,"Hệ thống đang gặp một số lỗi!",LENGTH_SHORT).show();
                        }
                    }
                });
    }
}