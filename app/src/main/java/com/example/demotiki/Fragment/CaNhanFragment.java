package com.example.demotiki.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demotiki.Login_Register.LoginActivity;
import com.example.demotiki.MainActivity;
import com.example.demotiki.MainActivity2;
import com.example.demotiki.R;
import com.example.demotiki.ThongTinTaiKhoan.ThongTinTaiKhoanActivity;
import com.example.demotiki.TrangCaNhan.TrangCaNhanActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaNhanFragment extends Fragment {

    public CaNhanFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    LinearLayout login;
    LinearLayout dangxuat;
    private CircleImageView avatar;
    private TextView tentk,welcome;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;


    LinearLayout tttk,trangcn;
    ProgressDialog progressDialog;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //Tra ve user

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        avatar = view.findViewById(R.id.img_avatar);
        tentk = view.findViewById(R.id.tv_tentk);
        welcome = view.findViewById(R.id.tv_welcome);
        dangxuat = view.findViewById(R.id.btn_ds);
        login = view.findViewById(R.id.tv_dangnhapdangki);
        tttk = view.findViewById(R.id.thongtintk);
        trangcn = view.findViewById(R.id.trangcanhan);

        trangcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TrangCaNhanActivity.class));
            }
        });
        tttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ThongTinTaiKhoanActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                signOutUser();
                            }
                        });
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getActivity(), MainActivity2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //FLAG_ACTIVITY_NEW_TASK: Cho phép Activity được mở trong một new task
                // FLAG_ACTIVITY_CLEAR_TASK: Xóa tất cả các Activity khác trong stack và chỉ giữ lại LoginActivity

                startActivity(i);
                getActivity().finish();
                firebaseAuth.signOut();
            }
        });
        showUserInformation();
        return view;
    }

    private void signOutUser() {
        Intent i = new Intent(getActivity(), MainActivity2.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);
        getActivity().finishAffinity();

    }
    private void showUserInformation(){

        if(user == null){
            trangcn.setVisibility(View.GONE);
            tttk.setVisibility(View.GONE);
            tentk.setVisibility(View.GONE);
            dangxuat.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.VISIBLE);
        }
        else{
            trangcn.setVisibility(View.VISIBLE);
            tttk.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);
            dangxuat.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = (String) snapshot.child("name").getValue();
                        String photoUrl = (String) snapshot.child("profile").getValue();
                        if (name == null) {
                            tentk.setText("");
                        } else {
                            tentk.setText(name);
                        }
                        Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(avatar);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu cần
                }
            });
        }
    }
}