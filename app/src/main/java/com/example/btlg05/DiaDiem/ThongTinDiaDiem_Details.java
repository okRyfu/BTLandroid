package com.example.btlg05.DiaDiem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ThongTinDiaDiem_Details extends Fragment {

    private TextView textViewName, textViewType, textViewAddress, textViewPhone;
    private ImageView imageViewPlace;
    private ImageButton imageBack;

    private Button buttonEdit, buttonDelete;
    private String id;
    private String name;
    private String type;
    private String address;
    private String imageUrl;
    private String sdt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_thongtindiadiem,container ,false);
        // Khởi tạo các view
        textViewName = view.findViewById(R.id.text_Ten);
        textViewType = view.findViewById(R.id.text_loaihang);
        textViewAddress = view.findViewById(R.id.text_dia_chi);
        textViewPhone = view.findViewById(R.id.text_phone_number);
        imageViewPlace = view.findViewById(R.id.img_detail_info);
        imageBack = view.findViewById(R.id.imagebuttonBack);
        buttonDelete=view.findViewById(R.id.btn_delete);
        buttonEdit = view.findViewById(R.id.btn_edit);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String id = arguments.getString("id");
            String name = arguments.getString("name");
            String type = arguments.getString("type");
            String address = arguments.getString("address");
            String phone = arguments.getString("sdt");
            String imageUrl = arguments.getString("imageUrl");

            // Hiển thị dữ liệu lên các view
            if (name != null) {
                textViewName.setText(name);
            }
            if (type != null) {
                textViewType.setText(type);
            }
            if (address != null) {
                textViewAddress.setText(address);
            }
            if (phone != null) {
                textViewPhone.setText(phone);
            }

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Bitmap bitmap = ImageManager.convertBase64ToBitmap(imageUrl);
                imageViewPlace.setImageBitmap(bitmap);
            } else {
                imageViewPlace.setVisibility(View.GONE);
            }
            setupBackButton();
//            Edit(); chưa có tính năng tạm ẩn
        }

        return view;
    }
    private void setupBackButton() {


        imageBack.setOnClickListener(v -> {

            getParentFragmentManager().popBackStack();
        });

    }



    private void Edit()
    {
//        buttonEdit.setOnClickListener(v -> {
//            Intent editIntent = new Intent(ThongTinDiaDiem_Details.this, EditThongTinDiaDiem.class);
//            editIntent.putExtra("id", id);
//            editIntent.putExtra("name", name);
//            editIntent.putExtra("type", type);
//            editIntent.putExtra("address", address);
//            editIntent.putExtra("sdt", sdt);
//            editIntent.putExtra("imageUrl", imageUrl);
//            startActivity(editIntent);
//            startActivityForResult(editIntent, 100); // Chờ kết quả từ Edit Activity
//        });
    }
}