package com.keplersegg.myself.interfaces

import com.keplersegg.myself.MySelfApplication
import com.keplersegg.myself.Room.AppDatabase
import com.keplersegg.myself.Room.Entity.Goal

interface ISyncGoalsHost : IHttpProvider {

    fun onSyncGoalsSuccess(list: List<Goal>)

    fun AppDB() : AppDatabase

    fun GetApplication() : MySelfApplication
}