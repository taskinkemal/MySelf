package com.keplersegg.myself.Adapters


import com.keplersegg.myself.Fragments.TasksFragment
import com.keplersegg.myself.Helper.Utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TaskPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val today = Utils.getToday()

        val day = today - (NumOfTabs - 1) + position

        return TasksFragment.newInstance(day)
    }

    override fun getCount(): Int {
        return NumOfTabs
    }

    companion object {

        var NumOfTabs = 4
    }
}
