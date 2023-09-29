package com.example.demotiki.ThongTinTaiKhoan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demotiki.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTinTaiKhoanActivity extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 100;
    private final int GALLERY_REQ_CODE2 = 90;
    private final int CAMERA_REQ_CODE = 100;
    Button luu, thoat, chonavatar, chon_anhbia;
    CircleImageView avatar;
    ImageView anh_bia;
    EditText ten_tk, sodt, ngaysinh;
    RadioGroup radioGroupGT;
    TextInputEditText diachi;
    ImageView ImgNS;

    Uri uri;
    Uri uriAnhBia;
    Bitmap photo;
    DatePickerDialog.OnDateSetListener setListener;

    CountryCodePicker countryCodePicker;
    String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    final private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                } else {
                    uriAnhBia = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriAnhBia);
                        anh_bia.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);
        initUI();
        initListenerAvatar();
        initListenerAnhBia();
        setUserInformation();

        thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        ImgNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinTaiKhoanActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                ngaysinh.setText(date);
            }
        };

        //SỰ kiện update profile
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UpdateProfile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Xu kien chon quoc tich
        
    }


    private void initUI(){
        luu = (Button) findViewById(R.id.btn_luu_edit);
        thoat = (Button) findViewById(R.id.btn_thoat_edt);
        avatar = (CircleImageView) findViewById(R.id.edt_avatar);
        anh_bia = (ImageView) findViewById(R.id.imageView);
        chonavatar = (Button) findViewById(R.id.btn_editavatar);
        chon_anhbia = (Button) findViewById(R.id.btn_editanhbia);
        ten_tk = (EditText) findViewById(R.id.edit_tenND);
        sodt = (EditText) findViewById(R.id.edit_SDTND);
        ngaysinh = (EditText) findViewById(R.id.edit_ngaysinh);
        radioGroupGT = (RadioGroup) findViewById(R.id.rad_group);
        diachi = (TextInputEditText) findViewById(R.id.edt_diachi);
        ImgNS = (ImageView) findViewById(R.id.img_calencer);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.chonquoctich);
    }

    private void UpdateProfile() throws IOException {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String fullName = ten_tk.getText().toString();
        String SDT = sodt.getText().toString();
        String NS = ngaysinh.getText().toString();
        //Lấy tên của đất nước
        String quoctich = countryCodePicker.getSelectedCountryNameCode();
        int selectGT = radioGroupGT.getCheckedRadioButtonId();
        String gioiTinh = "";
        if (selectGT != -1) {
            RadioButton selectedRadioButton = findViewById(selectGT);
            // Lấy thông tin từ RadioButton đã chọn
            gioiTinh = selectedRadioButton.getText().toString();
        }
        String diaChi = diachi.getText().toString();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        usersRef.child("name").setValue(fullName);
        usersRef.child("SoDT").setValue(SDT);
        usersRef.child("GioiTinh").setValue(gioiTinh);
        usersRef.child("NgaySinh").setValue(NS);
        usersRef.child("DiaChi").setValue(diaChi);
        usersRef.child("QuocTich").setValue(quoctich);

        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + user.getUid());
            StorageReference filRef = storageReference.child("avatar_"+user.getUid());

            filRef.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return filRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadURL = (Uri) task.getResult();
                    String anhavatar = downloadURL.toString();
                    usersRef.child("profile").setValue(anhavatar);
                }
            });
        }

        if (uriAnhBia != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + user.getUid());
            StorageReference filRef = storageReference.child("anhbia_"+user.getUid());

            filRef.putFile(uriAnhBia).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return filRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadURL = (Uri) task.getResult();
                    String anhBiaUri = downloadURL.toString();
                    usersRef.child("AnhBia").setValue(anhBiaUri);
                    Toast.makeText(ThongTinTaiKhoanActivity.this, "Upload ảnh bìa thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(ThongTinTaiKhoanActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    //Su kien lay anh
    private void initListenerAvatar() {
        chonavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ThongTinTaiKhoanActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Nếu chưa có quyền thì request
                    ActivityCompat.requestPermissions(ThongTinTaiKhoanActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_REQ_CODE);

                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    Intent chooserIntent = Intent.createChooser(intent, "Select image");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // Tạo hộp thoại "Chooser" để chọn ảnh từ thư viện hoặc camera
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
                    startActivityForResult(chooserIntent, GALLERY_REQ_CODE);
                }

            }
        });
    }

    //Su kien lay anh bia
    private void initListenerAnhBia() {
        chon_anhbia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                activityResultLauncher.launch(Intent.createChooser(intent1,"Select Photo"));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền CAMERA đã được cấp, gọi Intent chụp ảnh từ camera
                startCameraIntent();
            } else {
                // Quyền CAMERA bị từ chối, bạn có thể hiển thị thông báo cho người dùng
            }
        }
    }

    // Phương thức gọi Intent chụp ảnh từ camera
    private void startCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if(requestCode == GALLERY_REQ_CODE){
                if (data.getData() != null) {
                    // Xử lý chọn ảnh từ thư viện
                    uri = data.getData();
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        avatar.setImageBitmap(photo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Xử lý chụp ảnh từ Camera
                    photo = (Bitmap) data.getExtras().get("data");
                    uri = getImageUri(ThongTinTaiKhoanActivity.this,photo);
                    avatar.setImageBitmap(photo);
                }
            }

        if(requestCode == GALLERY_REQ_CODE2){
            if (data.getData() != null){
                uriAnhBia = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriAnhBia);
                    anh_bia.setImageURI(uriAnhBia);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public Uri getImageUri(ThongTinTaiKhoanActivity inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
//End su kien lay anh tu gallery

    private void setUserInformation(){
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
                    String photoUrl = (String) snapshot.child("profile").getValue();
                    String SoDT = (String) snapshot.child("SoDT").getValue();
                    String Gioitinh = (String) snapshot.child("GioiTinh").getValue();
                    String Ngaysinh = (String) snapshot.child("NgaySinh").getValue();
                    String Diachi = (String) snapshot.child("DiaChi").getValue();
                    String Anhbia = (String) snapshot.child("AnhBia").getValue();
                    String quocTich = (String) snapshot.child("QuocTich").getValue();
                    countryCodePicker.setCountryForNameCode(quocTich);
                    if (name == null) {
                        ten_tk.setText(null);
                    } else {
                        ten_tk.setText(name);
                    }
                    sodt.setText(SoDT);
                    radioGroupGT.clearCheck();
                    if(Gioitinh == null){
                        // Nếu dữ liệu là null, không chọn bất kỳ ô nào trong RadioGroup
                        radioGroupGT.clearCheck();
                    }
                    else {
                        // Nếu bạn có dữ liệu, chọn một lựa chọn trong RadioGroup tương ứng
                        if (Gioitinh.equals("Nam")) {
                            radioGroupGT.check(R.id.rad_nam);
                        } else if (Gioitinh.equals("Nữ")) {
                            radioGroupGT.check(R.id.rad_nu);
                        } else if (Gioitinh.equals("Khác")) {
                            radioGroupGT.check(R.id.rad_khac);
                        }
                    }
                    ngaysinh.setText(Ngaysinh);
                    diachi.setText(Diachi);
                    Glide.with(getApplicationContext()).load(Anhbia).error(R.drawable.baseline_add_photo_alternate_24).into(anh_bia);
                    Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.baseline_account_circle_24).into(avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });


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
        Glide.with(getApplicationContext()).clear(avatar);
        Glide.with(getApplicationContext()).clear(anh_bia);
    }
}