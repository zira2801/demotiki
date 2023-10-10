package com.example.demotiki.ThongTinTaiKhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.demotiki.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Objects;

public class ThongTinNhaBanActivity extends AppCompatActivity {
    String[] item = {"Điện thoại","Laptop","Đồ chơi"};
    ArrayAdapter adapter;
    ImageView trolai;
    EditText edt_hoten, edt_sdt, edt_emainb;
    CountryCodePicker countryCodePicker;
    TextInputEditText diachinb;
    AutoCompleteTextView tennganhhang;
    Button capnhat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nha_ban);
        UIinit();
        trolai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showInfo();

        capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProfile();
            }
        });
    }

    private void UIinit() {
        trolai = (ImageView) findViewById(R.id.back_ttnhaban);
        edt_hoten = (EditText) findViewById(R.id.edit_hovaten );
        edt_sdt = (EditText) findViewById(R.id.edt_SDTND);
        edt_emainb = (EditText) findViewById(R.id.edt_emaiND);
        countryCodePicker =  (CountryCodePicker) findViewById(R.id.quocgianhaban);
        diachinb = (TextInputEditText) findViewById(R.id.diachi_nhaBan);
        tennganhhang = (AutoCompleteTextView) findViewById(R.id.autonganhhang);
        capnhat = (Button) findViewById(R.id.btn_capnhat_edt_nhaban);
    }

    private void showInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("NhaBan").child("NB"+user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String hoten = (String) snapshot.child("hoten").getValue();
                String email = (String) snapshot.child("email").getValue();
                String SoDT = (String) snapshot.child("soDT").getValue();
                String quocgia = (String) snapshot.child("quocGia").getValue();
                String nganhhang = (String) snapshot.child("nganhHang").getValue();
                String Diachi = (String) snapshot.child("DiaChiNhaban").getValue();

                if(hoten == null){
                    return;
                }
                else{

                    edt_hoten.setText(hoten);
                }
                edt_emainb.setText(email);
                edt_sdt.setText(SoDT);
                diachinb.setText(Diachi);
                tennganhhang.setText(nganhhang);
                countryCodePicker.setCountryForNameCode(quocgia);
                adapter = new ArrayAdapter<>(ThongTinNhaBanActivity.this,R.layout.list_danhmucsp,item);
                tennganhhang.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Cập nhật thông tin nhà bán
    private void UpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String hoten = edt_hoten.getText().toString();
        String email = edt_emainb.getText().toString();
        String SoDT = edt_sdt.getText().toString();
        String quocgia = countryCodePicker.getSelectedCountryNameCode();
        String nganhhang = tennganhhang.getText().toString();
        String Diachi = diachinb.getText().toString();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("NhaBan").child("NB"+user.getUid());

        usersRef.child("hoten").setValue(hoten);
        usersRef.child("soDT").setValue(SoDT);
        usersRef.child("email").setValue(email);
        usersRef.child("nganhHang").setValue(nganhhang);
        usersRef.child("DiaChiNhaban").setValue(Diachi);
        usersRef.child("quocGia").setValue(quocgia);
        Toast.makeText(ThongTinNhaBanActivity.this, "Cập nhật Nhà bán thành công", Toast.LENGTH_SHORT).show();
    }
}