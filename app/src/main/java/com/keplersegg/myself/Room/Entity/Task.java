package com.keplersegg.myself.Room.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey
    public int Id;
    public String Label;
    public int DataType;
    public String Unit;
    public Boolean HasGoal;
    public int GoalMinMax;
    public int Goal;
    public int GoalTimeFrame;

    public int getId() { return this.Id; }
    public String getLabel() { return this.Label; }
    public int getDataType() { return this.DataType; }
    public String getUnit() { return this.Unit; }
    public Boolean getHasGoal() { return this.HasGoal; }
    public int getGoal() { return this.Goal; }
    public int getGoalMinMax() { return this.GoalMinMax; }
    public int getGoalTimeFrame() { return this.GoalTimeFrame; }

    public void setId(int id) { this.Id = id; }
    public void setLabel(String label) { this.Label = label; }
    public void setDataType(int dataType) { this.DataType = dataType; }
    public void setUnit(String unit) { this.Unit = unit; }
    public void setHasGoal(Boolean hasGoal) { this.HasGoal = hasGoal; }
    public void setGoal(int goal) { this.Goal = goal; }
    public void setGoalMinMax(int goalMinMax) { this.GoalMinMax = goalMinMax; }
    public void setGoalTimeFrame(int goalTimeFrame) { this.GoalTimeFrame = goalTimeFrame; }

    public static Task CreateItem(int id, String label, int dataType, String unit, Boolean hasGoal, int goal, int goalMinMax, int goalTimeFrame) {

        Task m = new Task();

        m.setId(id);
        m.setLabel(label);
        m.setDataType(dataType);
        m.setUnit(unit);
        m.setHasGoal(hasGoal);
        m.setGoal(goal);
        m.setGoalMinMax(goalMinMax);
        m.setGoalTimeFrame(goalTimeFrame);

        return m;
    }
}
