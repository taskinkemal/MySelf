package com.keplersegg.myself.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.keplersegg.myself.Fragments.TasksFragment;

import java.util.Calendar;

public class TaskPagerAdapter extends FragmentStatePagerAdapter {

    public static int NumOfTabs = 5;

    public TaskPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        int today = Calendar.getInstance().get(Calendar.DATE);

        int day = today - (NumOfTabs-1) + position;

        return TasksFragment.Companion.newInstance(day);
    }

    @Override
    public int getCount() {
        return NumOfTabs;
    }
}
