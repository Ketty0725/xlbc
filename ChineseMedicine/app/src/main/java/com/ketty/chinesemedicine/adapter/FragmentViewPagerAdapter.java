package com.ketty.chinesemedicine.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2022/5/30 - 20:08
 * @version: 1.0
 */
public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragments;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setItemViewCacheSize(getItemCount());
    }

    public FragmentViewPagerAdapter(@NonNull FragmentManager fm, @NonNull Lifecycle lifecycle, List<Fragment> fragmentList) {
        super(fm,lifecycle);
        this.mFragments = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }

}
