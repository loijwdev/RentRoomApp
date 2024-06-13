package com.example.appphongtro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appphongtro.R;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEditText, usernameEditText, fullNameEditText, phoneNumberEditText,
            passwordEditText, addressEditText, passwordConfirm;

    private TextView loginRedirect;
    private Button signUpButton;

    private DatabaseHelper databaseHelper;
    private UserTbl userTbl;

    FirebaseDatabase database;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new DatabaseHelper(this);
        userTbl = new UserTbl(this);
//        try {
//            databaseHelper.copyDatabaseFromAssets();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        emailEditText = findViewById(R.id.signup_email);
        usernameEditText = findViewById(R.id.signup_username);
        fullNameEditText = findViewById(R.id.signup_fullname);
        phoneNumberEditText = findViewById(R.id.signup_number);
        passwordEditText = findViewById(R.id.signup_password);
        addressEditText = findViewById(R.id.signup_address);
        passwordConfirm = findViewById(R.id.signup_confirm);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirect = findViewById(R.id.loginRedirect);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String confirmPassword = passwordConfirm.getText().toString().trim();
        int role = getSelectedRole();

        if(email.equals("") || username.equals("") || fullName.equals("") || phoneNumber.equals("") || password.equals("") || address.equals("") || role == -1) {
            Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {


            if(password.equals(confirmPassword)) {
                Boolean checkUserEmail = userTbl.checkEmail(email);
                if(checkUserEmail == false) {
                    User user = new User(email, username, fullName, phoneNumber, password, address, role, 0);
                    Boolean insert = userTbl.insertData(user);
                    if(insert == true) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getSelectedRole() {
        RadioButton landlordRadioButton = findViewById(R.id.role_chunha);
        RadioButton tenantRadioButton = findViewById(R.id.role_nguoithue);

        if (landlordRadioButton.isChecked()) {
            return 1; // Người cho thuê phòng
        } else if (tenantRadioButton.isChecked()) {
            return 2; // Người tìm kiếm phòng
        }
        return -1; // Không có role nào được chọn
    }

}