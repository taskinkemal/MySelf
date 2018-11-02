package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int TaskId;
    private String Label;
    private int DataType;

    public int getTaskId() { return this.TaskId; }
    public String getLabel() { return this.Label; }
    public int getDataType() { return this.DataType; }

    public void setTaskId(int taskId) { this.TaskId = taskId; }
    public void setLabel(String label) { this.Label = label; }
    public void setDataType(int dataType) { this.DataType = dataType; }

    public static Task CreateItem(String label, int dataType) {

        Task m = new Task();
        m.Label = label;
        m.DataType = dataType;

        return m;
    }
}
