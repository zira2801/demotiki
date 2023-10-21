package com.example.demotiki.ChiTietSanPham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demotiki.DanhsachAnhSP.AnhSP;
import com.example.demotiki.DanhsachAnhSP.AnhSPFragment;
import com.example.demotiki.DanhsachAnhSP.DSAnhSPAdapter;
import com.example.demotiki.Fragment.HomeFragment;
import com.example.demotiki.R;
import com.example.demotiki.Slider.SliderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChiTietSPActivity extends AppCompatActivity {

    ViewPager2 viewPager2_DSANH;
    ImageView close,next_tk;
    TextView tv_tensp,gia,diemdanhgia,tendm,xuatxu,thuonghieu,baohanh,thoigianbaohanh,tennguoiban,motasp;
    RatingBar ratingBar;
    LinearLayout themgiohang,muangay;
    CircleImageView avatar;

    ArrayList<AnhSP> dsAnnhSP;
    String tensp;
    String danhmucsp;
    AnhSP anh;
    Double giasp;
    String moTasp;
    String xuatxusp,thuongHieu,danhgiasp,baohanhsp,tgbaohanh,idsp,iduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_spactivity);
        Unit();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Đặt tên các trường
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setGroupingUsed(true);
        String[] anhsp = getIntent().getStringArrayExtra("arrayAnh");
        dsAnnhSP = new ArrayList<>();
        if(anhsp != null) {
            for (String anh : anhsp) {
                Uri uri = Uri.parse(anh);
                AnhSP anhSP = new AnhSP(uri);
                dsAnnhSP.add(anhSP);
            }
        }
        tensp = getIntent().getStringExtra("tensp");
        danhmucsp = getIntent().getStringExtra("danhmucsp");
        giasp = getIntent().getDoubleExtra("giasp",0.0);
        moTasp = getIntent().getStringExtra("motasp");
        xuatxusp = getIntent().getStringExtra("xuatxusp");
        thuongHieu = getIntent().getStringExtra("thuonghieu");
        danhgiasp = getIntent().getStringExtra("danhgiasp");
        String[] rateParts = danhgiasp.split("/");
        baohanhsp = getIntent().getStringExtra("baohanh");
        tgbaohanh = getIntent().getStringExtra("tgbaohanh");
        idsp = getIntent().getStringExtra("idsp");
        iduser = getIntent().getStringExtra("iduser");

        //Hiển thị lên giao diện
        tv_tensp.setText(tensp);
        String formattedNumber = formatter.format(giasp);
        gia.setText(formattedNumber + " VNĐ");
        tendm.setText(danhmucsp);
        xuatxu.setText(xuatxusp);
        thuonghieu.setText(thuongHieu);
        baohanh.setText(baohanhsp);
        thoigianbaohanh.setText(tgbaohanh);
        motasp.setText(moTasp);
        diemdanhgia.setText(danhgiasp);
        if (rateParts.length > 0) {
            try {
                float rating = Float.parseFloat(rateParts[0]);
                ratingBar.setRating(rating);
            } catch (NumberFormatException e) {
                // Xử lý nếu chuỗi không thể chuyển đổi thành số
            }
        }
        ratingBar.setIsIndicator(true);
        //Sự kiện Viewpager ảnh sp
        DSAnhSPAdapter AnhSPAdapter = new DSAnhSPAdapter(ChiTietSPActivity.this,dsAnnhSP);
        viewPager2_DSANH.setAdapter(AnhSPAdapter);
        viewPager2_DSANH.setClipToPadding(false);
        viewPager2_DSANH.setOffscreenPageLimit(dsAnnhSP.size());
        viewPager2_DSANH.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        //Thông tin người bán
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(iduser);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String photoUrl = (String) snapshot.child("profile").getValue();
                if (name == null) {
                    tennguoiban.setText(null);
                } else {
                    tennguoiban.setText(name);
                }
                Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_add_photo_alternate_24).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Unit(){
        viewPager2_DSANH = (ViewPager2) findViewById(R.id.viewpager_anhsp);
        close = (ImageView) findViewById(R.id.back_chitiet_sp);
        tv_tensp = (TextView) findViewById(R.id.Detail_TV);
        gia = (TextView) findViewById(R.id.Dt_gia);
        diemdanhgia = (TextView) findViewById(R.id.diemDanhGia);
        ratingBar = (RatingBar) findViewById(R.id.ratingBarProduct);
        themgiohang = (LinearLayout) findViewById(R.id.Detail_themgiohang);
        muangay = (LinearLayout) findViewById(R.id.Detail_muangay);
        tendm = (TextView) findViewById(R.id.Dt_DMSP);
        xuatxu = (TextView) findViewById(R.id.Dt_XS);
        thuonghieu = (TextView) findViewById(R.id.tv_thuonghieu);
        baohanh = (TextView) findViewById(R.id.tv_baohanh);
        thoigianbaohanh = (TextView) findViewById(R.id.tv_tg_baohanh);
        avatar = (CircleImageView) findViewById(R.id.Detail_avatar);
        tennguoiban = (TextView) findViewById(R.id.tv_taikhoannguoidang);
        next_tk = (ImageView) findViewById(R.id.next_account);
        motasp = (TextView) findViewById(R.id.Dt_mota);
    }


}