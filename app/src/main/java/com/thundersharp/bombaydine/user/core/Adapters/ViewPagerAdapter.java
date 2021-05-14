package com.thundersharp.bombaydine.user.core.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    int noofTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int nofotabs) {
        super(fm);
        this.noofTabs = nofotabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return noofTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return stringList.get(position);
    }

    public void addFragment(Fragment fragmentnew, String string){
        fragments.add(fragmentnew);
        stringList.add(string);
    }
}
