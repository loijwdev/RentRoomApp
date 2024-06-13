package com.example.appphongtro.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;
import com.example.appphongtro.session.SessionManagement;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

public class ManageAccountActivity extends AppCompatActivity {

    private EditText tv_account_fullname, tv_account_phone, tv_account_address, tv_account_email, tv_account_pass;

    private ImageView imgAcc;

    private Button btn_apply_change;
    private int user_id;

    private SessionManagement sessionManagement;

    private String[] cameraPermission;
    private String[] storagePermission;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    private UserTbl userTbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        userTbl = new UserTbl(this);
        sessionManagement = new SessionManagement(this);
        Intent intent = getIntent();
        if (intent.hasExtra("user_id")) {
            user_id = intent.getIntExtra("user_id", -1);
        } else {
            // Nếu Intent không có user_id, lấy từ Session Manager
            user_id = sessionManagement.getUserId();
        }
        findId();
        fillInfo();
        imagePick();
        editInfo();
    }

    private byte[] ImageViewToByte(ImageView imageRoom) {
        Bitmap bitmap = ((BitmapDrawable)imageRoom.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }
    private void editInfo() {
        btn_apply_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = tv_account_fullname.getText().toString();
                String phone = tv_account_phone.getText().toString();
                String address = tv_account_address.getText().toString();
                String email = tv_account_email.getText().toString();
                String pass = tv_account_pass.getText().toString();

                User user = new User(user_id, email,fullName, phone, pass, address, ImageViewToByte(imgAcc));
                Boolean updateUser = userTbl.editUser(user);
                if(updateUser == true) {
                    Toast.makeText(ManageAccountActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void imagePick() {
        imgAcc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int img = 0;
                if(img == 0) {
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    }else {
                        pickFromGallery();
                    }
                }else if (img==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        if (storagePermission == null) {
            storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (cameraPermission == null) {
            cameraPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        }
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result2=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accept&&storage_accept){
                        pickFromGallery();
                    }else{
                        //Toast.makeText(this, "enable camera and storage permission", Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length>0){
                    boolean storage_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accept){
                        pickFromGallery();
                    }else{
                        //Toast.makeText(this, "please enable storage permission", Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                }
            }
            break;
        }
    }

    //overrid method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                Picasso.with(this).load(resultUri).into(imgAcc);
            }
        }
    }

    private void fillInfo() {
        User user = userTbl.getUserbyId(user_id);

        tv_account_fullname.setText(user.getFullName());
        tv_account_address.setText(user.getAddress());
        tv_account_email.setText(user.getEmail());
        tv_account_phone.setText(user.getPhoneNumber());
        tv_account_pass.setText(user.getPassword());
        if(user.getAvatar() != null) {
            byte [] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            imgAcc.setImageBitmap(bitmap);
        } else {
            imgAcc.setImageResource(R.drawable.profile);
        }

    }

    private void findId() {
        tv_account_fullname = findViewById(R.id.tv_account_fullname);
        tv_account_phone = findViewById(R.id.tv_account_phone);
        tv_account_address = findViewById(R.id.tv_account_address);
        tv_account_email = findViewById(R.id.tv_account_email);
        tv_account_pass = findViewById(R.id.tv_account_pass);
        imgAcc = findViewById(R.id.imgAcc);
        btn_apply_change = findViewById(R.id.btn_apply_change);
    }


}