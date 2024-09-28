package com.example.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        options = Arrays.asList("Tài khoản cá nhân",  "Tìm kiếm sách", "Sách đã mượn", "Thông tin thư viện", "Thông báo của tôi","Về chúng tôi");
        adapter = new MoreAdapter(getContext(), options);
        rvMore.setAdapter(adapter);

    }
}
