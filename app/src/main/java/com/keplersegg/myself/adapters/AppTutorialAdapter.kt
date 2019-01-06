package com.keplersegg.myself.adapters

import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.keplersegg.myself.R

class AppTutorialAdapter : PagerAdapter() {

    val mSize = 4

    override fun getCount(): Int {
        return mSize
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(view: ViewGroup, position: Int, `object`: Any) {
        view.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {

        val viewAppTutorial = LayoutInflater.from(view.context).inflate(R.layout.view_app_tutorial, view, false)

        setContent(viewAppTutorial, position)

        view.addView(viewAppTutorial)
        return viewAppTutorial
    }

    private fun setContent(viewAppTutorial: View, position: Int) {

        val imgTutorial = viewAppTutorial.findViewById<ImageView>(R.id.imgTutorial)
        val lblTutorialHeader = viewAppTutorial.findViewById<TextView>(R.id.lblTutorialHeader)
        val lblTutorial = viewAppTutorial.findViewById<TextView>(R.id.lblTutorial)

        when (position) {
            0 -> {

                imgTutorial.setImageResource(R.drawable.ic_badge1_1)
                lblTutorialHeader.setText("Welcome to MySelf!")
                lblTutorial.setText("MySelf will help you track your daily routines.")
            }
            1 -> {

                imgTutorial.setImageResource(R.drawable.ic_badge1_2)
                lblTutorialHeader.setText("Tasks to help track yourself.")
                lblTutorial.setText("Setup your tasks to track your daily routines. Cups of coffee, hour of exercise.")
            }
            2 -> {

                imgTutorial.setImageResource(R.drawable.ic_badge1_1)
                lblTutorialHeader.setText("Goals to help achieve the results.")
                lblTutorial.setText("Set your own goals to help achieve your results.")
            }
            3 -> {

                imgTutorial.setImageResource(R.drawable.ic_badge1_3)
                lblTutorialHeader.setText("Check your stats.")
                lblTutorial.setText("Statistics will show you your tracking history.")
            }
            else -> { }
        }
    }
}