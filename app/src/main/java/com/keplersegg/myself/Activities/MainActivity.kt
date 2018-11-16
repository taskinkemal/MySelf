package com.keplersegg.myself.Activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.keplersegg.myself.Fragments.AddTaskFragment
import com.keplersegg.myself.Fragments.AutoTaskSelectorFragment
import com.keplersegg.myself.Fragments.MasterFragment
import com.keplersegg.myself.Fragments.ProfileFragment
import com.keplersegg.myself.Fragments.TasksPagerFragment
import com.keplersegg.myself.R
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationView()

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px)
        }
    }

    protected fun setupNavigationView() {

        navigation.setNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true

            lytDrawerHome.closeDrawers()

            NavigateFromMenu(menuItem.itemId)

            true
        }
    }

    override fun onResume() {
        super.onResume()

        val imgUserPicture = navigation.getHeaderView(0).findViewById<ImageView>(R.id.imgUserPicture)
        val lblNavUserName = navigation.getHeaderView(0).findViewById<TextView>(R.id.lblNavUserName)

        lblNavUserName.text = if (application!!.user != null) application!!.user!!.FirstName + " " + application!!.user!!.LastName else "Guest"

        if (application!!.user != null && application!!.user!!.PictureUrl != null && !application!!.user!!.PictureUrl!!.isEmpty()) {

            Glide.with(this)
                    .load(application!!.user!!.PictureUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24px)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        } else {

            Glide.with(this)
                    .load(R.drawable.ic_baseline_account_circle_24px)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        }
    }

    fun NavigateFromMenu(menuItemID: Int) {

        when (menuItemID) {

            R.id.action_tasks ->

                NavigateFragment(true, TasksPagerFragment.newInstance())
            R.id.action_profile ->

                NavigateFragment(true, ProfileFragment.newInstance())
            R.id.action_add_task ->

                NavigateFragment(true, AddTaskFragment.newInstance(-1))
            R.id.action_add_automated_task ->

                NavigateFragment(true, AutoTaskSelectorFragment.newInstance())
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            lytDrawerHome.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(menuItem)
    }

    fun NavigateFragment(addToBackStack: Boolean, fragment: MasterFragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(
                R.animator.fragment_enter,
                R.animator.fragment_exit,
                R.animator.fragment_pop_enter,
                R.animator.fragment_pop_exit)

        transaction.replace(R.id.fragment_frame, fragment)

        if (addToBackStack)
            transaction.addToBackStack(null)

        transaction.commit()
    }
}
