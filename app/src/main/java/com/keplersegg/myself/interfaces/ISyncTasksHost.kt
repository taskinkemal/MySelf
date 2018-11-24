package com.keplersegg.myself.interfaces

import com.keplersegg.myself.Room.AppDatabase

interface ISyncTasksHost : IHttpProvider {

    fun onSyncTasksSuccess()

    fun AppDB() : AppDatabase
}
