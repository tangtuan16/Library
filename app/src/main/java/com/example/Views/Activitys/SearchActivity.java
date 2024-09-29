package com.example.Views.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookRepository;
import com.example.Presenters.SearchBookPresenter;
import com.example.Views.Adapters.BookAdapter;
import com.example.btl_libary.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements BookContract.View {
    private SearchBookPresenter presenter;
    private RecyclerView rcvSearchResuilt;
    private Button btnSearch;
    private TextInputEditText edtTitle, edtAuthor;
    private AutoCompleteTextView edtDesc;
    private String edtTitleStr, edtAuthorStr, edtDescStr;
    private LinearLayout linearLayout;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        presenter = new SearchBookPresenter(this, this);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.edtDesc);
        String[] genres = getResources().getStringArray(R.array.literature_genres);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genres);
        autoCompleteTextView.setAdapter(adapter);

        linearLayout = findViewById(R.id.linearSearch);
        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtDesc = findViewById(R.id.edtDesc);
        btnSearch = findViewById(R.id.btnSearch);
        rcvSearchResuilt = findViewById(R.id.rcvSearchResuilt);

        BookRepository bookRepository = new BookRepository(this);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTitleStr = edtTitle.getText().toString().trim();
                edtAuthorStr = edtAuthor.getText().toString().trim();
                edtDescStr = edtDesc.getText().toString().trim();
                presenter.loadSearchBook(edtTitleStr, edtAuthorStr, edtDescStr);
                presenter.NotifySearch(edtTitleStr, edtAuthorStr, edtDescStr);
            }
        });
    }

    @Override
    public void displayBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvSearchResuilt.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvSearchResuilt.addItemDecoration(itemDecoration);
        rcvSearchResuilt.setAdapter(new BookAdapter(list));
    }

    @Override
    public void showSuccess(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }


}
