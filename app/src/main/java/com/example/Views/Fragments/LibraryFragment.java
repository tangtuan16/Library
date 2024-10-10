package com.example.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.BookContract;
import com.example.Models.Book;
import com.example.Models.BookModel;
import com.example.Presenters.BookPresenter;
import com.example.Views.Activitys.MainActivity;
import com.example.Views.Adapters.BookAdapter;
import com.example.btl_libary.R;

import java.util.List;

public class LibraryFragment extends Fragment implements BookContract.View.LibraryView {
    private RecyclerView rvBooks;
    private BookPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.library_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBooks = view.findViewById(R.id.rcvBook);
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

            presenter.loadBook();
        }
    }


    @Override
    public void displayBook(List<Book> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rvBooks.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvBooks.addItemDecoration(itemDecoration);
        rvBooks.setAdapter(new BookAdapter(list));
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
