package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class TaskSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initNavbarButtons();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initNavbarButtons() {
        ImageButton taskListButton = findViewById(R.id.listButtonS);
        ImageButton newTaskButton = findViewById(R.id.addButtonS);
        ImageButton settingsButton = findViewById(R.id.settingsButtonS);

        settingsButton.setEnabled(false);
        taskListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TaskSettings.this, TaskList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(TaskSettings.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });
    }


    private void initSettings() {

        String sortBy = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortfield","subject");
        String sortOrder = getSharedPreferences("MyTaskListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton subjectRadio = findViewById(R.id.subjectRadio);
        RadioButton priorityRadio = findViewById(R.id.priorityRadio);
        RadioButton dueDateRadio = findViewById(R.id.dueDateRadio);

        if (sortBy.equalsIgnoreCase("subject")) {
            subjectRadio.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("priority")) {
            priorityRadio.setChecked(true);
        } else {
            dueDateRadio.setChecked(true);
        }

        RadioButton ascendingRadio = findViewById(R.id.ascendingRadio);
        RadioButton descendingRadio =findViewById(R.id.descendingRadio);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            ascendingRadio.setChecked(true);
        } else {
            descendingRadio.setChecked(true);
        }
    }


    private void initSortByClick() {

        RadioGroup rgSortBy = findViewById(R.id.sortByRadioGroup);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton nameRadio = findViewById(R.id.subjectRadio);
                RadioButton cityRadio = findViewById(R.id.priorityRadio);
                if (nameRadio.isChecked()) {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "subject").apply();
                } else if (cityRadio.isChecked()) {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield","priority").apply();
                } else {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "dueDate").apply();
                }
            }
        });
    }


    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.orderByRadioGroup);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton ascendingRadio = findViewById(R.id.ascendingRadio);
                if (ascendingRadio.isChecked()) {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                } else {
                    getSharedPreferences("MyTaskListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }
            }
        });
    }

}
