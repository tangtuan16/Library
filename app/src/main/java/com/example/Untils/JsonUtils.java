package com.example.Untils;

import android.content.Context;

import com.example.Models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    public static List<Book> readBooksFromJson(Context context, String fileName) {
        List<Book> bookList = null;
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            Type bookListType = new TypeToken<List<Book>>() {
            }.getType();
            bookList = new Gson().fromJson(reader, bookListType);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
