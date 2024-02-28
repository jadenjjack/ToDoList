package com.example.todolist;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter {
    private ArrayList<Task> taskData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;
    public TaskAdapter(ArrayList<Task> taskData, Context context) {
        this.taskData = taskData;
        parentContext = context;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSubject;
        public TextView tvDueDate;
        public TextView tvPriority;
        public Button deleteButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.textSubject);
            tvDueDate = itemView.findViewById(R.id.textDueDate);
            tvPriority = itemView.findViewById(R.id.textPriority);
            deleteButton = itemView.findViewById(R.id.deleteTaskButton);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getTvSubject() {
            return tvSubject;
        }
        public TextView getTvDueDate() {
            return tvDueDate;
        }
        public TextView getTvPriority() {
            return tvPriority;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.list_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TaskViewHolder tvh = (TaskViewHolder) holder;
        // this line updates the information on the viewHolder based on its position
        // (I'm assuming it has to be the last position)
        tvh.getTvSubject().setText(taskData.get(position).getSubject());
        tvh.getTvDueDate().setText(DateFormat.format("MM/dd/yyyy", taskData.get(position).getDueDate()));
        tvh.getTvPriority().setText(taskData.get(position).getPriority());

        if (isDeleting) {
            tvh.getDeleteButton().setVisibility(View.VISIBLE);
            tvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(holder.getAdapterPosition());
                }
            });
        }else {
            // If not deleting, hide delete button
            tvh.getDeleteButton().setVisibility(View.GONE);
            // Remove click listener to prevent any accidental clicks
            tvh.getDeleteButton().setOnClickListener(null);
        }
    }

    public void  setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Task task =taskData.get(position);
        TaskDataSource ds = new TaskDataSource(parentContext);

        try {
            ds.open();
            boolean didDelete = ds.deleteTask(task.getTaskID());
            ds.close();

            if (didDelete) {
                taskData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    // when the adapter is set this method is called
    @Override
    public int getItemCount() {
        return taskData.size();
    }
}
