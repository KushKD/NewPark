package com.example.kushdhawan.mapstest.Abstract;

import android.app.Activity;

import com.example.kushdhawan.mapstest.Enums.TaskType;

/**
 * Created by kuush on 6/17/2016.
 */
public interface AsyncTaskListener {

    void onTaskCompleted(String result, TaskType taskType);

    void onTaskCompleted(Activity activity, String result, TaskType taskType);

}