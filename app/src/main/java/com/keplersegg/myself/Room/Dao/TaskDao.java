package com.keplersegg.myself.Room.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.keplersegg.myself.Room.Entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    List<Task> getAll();

    @Query("SELECT * FROM Task where TaskId = :id")
    Task get(int id);

    @Query("SELECT count(0) FROM Task where Label = :label")
    int getCountByLabel(String label);

    @Query("SELECT * FROM Task where TaskId <> :taskId and Label = :label")
    int getCountByLabelExcludeId(int taskId, String label);

    @Query("SELECT COUNT(0) from Task")
    int getCount();

    @Insert
    void insert(Task task);

    @Insert
    void insertAll(Task... tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
