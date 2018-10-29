package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Task {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int TaskId;
    private String Label;
    private int DataType;
    private int Value;

    public int getTaskId() { return this.TaskId; }
    public String getLabel() { return this.Label; }
    public int getDataType() { return this.DataType; }
    public int getValue() { return this.Value; }

    public void setTaskId(int taskId) { this.TaskId = taskId; }
    public void setLabel(String label) { this.Label = label; }
    public void setDataType(int dataType) { this.DataType = dataType; }
    public void setValue(int value) { this.Value = value; }

    public static Task CreateItem(String label, int dataType) {

        Task m = new Task();
        m.Label = label;
        m.DataType = dataType;

        return m;
    }
}
