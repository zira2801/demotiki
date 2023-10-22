package com.example.demotiki.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demotiki.Adapter.ViewPagerDMSP;
import com.example.demotiki.AnotherClass.DanhMucSP;
import com.example.demotiki.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhMucFragment extends Fragment {
    public DanhMucFragment() {
        // Required empty public constructor
    }
    private static final String KEY_CATEGORY_ID = "category_id";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TabLayout mTablayout;
    private ViewPager2 mViewPage;
    ArrayList<DanhMucSP> categories;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //Tra ve user

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;
    private ViewPagerDMSP adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view  = inflater.inflate(R.layout.fragment_danh_muc_s_p, container, false);
        mViewPage = view.findViewById(R.id.view_pager_dmsp);
        mTablayout = view.findViewById(R.id.tab_layout_dmsp);
        ArrayList<DanhMucSP> danhMucSPS = getlist();
        adapter = new ViewPagerDMSP(getActivity().getSupportFragmentManager(), getLifecycle(),danhMucSPS);
        mViewPage.setAdapter(adapter);
        new TabLayoutMediator(mTablayout, mViewPage,
                (tab, position) -> tab.setText(danhMucSPS.get(position).getTendanhmuc())
        ).attach();

  /*      // Thêm category mới vào firebase

// Cập nhật lại danh sách category
        categories.clear();
        categories.addAll(// lấy lại từ firebase);

// Thông báo dataset thay đổi cho adapter
                adapter.notifyDataSetChanged();*/
        return view;
    }

    private ArrayList<DanhMucSP> getlist() {
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý lỗi đọc dữ liệu nếu cần
            }
        });

        return categories;
    }

    public interface DataCallback {
        void onCategoriesLoaded(List<DanhMucSP> categories);
    }
}