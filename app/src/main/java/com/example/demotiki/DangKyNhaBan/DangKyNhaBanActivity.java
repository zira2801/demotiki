package com.example.demotiki.DangKyNhaBan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demotiki.AnotherClass.NhaBan;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.DangSanPham.DangSPActivity;
import com.example.demotiki.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class DangKyNhaBanActivity extends AppCompatActivity {

    String[] item = {"Điện thoại","Laptop","Đồ chơi"};
    ImageView back,wa_email,wa_hovaten,wa_sodt,wa_quocgia,wa_nganhhang;
    EditText ed_email,ed_hoten,ed_sdt;
    CountryCodePicker countryCodePicker;
    AutoCompleteTextView chonnganhhang;
    CheckBox checkBox;
    Button dangky;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_nha_ban);
        UIinit();
        adapter = new ArrayAdapter<>(this,R.layout.list_danhmucsp,item);
        chonnganhhang.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = ed_email.getText().toString();
                String hoten = ed_hoten.getText().toString();
                String sodt = ed_sdt.getText().toString();
                String nganhhang = chonnganhhang.getText().toString();
                String quocgia = countryCodePicker.getSelectedCountryNameCode();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(hoten) && !TextUtils.isEmpty(sodt) && !TextUtils.isEmpty(nganhhang)){
                    DangKyNhaBan();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(DangKyNhaBanActivity.this,"Bạn chưa nhập đầy đủ thông tin! Không thể đăng ký",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Các sự kiện hiển thị dialog
        wa_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_email_nhaban,null);
                AlertDialog.Builder b = new AlertDialog.Builder(DangKyNhaBanActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                ImageView close = dialog.findViewById(R.id.close_diaglogemail);
                Button ok = dialog.findViewById(R.id.btn_okeemailnhaban);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        wa_hovaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_hoten_nhaban,null);
                AlertDialog.Builder b = new AlertDialog.Builder(DangKyNhaBanActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                ImageView close = dialog.findViewById(R.id.close_diagloghoten);
                Button ok = dialog.findViewById(R.id.btn_okehotennhaban);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        wa_quocgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_quocgia_nhaban,null);
                AlertDialog.Builder b = new AlertDialog.Builder(DangKyNhaBanActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                ImageView close = dialog.findViewById(R.id.close_diaglogquocgia);
                Button ok = dialog.findViewById(R.id.btn_okequocgianhaban);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        wa_sodt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_sodt_nhaban,null);
                AlertDialog.Builder b = new AlertDialog.Builder(DangKyNhaBanActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                ImageView close = dialog.findViewById(R.id.close_diaglogdienthoai);
                Button ok = dialog.findViewById(R.id.btn_okesodtnhaban);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        wa_nganhhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_nganhhang_nhaban,null);
                AlertDialog.Builder b = new AlertDialog.Builder(DangKyNhaBanActivity.this);
                AlertDialog dialog = b.setView(view).create();
                dialog.show();
                ImageView close = dialog.findViewById(R.id.close_diaglognganhhang);
                Button ok = dialog.findViewById(R.id.btn_okenganhhangnhaban);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void DangKyNhaBan() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String email = ed_email.getText().toString();
        String hoten = ed_hoten.getText().toString();
        String sodt = ed_sdt.getText().toString();
        String nganhhang = chonnganhhang.getText().toString();
        String quocgia = countryCodePicker.getSelectedCountryNameCode();

        NhaBan nb = new NhaBan(""+user.getUid(),email,hoten,quocgia,sodt,nganhhang);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String id = "NB"+user.getUid();
        nb.setUserId(id);
        dbRef.child("NhaBan").child(id).setValue(nb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(DangKyNhaBanActivity.this,"Đăng ký Nhà bán thành công",Toast.LENGTH_SHORT).show();
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                usersRef.child("StatusTK").setValue("Nhà bán");
                finish();
            }
        });
    }

    private void UIinit() {
        back = (ImageView) findViewById(R.id.close_dknb);
        ed_email = (EditText) findViewById(R.id.edt_emailnhaban);
        ed_hoten = (EditText) findViewById(R.id.edt_hovatennhaban);
        ed_sdt = (EditText) findViewById(R.id.edt_sodienthoainhaban);
        wa_email = (ImageView) findViewById(R.id.warning_email);
        wa_hovaten = (ImageView) findViewById(R.id.warning_hovaten);
        wa_quocgia = (ImageView) findViewById(R.id.warning_quocgianhaban);
        wa_sodt = (ImageView) findViewById(R.id.warning_sodienthoai);
        wa_nganhhang = (ImageView) findViewById(R.id.warning_chonnganhhang);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.chonquocgia_nhaban);
        chonnganhhang = (AutoCompleteTextView) findViewById(R.id.auto_item_nganhhang);
        dangky = (Button) findViewById(R.id.btn_dangkynhaban);
        checkBox = (CheckBox) findViewById(R.id.check_oke);
    }
}