package com.ntxq.btl_ptudpm.views.zodiac;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ntxq.btl_ptudpm.R;
import com.ntxq.btl_ptudpm.ViewPagerFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ZodiacFragment extends Fragment {

    private ViewPager pager;
    private TabLayout tab;
    private ViewPagerFragmentAdapter pagerAdapter;
    private List<Fragment> listFragment;

    private ZodiacAstrologyFragment zodiacAstrologyFragment= new ZodiacAstrologyFragment();
    private ZodiacInfoFragment zodiacInfoFragment= new ZodiacInfoFragment();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zodiac, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        listFragment= new ArrayList<>();
        listFragment.add(zodiacAstrologyFragment);
        listFragment.add(zodiacInfoFragment);

        pager= getActivity().findViewById(R.id.viewPager_zodiac);
        tab= getActivity().findViewById(R.id.tabLayout_zodiac);
        pagerAdapter= new ViewPagerFragmentAdapter(getChildFragmentManager(), listFragment);
        pager.setAdapter(pagerAdapter);
        pager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pager.setOffscreenPageLimit(listFragment.size());
        tab.setupWithViewPager(pager, false);
        pager.setCurrentItem(0);
    }
}
