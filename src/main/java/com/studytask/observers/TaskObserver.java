package com.studytask.observers;

import com.studytask.models.Task;

public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskUpdated(Task task);
    void onTaskCompleted(Task task);
    void onTaskDeleted(int taskId);
}