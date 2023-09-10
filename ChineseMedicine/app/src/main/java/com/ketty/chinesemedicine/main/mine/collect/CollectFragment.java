package com.ketty.chinesemedicine.main.mine.collect;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.FragmentCollectBinding;
import com.ketty.chinesemedicine.main.store.orderform.OrderFormFragment;
import com.trello.rxlifecycle2.components.support.RxFragment;

public class CollectFragment extends RxFragment {
    private FragmentCollectBinding bind;
    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentCollectBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fm = getChildFragmentManager();
        initTabLayout();
    }

    private void initTabLayout() {
        bind.tabLayout.getTabAt(0).select();
        initFragment(new CollectWithNoteFragment());

        bind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initFragment(new CollectWithNoteFragment());
                } else if (tab.getPosition() == 1) {
                    initFragment(new CollectWithProductFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragment(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }
    }

}