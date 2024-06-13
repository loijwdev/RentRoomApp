package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListReviewAdapter;
import com.example.appphongtro.database.ReviewTbl;

import java.lang.reflect.Field;

public class ListReviewActivity extends AppCompatActivity {

    private RecyclerView recycler_list_review;
    private ListReviewAdapter listReviewAdapter;
    private TextView tv_no_review;

    private RatingBar ratingBarMean;

    private ReviewTbl reviewTbl;
    private int room_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_review);

        reviewTbl = new ReviewTbl(this);
        Intent intent = getIntent();
        room_id = intent.getIntExtra("room_id", -1);
        findId();
        float rating = reviewTbl.getAverageRatingForRoom(room_id);
        ratingBarMean.setRating(rating);
        lstReview();
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        recycler_list_review.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
    }

    private void findId() {
        recycler_list_review = findViewById(R.id.recycler_list_review);
        ratingBarMean = findViewById(R.id.ratingBarMean);
        tv_no_review = findViewById(R.id.tv_no_review);
    }

    private void lstReview() {
        Cursor cursor = reviewTbl.getReviewForRoom(room_id);
        if (cursor != null && cursor.getCount() > 0) {
            listReviewAdapter = new ListReviewAdapter(cursor, this, R.layout.single_review_list);
            recycler_list_review.setAdapter(listReviewAdapter);
        } else {
            tv_no_review.setVisibility(View.VISIBLE);
            ratingBarMean.setVisibility(View.GONE);
        }
    }


}