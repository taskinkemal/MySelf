package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_tasks_pager.*

import com.keplersegg.myself.R
import android.view.Menu
import com.keplersegg.myself.Adapters.TaskPagerAdapter
import java.text.DateFormatSymbols
import java.util.*
import android.view.MenuInflater
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class TasksPagerFragment : MasterFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_tasks_pager

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i: Int in 0 .. TaskPagerAdapter.NumOfTabs - 3) {

            val title = dateToString(TaskPagerAdapter.NumOfTabs - i)
            tabLayout.addTab(tabLayout.newTab().setText(title))
        }
        tabLayout.addTab(tabLayout.newTab().setText("Yesterday"))
        tabLayout.addTab(tabLayout.newTab().setText("Today"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TaskPagerAdapter(fragmentManager!!) // activity!!.supportFragmentManager)
        var viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        fabAdd.setOnClickListener { activity!!.NavigateFragment(true, AddTaskFragment.newInstance(0)) }
    }

    private fun dateToString(days: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -days)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        return DateFormatSymbols().getShortWeekdays()[dayOfWeek]
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_tasks)

        viewPager.currentItem = TaskPagerAdapter.NumOfTabs - 1
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_tasks, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_AddTask -> activity!!.NavigateFragment(true, AddTaskFragment.newInstance(0))
        }
        return true

    }

    companion object {

        fun newInstance(): TasksPagerFragment {
            return TasksPagerFragment()
        }
    }
}
