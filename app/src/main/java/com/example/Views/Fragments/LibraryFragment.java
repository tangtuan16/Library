package com.example.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Presenters.BookPresenter;
import com.example.Untils.SharedPreferencesUtil;
import com.example.Views.Activitys.MainActivity;
import com.example.Views.Activitys.SearchActivity;
import com.example.Views.Adapters.BookAdapter;
import com.example.Views.Adapters.BookFavAdapter;
import com.example.btl_libary.R;

import java.util.List;

public class LibraryFragment extends Fragment implements BookContract.View.LibraryView {
    private RecyclerView rvBooks;
    private BookPresenter presenter;
    Button btnLine;
    Button btnGrid;
    Button showPopupButton;
    ViewPager2 viewPager2;
    LinearLayout pageView;
    @Nullable
    @Override
    public void onResume() {
        super.onResume();
        presenter = new BookPresenter(getContext(), (BookContract.View.LibraryView) this);
        presenter.loadFavoriteBooks();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        showPopupButton = view.findViewById(R.id.show_popup_button);
        showPopupButton.setOnClickListener(v -> showSearchPopup());
        int userId = SharedPreferencesUtil.getUserId(view.getContext());
        Log.d("idcheck", "onCreateView: " + userId);

        //slide show yeeu thich
        viewPager2 = view.findViewById(R.id.viewpager);
        pageView = view.findViewById(R.id.pageView);
        presenter = new BookPresenter(getContext(), (BookContract.View.LibraryView) this);
        presenter.loadFavoriteBooks();

        return view;
    }


    private void showSearchPopup() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBooks = view.findViewById(R.id.rcvBook);
        btnLine = view.findViewById(R.id.btnLine);
        btnGrid = view.findViewById(R.id.btnGrid);


        rvBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    if (dy > 0) {
                        mainActivity.hideBottomNavigationView();
                    }
                    if (dy < 0) {
                        mainActivity.showBottomNavigationView();
                    }
                }
            }
        });

        if (getContext() != null) {
            presenter = new BookPresenter(getContext(), (BookContract.View.LibraryView) this);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            rvBooks.addItemDecoration(itemDecoration);

            presenter.loadBook();
        }
        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadBook();
            }
        });
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadGridBook();
            }
        });
    }

    @Override
    public void displayBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rvBooks.setLayoutManager(gridLayoutManager);
        rvBooks.setAdapter(new BookAdapter(list, 0));
    }

    public void displayGridBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvBooks.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvBooks.addItemDecoration(itemDecoration);
        rvBooks.setAdapter(new BookAdapter(list, 1));
    }

    @Override
    public void displayFavoriteBook(List<Book> newBooks) {

        if (newBooks == null || newBooks.isEmpty()) {
            pageView.setVisibility(View.GONE);
        } else {
            pageView.setVisibility(View.VISIBLE);
            viewPager2.setPageTransformer(new CarouselPageTransformer());
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);

            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            recyclerView.setPadding(400, 0, 400, 0);  // Điều chỉnh padding cho phù hợp
            recyclerView.setClipToPadding(false);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            viewPager2.setAdapter(new BookFavAdapter(newBooks, viewPager2));
        }
    }


    public class CarouselPageTransformer implements ViewPager2.PageTransformer {
            private static final float MIN_SCALE = 0.55f;  // Tỷ lệ nhỏ nhất cho các trang bên cạnh
            private static final float MIN_ALPHA = 0.5f;
            private static final float TRANSLATION_FACTOR = 60f;  // Yếu tố dịch chuyển

            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < -1) { // [-Infinity,-1)
                    page.setAlpha(0f);
                } else if (position <= 1) { // [-1,1]
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float translationX = position * -page.getWidth() / TRANSLATION_FACTOR;
                    page.setTranslationX(translationX);
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else { // (1,+Infinity]
                    page.setAlpha(0f);
                }
            }
        }



        @Override
    public void showSuccess(String mess) {
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String mess) {
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
    }


}
//Add data BOOKS
//            presenter.addBook(R.drawable.tuay, "Từ Ấy ", " Tố Hữu ", "Thơ");
//            presenter.addBook(R.drawable.baybuocmuahe, "Bảy bước đến mùa hè", " Nguyễn Nhật Ánh ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.chotoixinmotvedituoitho748382, "Cho tôi xin một vé đi tổi thơ", " Nguyễn Nhật Ánh ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.kinhvanhoa, "Kính Vạn Hoa", " Nguyễn Nhật Ánh ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.langmanrong, "Làng Mặn Rồng", "Phạm Công Thiện", "Truyện ngắn");
//            presenter.addBook(R.drawable.nguoicongvat, "Người Cầm Vật", "Nguyễn Huy Thiệp", "Tiểu luận");
//            presenter.addBook(R.drawable.catbui, "Cát Bụi", "Trần Đăng Khoa", "Hồi kí");
//            presenter.addBook(R.drawable.chidau, "Chị Dậu", "Ngô Tất Tố", "Tiểu thuyết");
//            presenter.addBook(R.drawable.dongque, "Đồng Quê", "Nguyễn Khuyến", "Thơ");
//            presenter.addBook(R.drawable.doanchieulang, "Đoạn Chiều Lặng", "Bùi Giáng", "Thơ");
//            presenter.addBook(R.drawable.chuyencuahang, "Chuyện Của Hằng", "Nguyễn Nhật Ánh", "Truyện ngắn");
//            presenter.addBook(R.drawable.thoixavang, "Thời xa vắng", "Lê Lựu", "Hồi kí");
//            presenter.addBook(R.drawable.chuyenlanthoai, "Chuyện Lân Thoại", "Nam Cao", "Tiểu luận");
//            presenter.addBook(R.drawable.chipheo, " Chí Phèo", "Nam Cao ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.giolong, " Gió Lộng", " Nam Cao", "Thơ");
//            presenter.addBook(R.drawable.cogaidentuhomqua, "Cô gái đến từ hôm qua", " Nguyễn Nhật Ánh ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.hoavangcoxanh, "Tôi thấy hoa vàng trên cỏ xanh ", " Nguyễn Nhật Ánh ", "Tiểu thuyết");
//            presenter.addBook(R.drawable.bongmatvang, "Bóng mắt vàng", "Nam Cao", "Tiểu luận");
//            presenter.addBook(R.drawable.chuyenxomcau, "Chuyện Xóm Cầu Mới", "Nguyễn Quang Sáng", "Tiểu thuyết");
//            presenter.addBook(R.drawable.truyencuoi, "Truyện Cười Dân Gian", "Vũ Trọng Phụng", "Truyện ngắn");
//            presenter.addBook(R.drawable.giaothua, "Giao Thừa", "Nguyễn Ngọc Tư", "Hồi kí");
//            presenter.addBook(R.drawable.doivanhong, "Đời Vẫn Hồng", "Xuân Diệu", "Thơ");
//
//    private void showSearchPopup() {
//        Dialog searchDialog = new Dialog(this.getContext());
//        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        searchDialog.setContentView(R.layout.activity_popup_search);
//
//
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(searchDialog.getWindow().getAttributes());
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        searchDialog.getWindow().setAttributes(layoutParams);
//
//        searchDialog.show();
//    }
//Button btnPrevious = view.findViewById(R.id.btnPrevious);
//Button btnNext = view.findViewById(R.id.btnNext);
//
//        btnPrevious.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        int previousPage = viewPager2.getCurrentItem() - 1;
//        if (previousPage >= 0) {
//            viewPager2.setCurrentItem(previousPage, true);
//        }
//    }
//});
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        int nextPage = viewPager2.getCurrentItem() + 1;
//        if (nextPage < viewPager2.getAdapter().getItemCount()) {
//            viewPager2.setCurrentItem(nextPage, true);
//        }
//    }
//});
