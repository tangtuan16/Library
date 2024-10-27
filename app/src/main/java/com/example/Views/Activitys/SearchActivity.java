package com.example.Views.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;
import com.example.Presenters.SearchBookPresenter;
import com.example.Views.Adapters.BookAdapter;
import com.example.btl_libary.R;


import java.util.List;

public class SearchActivity extends AppCompatActivity implements BookContract.View.LibraryView {
    private SearchBookPresenter presenter;
    private RecyclerView rcvSearchResuilt;
    private Button btnSearch;
    private AutoCompleteTextView edtTitle, edtAuthor, edtDesc;
    private String edtTitleStr, edtAuthorStr, edtDescStr;
    private LinearLayout linearLayout;
    private ImageView hide;
    private String selectedAuthor;
    private boolean showSearchBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = new SearchBookPresenter(this, this);
        linearLayout = findViewById(R.id.linearSearch);
        rcvSearchResuilt = findViewById(R.id.rcvSearchResuilt);
        hide = findViewById(R.id.imgHide);
        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtDesc = findViewById(R.id.edtDesc);
        btnSearch = findViewById(R.id.btnSearch);


        BookModel bookModel = new BookModel(this);


        List<String> titles = bookModel.getAllBookTitles();
        List<String> authors = bookModel.getAllAuthors();
        List<String> genres = bookModel.getAllCategories();

        ArrayAdapter<String> adapterTitles = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, titles);
        ArrayAdapter<String> adapterAuthors = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, authors);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genres);

        edtTitle.setAdapter(adapterTitles);
        edtAuthor.setAdapter(adapterAuthors);
        edtDesc.setAdapter(adapterCategories);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTitleStr = edtTitle.getText().toString().trim();
                edtAuthorStr = edtAuthor.getText().toString().trim();
                edtDescStr = edtDesc.getText().toString().trim();
                if (edtTitleStr.isEmpty() && edtAuthorStr.isEmpty() && edtDescStr.isEmpty()) {
                    Toast.makeText(v.getContext(), "Vui lòng nhập thông tin để tìm kiếm!", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.loadSearchBook(edtTitleStr, edtAuthorStr, edtDescStr);
                presenter.NotifySearch(edtTitleStr, edtAuthorStr, edtDescStr);
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout.isShown()) {
                    hideLinearSearch();
                } else {
                    showLinearSearch();
                }
            }
        });

        rcvSearchResuilt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && linearLayout.isShown()) {
                    hideLinearSearch();
                }
            }
        });
    }

    @Override
    public void displayBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvSearchResuilt.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvSearchResuilt.addItemDecoration(itemDecoration);
        rcvSearchResuilt.setAdapter(new BookAdapter(list, 0));
    }

    @Override
    public void showSuccess(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayGridBook(List<Book> books) {

    }

    @Override
    public void displayFavoriteBook(List<Book> newBooks) {

    }

    public void hideLinearSearch() {
        linearLayout.animate().alpha(0.0f).setDuration(200).withEndAction(() -> linearLayout.setVisibility(View.GONE));
    }

    public void showLinearSearch() {
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.animate().alpha(1.0f).setDuration(200);
    }

}
