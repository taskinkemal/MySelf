package com.keplersegg.myself.Room.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.keplersegg.myself.Room.Entity.Entry;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM Entry")
    List<Entry> getAll();

    @Query("SELECT * FROM Entry where Day = :day and TaskId = :taskId")
    Entry get(int day, int taskId);

    @Query("SELECT COUNT(0) from Entry")
    int getCount();

    @Insert
    void insertAll(Entry... entries);

    @Delete
    void delete(Entry entry);
}
