package com.example.demotiki.DangSanPham;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.demotiki.R;

public class DangSPActivity extends AppCompatActivity {

    String[] item = {"Điện thoại","Laptop","Trang điểm"};

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_spactivity);

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
    }
}