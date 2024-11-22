package com.example.btlg05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.btlg05.DiaDiem.DiemThuFragment;
import com.example.btlg05.DonHang.TaoDonFragment;
import com.example.btlg05.LichSuDonHang.LichSuFragment;
import com.example.btlg05.TaiKhoan.TaiKhoanFragment;
import com.example.btlg05.TrangChu.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("emailng");

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                      selectedFragment = new HomeFragment();
                      break;
                case R.id.nav_create_order:
                    TaoDonFragment taoDonFragment = new TaoDonFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("emailng", email);

                    taoDonFragment.setArguments(bundle);

                    selectedFragment = taoDonFragment;
                     break;
                case R.id.nav_search:
                    selectedFragment = new DiemThuFragment();
                    break;
                case R.id.nav_order_history:

                    LichSuFragment lichSuFragment= new LichSuFragment();
                    Bundle bundlels = new Bundle();
                    bundlels.putString("emailng", email);

                    lichSuFragment.setArguments(bundlels);

                    selectedFragment = lichSuFragment;
                    break;
                case R.id.nav_account:
                    selectedFragment = new TaiKhoanFragment();
                    break;
            }
            // Thay thế Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // Mặc định là Home
        }
    }
}
