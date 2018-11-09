package com.keplersegg.myself.Fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.keplersegg.myself.Activities.MainActivity
import com.keplersegg.myself.Interfaces.IErrorMessage
import com.keplersegg.myself.Room.AppDatabase
import java.lang.Exception

open class MasterFragment : android.support.v4.app.Fragment(), IErrorMessage {

    var activity: MainActivity? = null
    protected var rootView: View? = null
    protected var layout: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {

            rootView = inflater.inflate(layout, container, false)
            activity = rootView!!.context as MainActivity

            onCreateViewInternal()
        }

        return rootView
    }

    open fun onCreateViewInternal() { }

    fun SetTitle(id: Int) {

        activity!!.setTitle(id)
    }

    fun SetTitle(title: String) {

        activity!!.title = title
    }

    fun getConnectivityManager(): ConnectivityManager {
        return activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun getAppDB(): AppDatabase { return activity!!.AppDB() }

    fun getDeviceId(): String? { return activity!!.getDeviceId() }

    fun getAccessToken(): String? { return activity!!.application.dataStore.getAccessToken() }

    override fun logException(exception: Exception, message: String) {

        //CrashLogger.AddExceptionLog(message, exc);

        activity!!.showErrorMessage(message)
    }

    override fun showErrorMessage(message: String) {

        activity!!.showErrorMessage(message)
    }
}
