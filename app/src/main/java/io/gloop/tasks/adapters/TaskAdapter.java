package io.gloop.tasks.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import io.gloop.GloopList;
import io.gloop.GloopOnChangeListener;
import io.gloop.tasks.R;
import io.gloop.tasks.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView itemNameTextView;
        ImageView imageView;
        ImageView privateIcon;
        ImageView checkIcon;

        ItemViewHolder(View view) {
            super(view);
            this.relativeLayout = view.findViewById(R.id.rl_item_list);
            this.itemNameTextView = view.findViewById(R.id.task_item_name);
            this.imageView = view.findViewById(R.id.item_circular_view);
            this.checkIcon = view.findViewById(R.id.check_icon);
            this.privateIcon = view.findViewById(R.id.private_icon);
            view.setClickable(true);
        }
    }

    private GloopList<Task> taskList;

    public TaskAdapter(GloopList<Task> taskList) {
        this.taskList = taskList;
        this.taskList.addOnChangeListener(new GloopOnChangeListener() {

            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public TaskAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Task item = this.taskList.get(position);

        holder.itemNameTextView.setText(item.getDescription());

        String firstLetter = "NULL";
        if (item.getDescription() != null && item.getDescription().length() > 0) {
            firstLetter = item.getDescription().substring(0, 1).toUpperCase();
        }


        setItemSelected(holder, item.isDone(), firstLetter);

        // tODO check if owner is a group
//        if (item.getOwner() != null )
//            if (!item.getOwner().isGroup())
//                holder.privateIcon.setVisibility(View.VISIBLE);

        final String finalFirstLetter = firstLetter;
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean itemDone = item.isDone();
                item.setDone(!itemDone);
                item.save();
                setItemSelected(holder, !itemDone, finalFirstLetter);
            }
        });
    }

    private void setItemSelected(ItemViewHolder holder, boolean done, String firstLetter) {
        if (!done) {
            holder.imageView.setImageDrawable(TextDrawable.builder().buildRound(firstLetter, ColorGenerator.MATERIAL.getColor(firstLetter)));
            holder.checkIcon.setVisibility(View.GONE);
        } else {
            holder.imageView.setImageDrawable(TextDrawable.builder().buildRound("", R.color.grey_500));
            holder.checkIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

}