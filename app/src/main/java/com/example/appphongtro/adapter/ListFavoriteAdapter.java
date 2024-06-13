package com.example.appphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.DetailRoomActivity;

import java.text.DecimalFormat;

public class ListFavoriteAdapter extends RecyclerView.Adapter<ListFavoriteAdapter.ViewHolder>{
    private Cursor cursor;
    Context context;
    int singleData;

    public ListFavoriteAdapter(Cursor cursor, Context context, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public ListFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room, null);
        return new ListFavoriteAdapter.ViewHolder(view);
    }
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ListFavoriteAdapter.ViewHolder holder, int position) {
        if (cursor != null &&cursor.moveToPosition(position)) {
            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            int id = cursor.getInt(cursor.getColumnIndex("room_id"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(price);

            holder.viewImgRoom.setImageBitmap(bitmap);
            holder.txt_name.setText(name);
            holder.txt_address.setText(address);
            holder.txt_price.setText(formattedPrice+ " VND/1 tháng");
            holder.viewImgRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailRoomActivity.class);
                    intent.putExtra("roomId", id);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView viewImgRoom;
        TextView txt_name, txt_price, txt_address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImgRoom = itemView.findViewById(R.id.viewImgRoom);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
        }
    }
}
