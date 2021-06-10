package com.example.appfinal.UI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appfinal.R;
import com.example.appfinal.UI.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        ViewPager2 viewPager = rootView.findViewById(R.id.vp_fav);
        viewPager.setAdapter(new ViewPagerAdapter(getActivity()));

        TabLayout tabLayout = rootView.findViewById(R.id.tab_fav);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();


        (tabLayout.getTabAt(0)).setText("Movie");
        (tabLayout.getTabAt(1)).setText("TV Show");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }


}