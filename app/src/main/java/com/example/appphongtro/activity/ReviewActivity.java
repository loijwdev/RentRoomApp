package com.example.appphongtro.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ReviewPhotoAdapter;
import com.example.appphongtro.database.PhotoReviewTbl;
import com.example.appphongtro.database.ReviewTbl;
import com.example.appphongtro.model.PhotoReview;
import com.example.appphongtro.model.Review;
import com.example.appphongtro.session.SessionManagement;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private Button btnUploadImage;
    private RecyclerView recyclerPhotoReview;
    private EditText editTextReview;
    private TextView tv_review;
    private RatingBar ratingBar;
    private Button btnSubmitReview;

    ArrayList<PhotoReview> uri = new ArrayList<>();
    ReviewPhotoAdapter reviewPhotoAdapter;

    public static final int Read_permission = 101;

    private PhotoReviewTbl photoReviewTbl;
    private ReviewTbl reviewTbl;

    private SessionManagement sessionManagement;
    private int tenant_id;
    private int room_id;
    private long reviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        tv_review = findViewById(R.id.tv_review);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        recyclerPhotoReview = findViewById(R.id.recyclerPhotoReview);
        editTextReview = findViewById(R.id.editTextReview);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        photoReviewTbl = new PhotoReviewTbl(this);
        reviewTbl = new ReviewTbl(this);
        sessionManagement = new SessionManagement(this);
        tenant_id = sessionManagement.getUserId();

        reviewPhotoAdapter = new ReviewPhotoAdapter(uri);
        recyclerPhotoReview.setLayoutManager(new GridLayoutManager(ReviewActivity.this, 3));
        recyclerPhotoReview.setAdapter(reviewPhotoAdapter);

        Intent intent = getIntent();
        String nameRoom = intent.getStringExtra("nameRoom");
        room_id = intent.getIntExtra("roomId", -1);
        tv_review.setText("Đánh giá nhận xét "+nameRoom);

        if(ContextCompat.checkSelfPermission(ReviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReviewActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, Read_permission);
        }

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), 1);


                // Get review details
                String reviewText = editTextReview.getText().toString();
                float rating = ratingBar.getRating();

                // Insert review into the database
                Review review = new Review();
                review.setReview_text(reviewText);
                review.setRating(rating);
                review.setRoom_id(room_id);
                review.setTenant_id(tenant_id);
                reviewId = reviewTbl.insertReview(review);
                System.out.println(reviewId);
            }
        });

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Associate images with the review and save to the database
                for (PhotoReview photoReview : uri) {
                    photoReview.setReview_id((int) reviewId);
                    try {
                        photoReviewTbl.insertImage(photoReview);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Clear the review data and update the UI
                uri.clear();
                reviewPhotoAdapter.notifyDataSetChanged();
                editTextReview.setText("");
                ratingBar.setRating(0.0f);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    PhotoReview photoReview = new PhotoReview();
                    photoReview.setImageUri(imageUri);
                    uri.add(photoReview);
                }
                reviewPhotoAdapter.notifyDataSetChanged();
            } else if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                PhotoReview photoReview = new PhotoReview();
                photoReview.setImageUri(imageUri);
                uri.add(photoReview);
                reviewPhotoAdapter.notifyDataSetChanged();
            }
        }
    }
}
