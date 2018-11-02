package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;

@Entity(primaryKeys={"Day", "TaskId"})
public class Entry {

    public int Day;
    public int TaskId;
    public int Value;

    public int getDay() { return this.Day; }
    public int getTaskId() { return this.TaskId; }
    public int getValue() { return this.Value; }

    public void setDay(int day) { this.Day = day; }
    public void setTaskId(int taskId) { this.TaskId = taskId; }
    public void setValue(int value) { this.Value = value; }

    public static Entry CreateItem(int day, int taskId) {

        Entry m = new Entry();

        m.setDay(day);
        m.setTaskId(taskId);
        m.setValue(0);

        return m;
    }
}
