package com.example.demotiki.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demotiki.AnotherClass.DanhMucSP;

import java.util.ArrayList;

public class AdapterDanhMuc extends BaseAdapter {

    ArrayList<DanhMucSP> danhMucs;
Context context;
   public AdapterDanhMuc(ArrayList<DanhMucSP> danhMucs,Context context) {
        this.danhMucs = danhMucs;
        this.context = context;
    }
    @Override
    public int getCount() {
        return danhMucs.size();
    }

    @Override
    public Object getItem(int i) {
        return danhMucs.get(i).getTendanhmuc();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(getItem(position).toString());
        return view;
    }

}
