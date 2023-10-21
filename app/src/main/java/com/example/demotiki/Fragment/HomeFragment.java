package com.example.demotiki.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demotiki.Adapter.SanPhamAdapter;
import com.example.demotiki.AnotherClass.SanPham;
import com.example.demotiki.R;
import com.example.demotiki.Slider.SliderAdapter;
import com.example.demotiki.Slider.SliderTin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private List<SliderTin> listTinTuc;
    private Handler handler = new Handler();

    RecyclerView recyclerViewSP;
    private List<SanPham> sanPhamList;
    SanPhamAdapter sanPhamAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 =  view.findViewById(R.id.viewPager);
        circleIndicator3 = view.findViewById(R.id.circle_indicator);
        recyclerViewSP = view.findViewById(R.id.recygoiy);
        listTinTuc = getListPhoto();
        SliderAdapter sliderAdapter = new SliderAdapter(HomeFragment.this,listTinTuc);
        viewPager2.setAdapter(sliderAdapter);
        circleIndicator3.setViewPager(viewPager2);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(listTinTuc.size());
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        //Hieu ung chay slide cua viewpager
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                float r = 1-Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        //auto sider
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                handler.removeCallbacks(sliderRunnable);
                handler.postDelayed(sliderRunnable,3000);
            }
        });

        //Hiển thị danh sách sản phẩm lên trang home
        sanPhamList = new ArrayList<>();
        loadProductdata();
        sanPhamAdapter = new SanPhamAdapter(sanPhamList,getContext());
        recyclerViewSP.setAdapter(sanPhamAdapter);
        recyclerViewSP.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return view;
    }

    private List<SliderTin> getListPhoto() {
        List<SliderTin> list = new ArrayList<>();
        list.add(new SliderTin(R.drawable.banner1));
        list.add(new SliderTin(R.drawable.banner2));
        list.add(new SliderTin(R.drawable.banner3));
        list.add(new SliderTin(R.drawable.banner4));
        return list;
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = viewPager2.getCurrentItem();
            if(currentPosition == listTinTuc.size()-1){
                viewPager2.setCurrentItem(0);
            }
            else{
                viewPager2.setCurrentItem(currentPosition+1);
            }
        }
    };

    private void loadProductdata() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("SanPham");
        usersRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot productSnapshot : snapshot.getChildren()){
                    String productId = productSnapshot.getKey();

                    String name = productSnapshot.child("tenSP").getValue(String.class);
                    String userId = productSnapshot.child("idUser").getValue(String.class);

                    String category = productSnapshot.child("danhMucSP").getValue(String.class);

                    String description = productSnapshot.child("moTa").getValue(String.class);

                    String brand = productSnapshot.child("thuongHieu").getValue(String.class);

                    String origin = productSnapshot.child("xuatXu").getValue(String.class);

                    double price = productSnapshot.child("giaban").getValue(Double.class);

                    String warranty = productSnapshot.child("checkBaoHanh").getValue(String.class);

                    String warrantyPeriod = productSnapshot.child("thoiGianBaoHanh").getValue(String.class);

                    String rating = productSnapshot.child("danhgia").getValue(String.class);

                    ArrayList<String> imageList = new ArrayList<>();
                    for(DataSnapshot imageSnapshot : productSnapshot.child("dsAnh").getChildren()) {
                        String imageUrl = imageSnapshot.getValue(String.class);
                        imageList.add(imageUrl);
                    }

                    SanPham sanPham = new SanPham(productId,userId,name,category,brand,price,warrantyPeriod,origin,description,warranty,imageList,rating);

                    sanPhamList.add(sanPham);
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}