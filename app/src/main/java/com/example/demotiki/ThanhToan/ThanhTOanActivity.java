package com.example.demotiki.ThanhToan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demotiki.Adapter.AdapterCart;
import com.example.demotiki.Adapter.DonHangAdapter;
import com.example.demotiki.AnotherClass.Cart;
import com.example.demotiki.AnotherClass.DonHang;
import com.example.demotiki.AnotherClass.SanPhamDonHang;
import com.example.demotiki.DangKyNhaBan.DangKyNhaBanActivity;
import com.example.demotiki.R;
import com.example.demotiki.TrangBill.TrangBillActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThanhTOanActivity extends AppCompatActivity {

    private boolean isMoMoSelected = false;
    private boolean isTienMatSelected = false;
    private boolean isGPaySelected = false;
    private ImageSwitcher imgMoMo;
    private ImageSwitcher imgTienMat;
    private ImageSwitcher imgGPay;
    private ArrayList<Cart> gioHangList ;

    DonHangAdapter adapterCart;
    private RecyclerView recyclerView;
    TextView tonggia,hinhthuocthantoan;
    Button xacnhan,huy;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CircleImageView avatar;
    EditText tennguoi,sdt;
    TextInputEditText diachi;
    ProgressDialog dialogPro;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        UI();
        showInfo();
        dialogPro = new ProgressDialog(ThanhTOanActivity.this);
        dialogPro.setMessage("Vui lòng chờ...");
        gioHangList = new ArrayList<>();
        getListGioHang();
        recyclerView = findViewById(R.id.recyclevview_dathang);
        xacnhan = findViewById(R.id.XNTT_btn_XacNhanthanhtoan);
        huy  = findViewById(R.id.XNTT_btn_Huythanhtoan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        adapterCart = new DonHangAdapter(gioHangList,this);
        recyclerView.setAdapter(adapterCart);
        tonggia = findViewById(R.id.tongtiendathang);
        hinhthuocthantoan = findViewById(R.id.tv_hinhthucthanhtoan);
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        tonggia.setText(String.format("%,.0f VNĐ", totalPrice));

        // Khởi tạo ImageSwitcher và bắt sự kimgMoMoiện khi người dùng chọn
        imgMoMo = findViewById(R.id.XNTT_momo);
        imgMoMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMoMo();
                isMoMoSelected = true;
            }
        });

        imgTienMat = findViewById(R.id.XNTT_imgtienmat);
        imgTienMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTienMat();
                isMoMoSelected = true;

            }
        });

        imgGPay = findViewById(R.id.XNTT_imgGPay);
        imgGPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTechcombank();
                isMoMoSelected = true;

            }
        });

        resetImages();

        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tennm = tennguoi.getText().toString();
                String sdtnm = sdt.getText().toString();
                String dc = diachi.getText().toString();
                if(tennm.isEmpty() || sdtnm.isEmpty() || dc.isEmpty()){
                    Toast.makeText(ThanhTOanActivity.this,"Bạn chưa nhập đủ thông tin giao hàng",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!isMoMoSelected){
                        Toast.makeText(ThanhTOanActivity.this,"Bạn chưa chọn hình thức thanh toán",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        LayoutInflater inflater1 = getLayoutInflater();
                        view = inflater1.inflate(R.layout.dialog_thongtintruocdathang,null);
                        AlertDialog.Builder b = new AlertDialog.Builder(ThanhTOanActivity.this);
                        AlertDialog dialog1 = b.setView(view).create();
                        dialog1.show();
                        LinearLayout close = dialog1.findViewById(R.id.dialog_thoatxacnhanthanhtoan);
                        Button xacnhan1 = dialog1.findViewById(R.id.dialog_xacnhanthanhtoan);
                        TextView tennguoimua = dialog1.findViewById(R.id.tennguoimua_dialog);
                        TextView sodienthoainguoimua = dialog1.findViewById(R.id.sodtnguoimua_dialog);
                        TextView diachinm = dialog1.findViewById(R.id.diachigiaohangnguoimua_dialog);
                        TextView hinhthucTT = dialog1.findViewById(R.id.hinhthucthanhtoan_dialog);
                        TextView ngaydat = dialog1.findViewById(R.id.ngaydathang_dialog);
                        tennguoimua.setText(tennm);
                        sodienthoainguoimua.setText(sdtnm);
                        diachinm.setText(dc);
                        Date currentDate = new Date();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = formatter.format(currentDate);
                        hinhthucTT.setText(hinhthuocthantoan.getText().toString());
                        ngaydat.setText(formattedDate);
                        TextView tongtien = dialog1.findViewById(R.id.tongtiendathang_dialog);
                        double totalPrice1 = getIntent().getDoubleExtra("totalPrice", 0.0);
                        tongtien.setText(String.format("%,.0f VNĐ", totalPrice1));
                        RecyclerView recyclerView1 = dialog1.findViewById(R.id.recycel_dialog_thanhtoan);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ThanhTOanActivity.this);
                        recyclerView.addItemDecoration(new DividerItemDecoration(ThanhTOanActivity.this,DividerItemDecoration.VERTICAL));
                        recyclerView1.setLayoutManager(layoutManager);

                        recyclerView1.setAdapter(adapterCart);
                        //Su kien tao don hang
                        xacnhan1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogPro.show();
                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
                               ArrayList<SanPhamDonHang> id_SP = new ArrayList<>();
                                Map<String, SanPhamDonHang> productMap = new HashMap<>();
                                ref1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        id_SP.clear();
                                        for (DataSnapshot child : snapshot.getChildren()){
                                            String spid = child.child("id_sp").getValue(String.class);
                                            int quantity = child.child("soluong").getValue(int.class);
                                            SanPhamDonHang sanPhamDonHang = new SanPhamDonHang(spid,quantity);
                                            id_SP.add(sanPhamDonHang);
                                            productMap.put("SP"+spid, sanPhamDonHang);
                                        }
                                        DonHang donHang = new DonHang("",user.getUid(),productMap,dc,tennm,sdtnm,hinhthucTT.getText().toString(),totalPrice1,"Chờ thanh toán",formattedDate);
                                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                        String id = dbRef.child("DonHang").push().getKey();
                                        donHang.setId_donhang("DH"+id);
                                        dbRef.child("DonHang").child("DH"+id).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialogPro.dismiss();
                                                LayoutInflater inflater = getLayoutInflater();
                                                View view = inflater.inflate(R.layout.dialog_thanhtoanthanhcong,null);
                                                AlertDialog.Builder b = new AlertDialog.Builder(ThanhTOanActivity.this);
                                                AlertDialog dialog = b.setView(view).create();
                                                dialog.show();
                                                Button tiep = dialog.findViewById(R.id.btn_toitrangbill);
                                                tiep.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                        dialog1.dismiss();
                                                        Intent intent = new Intent(ThanhTOanActivity.this,TrangBillActivity.class);
                                                        String idsp = "DH"+id;
                                                        intent.putExtra("id_donhang",idsp);
                                                        intent.putExtra("tenNguoiMua",tennm);
                                                        intent.putExtra("sdtNguoimua",sdtnm);
                                                        intent.putExtra("diachiNguoiMua",dc);
                                                        intent.putExtra("hinhthucthanhtoan",hinhthucTT.getText().toString());
                                                        intent.putExtra("ngaydathang",formattedDate);
                                                        intent.putExtra("tonggiadonhang",totalPrice1);
                                                        intent.putParcelableArrayListExtra("danhsachspgiohang",gioHangList);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });
                        xacnhan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void selectMoMo() {
        isMoMoSelected = true;
        isTienMatSelected = false;
        isGPaySelected = false;
        imgMoMo.setAlpha(1f);
        imgTienMat.setAlpha(0.3f);
        imgGPay.setAlpha(0.3f);
        hinhthuocthantoan.setText("MoMo");
    }

    @SuppressLint("SetTextI18n")
    private void selectTienMat() {
        isMoMoSelected = false;
        isTienMatSelected = true;
        isGPaySelected = false;
        imgTienMat.setAlpha(1f);
        imgMoMo.setAlpha(0.3f);
        imgGPay.setAlpha(0.3f);
        hinhthuocthantoan.setText("Trả bằng tiền mặt");
    }

    @SuppressLint("SetTextI18n")
    private void selectTechcombank() {
        isMoMoSelected = false;
        isTienMatSelected = false;
        isGPaySelected= true;
        imgMoMo.setAlpha(0.3f);
        imgTienMat.setAlpha(0.3f);
        imgGPay.setAlpha(1f);
        hinhthuocthantoan.setText("GPay");
    }
    private void resetImages() {
        imgMoMo.setAlpha(1f);
        imgTienMat.setAlpha(1f);
        imgGPay.setAlpha(1f);
        hinhthuocthantoan.setText("");
    }

    private void getListGioHang(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GioHang").child("gio_hang_"+user.getUid());
        gioHangList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gioHangList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String spgh = child.child("id_sp").getValue(String.class);
                    String idnguoidung = child.child("id_nguoidung").getValue(String.class);
                    String tenSP = child.child("tenSP").getValue(String.class);
                    Double giaSP = child.child("giasp").getValue(Double.class);
                    String anhsanpham = child.child("anhSP").getValue(String.class);
                    int soluong = child.child("soluong").getValue(Integer.class);
                    Cart cart = new Cart(spgh, idnguoidung, tenSP,giaSP,anhsanpham,soluong);
                    gioHangList.add(cart);
                }
                adapterCart.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UI(){
        avatar = (CircleImageView) findViewById(R.id.avatar_dathang);
        tennguoi = (EditText) findViewById(R.id.edt_tennguoidathang);
        sdt = (EditText) findViewById(R.id.edt_sodienthoainguoidat);
        diachi = (TextInputEditText) findViewById(R.id.edt_diachigiaohang);
    }

    private void showInfo(){
        if(user == null){
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("name").getValue();
                String photoUrl = (String) snapshot.child("profile").getValue();
                String SoDT = (String) snapshot.child("SoDT").getValue();
                String Diachi = (String) snapshot.child("DiaChi").getValue();

                if (name == null) {
                    tennguoi.setText("");
                } else {
                    tennguoi.setText(name);
                }
                sdt.setText(SoDT);
                diachi.setText(Diachi);
                Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
