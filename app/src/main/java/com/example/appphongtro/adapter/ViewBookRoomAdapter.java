package com.example.appphongtro.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.database.AppointmentDb;
import com.example.appphongtro.session.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewBookRoomAdapter extends RecyclerView.Adapter<ViewBookRoomAdapter.ViewHolder>{

    private Cursor cursor;

    Context context;
    int singleData;
    private AppointmentDb appointmentDb;
    private int owner_id;
    private SessionManagement sessionManagement;


    public ViewBookRoomAdapter(Context context, Cursor cursor, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;

    }

    @NonNull
    @Override
    public ViewBookRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_confirm_book_room, null);
        return new ViewBookRoomAdapter.ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ViewBookRoomAdapter.ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            // Lấy dữ liệu từ Cursor
            byte[] image = cursor.getBlob(cursor.getColumnIndex("imgRoom"));
            String tenantName = cursor.getString(cursor.getColumnIndex("tenantName"));
            String appointmentDateTime = cursor.getString(cursor.getColumnIndex("appointmentDateTime"));
            String nameRoom = cursor.getString(cursor.getColumnIndex("roomName"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
            int appointmentId = cursor.getInt(cursor.getColumnIndex("appointmentId"));
            int hasVisited = cursor.getInt(cursor.getColumnIndex("hasVisited"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.viewImgBookRoomConfirm.setImageBitmap(bitmap); // Thay đổi hình ảnh
            holder.txt_name_tenant.setText(tenantName);
            long timestamp = cursor.getLong(cursor.getColumnIndex("appointmentDateTime"));
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Đặt định dạng mong muốn
            String formattedDate = sdf.format(date);
            holder.tv_time.setText(formattedDate);
            holder.txt_name_book_room.setText(nameRoom);
            holder.txt_phone_tenant.setText(phoneNumber);
            long currentTimeMillis = System.currentTimeMillis();
            appointmentDb = new AppointmentDb(context);
            if (appointmentDb.isAppointmentSuccessful(appointmentId)) {
                holder.btn_ok.setVisibility(View.GONE);
                holder.tv_confirm.setVisibility(View.VISIBLE);
                if (currentTimeMillis - timestamp > 2 * 60 * 60 * 1000) {
                    holder.btn_confirm_cus.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.GONE);
                    holder.btn_confirm_cus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appointmentDb.updateVisitedStatus(appointmentId, 1);
                        }
                    });
                    if(hasVisited == 1) {
                        holder.tv_confirm_cus.setVisibility(View.VISIBLE);
                        holder.btn_confirm_cus.setVisibility(View.GONE);
                    }
                } else {
                    holder.btn_confirm_cus.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }
            } else {
                // Nếu chưa xác nhận hoặc trạng thái khác, hiển thị btn_ok và ẩn tv_confirm
                holder.btn_ok.setVisibility(View.VISIBLE);
                holder.tv_confirm.setVisibility(View.GONE);
                holder.btn_confirm_cus.setVisibility(View.GONE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appointmentDb.updateStatusAppointment(appointmentId, "Thành công");
                        notifyItemChanged(position);
                    }
                });
            }


            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCancelConfirmationDialog(appointmentId, holder.getAdapterPosition());
                }
            });
        }
    }




    private void showCancelConfirmationDialog(int appointmentId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận hủy cuộc hẹn");
        builder.setMessage("Bạn có chắc muốn hủy cuộc hẹn không?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                appointmentDb = new AppointmentDb(context);
                appointmentDb.updateStatusAppointment(appointmentId, "Không thành công");
                sessionManagement = new SessionManagement(context);
                owner_id = sessionManagement.getUserId();
                cursor = appointmentDb.queryAppointmentsForOwner(owner_id); // Reload the cursor
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Nếu người dùng hủy bỏ, đóng dialog
                dialogInterface.dismiss();
            }
        });

        // Hiển thị dialog
        builder.show();
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView viewImgBookRoomConfirm;
        TextView txt_name_book_room, txt_name_tenant, tv_time, txt_phone_tenant, tv_confirm, tv_confirm_cus;

        Button btn_ok, btn_cancel,btn_confirm_cus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImgBookRoomConfirm = itemView.findViewById(R.id.viewImgBookRoomConfirm);
            txt_name_book_room = itemView.findViewById(R.id.txt_name_book_room);
            txt_name_tenant = itemView.findViewById(R.id.txt_name_tenant);
            tv_time = itemView.findViewById(R.id.tv_time);
            txt_phone_tenant = itemView.findViewById(R.id.txt_phone_tenant);
            btn_ok = itemView.findViewById(R.id.btn_ok);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            tv_confirm = itemView.findViewById(R.id.tv_confirm);
            btn_confirm_cus = itemView.findViewById(R.id.btn_confirm_cus);
            tv_confirm_cus = itemView.findViewById(R.id.tv_confirm_cus);
        }
    }
}
