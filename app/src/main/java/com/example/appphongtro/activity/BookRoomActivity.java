package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.database.AppointmentDb;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.Appointment;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.session.SessionManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookRoomActivity extends AppCompatActivity {

    private TextView nameBookRoom;
    private DatePicker datePicker;
    private EditText edtNote;
    private Button btnSetAppointment;
    private DatabaseHelper databaseHelper;
    private int tenant_id;
    private SessionManagement sessionManagement;
    private Spinner timeSlotSpinner;
    private static final int MIN_HOUR = 8;
    private static final int MAX_HOUR = 21;
    private int roomId;
    private RoomTbl roomTbl;

    private AppointmentDb appointmentDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        databaseHelper = new DatabaseHelper(this);
        roomTbl = new RoomTbl(this);
        appointmentDb = new AppointmentDb(this);
        sessionManagement = new SessionManagement(this);
        tenant_id = sessionManagement.getUserId();
        findId();
        Intent intent = getIntent();
        roomId = intent.getIntExtra("roomIdDetail", -1);
        Room room = roomTbl.getRoomById(roomId);
        nameBookRoom.setText(room.getName());
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        populateTimeSlots(year, monthOfYear, dayOfMonth);
                    }
                });
        // Khởi tạo danh sách khung giờ với ngày mặc định của DatePicker
        populateTimeSlots(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        configureDatePicker();
        insertBookRoom();
    }

    private void insertBookRoom() {
        btnSetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();
                int selectedPosition = timeSlotSpinner.getSelectedItemPosition();
                if (selectedPosition == -1) {
                    // Không có khung giờ nào được chọn
                    Toast.makeText(BookRoomActivity.this, "Vui lòng chọn một khung giờ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy khung giờ được chọn
                String selectedTimeSlot = (String) timeSlotSpinner.getSelectedItem();
                // Phân tích chuỗi thời gian được chọn để lấy thời gian bắt đầu
                Date startTime = parseTime(selectedTimeSlot);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, startTime.getHours(), startTime.getMinutes());
                Date appointmentDate = calendar.getTime();

                if (!isValidTime(appointmentDate)) {
                    Toast.makeText(BookRoomActivity.this, "Vui lòng chọn thời gian từ 8h sáng đến 9h tối", Toast.LENGTH_SHORT).show();
                    return;
                }
                String note = edtNote.getText().toString();
                Intent intent = getIntent();
                int roomId = intent.getIntExtra("roomIdDetail", -1);

                // Kiểm tra xem đã có lịch hẹn trước đó chưa
                Appointment existingAppointment = appointmentDb.getAppointment(roomId, tenant_id);

                if (existingAppointment != null) {
                    // Nếu có, cập nhật lịch hẹn
                    existingAppointment.setNote(note);
                    existingAppointment.setStatus("Đang xử lý");
                    existingAppointment.setAppointmentDate(appointmentDate);
                    Boolean updateAppointment = appointmentDb.updateAppointment(existingAppointment);

                    if (updateAppointment) {
                        Toast.makeText(BookRoomActivity.this, "Đã cập nhật lịch hẹn thành công", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Nếu không có, thêm mới lịch hẹn
                    Appointment newAppointment = new Appointment();
                    newAppointment.setAppointmentDate(appointmentDate);
                    newAppointment.setNote(note);
                    newAppointment.setTenant_id(tenant_id);
                    newAppointment.setRoom_id(roomId);
                    newAppointment.setStatus("Đang xử lý");
                    newAppointment.setHasVisited(0);
                    Boolean insertAppointment = appointmentDb.addAppointment(newAppointment);
                    if (insertAppointment) {
                        Toast.makeText(BookRoomActivity.this, "Đã đặt lịch hẹn thành công", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void populateTimeSlots(int selectedYear, int selectedMonth, int selectedDay) {
        // Lấy thời gian hiện tại
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);

        // Tạo khung giờ cho 24 giờ tiếp theo
        List<String> timeSlots = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);

        // Gọi phương thức getBookedTimeSlots để lấy danh sách các khung giờ đã đặt
        List<String> bookedTimeSlots = appointmentDb.getBookedTimeSlots(roomId, selectedDate.getTime(), tenant_id);
        List<String> nextHourBookedTimeSlots = getNextHourBookedTimeSlots(bookedTimeSlots);

        for (int i = (currentTime.get(Calendar.YEAR) == selectedYear
                && currentTime.get(Calendar.MONTH) == selectedMonth
                && currentTime.get(Calendar.DAY_OF_MONTH) == selectedDay) ? currentHour : 8;
             i <= 21; i++) {

            if (i >= 8 && i <= 21) {
                // Tính toán thời gian bắt đầu của khung giờ
                Calendar timeSlotStartTime = Calendar.getInstance();
                timeSlotStartTime.set(selectedYear, selectedMonth, selectedDay, i, 0);

                // Chuyển đổi thời gian thành chuỗi "HH:mm"
                String timeSlot = sdf.format(timeSlotStartTime.getTime());
                System.out.println(timeSlot);
                if (!bookedTimeSlots.contains(timeSlot) && !nextHourBookedTimeSlots.contains(timeSlot)) {
                    timeSlots.add(timeSlot);
                }
                System.out.println();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSlotSpinner.setAdapter(adapter);
    }

    private List<String> getNextHourBookedTimeSlots(List<String> bookedTimeSlots) {
        List<String> nextHourBookedTimeSlots = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (String timeSlot : bookedTimeSlots) {
            try {
                // Chuyển đổi thời gian thành đối tượng Date
                Date date = sdf.parse(timeSlot);

                // Tính toán thời gian tiếp theo bằng cách cộng thêm 1 giờ
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 1);

                // Chuyển đổi lại thành chuỗi "HH:mm" và thêm vào danh sách
                nextHourBookedTimeSlots.add(sdf.format(calendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return nextHourBookedTimeSlots;
    }


    private void configureDatePicker() {
        // Lấy thời gian hiện tại
        Calendar currentDate = Calendar.getInstance();

        // Set giới hạn cho DatePicker
        datePicker.setMinDate(currentDate.getTimeInMillis());
    }


    private Date parseTime(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.parse(timeString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        return hourOfDay >= MIN_HOUR && hourOfDay <= MAX_HOUR;
    }


    private void findId() {
        nameBookRoom = findViewById(R.id.nameBookRoom);
        datePicker = findViewById(R.id.datePicker);
        timeSlotSpinner = findViewById(R.id.timeSlotSpinner);
        edtNote = findViewById(R.id.edtNote);
        btnSetAppointment = findViewById(R.id.btnSetAppointment);

    }
}