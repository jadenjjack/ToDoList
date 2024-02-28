package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDataSource {
    private SQLiteDatabase database;
    private TaskDBHelper dbHelper;

    public TaskDataSource(Context context) {
        dbHelper = new TaskDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean addTask(Task t) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", t.getSubject());
            initialValues.put("item", t.getTask());
            initialValues.put("priority", t.getPriority());
            initialValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));

            didSucceed = database.insert("tasks", null, initialValues) > 0;
        }
        catch (Exception e) {
            // Do nothing - will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateTask (Task t) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) t.getTaskID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", t.getSubject());
            updateValues.put("item", t.getTask());
            updateValues.put("priority", t.getPriority());
            updateValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));

            didSucceed = database.update("tasks", updateValues, "_id=" + rowId, null) > 0;

        } catch (Exception e) {
            // Do nothing, will return false if there is an exemption
        }
        return didSucceed;
    }

    public int getLastTaskID() {
        int lastId;
        try {
            String query = "Select MAX(_id) from tasks";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Task> getTasks(String sortField, String sortOrder) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            String query = "SELECT * FROM tasks ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Task newTask;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newTask = new Task();
                newTask.setTaskID(cursor.getInt(0));
                newTask.setSubject(cursor.getString(1));
                newTask.setTask(cursor.getString(2));
                newTask.setPriority(cursor.getString(3));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
                newTask.setDueDate(calendar);
                tasks.add(newTask);

                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public Task getSpecificTask(int taskID) {
        Task task = new Task();
        String query = "SELECT * FROM tasks WHERE _id=" + taskID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            task.setTaskID(cursor.getInt(0));
            task.setSubject(cursor.getString(1));
            task.setTask(cursor.getString(2));
            task.setPriority(cursor.getString(3));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
            task.setDueDate(calendar);

            cursor.close();
        }
        return task;
    }

    public boolean deleteTask(int taskID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("tasks", "_id=" + taskID, null) > 0;
        }
        catch(Exception e) {
            // Do nothing return value already set to false
        }
        return didDelete;
    }
}
