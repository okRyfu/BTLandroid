package com.example.btlg05.DiaDiem;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlg05.Helpers.ImageManager;
import com.example.btlg05.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThongTinDiaDiem_Adapter extends RecyclerView.Adapter<ThongTinDiaDiem_Adapter.PlaceViewHolder> {

    private Context context;
    private List<ThongTinDiaDiem> placeList;
    private DatabaseReference favoritesRef;

    public ThongTinDiaDiem_Adapter(Context context, List<ThongTinDiaDiem> placeList) {
        this.context = context;
        this.placeList = placeList;
        this.favoritesRef = FirebaseDatabase.getInstance().getReference("favorites"); // Tham chiếu Firebase
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongtindiadiem, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        ThongTinDiaDiem place = placeList.get(position);

        holder.textViewName.setText(place.getName());
        holder.textViewType.setText(place.getType());
        holder.textViewAddress.setText(place.getAddress());

        // Tải hình ảnh (nếu có)
        if (place.getImageUrl() != null) {
            if (isUrl(place.getImageUrl())) {
                Picasso.get().load(place.getImageUrl()).into(holder.imageViewPlace);
            } else {
                holder.imageViewPlace.setImageBitmap(ImageManager.convertBase64ToBitmap(place.getImageUrl()));
            }
        } else {
            holder.imageViewPlace.setVisibility(View.GONE);
        }

//
//        // Kiểm tra trạng thái "Save/Đã Save" từ Firebase
//        favoritesRef.child(place.getId()).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult().exists()) {
//                holder.saveIcon.setImageResource(R.drawable.save_favourite); // Icon "Đã Save"
//            } else {
//                holder.saveIcon.setImageResource(R.drawable.save); // Icon "Save"
//            }
//        });
//
//        // Xử lý khi nhấn nút Save
//        holder.saveIcon.setOnClickListener(v -> {
//            favoritesRef.child(place.getId()).get().addOnCompleteListener(task -> {
//                if (task.isSuccessful() && task.getResult().exists()) {
//                    // Đã lưu -> Xóa khỏi danh sách yêu thích
//                    favoritesRef.child(place.getId()).removeValue().addOnCompleteListener(removeTask -> {
//                        if (removeTask.isSuccessful()) {
//                            holder.saveIcon.setImageResource(R.drawable.save); // Cập nhật lại icon
//                            Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    // Chưa lưu -> Thêm vào danh sách yêu thích
//                    favoritesRef.child(place.getId()).setValue(place).addOnCompleteListener(saveTask -> {
//                        if (saveTask.isSuccessful()) {
//                            holder.saveIcon.setImageResource(R.drawable.save_favourite); // Cập nhật lại icon
//                            Toast.makeText(context, "Đã lưu địa điểm!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Lưu thất bại!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        });


        // Sự kiện nhấn vào item để mở giao diện chi tiết
        holder.itemView.setOnClickListener(v -> {
            // Lấy dữ liệu từ đối tượng 'place'
            String id = place.getId();
            String name = place.getName();
            String type = place.getType();
            String address = place.getAddress();
            String sdt = place.getSdt();
            String imageUrl = place.getImageUrl();

            // Tạo đối tượng Fragment
            ThongTinDiaDiem_Details detailsFragment = new ThongTinDiaDiem_Details();

            // Tạo Bundle để truyền dữ liệu vào Fragment
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putString("name", name);
            args.putString("type", type);
            args.putString("address", address);
            args.putString("sdt", sdt);
            args.putString("imageUrl", imageUrl);

            // Gắn Bundle vào Fragment
            detailsFragment.setArguments(args);

            // Kiểm tra Activity đã được đính kèm và thực hiện chuyển fragment
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                // Thực hiện thay thế Fragment
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, detailsFragment);  // Đảm bảo fragment_container là ID của container chứa Fragments
                transaction.addToBackStack(null);  // Thêm vào back stack để có thể quay lại Fragment trước đó
                transaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewAddress;
        ImageView imageViewPlace, saveIcon; // Biểu tượng Save

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textten);
            textViewType = itemView.findViewById(R.id.textloaihang);
            textViewAddress = itemView.findViewById(R.id.textdiachi);
            imageViewPlace = itemView.findViewById(R.id.image);
            saveIcon = itemView.findViewById(R.id.btnSave); // Icon Save
        }
    }

    // Kiểm tra xem URL có hợp lệ hay không
    public boolean isUrl(String imageUrl) {
        return imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
    }
}
