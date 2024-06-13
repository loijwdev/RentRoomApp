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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.ContractActivity;
import com.example.appphongtro.activity.DetailContractActivity;
import com.example.appphongtro.database.ContractTbl;
import com.example.appphongtro.model.Room;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomDepositedAdapter extends RecyclerView.Adapter<RoomDepositedAdapter.ViewHolder> {
    private Cursor cursor;

    Context context;
    int singleData;

    private ContractTbl contractTbl;

    public RoomDepositedAdapter(Cursor cursor, Context context, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public RoomDepositedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room_owner, null);
        return new RoomDepositedAdapter.ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull RoomDepositedAdapter.ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            byte[] image = cursor.getBlob(cursor.getColumnIndex("imgRoom"));
            String nameRoom = cursor.getString(cursor.getColumnIndex("roomName"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Đặt định dạng mong muốn
            String formattedDate = sdf.format(date);
            String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imgRoom.setImageBitmap(bitmap);
            int room_id = cursor.getInt(cursor.getColumnIndex("IdRoom"));
            int tenant_id = cursor.getInt(cursor.getColumnIndex("IdTenant"));
            holder.txt_name.setText(nameRoom);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(amount);
            holder.txt_price.setText("Số tiền đặt cọc: " + formattedPrice + " VND");
            holder.txt_address.setText("Người thuê: "+ name);
            holder.txt_sdt.setText("SĐT: " +phoneNumber );
            holder.txt_time.setText("Ngày giờ đặt cọc: "+formattedDate);
            holder.createContract.setVisibility(View.VISIBLE);
            holder.txt_sdt.setVisibility(View.VISIBLE);
            contractTbl = new ContractTbl(context);
            if(contractTbl.isContractCreated(tenant_id, room_id)) {
                holder.createContract.setText("Đã ký hợp đồng");
                holder.createContract.setEnabled(false);
            }
            holder.createContract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ContractActivity.class);
                    intent.putExtra("tenant_id", tenant_id);
                    intent.putExtra("room_id", room_id);
                    context.startActivity(intent);
                }
            });
            holder.imgRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailContractActivity.class);
                    intent.putExtra("tenant_id", tenant_id);
                    intent.putExtra("room_id", room_id);
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

        ImageView imgRoom;
        TextView tv_confirm, txt_name, txt_price, txt_address, txt_time, txt_sdt;
        Button createContract;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRoom = itemView.findViewById(R.id.imgRoom);
            tv_confirm = itemView.findViewById(R.id.tv_confirm);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_sdt = itemView.findViewById(R.id.txt_sdt);
            createContract = itemView.findViewById(R.id.createContract);
        }
    }
}
