package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Entry {

    @PrimaryKey
    private int Day;
    @PrimaryKey
    private int TaskId;
    private int Value;

    public int getDay() { return this.Day; }
    public int getTaskId() { return this.TaskId; }
    public int getValue() { return this.Value; }

    public void setDay(int day) { this.Day = day; }
    public void setTaskId(int taskId) { this.TaskId = taskId; }
    public void setValue(int value) { this.Value = value; }

    public static Entry CreateItem(int day, int taskId) {

        Entry m = new Entry();
        m.Day = day;
        m.TaskId = taskId;

        return m;
    }
}
