package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.appphongtro.R;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.HistoryFindTbl;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FindRoomActivity extends AppCompatActivity {

    private String [] prices = {"0$ - 50$", "51$ - 100$", "101$ - 150$", "151$ - 200$", "201$ - 250$", "250$ +"};
    private String[] amenities = {"Wifi", "Chỗ đỗ xe", "Điều hòa nhiệt độ", "TV", "Bếp", "Máy giặt", "Phòng tập gym", "Hồ bơi", "Ban công", "Nhà vệ sinh riêng", "Thang máy"};

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<String> adapterPrices;
    private BottomNavigationView bottom_navigation;
    private EditText edtFindRoomLocation, edtFindRoomArea;
    MultiAutoCompleteTextView edtFindRoomBenefit;
    private Button searchRoom;
    private DatabaseHelper databaseHelper;
    private HistoryFindTbl historyFindTbl;

    private SessionManagement sessionManagement;
    private int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        databaseHelper  = new DatabaseHelper(this);
        historyFindTbl = new HistoryFindTbl(this);
        findId();
        adapterPrices = new ArrayAdapter<String>(this, R.layout.list_price, prices);

        autoCompleteTextView.setAdapter(adapterPrices);
        sessionManagement = new SessionManagement(this);
        user_id = sessionManagement.getUserId();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(FindRoomActivity.this, "item+ =" + item, Toast.LENGTH_LONG).show();
            }
        });
        bottom_navigation.setSelectedItemId(R.id.findRoom);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));
        NavigationHelper.hideAddRoomMenu(this);
        searchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        // Get the entered search criteria
        String location = edtFindRoomLocation.getText().toString();
        String area = edtFindRoomArea.getText().toString();
        String benefit = edtFindRoomBenefit.getText().toString();
        String priceRange = autoCompleteTextView.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        bundle.putString("area", area);
        bundle.putString("benefit", benefit);
        bundle.putString("priceRange", priceRange);
        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        intent.putExtra("dataSearchRoom", bundle);
        startActivity(intent);

        String searchCateria = location +" - " +benefit;
        historyFindTbl.insertSearchHistory(user_id,searchCateria);

    }

    private void findId() {
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        edtFindRoomLocation = findViewById(R.id.edtFindRoomLocation);
        edtFindRoomArea = findViewById(R.id.edtFindRoomArea);
        searchRoom = findViewById(R.id.searchRoom);

        edtFindRoomBenefit = findViewById(R.id.edtFindRoomBenefit);
        ArrayAdapter<String> amenitiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, amenities);
        edtFindRoomBenefit.setAdapter(amenitiesAdapter);

        // Set a custom tokenizer to handle multiple selections
        edtFindRoomBenefit.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
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
}