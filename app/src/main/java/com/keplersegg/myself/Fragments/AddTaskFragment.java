package com.keplersegg.myself.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.keplersegg.myself.Activities.MainActivity;
import com.keplersegg.myself.Async.GetTasks;
import com.keplersegg.myself.Async.RunAsync;
import com.keplersegg.myself.R;
import com.keplersegg.myself.Room.Entity.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class AddTaskFragment extends MasterFragment {

    public int TaskId = -1; // -1 means new task.
    private Task task = null;

    private EditText txtLabel;
    private Switch swcUnits;
    private LinearLayout lytUnits;
    private EditText txtUnits;
    private Switch swcHasGoal;
    private LinearLayout lytGoal;
    private EditText txtGoal;
    private TextView lblUnits;
    private Spinner spnGoalMinMax;
    private Spinner spnGoalTimeFrame;

    public AddTaskFragment() { }

    public static AddTaskFragment newInstance(int taskId) {

        AddTaskFragment fragment = new AddTaskFragment();

        fragment.TaskId = taskId;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
            activity = (MainActivity) rootView.getContext();

            txtLabel = rootView.findViewById(R.id.txtLabel);
            swcUnits = rootView.findViewById(R.id.swcUnits);
            lytUnits = rootView.findViewById(R.id.lytUnits);
            txtUnits = rootView.findViewById(R.id.txtUnits);
            swcHasGoal = rootView.findViewById(R.id.swcHasGoal);
            lytGoal = rootView.findViewById(R.id.lytGoal);
            txtGoal = rootView.findViewById(R.id.txtGoal);
            lblUnits = rootView.findViewById(R.id.lblUnits);
            spnGoalMinMax = rootView.findViewById(R.id.spnGoalMinMax);
            spnGoalTimeFrame = rootView.findViewById(R.id.spnGoalTimeFrame);
            Button btnSave = rootView.findViewById(R.id.btnSave);

            txtUnits.addTextChangedListener(new InputTextWatcher());

            swcUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SetUnitsVisibility(isChecked);
                    SetUnitsLabel(isChecked, txtUnits.getText().toString());
                }
            });

            swcHasGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SetGoalVisibility(isChecked);
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String label = txtLabel.getText().toString();
                    final int dataType = swcUnits.isChecked() ? 1 : 0;
                    final String unit = dataType == 1 ? txtUnits.getText().toString() : "";
                    final Boolean hasGoal = swcHasGoal.isChecked();
                    final int goal = hasGoal ? ConvertToInteger(txtGoal.getText().toString()) : 0;
                    final int goalMinMax = hasGoal ? (int)spnGoalMinMax.getSelectedItemId() : 0;
                    final int goalTimeFrame = hasGoal ? (int)spnGoalTimeFrame.getSelectedItemId() : 0;

                    if (label.length() > 0 &&
                            (dataType == 0 || unit.length() > 0) &&
                            goal >= 0 &&
                            (!hasGoal || (goalMinMax >= 1 && goalMinMax <= 3 && goalTimeFrame >= 1 && goalTimeFrame <= 3))) {

                        
                        new RunAsync(activity)
                        {
                            @Override
                            public void OnSuccess() {
                                activity.NavigateFragment(true, TasksFragment.newInstance());
                            }
                        }.Execute(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {

                                if (TaskId > -1) {

                                    if (activity.AppDB().taskDao().getCountByLabelExcludeId(TaskId, label) == 0) {

                                        task.setLabel(label);
                                        task.setDataType(dataType);
                                        task.setUnit(unit);
                                        task.setHasGoal(hasGoal);
                                        task.setGoal(goal);
                                        task.setGoalMinMax(goalMinMax);
                                        task.setGoalTimeFrame(goalTimeFrame);

                                        activity.AppDB().taskDao().update(task);

                                        return true;
                                    }
                                }
                                else {

                                    if (activity.AppDB().taskDao().getCountByLabel(label) == 0) {

                                        final Task newTask = Task.CreateItem(label, dataType, unit, hasGoal, goal, goalMinMax, goalTimeFrame);

                                        activity.AppDB().taskDao().insert(newTask);

                                        return true;
                                    }
                                }

                                return false;
                            }
                        });
                    }
                }
            });
        }

        if (TaskId > -1) {

            task = activity.AppDB().taskDao().get(TaskId);

            if (task != null) {

                txtLabel.setText(task.getLabel());
                swcUnits.setChecked(task.getDataType() == 1);
                SetUnitsVisibility(task.getDataType() == 1);
                txtUnits.setText(task.getUnit());
                swcHasGoal.setChecked(task.getHasGoal());
                SetGoalVisibility(task.getHasGoal());
                txtGoal.setText(String.format(Locale.getDefault(), "%d", task.getGoal()));
                String unit = task.getDataType() == 0 ? task.getUnit() : "";
                lblUnits.setText(unit != null && unit.length() > 0 ? unit : "items");
                if (task.getGoalMinMax() > 0) {

                    int position = Arrays.asList(activity.getResources().getStringArray(R.array.spinner_goalMinMax_values)).indexOf(String.format(Locale.getDefault(), "%d", task.getGoalMinMax()));

                    spnGoalMinMax.setSelection(position);
                }

                if (task.getGoalTimeFrame() > 0) {

                    int position = Arrays.asList(activity.getResources().getStringArray(R.array.spinner_goalTimeFrame_values)).indexOf(String.format(Locale.getDefault(), "%d", task.getGoalTimeFrame()));

                    spnGoalMinMax.setSelection(position);
                }
            }
        }
        else {

            SetUnitsVisibility(false);
            SetGoalVisibility(false);
        }

        return rootView;
    }

    private int ConvertToInteger(String text) {

        int result = 0;

        try { result = Integer.parseInt(text); }
        catch(NumberFormatException nfe) { }

        return result;
    }

    private void SetUnitsVisibility(boolean isVisible) {

        lytUnits.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void SetGoalVisibility(boolean isVisible) {

        lytGoal.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        spnGoalMinMax.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        spnGoalTimeFrame.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void SetUnitsLabel(boolean isUnitsSelected, String text) {

        String result = isUnitsSelected && text != null && text.length() > 0 ? text : "";

        lblUnits.setText(result.length() > 0 ? result : "items");
    }

    @Override
    public void onResume() {
        super.onResume();
        SetTitle(R.string.lbl_task_details);
    }

    private class InputTextWatcher implements TextWatcher {

        private InputTextWatcher() {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            SetUnitsLabel(swcUnits.isChecked(), s.toString());
        }
    }
}
