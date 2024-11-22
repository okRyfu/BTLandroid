package com.example.btlg05.DiaDiem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.btlg05.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.example.btlg05.Helpers.ImageManager;
import java.io.IOException;

public class AddThongTinDiaDiem extends Fragment {

    private EditText editName, editType, editAddress, editSdt;
    private Button buttonAddImage, buttonAddPlace;
    private ImageView imageViewPreview;
    private Uri imageUri;
    private DatabaseReference placesRef;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.activity_add_thongtindiadiem, container, false);
        editName = view.findViewById(R.id.editName);
        editType = view.findViewById(R.id.editloaihang);
        editAddress = view.findViewById(R.id.editDiaChi);
        editSdt = view.findViewById(R.id.editSDT);
        buttonAddImage = view.findViewById(R.id.buttonAddImage);
        imageViewPreview = view.findViewById(R.id.imageViewPreview);
        buttonAddPlace = view.findViewById(R.id.buttonAdd);

        placesRef = FirebaseDatabase.getInstance().getReference().child("locations");
        storageRef = FirebaseStorage.getInstance().getReference("locations_images");

        buttonAddImage.setOnClickListener(v -> openGallery());
        buttonAddPlace.setOnClickListener(v -> addPlace());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewPreview.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUri).into(imageViewPreview);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }
    private void addPlace() {
        String name = editName.getText().toString().trim();
        String type = editType.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String sdt = editSdt.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(address) || TextUtils.isEmpty(sdt)) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String placeId = placesRef.push().getKey();

        if (imageUri != null) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imageUrl = ImageManager.convertBitmapToBase64(bitmap);

            ThongTinDiaDiem place = new ThongTinDiaDiem(name, type, address, imageUrl, sdt);
            placesRef.child(placeId).setValue(place)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm địa điểm thành công", Toast.LENGTH_SHORT).show();
                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        } else {
                            Toast.makeText(getContext(), "Thêm địa điểm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ThongTinDiaDiem place = new ThongTinDiaDiem(name, type, address, null, sdt);
            placesRef.child(placeId).setValue(place)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm địa điểm thành công", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Thêm địa điểm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}