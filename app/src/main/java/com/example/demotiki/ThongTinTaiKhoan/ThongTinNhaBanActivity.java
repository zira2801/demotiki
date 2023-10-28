package com.example.demotiki.ThongTinTaiKhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demotiki.AnotherClass.DanhMucSP;
import com.example.demotiki.DangSanPham.DangSPActivity;
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
import java.util.ArrayList;
import java.util.Objects;

public class ThongTinNhaBanActivity extends AppCompatActivity {
    String[] item = {"Điện thoại","Laptop","Đồ chơi"};
    ArrayAdapter<DanhMucSP> adapter;
    ImageView trolai;
    EditText edt_hoten, edt_sdt, edt_emainb;
    CountryCodePicker countryCodePicker;
    TextInputEditText diachinb;
    AutoCompleteTextView tennganhhang;
    Button capnhat;
    ArrayList<DanhMucSP> categories;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nha_ban);
        UIinit();
        loadData();
        tennganhhang.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DanhMucSP danhMucSP = (DanhMucSP) adapterView.getItemAtPosition(i);
                String tenDanhMuc = danhMucSP.getTendanhmuc();
                tennganhhang.setText(tenDanhMuc);
                loadData();
            }
        });
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
                loadData();
                countryCodePicker.setCountryForNameCode(quocgia);
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

    private void loadData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DanhMucSP");
        categories = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categories.clear();
                for (DataSnapshot child : snapshot.getChildren()) {

                    String categoryId = child.getKey();
                    DataSnapshot tenDanhMucNode = child.child("Tendanhmuc");
                    String tenDanhMuc = tenDanhMucNode.getValue(String.class);
                    DanhMucSP category = new DanhMucSP(categoryId, tenDanhMuc);
                    categories.add(category);
                }// Gửi dữ liệu qua callback
                // Khởi tạo adapter sau khi có data
                adapter = new ArrayAdapter<DanhMucSP>(ThongTinNhaBanActivity.this, android.R.layout.simple_list_item_1, categories){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        DanhMucSP danhMuc = getItem(position);

                        textView.setText(danhMuc.getTendanhmuc());

                        return view;
                    }};

                // Gán adapter cho AutoCompleteTextView
                tennganhhang.setAdapter(adapter);
                tennganhhang.setDropDownHeight(500);
                // Thông báo thay đổi dữ liệu cho adapter
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
}