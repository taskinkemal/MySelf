package com.keplersegg.myself.Adapters


import com.keplersegg.myself.Fragments.TasksFragment
import com.keplersegg.myself.Helper.Utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class TaskPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val today = Utils.getToday()

        val day = today - (NumOfTabs - 1) + position

        return TasksFragment.newInstance(day)
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is TasksFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            `object`.notifyUpdate()
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(`object`)
    }

    override fun getCount(): Int {
        return NumOfTabs
    }

    companion object {

        var NumOfTabs = 4
    }
}
