package com.example.btlg05.DiaDiem;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.btlg05.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiemThuFragment extends Fragment {
    private FloatingActionButton fabAddthongtindiadiem;
    private RecyclerView recycler;
    private ThongTinDiaDiem_Adapter locationAdapter;
    private List<ThongTinDiaDiem> locationList; // Danh sách gốc
    private List<ThongTinDiaDiem> filteredList; // Danh sách đã lọc
    private DatabaseReference placesRef;
    private ImageButton btnhome;
    private EditText editSearch; // Thanh tìm kiếm

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_diem_thu, container, false);
        recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        locationList = new ArrayList<>();
        filteredList = new ArrayList<>();
        locationAdapter = new ThongTinDiaDiem_Adapter(requireContext(), filteredList);
        recycler.setAdapter(locationAdapter);


        placesRef = FirebaseDatabase.getInstance().getReference("locations");

        // 4. Thiết lập thanh tìm kiếm
        editSearch = view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLocations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        placesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThongTinDiaDiem place = snapshot.getValue(ThongTinDiaDiem.class);
                    if (place != null) {
                        locationList.add(place);
                    }
                }
                // Lọc lại danh sách theo từ khóa tìm kiếm hiện tại
                filterLocations(editSearch.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
        // 6. Xử lý khi nhấn nút thêm địa điểm
        fabAddthongtindiadiem = view.findViewById(R.id.fabAdd);
        fabAddthongtindiadiem.setOnClickListener(v -> {
            // Tạo một instance của AddThongTinDiaDiemFragment
            AddThongTinDiaDiem addThongTinDiaDiemFragment = new AddThongTinDiaDiem();

            // Thực hiện thay thế Fragment hiện tại bằng AddThongTinDiaDiemFragment
            getFragmentManager().beginTransaction()
                    .replace(getId(), addThongTinDiaDiemFragment)  // R.id.fragment_container là nơi chứa các Fragments trong Activity
                    .addToBackStack(null)  // Thêm vào back stack nếu bạn muốn quay lại Fragment trước đó
                    .commit();
        });

        return view;
    }
    private void filterLocations(String keyword) {
        filteredList.clear();

        if (keyword.isEmpty()) {
            // Nếu không có từ khóa, hiển thị toàn bộ danh sách
            filteredList.addAll(locationList);
        } else {
            for (ThongTinDiaDiem place : locationList) {
                // Kiểm tra null trước khi so sánh
                String name = place.getName() != null ? place.getName().toLowerCase() : "";
                String address = place.getAddress() != null ? place.getAddress().toLowerCase() : "";

                if (name.contains(keyword.toLowerCase()) || address.contains(keyword.toLowerCase())) {
                    filteredList.add(place);
                }
            }
        }

        // Cập nhật RecyclerView sau khi lọc
        locationAdapter.notifyDataSetChanged();
    }
}