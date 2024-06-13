package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.DepositTbl;
import com.example.appphongtro.database.FavoriteTbl;
import com.example.appphongtro.database.ReportTbl;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.Deposit;
import com.example.appphongtro.model.Report;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.model.User;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import vn.momo.momo_partner.AppMoMoLib;

public class DetailRoomActivity extends AppCompatActivity {

    private TextView nameDetailRoom, tv_deposit, descDetailRoom, priceDetailRoom, areaDetailRoom, benefitDetailRoom, ruleDetailRoom, nameUserDetail, toListRoomOfOwner, tv_to_img_detail_room, tv_to_list_review, tv_ic1,tv_ic2,tv_ic3,tv_ic4, tv_time;
    private ImageView ImgDetailRoom, imgUserDetail;
    private MaterialButton btnRp;
    private MaterialCardView matCardView;

    private DatabaseHelper databaseHelper;
    private BottomNavigationView bottom_navigation;

    private Button btnBookRoom, btnContact, btnMoMo, btnMap;
    private LikeButton heart_button;
    private RoomTbl roomTbl;

    private ReportTbl reportTbl;

    private int roomId;
    private int user_id;

    private UserTbl userTbl;
    private FavoriteTbl favoriteDb;

    private SessionManagement sessionManagement;

    private String amount;
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "NGUYỄN QUANG LUÂN";
    private String merchantCode = "MOMOKGO920220822";
    private String merchantNameLabel = "NGUYỄN QUANG LUÂN";
    private String description = "Đặt cọc phòng";
    private DepositTbl depositTbl;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);
        databaseHelper = new DatabaseHelper(this);
        roomTbl = new RoomTbl(this);
        userTbl = new UserTbl(this);
        reportTbl = new ReportTbl(this);
        favoriteDb = new FavoriteTbl(this);
        depositTbl = new DepositTbl(this);
        sessionManagement = new SessionManagement(this);
        user_id = sessionManagement.getUserId();
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomId", -1);
        findId();
        viewDetailRoom();

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        btnBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectBookRoom();
            }
        });

        btnRp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReportDialog();
            }
        });
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this, item));

        toListRoomOfOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRoomActivity.this, ListRoomOfOwnerActivity.class);
                int owner_id = roomTbl.getOwnerIdByRoomId(roomId);
                intent.putExtra("owner_id", owner_id);
                startActivity(intent);
            }
        });
        tv_to_list_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRoomActivity.this, ListReviewActivity.class);
                intent.putExtra("room_id", roomId);
                startActivity(intent);
            }
        });

        tv_to_img_detail_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRoomActivity.this, ListImageRoomActivity.class);
                intent.putExtra("room_id", roomId);
                startActivity(intent);
            }
        });

        if (favoriteDb.isRoomInFavorites(user_id, roomId)) {
            heart_button.setLiked(true);
        }
        heart_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                favoriteDb.addFavorite(user_id, roomId);
                Toast.makeText(DetailRoomActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_LONG).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                favoriteDb.removeFavorite(user_id, roomId);
                Toast.makeText(DetailRoomActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_LONG).show();
            }
        });

        String depositStatus = depositTbl.getStatus(roomId, user_id);
        if (depositStatus != null && depositStatus.equals("Đã thanh toán")) {
            btnMoMo.setText("Đã đặt cọc");
            btnMoMo.setEnabled(false);
        }

        btnMoMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(DetailRoomActivity.this, MapsActivity.class);
//                intent.putExtra("ADDRESS", address);
//                startActivity(intent);
                openMapWithAddress(address);
            }
        });
    }

    private void openMapWithAddress(String address) {
        // Tạo Uri từ địa chỉ
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        // Tạo Intent với hành động ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
    public long generateId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextLong(10_000_000_000L, 100_000_000_000L);
    }
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        long orderId = generateId();
        eventValue.put("orderId", String.valueOf(orderId)); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", String.valueOf(orderId)); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(objExtraData);
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }

    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("thanh cong", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    System.out.println("token: " + token);
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }
                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                        Deposit deposit = new Deposit();
                        deposit.setAmount(Double.parseDouble(String.valueOf(amount)));
                        deposit.setRoomId(roomId);
                        deposit.setStatus("Đã thanh toán");
                        deposit.setTransactionMomoToken(token);
                        deposit.setTenantId(user_id);
                        depositTbl.addOrUpdateDeposit(deposit);
                        System.out.println(depositTbl);
                        roomTbl.updateRoomStatus(roomId,0);
                        Toast.makeText(DetailRoomActivity.this, "Đã thanh toán thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("thanh cong", "ko thah cong");

                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Log.d("thanh cong", "ko thah cong1");
                } else if (data.getIntExtra("status", -1) == 2) {
                    Log.d("thanh cong", "ko thah cong2");

                } else {
                    Log.d("thanh cong", "ko thah cong3");
                    Deposit deposit = new Deposit();
                    deposit.setAmount(Double.parseDouble(String.valueOf(amount)));
                    deposit.setRoomId(roomId);
                    deposit.setStatus("Chưa thanh toán");
                    deposit.setTransactionMomoToken(null);
                    deposit.setTenantId(user_id);
                    depositTbl.addDepositToDatabase(deposit);
                    System.out.println(depositTbl);
                    Toast.makeText(DetailRoomActivity.this, "Thanh toán không thành công", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("thanh cong", "ko thah cong4");

            }
        } else {
            Log.d("thanh cong", "ko thah cong5");

        }
    }

    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vì sao bạn không thích?");

        String[] reportOptions = {"Phòng trọ không phù hợp", "Thông tin không chính xác", "Khác"};
        builder.setItems(reportOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == reportOptions.length - 1) {
                    // Nếu người dùng chọn "Khác", thêm EditText vào dialog để nhập lý do
                    showOtherReasonDialog();
                } else {
                    // Xử lý các tùy chọn khác
                    String selectedOption = reportOptions[which];
                    Report report = new Report();
                    report.setRoom_id(roomId);
                    report.setReason(selectedOption);
                    report.setReport_id(user_id);
                    boolean rp = reportTbl.saveReportToDatabase(report);
                    if (rp == true) {
                        Toast.makeText(DetailRoomActivity.this, "Đã gửi báo cáo Admin", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });

        // Hiển thị dialog
        builder.show();
    }

    private void showOtherReasonDialog() {
        // Tạo dialog mới với EditText để nhập lý do
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lý do báo cáo");

        final EditText input = new EditText(this);
        input.setHint("Nhập lý do...");
        builder.setView(input);
        builder.setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy lý do từ EditText và xử lý nó
                String reason = input.getText().toString();
                Report report = new Report();
                report.setRoom_id(roomId);
                report.setReason(reason);
                report.setReport_id(user_id);
                boolean rp = reportTbl.saveReportToDatabase(report);
                if (rp == true) {
                    Toast.makeText(DetailRoomActivity.this, "Đã gửi báo cáo cho Admin", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Hiển thị dialog
        builder.show();
    }


    private void redirectBookRoom() {
        Intent intent = getIntent();
        int roomId = intent.getIntExtra("roomId", -1);

        Intent intentBook = new Intent(getApplicationContext(), BookRoomActivity.class);
        intentBook.putExtra("roomIdDetail", roomId);
        startActivity(intentBook);
    }

    private void viewDetailRoom() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        Room room = roomTbl.getRoomById(roomId);
        address = room.getAddress();
        byte[] image = room.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImgDetailRoom.setImageBitmap(bitmap);
        nameDetailRoom.setText(room.getName());
        descDetailRoom.setText(room.getDescription());
        String formattedPrice = decimalFormat.format(room.getPrice());
        priceDetailRoom.setText(formattedPrice + " VND/1 tháng");
        areaDetailRoom.setText(String.valueOf(room.getAre()) + "/m2");
        ruleDetailRoom.setText(room.getRules());
        benefitDetailRoom.setText(room.getAmenities());
        amount = String.valueOf(room.getDepositMoney());
        String formattedDeposit = decimalFormat.format(room.getDepositMoney()) + " VND";
        tv_deposit.setText(formattedDeposit);
        String formattedElectricMoney = decimalFormat.format(room.getElectricity_money()) + "k";
        tv_ic1.setText(formattedElectricMoney);
        String formattedWaterMoney = decimalFormat.format(room.getWater_money()) +"k";
        tv_ic2.setText(formattedWaterMoney);
        String formattedInternetMoney = decimalFormat.format(room.getInternet_money()) +"k";
        tv_ic4.setText(formattedInternetMoney);
        tv_ic3.setText(room.getParking_fee());

        Date date = new Date(room.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Đặt định dạng mong muốn
        String formattedDate = sdf.format(date);
        tv_time.setText(formattedDate);
        User user = userTbl.getOwnerInfoByRoomId(roomId);
        if (user.getAvatar() != null) {
            byte[] imageOwner = user.getAvatar();
            Bitmap bitmapImgOwner = BitmapFactory.decodeByteArray(imageOwner, 0, imageOwner != null ? imageOwner.length : 0);
            imgUserDetail.setImageBitmap(bitmapImgOwner);
        } else {
            imgUserDetail.setImageResource(R.drawable.profile);
        }
        nameUserDetail.setText(user.getFullName());
        String ownerPhoneNumber = user.getPhoneNumber();
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(ownerPhoneNumber);
            }
        });


    }

    private void showContactDialog(String ownerPhoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Liên hệ với chủ nhà?");
        builder.setMessage("Bạn có muốn gọi " + ownerPhoneNumber);

        builder.setPositiveButton("Gọi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Initiate a phone call using an implicit intent
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ownerPhoneNumber));
                startActivity(dialIntent);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Display the dialog
        builder.show();
    }

    private void findId() {
        nameDetailRoom = findViewById(R.id.nameDetailRoom);
        descDetailRoom = findViewById(R.id.descDetailRoom);
        priceDetailRoom = findViewById(R.id.priceDetailRoom);
        areaDetailRoom = findViewById(R.id.areaDetailRoom);
        benefitDetailRoom = findViewById(R.id.benefitDetailRoom);
        ruleDetailRoom = findViewById(R.id.ruleDetailRoom);
        ImgDetailRoom = findViewById(R.id.ImgDetailRoom);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        btnRp = findViewById(R.id.btnRp);
        imgUserDetail = findViewById(R.id.imgUserDetail);
        nameUserDetail = findViewById(R.id.nameUserDetail);
        btnContact = findViewById(R.id.btnContact);
        toListRoomOfOwner = findViewById(R.id.toListRoomOfOwner);
        tv_to_list_review = findViewById(R.id.tv_to_list_review);
        tv_to_img_detail_room = findViewById(R.id.tv_to_img_detail_room);
        matCardView = findViewById(R.id.matCardView);
        heart_button = findViewById(R.id.heart_button);
        btnMoMo = findViewById(R.id.btnMoMo);
        tv_deposit = findViewById(R.id.tv_deposit);
        btnMap = findViewById(R.id.btnMap);
        tv_ic1 = findViewById(R.id.tv_ic1);
        tv_ic2 = findViewById(R.id.tv_ic2);
        tv_ic3 = findViewById(R.id.tv_ic3);
        tv_ic4 = findViewById(R.id.tv_ic4);
        tv_time = findViewById(R.id.tv_time);
    }


}