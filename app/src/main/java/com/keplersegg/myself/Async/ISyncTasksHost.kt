package com.keplersegg.myself.Async

import com.keplersegg.myself.Room.AppDatabase

interface ISyncTasksHost : IHttpProvider {

    fun onSyncTasksSuccess()

    fun AppDB() : AppDatabase
}
