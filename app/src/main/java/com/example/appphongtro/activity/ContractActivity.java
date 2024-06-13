package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.database.ContractTbl;
import com.example.appphongtro.model.Contract;

import java.util.Calendar;
import java.util.Date;

public class ContractActivity extends AppCompatActivity {
    EditText edtDatcoc, edtTienThue, edtPhiKhac, edtTotal;
    Button idBtnPickDate, createContract;

    TextView idTVSelectedDate;

    private ContractTbl contractTbl;
    int room_id, tenant_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        contractTbl = new ContractTbl(this);
        room_id = getIntent().getIntExtra("room_id", -1);
        tenant_id = getIntent().getIntExtra("tenant_id", -1);
        findId();
        pickTime();
        setupTextChangeListeners();
        createContract();
    }

    private void createContract() {
        createContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double deposit_money = Double.parseDouble(edtDatcoc.getText().toString());
                double rent_money = Double.parseDouble(edtTienThue.getText().toString());
                double other_fee = Double.parseDouble(edtPhiKhac.getText().toString());
                double total_money = Double.parseDouble(edtTotal.getText().toString());
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, day);
                Date startDate = calendar.getTime();
                Contract contract = new Contract(tenant_id, room_id, startDate, null, deposit_money, rent_money, other_fee, "Đã tạo", "Chưa thanh toán");
                boolean result = contractTbl.addContract(contract);
                if (result) {
                    Toast.makeText(ContractActivity.this, "Đã tạo thành công", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void setupTextChangeListeners() {
        // Add TextChangedListeners to your EditText fields
        edtDatcoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

        edtTienThue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

        edtPhiKhac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });
    }

    private void calculateTotal() {
        try {
            double deposit_money = Double.parseDouble(edtDatcoc.getText().toString());
            double rent_money = Double.parseDouble(edtTienThue.getText().toString());
            double other_fee = Double.parseDouble(edtPhiKhac.getText().toString());

            double total =  rent_money + other_fee;

            edtTotal.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            edtTotal.setText("");
        }
    }

    private void pickTime() {
        idBtnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        ContractActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                idTVSelectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    private void findId() {
        edtDatcoc = findViewById(R.id.edtDatcoc);
        edtTienThue = findViewById(R.id.edtTienThue);
        edtPhiKhac = findViewById(R.id.edtPhiKhac);
        edtTotal = findViewById(R.id.edtTotal);
        idBtnPickDate = findViewById(R.id.idBtnPickDate);
        idTVSelectedDate = findViewById(R.id.idTVSelectedDate);
        createContract = findViewById(R.id.createContract);
    }


}