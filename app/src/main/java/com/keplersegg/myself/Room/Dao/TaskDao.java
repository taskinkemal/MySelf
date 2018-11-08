package com.keplersegg.myself.Room.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.keplersegg.myself.Room.Entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    List<Task> getAll();

    @Query("SELECT * FROM Task where Id = :id")
    Task get(int id);

    @Query("SELECT count(0) FROM Task where Label = :label")
    int getCountByLabel(String label);

    @Query("SELECT * FROM Task where Id <> :taskId and Label = :label")
    int getCountByLabelExcludeId(int taskId, String label);

    @Query("SELECT COUNT(0) from Task")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM Task")
    void deleteAll();
}
