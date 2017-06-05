package com.example.haint.model;

/**
 * Created by haint on 03/06/2017.
 */

public class TaskModel{
    private Integer ID;
    private String nameTask;
    private String contentTask;
    private int mDay;
    private int mMonth;
    private int mYear;
    private Integer priorityLevel;
    private Integer statusTask;

    public TaskModel() {
    }

    public TaskModel(Integer ID, String nameTask, String contentTask, int mDay, int mMonth, int mYear, Integer priorityLevel, Integer statusTask) {
        this.ID = ID;
        this.nameTask = nameTask;
        this.contentTask = contentTask;
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.priorityLevel = priorityLevel;
        this.statusTask = statusTask;
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getContentTask() {
        return contentTask;
    }

    public void setContentTask(String contentTask) {
        this.contentTask = contentTask;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Integer getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(Integer statusTask) {
        this.statusTask = statusTask;
    }
}
