package com.example.appphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appphongtro.R;
import com.example.appphongtro.database.PhotoReviewTbl;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.ViewHolder> {
    private Cursor cursor;
    Context context;
    int singleData;
    private PhotoReviewTbl photoReviewTbl;

    public ListReviewAdapter(Cursor cursor, Context context, int singleData) {
        this.cursor = cursor;
        this.context = context;
        this.singleData = singleData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_review_list, null);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            byte[] image = cursor.getBlob(cursor.getColumnIndex("imgUser"));
            float ratingBar = cursor.getFloat(cursor.getColumnIndex("rating"));
            String name = cursor.getString(cursor.getColumnIndex("nameUser"));
            String reviewText = cursor.getString(cursor.getColumnIndex("review_text"));
            int reviewId = cursor.getInt(cursor.getColumnIndex("reviewId"));

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                holder.reviewImgUser.setImageBitmap(bitmap);
            } else {
                holder.reviewImgUser.setImageResource(R.drawable.profile);
            }

            holder.tv_review_text.setText(reviewText);
            holder.txt_name_review_user.setText(name);
            holder.tv_rating_bar.setText(String.valueOf(ratingBar) + "/5");
            System.out.println(reviewId);

            photoReviewTbl = new PhotoReviewTbl(context);
            try {
                ArrayList<byte[]> imageList = photoReviewTbl.getImagesByReviewId(reviewId);
                ListReviewPhotoAdapter listReviewPhotoAdapter = new ListReviewPhotoAdapter(imageList);
                holder.recyclerViewImages.setLayoutManager(new GridLayoutManager(context, 3));
                holder.recyclerViewImages.setAdapter(listReviewPhotoAdapter);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewImgUser;
        TextView txt_name_review_user, tv_rating_bar, tv_review_text;
        RecyclerView recyclerViewImages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewImgUser = itemView.findViewById(R.id.reviewImgUser);
            txt_name_review_user = itemView.findViewById(R.id.txt_name_review_user);
            tv_rating_bar = itemView.findViewById(R.id.tv_rating_bar);
            tv_review_text = itemView.findViewById(R.id.tv_review_text);
            recyclerViewImages = itemView.findViewById(R.id.recyclerViewImages);
        }
    }
}
