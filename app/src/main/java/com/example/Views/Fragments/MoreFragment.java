package com.example.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.OnClickContracts;
import com.example.Views.Activitys.AccountActivity;
import com.example.Views.Activitys.BorrowedBooksActivity;
import com.example.Views.Activitys.CartActivity;
import com.example.Views.Activitys.LibraryInforActivity;
import com.example.Views.Activitys.NotificationActivity;
import com.example.Views.Activitys.SearchActivity;
import com.example.Views.Adapters.MoreAdapter;
import com.example.btl_libary.R;

import java.util.Arrays;
import java.util.List;

public class MoreFragment extends Fragment {
    private List<String> options;
    private MoreAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvMore = view.findViewById(R.id.rvMore);
        rvMore.setLayoutManager(new LinearLayoutManager(getContext()));
        options = Arrays.asList("Tài khoản cá nhân", "Tìm kiếm sách","Giỏ sách chờ mượn", "Sách đã mượn", "Thông tin thư viện", "Thông báo của tôi");
        adapter = new MoreAdapter(getContext(), options);
        adapter.setOnItemClickListener(new OnClickContracts.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), AccountActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), SearchActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getContext(), CartActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getContext(), BorrowedBooksActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getContext(), LibraryInforActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getContext(), NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        break;
                }
            }
        });
        rvMore.setAdapter(adapter);
    }
}
