package com.example.Views.Activitys;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BorrowedBook;
import com.example.Presenters.BorrowPresenter;
import com.example.Untils.DBManager; // Thêm import cho DBManager
import com.example.Untils.NotificationReceiver;
import com.example.Untils.SharedPreferencesUtil;
import com.example.btl_libary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BorrowBookActivity extends AppCompatActivity implements BookContract.View.BorrowBookView {

    private TextView textBorrowDate, textReturnDate;
    private TextView numberOfBorrowedBooks;
    private EditText quantityToBorrow;
    private Button buttonSelectBorrowDate ;
    private Button buttonSelectReturnDate ;
    private TextView  descriptionQuantityToBorrow;
    private Button buttonBorrow;
    private int borrowedCount;
    private BorrowPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        presenter= new BorrowPresenter(this,(BookContract.View.BorrowBookView) this);
        borrowedCount = presenter.getNumberOfBorrowedBooks(getIntent().getIntExtra("id_book", 0));


        // Khởi tạo các thành phần giao diện
        descriptionQuantityToBorrow = findViewById(R.id.descriptionQuantityToBorrow);
        numberOfBorrowedBooks = findViewById(R.id.numberOfBorrowedBooks);
        quantityToBorrow = findViewById(R.id.quantityToBorrow);
        textBorrowDate = findViewById(R.id.textBorrowDate);
        textReturnDate = findViewById(R.id.textReturnDate);
        buttonSelectBorrowDate = findViewById(R.id.buttonSelectBorrowDate);
        buttonSelectReturnDate = findViewById(R.id.buttonSelectReturnDate);
        buttonBorrow = findViewById(R.id.buttonBorrow);

        numberOfBorrowedBooks.setText("Số lượng sách đã mượn: " + borrowedCount + "/3");
        if (borrowedCount >= 3) {
            // Ẩn các thành phần nhập dữ liệu và hiển thị thông báo
            descriptionQuantityToBorrow.setVisibility(View.VISIBLE);
            quantityToBorrow.setVisibility(View.GONE);
            textBorrowDate.setVisibility(View.GONE);
            textReturnDate.setVisibility(View.GONE);
            buttonSelectBorrowDate.setVisibility(View.GONE);
            buttonSelectReturnDate.setVisibility(View.GONE);
            buttonBorrow.setVisibility(View.GONE);

        }



        // Xử lý sự kiện chọn ngày gửi
        buttonSelectBorrowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true); // true để chọn ngày gửi
            }
        });

        // Xử lý sự kiện chọn ngày trả
        buttonSelectReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false); // false để chọn ngày trả
            }
        });

        // Xử lý nút mượn sách
        buttonBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowBook(); // Gọi phương thức để mượn sách
            }
        });
    }

    private void borrowBook() {
        // Lấy thông tin người dùng và ngày gửi, ngày trả
        int userId = SharedPreferencesUtil.getUserId(this); // Thay đổi thành ID người dùng thực tế
        int bookId = getIntent().getIntExtra("id_book", 0); // Lấy ID của sách
        int quantity = Integer.parseInt(quantityToBorrow.getText().toString());
        if (borrowedCount + quantity > 3) {
            Toast.makeText(this, "Bạn không thể mượn quá 3 cuốn sách.", Toast.LENGTH_SHORT).show();
            return; // Dừng hàm nếu vượt quá 3 cuốn
        }
        String borrowDateString = textBorrowDate.getText().toString();
        String returnDateString = textReturnDate.getText().toString();

        // Định dạng cho ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date borrowDate;
        Date returnDate;

        try {
            // Chuyển đổi chuỗi thành Date
            borrowDate = sdf.parse(borrowDateString);
            returnDate = sdf.parse(returnDateString);
        } catch (ParseException e) {
            Toast.makeText(this, "Ngày không hợp lệ.", Toast.LENGTH_SHORT).show();
            return; // Dừng hàm nếu định dạng ngày không hợp lệ
        }

        // Kiểm tra nếu ngày trả phải lớn hơn ngày mượn
        if (returnDate.before(borrowDate)) {
            Toast.makeText(this, "Ngày trả phải lớn hơn ngày mượn.", Toast.LENGTH_SHORT).show();
            return; // Dừng hàm nếu ngày trả không hợp lệ
        }

        // Tạo đối tượng ContentValues để lưu trữ dữ liệu
        ContentValues values = new ContentValues();
        values.put("user_ID", userId);
        values.put("book_ID", bookId);
        values.put("book_Total", quantity);
        values.put("date_Borrow", borrowDateString);
        values.put("date_Return", returnDateString);



        long result = presenter.InsertBorrowedBook(values); // Thêm vào bảng borrowbook


        if (result != -1) {
            borrowedCount+=quantity;
            if (borrowedCount >= 3) {
                // Ẩn các thành phần nhập dữ liệu và hiển thị thông báo
                descriptionQuantityToBorrow.setVisibility(View.VISIBLE);
                quantityToBorrow.setVisibility(View.GONE);
                textBorrowDate.setVisibility(View.GONE);
                textReturnDate.setVisibility(View.GONE);
                buttonSelectBorrowDate.setVisibility(View.GONE);
                buttonSelectReturnDate.setVisibility(View.GONE);
                buttonBorrow.setVisibility(View.GONE);

            }
            numberOfBorrowedBooks.setText("Số lượng sách đã mượn: " + borrowedCount + "/3");
            Toast.makeText(this, "Book borrowed successfully!", Toast.LENGTH_SHORT).show();

            // Lên lịch thông báo
            scheduleNotifications(borrowDate, returnDate,quantity,getIntent().getStringExtra("title"));        }
             else {
            Toast.makeText(this, "Error borrowing book.", Toast.LENGTH_SHORT).show();
             }
    }

    private void showDatePickerDialog(boolean isBorrowDate) {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Cập nhật TextView tương ứng với ngày đã chọn
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            if (isBorrowDate) {
                textBorrowDate.setText(date);
            } else {
                textReturnDate.setText(date);
            }
        }, year, month, day);

        // Nếu bạn muốn không cho phép chọn ngày trước ngày hiện tại
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
    private void scheduleNotifications(Date borrowDate, Date returnDate,int quantity,String title) {

        Calendar borrowCal = Calendar.getInstance();
        borrowCal.setTime(borrowDate);
        borrowCal.add(Calendar.DAY_OF_YEAR, -1); // Trước 1 ngày
        borrowCal.set(Calendar.HOUR_OF_DAY, 16);// Lên lịch thông báo trước ngày muon 1 ngày vào lúc 8 PM
        borrowCal.set(Calendar.MINUTE, 1);
        borrowCal.set(Calendar.SECOND, 0);
        borrowCal.set(Calendar.MILLISECOND, 0);


        Calendar returnCal = Calendar.getInstance();
        returnCal.setTime(returnDate);
        returnCal.add(Calendar.DAY_OF_YEAR, -1); // Trước 1 ngày
        returnCal.set(Calendar.HOUR_OF_DAY, 20);
        returnCal.set(Calendar.MINUTE, 0);
        returnCal.set(Calendar.SECOND, 0);
        returnCal.set(Calendar.MILLISECOND, 0);

        // Kiểm tra nếu thời gian lên lịch đã qua, không lên lịch
        if (borrowCal.getTimeInMillis() > System.currentTimeMillis()) {
            setAlarm(borrowCal, "Nhắc nhở mượn sách", "Mai la han muon "+quantity+" cuon "+title+"\n" );
        }

        if (returnCal.getTimeInMillis() > System.currentTimeMillis()) {
            setAlarm(returnCal, "Nhắc nhở trả sách","Mai la han tra "+quantity+" cuon "+title +"\n");
        }
    }

    private int setAlarm(Calendar calendar, String title, String message) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("content", message);


        // Sử dụng requestCode duy nhất để phân biệt các thông báo
        int requestCode = (int) System.currentTimeMillis();
        intent.putExtra("requestCode", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            // Đặt alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        return requestCode;
    }


    @Override
    public void SetData(List<BorrowedBook> list) {

    }
}
