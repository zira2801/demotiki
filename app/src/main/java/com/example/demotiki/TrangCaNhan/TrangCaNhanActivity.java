package com.example.demotiki.TrangCaNhan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demotiki.DangKyNhaBan.DangKyNhaBanActivity;
import com.example.demotiki.DangSanPham.DangSPActivity;
import com.example.demotiki.FragmentCaNhan.ViewPagerAdapter;
import com.example.demotiki.R;
import com.example.demotiki.ThongTinTaiKhoan.ThongTinTaiKhoanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrangCaNhanActivity extends AppCompatActivity {

    ImageView anhbia;
    CircleImageView ava;
    ImageView btn_back;
    TextView tentk;
    LinearLayout suatk,dangkynhaban;
    private TabLayout mTablayout;
    private ViewPager2 mViewPage;
    private ViewPagerAdapter mViewPagerAdapter;
    FloatingActionButton fab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);

        anhbia = (ImageView) findViewById(R.id.imageViewtt);
        ava = (CircleImageView) findViewById(R.id.anh_avatar);
        tentk = (TextView) findViewById(R.id.tv_username);
        btn_back = (ImageView) findViewById(R.id.backTTTK);
        suatk = (LinearLayout) findViewById(R.id.suatt);
        mTablayout = findViewById(R.id.tab_layout);
        mViewPage = findViewById(R.id.view_pager);
        fab = (FloatingActionButton) findViewById(R.id.fab_bn);
        dangkynhaban = (LinearLayout) findViewById(R.id.dangkynhaban);
        suatk.setVisibility(View.GONE);
        dangkynhaban.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanActivity.this, DangSPActivity.class));
            }
        });
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPage.setAdapter(mViewPagerAdapter);

        new TabLayoutMediator(mTablayout, mViewPage, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Sản phẩm");
                    break;
                case 1:
                    tab.setText("Đơn hàng");
                    break;
            }
        }).attach();
        suatk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanActivity.this, ThongTinTaiKhoanActivity.class));
            }
        });

        dangkynhaban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrangCaNhanActivity.this, DangKyNhaBanActivity.class));
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showUserInformation();

    }

    @SuppressLint("SetTextI18n")
    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = (String) snapshot.child("name").getValue();
                    String Anhbia = snapshot.child("AnhBia").getValue().toString();
                    String trangthaitk = (String) snapshot.child("StatusTK").getValue().toString();
                    if(trangthaitk.equals("Khách hàng")){
                        dangkynhaban.setVisibility(View.VISIBLE);
                    }
                    else{
                        suatk.setVisibility(View.VISIBLE);
                    }
                    if (name == null) {
                        tentk.setText("Tên tài khoản");
                    } else {
                        tentk.setText(name);
                    }
                    if(snapshot.hasChild("profile")){
                        String photoUrl = snapshot.child("profile").getValue().toString();
                        Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(ava);
                    }
                    Glide.with(getApplicationContext()).load(Anhbia).error(R.drawable.baseline_add_photo_alternate_24).into(anhbia);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private String getPath(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy tất cả tác vụ tải hình ảnh khi activity bị hủy bỏ
        /*Glide.with(getApplicationContext()).clear(avatar);*/
        Glide.with(getApplicationContext()).clear(ava);
    }
}