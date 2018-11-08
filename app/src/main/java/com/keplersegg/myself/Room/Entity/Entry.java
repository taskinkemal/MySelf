package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;

import com.keplersegg.myself.Room.Converter.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys={"Day", "TaskId"},
        foreignKeys={@ForeignKey(
                onDelete = CASCADE,
                onUpdate = CASCADE,
                entity = Task.class,
                parentColumns = "Id",childColumns = "TaskId")
        },
        indices = {@Index("TaskId")})
public class Entry {

    public int Day;
    public int TaskId;
    public int Value;
    @TypeConverters({DateConverter.class})
    public Date ModificationDate;

    public int getDay() { return this.Day; }
    public int getTaskId() { return this.TaskId; }
    public int getValue() { return this.Value; }
    public Date getModificationDate() { return this.ModificationDate; }

    public void setDay(int day) { this.Day = day; }
    public void setTaskId(int taskId) { this.TaskId = taskId; }
    public void setValue(int value) { this.Value = value; }
    public void setModificationDate(Date modificationDate) { this.ModificationDate = modificationDate; }

    public static Entry CreateItem(int day, int taskId) {

        Entry m = new Entry();

        m.setDay(day);
        m.setTaskId(taskId);
        m.setValue(0);
        m.setModificationDate(new Date(System.currentTimeMillis()));

        return m;
    }
}
