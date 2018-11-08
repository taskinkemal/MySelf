package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;

@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class TaskEntry {

    @Embedded
    private Entry entry;

    @Embedded
    private Task task;

    @PrimaryKey(autoGenerate = true)
    private int TaskEntryId;

    public int getTaskEntryId() { return TaskEntryId; }
    public Entry getEntry() { return this.entry; }
    public Task getTask() { return this.task; }

    public void setTaskEntryId(int taskEntryId) { this.TaskEntryId = taskEntryId; }
    public void setEntry(Entry entry) { this.entry = entry; }
    public void setTask(Task task) { this.task = task; }
}
