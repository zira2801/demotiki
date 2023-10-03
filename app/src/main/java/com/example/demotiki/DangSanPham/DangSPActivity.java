package com.example.demotiki.DangSanPham;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demotiki.R;



import java.util.ArrayList;

public class DangSPActivity extends AppCompatActivity implements RecycleAdapter.CountImage{

    String[] item = {"Điện thoại","Laptop","Trang điểm"};

    RecyclerView recyclerView;
    RelativeLayout chonanh;

    TextView totalanh;
    RecycleAdapter imageadapter;
    ArrayList<Uri> uris = new ArrayList<>();

    private static final int Read_Permission = 101;
    private static final int PICK_IMAGE = 1;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_spactivity);
        UIinit();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_item);
        adapter = new ArrayAdapter<>(this,R.layout.list_danhmucsp,item);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(DangSPActivity.this,"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        if(ContextCompat.checkSelfPermission(DangSPActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DangSPActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }
        chonanh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                //intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });
        imageadapter = new RecycleAdapter(uris,DangSPActivity.this,this::clicked);
        recyclerView.setLayoutManager(new GridLayoutManager(DangSPActivity.this,4));
        recyclerView.setAdapter(imageadapter);

    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && null != data){
            if(data.getClipData() != null){
                int x = data.getClipData().getItemCount();
                for (int i =0;i<x;i++){
                    if(uris.size() < 3){
                        Uri image = data.getClipData().getItemAt(i).getUri();
                        uris.add(image);
                    }
                    else{
                        Toast.makeText(this,"Bạn chỉ lấy được giới hạn 3 ảnh",Toast.LENGTH_SHORT).show();
                    }
                }
                imageadapter.notifyDataSetChanged();
                if(uris.size() == 0){
                    totalanh.setVisibility(View.GONE);
                }
                else {
                    totalanh.setVisibility(View.VISIBLE);
                    totalanh.setText("Số lượng ảnh: " + uris.size());
                }
            }
            else{
                //Cai nay là danh cho chọn 1 ảnh
                Uri imageuri = data.getData();
                uris.add(imageuri);
            }
            imageadapter.notifyDataSetChanged();
            totalanh.setText("Số lượng ảnh: " + uris.size());
        }
        else{
            Toast.makeText(this,"Bạn chưa chọn ảnh !",Toast.LENGTH_SHORT).show();
        }
    }

    private void UIinit(){
        totalanh = findViewById(R.id.totalimage);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_chonanh);
        chonanh = findViewById(R.id.chonhinhanh);
    }

    @Override
    public void clicked(int getSize) {
        if(uris.size() == 0){
            totalanh.setVisibility(View.GONE);
        }
        else {
            totalanh.setVisibility(View.VISIBLE);
            totalanh.setText("Số lượng ảnh: " + uris.size());
        }
    }

}