package com.example.appphongtro.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.appphongtro.activity.BookRoomActivity;
import com.example.appphongtro.activity.ReviewActivity;
import com.example.appphongtro.database.AppointmentDb;
import com.example.appphongtro.database.ReviewTbl;
import com.example.appphongtro.session.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListAppointmentScheduleAdapter extends RecyclerView.Adapter<ListAppointmentScheduleAdapter.ViewHolder>{
    private Cursor cursor;

    Context context;
    int singleData;
    private int owner_id;
    private SessionManagement sessionManagement;

    private AppointmentDb appointmentDb;
    private ReviewTbl reviewTbl;
    public ListAppointmentScheduleAdapter(Cursor cursor, Context context, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public ListAppointmentScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_book_room, null);
        return new ListAppointmentScheduleAdapter.ViewHolder(view);
    }
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ListAppointmentScheduleAdapter.ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            // Lấy dữ liệu từ Cursor
            byte[] image = cursor.getBlob(cursor.getColumnIndex("imgRoom"));
            String nameRoom = cursor.getString(cursor.getColumnIndex("roomName"));
            String status = cursor.getString(cursor.getColumnIndex("statusBooking"));
            int appointmentId = cursor.getInt(cursor.getColumnIndex("appointmentId"));
            int roomId = cursor.getInt(cursor.getColumnIndex("roomId"));
            int hasVisited = cursor.getInt(cursor.getColumnIndex("hasVisited"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            holder.viewImgBooking.setImageBitmap(bitmap); // Thay đổi hình ảnh
            long timestamp = cursor.getLong(cursor.getColumnIndex("appointmentDateTime"));
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Đặt định dạng mong muốn
            String formattedDate = sdf.format(date);
            holder.tv_time_booking.setText(formattedDate);
            holder.txt_name_booking.setText(nameRoom);
            holder.txt_status_booking.setText(status);

            if(hasVisited == 1) {
                holder.btn_change_appointment.setVisibility(View.GONE);
                holder.btn_cancel_booking.setVisibility(View.GONE);

                holder.tv_to_review.setVisibility(View.VISIBLE);
                holder.tv_to_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ReviewActivity.class);
                        intent.putExtra("nameRoom", nameRoom);
                        intent.putExtra("roomId", roomId);
                        context.startActivity(intent);
                    }
                });
                reviewTbl = new ReviewTbl(context);
                sessionManagement = new SessionManagement(context);
                if (reviewTbl.hasUserReviewedRoom(roomId, sessionManagement.getUserId())) {
                    holder.tv_to_review.setText("Bạn đã đánh giá");
                    holder.tv_to_review.setEnabled(false);
                }
            }
            holder.btn_change_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editIntent = new Intent(context, BookRoomActivity.class);
                    editIntent.putExtra("roomIdDetail", roomId);
                    context.startActivity(editIntent);
                }
            });

            holder.btn_cancel_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lấy ID của appointment cần xóa
                    System.out.println(appointmentId);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận xóa");
                    builder.setMessage("Bạn có chắc chắn muốn hủy lịch đặt phòng này?");
                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Xóa appointment khi người dùng xác nhận
                            appointmentDb = new AppointmentDb(context);
                            appointmentDb.deleteAppointment(appointmentId, roomId);
                            sessionManagement = new SessionManagement(context);
                            owner_id = sessionManagement.getUserId();
                            cursor = appointmentDb.queryAppointments(owner_id); // Reload the cursor
                            notifyItemRangeRemoved(0, cursor.getCount()+1);
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Đóng AlertDialog khi người dùng chọn Hủy
                            dialogInterface.dismiss();
                        }
                    });

                    // Hiển thị AlertDialog
                    builder.show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView viewImgBooking;
        TextView txt_name_booking, txt_status_booking, tv_time_booking, tv_to_review;
        Button btn_change_appointment, btn_cancel_booking;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImgBooking = itemView.findViewById(R.id.viewImgBooking);
            txt_name_booking = itemView.findViewById(R.id.txt_name_booking);
            txt_status_booking = itemView.findViewById(R.id.txt_status_booking);
            tv_time_booking = itemView.findViewById(R.id.tv_time_booking);
            btn_change_appointment = itemView.findViewById(R.id.btn_change_appointment);
            btn_cancel_booking = itemView.findViewById(R.id.btn_cancel_booking);
            tv_to_review = itemView.findViewById(R.id.tv_to_review);
        }
    }
}
