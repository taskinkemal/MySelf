package com.keplersegg.myself.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.keplersegg.myself.Activities.MainActivity

open class MasterFragment : android.support.v4.app.Fragment() {

    var activity: MainActivity? = null
    protected var rootView: View? = null
    protected var layout: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {

            rootView = inflater.inflate(layout, container, false)
            activity = rootView!!.context as MainActivity
        }

        return rootView
    }

    fun SetTitle(id: Int) {

        activity!!.setTitle(id)
    }

    fun SetTitle(title: String) {

        activity!!.title = title
    }
}
