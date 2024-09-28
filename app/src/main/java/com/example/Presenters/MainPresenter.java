package com.example.Presenters;

import androidx.fragment.app.Fragment;

import com.example.Contracts.MainContract;
import com.example.Views.Fragments.AccountFragment;
import com.example.Views.Fragments.HomeFragment;
import com.example.Views.Fragments.LibraryFragment;
import com.example.Views.Fragments.MoreFragment;
import com.example.btl_libary.R;

import java.util.HashMap;
import java.util.Map;

public class MainPresenter implements MainContract.Presenter {
    public Map<Integer, Class<? extends Fragment>> loadMap() {
        Map<Integer, Class<? extends Fragment>> fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.navigation_home, HomeFragment.class);
        fragmentMap.put(R.id.navigation_library, LibraryFragment.class);
        fragmentMap.put(R.id.navigation_account, AccountFragment.class);
        fragmentMap.put(R.id.navigation_more, MoreFragment.class);
        return fragmentMap;
    }
}
