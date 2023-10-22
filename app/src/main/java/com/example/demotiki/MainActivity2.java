package com.example.demotiki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.demotiki.Fragment.CaNhanFragment;
import com.example.demotiki.Fragment.ChatFragment;
import com.example.demotiki.Fragment.DanhMucFragment;
import com.example.demotiki.Fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationItemView;
    FragmentManager fragmentManager;
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationItemView.setBackground(null);
        fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_lay, new HomeFragment()).commit();
        bottomNavigationItemView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.khampha) {
                    phanFragment(new HomeFragment());
                    return true;
                }
                if (itemid == R.id.congdong) {
                    phanFragment(new DanhMucFragment());
                    return true;
                }
                if (itemid == R.id.chat) {
                    phanFragment(new ChatFragment());
                    return true;
                }
                if (itemid == R.id.canhan) {
                    phanFragment(new CaNhanFragment());
                    return true;
                }
                return false;
            }
        });
    }
    public void phanFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_lay, fragment);
        transaction.commit();
    }
}