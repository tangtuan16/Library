package com.example.Views.Activitys;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Views.Adapters.BookContentAdapter;
import com.example.Views.yCustom.TickSeekBar;
import com.example.btl_libary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadBookActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BookContentAdapter adapter;
    TextView bookname;
    TickSeekBar seekBarTextSize;
    private float defaultTextSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_book);
        bookname = findViewById(R.id.bookname);
        viewPager2 = findViewById(R.id.viewpager);


        bookname.setText(getIntent().getStringExtra("title")+" - "+getIntent().getStringExtra("author"));
        int bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId != -1) {
            String jsonData = loadJSONFromAsset();
            try {
                JSONArray booksArray = new JSONArray(jsonData);
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject book = booksArray.getJSONObject(i);
                    if (book.getInt("id") == bookId) {
                        String content = book.getString("content");
                        List<String> textChunks = splitTextIntoChunks(content, 600);
                        adapter = new BookContentAdapter(textChunks);
                        viewPager2.setAdapter(adapter);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        seekBarTextSize = findViewById(R.id.seekBarTextSize);

        // Lưu kích thước chữ mặc định
        defaultTextSize = getResources().getDimension(R.dimen.text_size);

        // Thiết lập SeekBar
        seekBarTextSize.setMax(30); // Giả sử range từ 0 đến 30
        seekBarTextSize.setProgress(15); // Giá trị mặc định

        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scaleFactor = (progress + 10) / 25f; // Để tránh kích thước quá nhỏ
                updateTextSize(defaultTextSize * scaleFactor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


    }


    private void updateTextSize(float newSize) {
        if (adapter != null) {
            adapter.updateTextSize(newSize);
        }
    }
    // Hàm để đọc file JSON từ thư mục assets
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("books.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            // Thay thế \n\n bằng \n\n\t để lùi dòng
            json = json.replace("\\n\\n", "\\n\\n\t\t\t\t");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private List<String> splitTextIntoChunks(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += chunkSize) {
            chunks.add(text.substring(i, Math.min(length, i + chunkSize)));
        }
        return chunks;
    }
}
