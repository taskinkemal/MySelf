package com.keplersegg.myself.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keplersegg.myself.Activities.MasterActivity;
import com.keplersegg.myself.R;
import com.keplersegg.myself.Room.Entity.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.DataObjectHolder> {

    private MasterActivity activity;
    private List<Task> items;

    final class DataObjectHolder extends RecyclerView.ViewHolder
    {
        final LinearLayout lytListItem;
        final TextView lblLabel;

        DataObjectHolder(View itemView) {

            super(itemView);

            lytListItem = itemView.findViewById(R.id.lytListItem);
            lblLabel = itemView.findViewById(R.id.lblLabel);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task, parent, false);

        return new DataObjectHolder(view);
    }

    public TasksAdapter(MasterActivity activity, List<Task> items) {

        this.setHasStableIds(true);
        this.activity = activity;
        this.items = items;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        // object item based on the position
        final Task item = items != null ? items.get(position) : null;

        if (item != null) {

            holder.lblLabel.setText(item.getLabel());

            setAnimation(holder.lytListItem, position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item != null) {

                }
            }
        });
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(DataObjectHolder holder) {

        super.onViewDetachedFromWindow(holder);

        if (holder.lytListItem != null)
            holder.lytListItem.clearAnimation();
    }

    @Override
    public long getItemId(int position) {

        return items.get(position).getTaskId();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}