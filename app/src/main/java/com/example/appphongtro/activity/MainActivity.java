package com.example.appphongtro.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.RoomPhotoAdapter;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.PhotoRoomTbl;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.PhotoRoom;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String[] amenities = {"Wifi", "Chỗ đỗ xe", "Điều hòa nhiệt độ", "TV", "Bếp", "Máy giặt", "Phòng tập gym", "Hồ bơi", "Ban công", "Nhà vệ sinh riêng", "Thang máy"};

    private String[] parkingFee = {"Miễn phí", "70000", "80000", "90000", "100000"};

    private DatabaseHelper databaseHelper;
    Button btnAddRoom, btnDisplay, btn_edit, btn_upload_img_room;
    RecyclerView recyclerImageRoom;
    ImageView imageRoom;
    EditText edtNameRoom, edtDesc, edtPrice, edtAddress, edtArea, edtRule, edtDepositMoney, edtElectricMoney, edtWaterMoney, edtInternetMoney;
    MultiAutoCompleteTextView edtAmenities, edtFeeParking;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    private int owner_id;
    private String[] cameraPermission;
    private String[] storagePermission;
    private BottomNavigationView bottom_navigation;
    int id = 0;

    private SessionManagement sessionManagement;

    private RoomTbl roomTbl;

    public static final int Read_permission = 101;

    private long room_id;

    ArrayList<PhotoRoom> uri = new ArrayList<>();
    private PhotoRoomTbl photoRoomTbl;
    private RoomPhotoAdapter roomPhotoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        roomTbl = new RoomTbl(this);
        photoRoomTbl = new PhotoRoomTbl(this);
        sessionManagement = new SessionManagement(this);

        findById();
        owner_id = sessionManagement.getUserId();
        System.out.println("id nhan la " + owner_id);
        insertData();
        imagePick();
        editData();
        bottom_navigation.setSelectedItemId(R.id.addRoom);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this, item));
        NavigationHelper.hideAddRoomMenu(this);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        roomPhotoAdapter = new RoomPhotoAdapter(uri);
        recyclerImageRoom.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        recyclerImageRoom.setAdapter(roomPhotoAdapter);
        pickMulImageForRoom();
    }

    private void pickMulImageForRoom() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_permission);
        }
        btn_upload_img_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), 1);
            }
        });

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtNameRoom.getText().toString();
                String description = edtDesc.getText().toString();
                String priceString = edtPrice.getText().toString();
                double price = priceString.isEmpty() ? 0.0 : Double.parseDouble(priceString);

                String areaString = edtArea.getText().toString();
                double area = areaString.isEmpty() ? 0.0 : Double.parseDouble(areaString);
                String address = edtAddress.getText().toString();
                String amenities = edtAmenities.getText().toString();
                String rule = edtRule.getText().toString();
                String depositMoneyString = edtDepositMoney.getText().toString();
                double depositMoney = depositMoneyString.isEmpty() ? 0.0 : Double.parseDouble(depositMoneyString);
                String electricity_moneyString = edtElectricMoney.getText().toString();
                double electricity_money = electricity_moneyString.isEmpty() ? 0.0 : Double.parseDouble(electricity_moneyString);
                String water_moneyString = edtWaterMoney.getText().toString();
                double water_money = water_moneyString.isEmpty() ? 0.0 : Double.parseDouble(water_moneyString);
                String internet_moneyString = edtInternetMoney.getText().toString();
                double internet_money = internet_moneyString.isEmpty() ? 0.0 : Double.parseDouble(internet_moneyString);
                String parkingFee = edtFeeParking.getText().toString();
                if (depositMoney > price) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập số tiền đặt cọc nhỏ hay bằng giá thuê", Toast.LENGTH_LONG).show();
                    edtDepositMoney.setText("");
                } else {
                    Room room = new Room(name, description, price, address, area, amenities, rule, owner_id, ImageViewToByte(imageRoom), 1, depositMoney, electricity_money, water_money, internet_money, parkingFee);
                    room_id = roomTbl.addRoom(room);
                    for (PhotoRoom photoRoom : uri) {
                        photoRoom.setRoom_id((int) room_id);
                        try {
                            photoRoomTbl.insertImage(photoRoom);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    uri.clear();
                    roomPhotoAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();
                    imageRoom.setImageResource(android.R.drawable.alert_light_frame);
                }
            }
        });


    }

    private void editData() {
        if (getIntent().getBundleExtra("dataRoom") != null) {
            Bundle bundle = getIntent().getBundleExtra("dataRoom");
            id = bundle.getInt("id");
            //for image
//            byte[]bytes =bundle.getByteArray("img");
//            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//            imageRoom.setImageBitmap(bitmap);
            //for set name
            edtNameRoom.setText(bundle.getString("name"));
            edtPrice.setText(String.valueOf(bundle.getDouble("price")));
            edtAddress.setText(bundle.getString("address"));
            edtAmenities.setText(bundle.getString("amenities"));
            edtArea.setText(String.valueOf(bundle.getDouble("are")));
            edtRule.setText(bundle.getString("rules"));
            edtDesc.setText(bundle.getString("description"));
            edtDepositMoney.setText(String.valueOf(bundle.getDouble("deposit_money")));
            edtElectricMoney.setText(String.valueOf(bundle.getDouble("electricity_money")));
            edtWaterMoney.setText(String.valueOf(bundle.getDouble("water_money")));
            edtInternetMoney.setText(String.valueOf(bundle.getDouble("internet_money")));
            edtFeeParking.setText(bundle.getString("parking_fee"));
            //visible edit button and hide submit button
            btnAddRoom.setVisibility(View.GONE);
            btn_edit.setVisibility(View.VISIBLE);
        }
    }


    private void imagePick() {

        imageRoom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int img = 0;
                if (img == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (img == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    private void insertData() {

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DisplayRoom.class));
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtNameRoom.getText().toString();
                String description = edtDesc.getText().toString();
                String priceString = edtPrice.getText().toString();
                double price = Double.parseDouble(priceString);
                String areaString = edtArea.getText().toString();
                double area = Double.parseDouble(areaString);
                String address = edtAddress.getText().toString();
                String amenities = edtAmenities.getText().toString();
                String rule = edtRule.getText().toString();
                double deposit_money = Double.parseDouble(edtDepositMoney.getText().toString());
                double electric_money = Double.parseDouble(edtElectricMoney.getText().toString());
                double water_money = Double.parseDouble(edtWaterMoney.getText().toString());
                double internet_money = Double.parseDouble(edtInternetMoney.getText().toString());
                String parking_fee = edtFeeParking.getText().toString();
                Room room = new Room(id, name, description, price, address, area, amenities, rule, owner_id, ImageViewToByte(imageRoom), deposit_money, electric_money, water_money, internet_money, parking_fee);
                Boolean updateRoom = roomTbl.editRoom(room);
                try {
                    photoRoomTbl.deleteImage(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for (PhotoRoom photoRoom : uri) {
                    try {
                        photoRoom.setRoom_id(id);
                        photoRoomTbl.insertImage(photoRoom);
                        System.out.println("scccc");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (updateRoom == true) {
                    Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    imageRoom.setImageResource(R.mipmap.ic_launcher);
                    btn_edit.setVisibility(View.GONE);
                    btnAddRoom.setVisibility(View.VISIBLE);
                    uri.clear();
                }
                roomPhotoAdapter.notifyDataSetChanged();
            }
        });
    }

    private byte[] ImageViewToByte(ImageView imageRoom) {
        Bitmap bitmap = ((BitmapDrawable) imageRoom.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void findById() {
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnDisplay = findViewById(R.id.btnDisplay);
        imageRoom = findViewById(R.id.imageRoom);
        edtNameRoom = findViewById(R.id.edtNameRoom);
        edtDesc = findViewById(R.id.edtDesc);
        edtPrice = findViewById(R.id.edtPrice);
        edtAddress = findViewById(R.id.edtAddress);
        edtArea = findViewById(R.id.edtArea);
        edtAmenities = findViewById(R.id.edtAmenities);
        edtRule = findViewById(R.id.edtRule);
        btn_edit = findViewById(R.id.btn_edit);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        btn_upload_img_room = findViewById(R.id.btn_upload_img_room);
        recyclerImageRoom = findViewById(R.id.recyclerImageRoom);
        edtDepositMoney = findViewById(R.id.edtDepositMoney);
        edtFeeParking = findViewById(R.id.edtFeeParking);
        edtElectricMoney = findViewById(R.id.edtElectricMoney);
        edtWaterMoney = findViewById(R.id.edtWaterMoney);
        edtInternetMoney = findViewById(R.id.edtInternetMoney);

        ArrayAdapter<String> parkingFeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, parkingFee);
        edtFeeParking.setThreshold(1);
        edtFeeParking.setAdapter(parkingFeeAdapter);

        edtFeeParking.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                return charSequence;
            }
        });

        ArrayAdapter<String> amenitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, amenities);
        edtAmenities.setAdapter(amenitiesAdapter);

        // Set a custom tokenizer to handle multiple selections
        edtAmenities.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;

                while (i > 0 && text.charAt(i - 1) != ',') {
                    i--;
                }

                // Skip any spaces before the comma
                while (i < cursor && text.charAt(i) == ' ') {
                    i++;
                }

                return i;
            }

            @Override
            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                int len = text.length();

                while (i < len) {
                    if (text.charAt(i) == ',') {
                        return i;
                    } else {
                        i++;
                    }
                }

                return len;
            }

            @Override
            public CharSequence terminateToken(CharSequence text) {
                // Add a comma and space after selecting an item
                return text + ", ";
            }
        });
    }

    //overrid method onRequestPermissionResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accept && storage_accept) {
                        pickFromGallery();
                    } else {
                        //Toast.makeText(this, "enable camera and storage permission", Toast.LENGTH_SHORT).show();
                        pickFromGallery();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accept) {
                        pickFromGallery();
                    } else {
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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(imageRoom);
            }
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    PhotoRoom photoRoom = new PhotoRoom();
                    photoRoom.setImageRoomUri(imageUri);
                    uri.add(photoRoom);
                }
                roomPhotoAdapter.notifyDataSetChanged();
            } else if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                PhotoRoom photoRoom = new PhotoRoom();
                photoRoom.setImageRoomUri(imageUri);
                uri.add(photoRoom);
                roomPhotoAdapter.notifyDataSetChanged();
            }
        }
    }
}