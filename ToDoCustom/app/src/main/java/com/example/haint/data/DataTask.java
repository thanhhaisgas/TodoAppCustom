package com.example.haint.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.haint.model.TaskModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by haint on 03/06/2017.
 */

public class DataTask {

    private String DATABASE_NAME="taskdata.sqlite";
    private String DB_PATH_SUFFIX="/databases/";
    private SQLiteDatabase database=null;

    private Context mContext;

    public DataTask(Context mContext) {
        this.mContext = mContext;
    }
    private String getPath(){
        return mContext.getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }

    private void copySQL(){
        try {
            InputStream inputStream = mContext.getAssets().open(DATABASE_NAME);
            String outFileName=getPath();
            File f = new File(mContext.getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
        catch (Exception e){
            Log.e("Loi sao chep: ",e.toString());
        }
    }

    private void copySQLfromAssets(){
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try{
                copySQL();
            }
            catch (Exception e){
                Log.e("Loi sao chep tu Assets:",e.toString());
            }
        }
    }

    public ArrayList<TaskModel> getTasks(ArrayList<TaskModel> temp){
        String nameTask = "";
        String contentTask = "";
        temp = new ArrayList<>();
        copySQLfromAssets();
        database = mContext.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor;
        cursor = database.rawQuery("SELECT * FROM tasktable", null);
        while (cursor.moveToNext()){
            nameTask = cursor.getString(1);
            contentTask = cursor.getString(2);
            int mDay = cursor.getInt(3);
            int mMonth = cursor.getInt(4);
            int mYear = cursor.getInt(5);
            int priorityLevel = cursor.getInt(6);
            int statusTask = cursor.getInt(7);
            int ID = cursor.getInt(0);
            temp.add(new TaskModel(ID, nameTask, contentTask, mDay, mMonth, mYear, priorityLevel, statusTask));
        }
        cursor.close();
        return temp;
    }

    public void addTask(TaskModel temp){
        copySQLfromAssets();
        database = mContext.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskName", temp.getNameTask());
        contentValues.put("ContentTask", temp.getContentTask());
        contentValues.put("Day", temp.getmDay());
        contentValues.put("Month", temp.getmMonth());
        contentValues.put("Year", temp.getmYear());
        contentValues.put("PriorityLevel", temp.getPriorityLevel());
        contentValues.put("StatusTask", temp.getStatusTask());
        database.insert("tasktable", null, contentValues);
    }

    public TaskModel getTask(TaskModel temp, int IDc){
        String nameTask = "";
        String contentTask = "";
        copySQLfromAssets();
        database = mContext.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor;
        cursor = database.rawQuery("SELECT * FROM tasktable WHERE ID=?", new String[]{String.valueOf(IDc)});
        while (cursor.moveToNext()){
            nameTask = cursor.getString(1);
            contentTask = cursor.getString(2);
            int mDay = cursor.getInt(3);
            int mMonth = cursor.getInt(4);
            int mYear = cursor.getInt(5);
            int priorityLevel = cursor.getInt(6);
            int statusTask = cursor.getInt(7);
            int ID = cursor.getInt(0);
            temp = new TaskModel(ID, nameTask, contentTask, mDay, mMonth, mYear, priorityLevel, statusTask);
        }
        cursor.close();
        return temp;
    }

    public void updateTeask(TaskModel temp){
        copySQLfromAssets();
        database = mContext.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskName", temp.getNameTask());
        contentValues.put("ContentTask", temp.getContentTask());
        contentValues.put("Day", temp.getmDay());
        contentValues.put("Month", temp.getmMonth());
        contentValues.put("Year", temp.getmYear());
        contentValues.put("PriorityLevel", temp.getPriorityLevel());
        contentValues.put("StatusTask", temp.getStatusTask());
        database.update("tasktable", contentValues, "ID=?", new String[]{String.valueOf(temp.getID())});
    }

    public void deleteTask(int ID){
        copySQLfromAssets();
        database = mContext.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        database.delete("tasktable", "ID=?", new String[]{String.valueOf(ID)});
    }
}
