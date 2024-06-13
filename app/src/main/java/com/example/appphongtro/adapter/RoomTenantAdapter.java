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
import com.example.appphongtro.session.SessionManagement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RoomTenantAdapter extends RecyclerView.Adapter<RoomTenantAdapter.ViewHolder> {
    private Cursor cursor;

    Context context;
    int singleData;

    private SessionManagement sessionManagement;
    private ContractTbl contractTbl;


    public RoomTenantAdapter(Cursor cursor, Context context, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public RoomTenantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_room_owner, null);
        return new RoomTenantAdapter.ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull RoomTenantAdapter.ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            byte[] image = cursor.getBlob(cursor.getColumnIndex("imgRoom"));
            String nameRoom = cursor.getString(cursor.getColumnIndex("roomName"));
            double price = cursor.getDouble(cursor.getColumnIndex("roomPrice"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            String address = cursor.getString(cursor.getColumnIndex("roomAdress"));
            long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Đặt định dạng mong muốn
            String formattedDate = sdf.format(date);

            int room_id = cursor.getInt(cursor.getColumnIndex("roomId"));

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imgRoom.setImageBitmap(bitmap);
            holder.txt_time.setText("Ngày đặt cọc: "+formattedDate);
            holder.txt_name.setText(nameRoom);
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedPrice = decimalFormat.format(amount);
            holder.txt_price.setText("Số tiền đặt cọc: " + formattedPrice);
            holder.txt_address.setText(address);
            if (status.equals("Đã thanh toán")) {
                holder.tv_confirm.setText("Đã đặt cọc");
                holder.tv_confirm.setVisibility(View.VISIBLE);
            }
            contractTbl = new ContractTbl(context);
            sessionManagement = new SessionManagement(context);
            int tenant_id = sessionManagement.getUserId();
            String statusContract = contractTbl.getContractStatus(tenant_id, room_id);
            System.out.println(statusContract);
            if (statusContract != null && statusContract.equals("Đã tạo")) {
                holder.tv_confirm.setText("Đã ký hợp đồng");
                holder.tv_confirm.setVisibility(View.VISIBLE);
            }

            holder.imgRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailContractActivity.class);
                    intent.putExtra("room_id", room_id);
                    intent.putExtra("tenant_id", tenant_id);
                    context.startActivity(intent);
                }
            });
            String statusPay = contractTbl.isRentPaid(tenant_id, room_id);
            System.out.println(statusPay);
            Date contractStartDate = contractTbl.getContractStartDate(tenant_id);
            // Kiểm tra nếu ngày bắt đầu hợp đồng không null
            if (contractStartDate != null) {
                Calendar currentDate = Calendar.getInstance();
                currentDate.setTime(new Date());

                // Tính toán ngày hết hạn (1 tháng sau ngày bắt đầu)
                Calendar expirationDate = Calendar.getInstance();
                expirationDate.setTime(contractStartDate);
                expirationDate.add(Calendar.MONTH, 1);
                if (currentDate.after(expirationDate) && statusPay != null && !statusPay.equals("Đã thanh toán")) {
                    holder.tv_confirm.setText("Tới hạn thanh toán");
                    holder.dayPayment.setVisibility(View.VISIBLE);
                    holder.dayPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, DetailContractActivity.class);
                            intent.putExtra("tenant_id", tenant_id);
                            intent.putExtra("room_id", room_id);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.dayPayment.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom;
        TextView tv_confirm, txt_name, txt_price, txt_address, txt_time;
        Button dayPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRoom = itemView.findViewById(R.id.imgRoom);
            tv_confirm = itemView.findViewById(R.id.tv_confirm);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_address = itemView.findViewById(R.id.txt_address);
            txt_time = itemView.findViewById(R.id.txt_time);
            dayPayment = itemView.findViewById(R.id.dayPayment);
        }
    }
}
