package com.example.Views.Activitys;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

import com.example.Models.Book;
import com.example.Untils.DBManager; // Thêm import cho DBManager
import com.example.btl_libary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BorrowBookActivity extends AppCompatActivity {

    private TextView textBorrowDate, textReturnDate;
    private TextView numberOfBorrowedBooks;
    private EditText quantityToBorrow;
    private DBManager dbManager; // Thêm DBManager để quản lý cơ sở dữ liệu
    private Button buttonSelectBorrowDate ;
    private Button buttonSelectReturnDate ;
    private TextView  descriptionQuantityToBorrow;
    Button buttonBorrow;
    int borrowedCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        // Khởi tạo DBManager
        dbManager = new DBManager(this);
        dbManager.Open();

        borrowedCount = dbManager.getNumberOfBorrowedBooks(1,getIntent().getIntExtra("id_book", 0));


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
        int userId = 1; // Thay đổi thành ID người dùng thực tế
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

        // Mở cơ sở dữ liệu
        dbManager.Open();
        long result = dbManager.Insert("bookborrow", values); // Thêm vào bảng borrowbook
        dbManager.Close(); // Đóng cơ sở dữ liệu

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
        } else {
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



}
