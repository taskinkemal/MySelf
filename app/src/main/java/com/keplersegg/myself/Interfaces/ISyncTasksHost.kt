package com.keplersegg.myself.Interfaces

import com.keplersegg.myself.Room.AppDatabase

interface ISyncTasksHost : IHttpProvider {

    fun onSyncTasksSuccess()

    fun AppDB() : AppDatabase
}
