package com.example.Views.Activitys;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Views.Adapters.BookContentAdapter;
import com.example.btl_libary.R;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

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
    private TextView bookname;
    private Button btnIncreaseFontSize, btnDecreaseFontSize;
    private float currentTextSize;
    private static final float MIN_TEXT_SIZE = 39f;
    private static final float MAX_TEXT_SIZE = 81f;
    private static final float TEXT_SIZE_STEP = 5f;
    private String bookContent;
    List<String> textChunks;
    private LinearLayout linearLayout;
    CheckBox checkBoxVisibility;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_book);

        //admob banner
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //admob intiti gi do ko nho
        AdRequest adRequest2 = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest2,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(ReadBookActivity.this);
                        Log.i("TAGad", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAGad", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        bookname = findViewById(R.id.bookname);
        viewPager2 = findViewById(R.id.viewpager);
        bookname.setText(getIntent().getStringExtra("title") + " - " + getIntent().getStringExtra("author"));

        currentTextSize = getResources().getDimension(R.dimen.text_size);

        int bookId = getIntent().getIntExtra("bookId", -1);
        if (bookId != -1) {
            String jsonData = loadJSONFromAsset();
            try {
                JSONArray booksArray = new JSONArray(jsonData);
                for (int i = 0; i < booksArray.length(); i++) {
                    JSONObject book = booksArray.getJSONObject(i);
                    if (book.getInt("id") == bookId) {
                        bookContent = book.getString("content");
                        textChunks = new ArrayList<>();
                        textChunks.add(bookContent);
                        int chapterNumber = 1;
                        while (book.has("chuong" + chapterNumber)) {
                            bookContent = book.getString("chuong" + chapterNumber);
                            textChunks.add(bookContent);
                            chapterNumber++;
                        }

                        updateContent();
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //pop up linearelayout
        linearLayout = findViewById(R.id.linearLayoutdong); // ID của LinearLayout bạn muốn ẩn/hiện
        checkBoxVisibility = findViewById(R.id.checkBoxVisibility);

        checkBoxVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        });
        //tawng giam kich co
        btnIncreaseFontSize = findViewById(R.id.btnIncreaseFontSize);
        btnDecreaseFontSize = findViewById(R.id.btnDecreaseFontSize);
        btnIncreaseFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("ad", "Increase button clicked. Current size: " + currentTextSize);
                changeTextSize(true);
            }
        });

        btnDecreaseFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextSize(false);
            }
        });
    }
    @Nullable
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("books.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            json = json.replace("\\n\\n", "\\n\\n\t\t\t\t");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void changeTextSize(boolean increase) {
        if (increase && currentTextSize < MAX_TEXT_SIZE) {
            currentTextSize += TEXT_SIZE_STEP;

        } else if (!increase && currentTextSize > MIN_TEXT_SIZE) {
            currentTextSize -= TEXT_SIZE_STEP;
        }
        updateContent();
    }

    private void updateContent() {
        adapter = new BookContentAdapter(textChunks, currentTextSize);
        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new BookPageTransformer());
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
   //      Thêm padding cho ViewPager2 để hiệu ứng được nhìn thấy rõ hơn
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        viewPager2.setPadding(padding, 0, padding, 0);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);

    }

    public class BookPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Move it behind the left page
                view.setTranslationZ(-1f);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}