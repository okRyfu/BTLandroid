package com.example.btlg05.TaiKhoan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.btlg05.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaiKhoanFragment extends Fragment {
    TextView name, email, accountInfo, oderCount, materialWeight, logOut, cancelAccount;
    private FirebaseDatabase database;
    private DatabaseReference  userRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_tai_khoan, container, false);
        name = view.findViewById(R.id.tvName);
        email = view.findViewById(R.id.tvEmail);
        accountInfo = view.findViewById(R.id.tvAccountInfo);
        oderCount = view.findViewById(R.id.tvOrderCount);
        materialWeight = view.findViewById(R.id.tvMaterialWeight);
        logOut = view.findViewById(R.id.tvDangXuat);
        cancelAccount = view.findViewById(R.id.tvHuyTK);
        
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        //set email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        if (user != null) {
            String emailAccount = user.getEmail();
            if (email != null) {
                email.setText(emailAccount);
            } else {
                email.setText("Xin chào");
            }
        } else {
            email.setText("No user signed in");
        }
        //set name
        userRef.child(userID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}