package com.keplersegg.myself.Room.Dao

import com.keplersegg.myself.Room.Entity.Task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @get:Query("SELECT * FROM Task")
    val all: List<Task>

    @get:Query("SELECT case when count(0) = 0 then 1 else max(Id)+1 end FROM Task")
    val maxId: Int

    @get:Query("SELECT COUNT(0) from Task")
    val count: Int

    @Query("SELECT * FROM Task where Id = :id")
    operator fun get(id: Int): Task

    @Query("SELECT count(0) FROM Task where Label = :label")
    fun getCountByLabel(label: String): Int

    @Query("SELECT * FROM Task where Id <> :taskId and Label = :label")
    fun getCountByLabelExcludeId(taskId: Int, label: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(task: Task): Int

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM Task")
    fun deleteAll()
}
