package com.keplersegg.myself.Room

import android.content.Context

import com.keplersegg.myself.Room.Dao.EntryDao
import com.keplersegg.myself.Room.Dao.TaskDao
import com.keplersegg.myself.Room.Dao.TaskEntryDao
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.Task
import com.keplersegg.myself.Room.Entity.TaskEntry

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Task::class, Entry::class, TaskEntry::class), version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun entryDao(): EntryDao
    abstract fun taskEntryDao(): TaskEntryDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {

            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, "myself-database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
