package com.keplersegg.myself.Room.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.keplersegg.myself.Room.Entity.TaskEntry;

import java.util.List;

@Dao
public interface TaskEntryDao {

    @Query("SELECT e.*, t.*, t.Id as TaskEntryId FROM Task t LEFT OUTER JOIN Entry e ON e.TaskId = t.Id and e.Day = :day")
    List<TaskEntry> getTasks(int day);
}
