package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.database.ContractTbl;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.Contract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import vn.momo.momo_partner.AppMoMoLib;

public class DetailContractActivity extends AppCompatActivity {
    TextView tv_deposit, tv_rent, tv_fee, tv_amount, tv_time, tv_nameTenant, tv_nameOwner;
    ImageView Img;
    Button btnPay;
    private ContractTbl contractTbl;
    private RoomTbl roomTbl;
    int tenant_id;
    int room_id;

    double amount;

    private String merchantName = "NGUYỄN QUANG LUÂN";
    private String merchantCode = "MOMOKGO920220822";
    private String merchantNameLabel = "NGUYỄN QUANG LUÂN";
    private String description = "Đặt cọc phòng";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contract);
        contractTbl = new ContractTbl(this);
        roomTbl = new RoomTbl(this);
        tenant_id = getIntent().getIntExtra("tenant_id", -1);
        room_id = getIntent().getIntExtra("room_id", -1);
        findId();
        showInfo();
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment();
            }
        });

        String statuspay = contractTbl.isRentPaid(tenant_id, room_id);
        String hexColorCode = "#DCC687";
        int color = Color.parseColor(hexColorCode);
        if(statuspay != null && statuspay.equals("Đã thanh toán")) {
            btnPay.setEnabled(false);
            btnPay.setBackgroundColor(color);
            btnPay.setText("Đã thanh toán tiền");
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
                        contractTbl.updateIsPaidRent(tenant_id, room_id, "Đã thanh toán");
                        Toast.makeText(DetailContractActivity.this, "Đã thanh toán thành công", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DetailContractActivity.this, "Thanh toán không thành công", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("thanh cong", "ko thah cong4");

            }
        } else {
            Log.d("thanh cong", "ko thah cong5");

        }
    }
    private void showInfo() {
        Contract contract = contractTbl.getContractDetails(tenant_id, room_id);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String depositFormatted = decimalFormat.format(contract.getDeposit());
        String rentFormatted = decimalFormat.format(contract.getRent());
        String otherFeeFormatted = decimalFormat.format(contract.getOther_fees());
        tv_deposit.setText(depositFormatted +" VND");
        tv_rent.setText(rentFormatted +" VND");
        tv_fee.setText(otherFeeFormatted +" VND");
        amount = contract.getRent() + contract.getOther_fees();
        String amountFormatted = decimalFormat.format(amount);
        tv_amount.setText(amountFormatted + " VND");
        long timestamp = contract.getStart_date().getTime();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd"); // Đặt định dạng mong muốn
        String formattedDate = sdf.format(date);
        tv_time.setText(formattedDate);
        tv_nameTenant.setText(contract.getTenant_name());
        byte[] image = contract.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
        Img.setImageBitmap(bitmap);

        int room_id = contract.getRoom_id();
        String nameOwner = roomTbl.getOwnerNameByRoomId(room_id);
        tv_nameOwner.setText(nameOwner);

    }



    private void findId() {
        Img = findViewById(R.id.Img);
        tv_deposit = findViewById(R.id.tv_deposit);
        tv_rent = findViewById(R.id.tv_rent);
        tv_fee = findViewById(R.id.tv_fee);
        tv_amount = findViewById(R.id.tv_amount);
        tv_time = findViewById(R.id.tv_time);
        tv_nameTenant = findViewById(R.id.tv_nameTenant);
        tv_nameOwner = findViewById(R.id.tv_nameOwner);
        btnPay = findViewById(R.id.btnPay);
    }
}