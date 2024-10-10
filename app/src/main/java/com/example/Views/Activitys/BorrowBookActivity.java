package com.example.Views.Activitys;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.Book;
import com.example.Untils.DBManager; // Thêm import cho DBManager
import com.example.btl_libary.R;

import java.util.Calendar;

public class BorrowBookActivity extends AppCompatActivity {

    private TextView textBorrowDate, textReturnDate;
    private Book book; // Đối tượng Book
    private DBManager dbManager; // Thêm DBManager để quản lý cơ sở dữ liệu

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        // Khởi tạo DBManager
        dbManager = new DBManager(this);

        // Nhận đối tượng Book từ Intent
        book = getIntent().getParcelableExtra("book");
        book=new Book(1,R.drawable.tuay,"Từ Ấy","To Huu","Thơ");

        // Khởi tạo các thành phần giao diện
        ImageView imageBookAvatar = findViewById(R.id.imageBookAvatar);
        textBorrowDate = findViewById(R.id.textBorrowDate);
        textReturnDate = findViewById(R.id.textReturnDate);
        Button buttonSelectBorrowDate = findViewById(R.id.buttonSelectBorrowDate);
        Button buttonSelectReturnDate = findViewById(R.id.buttonSelectReturnDate);
        Button buttonBorrow = findViewById(R.id.buttonBorrow);

        // Hiển thị thông tin sách
        imageBookAvatar.setImageResource(book.getAvt());
        TextView textBookTitle = findViewById(R.id.textBookTitle);
        textBookTitle.setText(book.getTitle());

        TextView textBookAuthor = findViewById(R.id.textBookAuthor);
        textBookAuthor.setText(book.getAuthor());

        TextView textBookDescription = findViewById(R.id.textBookDescription);
        textBookDescription.setText(book.getDesc());

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
        int bookId = book.getId(); // Lấy ID của sách
        String borrowDate = textBorrowDate.getText().toString();
        String returnDate = textReturnDate.getText().toString();

        // Tạo đối tượng ContentValues để lưu trữ dữ liệu
        ContentValues values = new ContentValues();
        values.put("userid", userId);
        values.put("bookid", bookId);
        values.put("borrowdate", borrowDate);
        values.put("returndate", returnDate);

        // Mở cơ sở dữ liệu
        dbManager.Open();
        long result = dbManager.Insert("borrowbook", values); // Thêm vào bảng borrowbook
        dbManager.Close(); // Đóng cơ sở dữ liệu

        if (result != -1) {
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
        datePickerDialog.show();
    }
}
