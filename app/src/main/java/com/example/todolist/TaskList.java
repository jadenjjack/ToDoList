package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TaskList extends AppCompatActivity {
    private TaskAdapter ta;
    private ArrayList<Task> tasks;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int taskID = tasks.get(position).getTaskID();
            Intent intent = new Intent(TaskList.this, MainActivity.class);
            intent.putExtra("taskID", taskID);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        initNavbarButtons();
        initAddTaskButton();
        initDeleteSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyTaskListPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("MyTaskListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        TaskDataSource ds = new TaskDataSource(this);

        try {
            ds.open();
            tasks = ds.getTasks(sortBy, sortOrder);
            ds.close();
            if (tasks.size() > 0) {
                RecyclerView contactList = findViewById(R.id.rvTask);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
                contactList.setLayoutManager(lm);
                ta = new TaskAdapter(tasks, TaskList.this );
                contactList.setAdapter(ta);
                ta.setOnItemClickListener(onItemClickListener);
            }
            else {
                Intent intent = new Intent(TaskList.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving tasks", Toast.LENGTH_LONG).show();
        }
    }

    private void initNavbarButtons() {
        ImageButton taskListButton = findViewById(R.id.listButtonL);
        ImageButton addButton = findViewById(R.id.addButtonL);
        ImageButton settingsButton = findViewById(R.id.settingsButtonL);

        taskListButton.setEnabled(false);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TaskList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TaskList.this, TaskSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });
    }

    private void initAddTaskButton() {
        Button newConTact = findViewById(R.id.addBtn);
        newConTact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskList.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDeleteTask);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                ta.setDelete(status);
                ta.notifyDataSetChanged();
            }
        });
    }

}
