package com.example.appphongtro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appphongtro.R;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.session.SessionManagement;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    EditText login_username, login_password;
    Button login_button;
    TextView signUpRedirect;
    private UserTbl userTbl;

    private SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        userTbl = new UserTbl(this);
        try {
            databaseHelper.copyDatabaseFromAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sessionManagement = new SessionManagement(this);
        login_button = findViewById(R.id.login_button);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        signUpRedirect = findViewById(R.id.signUpRedirect);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }

    public void login() {
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        if(username.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            Boolean checkCredential = userTbl.checkUserNamePassword(username, password);

            if (checkCredential == true) {
                int owner_id = userTbl.getOwnerIdByUsername(username, password);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                sessionManagement.saveUserId(owner_id);
                System.out.println("id la" + owner_id);
                int role = userTbl.getRoleByUsername(username);
                sessionManagement.saveRole(role);
                if(role == 0) {
                    Intent intent = new Intent(getApplicationContext(), AdminManageAccActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), DisplayRoom.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

}