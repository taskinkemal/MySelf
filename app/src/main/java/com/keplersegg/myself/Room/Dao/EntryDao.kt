package com.keplersegg.myself.Room.Dao

import com.keplersegg.myself.Room.Entity.Entry

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EntryDao {

    @get:Query("SELECT * FROM Entry")
    val all: List<Entry>

    @get:Query("SELECT COUNT(0) from Entry")
    val count: Int

    @Query("SELECT * FROM Entry where Day = :day and TaskId = :taskId")
    operator fun get(day: Int, taskId: Int): Entry?

    @Insert
    fun insert(entry: Entry)

    @Update
    fun update(entry: Entry)

    @Delete
    fun delete(entry: Entry)

    @Query("SELECT TaskId, sum(Value) AS EntryCount FROM Entry group by TaskId")
    fun getCounts(): List<TaskEntryCount>

    class TaskEntryCount {
        var TaskId: Int? = null
        var EntryCount: Int? = null
    }
}
