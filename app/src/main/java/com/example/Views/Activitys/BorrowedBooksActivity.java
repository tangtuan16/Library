package com.example.Views.Activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Models.BorrowedBook;
import com.example.Presenters.BorrowPresenter;
import com.example.Untils.DBManager;
import com.example.Views.Adapters.BorrowedBookAdapter;
import com.example.btl_libary.R;

import java.util.List;

public class BorrowedBooksActivity extends AppCompatActivity implements BookContract.View.BorrowBookView{
    private List<BorrowedBook> list;
    private RecyclerView rcvBorrowedBooks;
    private BorrowedBookAdapter adapter;
    private BorrowPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);
        presenter= new BorrowPresenter(this,(BookContract.View.BorrowBookView) this);
        rcvBorrowedBooks = findViewById(R.id.rcvBorrowedBook);
        rcvBorrowedBooks.setLayoutManager(new LinearLayoutManager(this));
        Init();



    }
    private void Init()
    {

        list = presenter.GetAllBorrowedBooks();
        adapter=new BorrowedBookAdapter(list);
        int borrowedBooksCount = adapter.getItemCount();
        rcvBorrowedBooks.setAdapter(adapter);


    }
}