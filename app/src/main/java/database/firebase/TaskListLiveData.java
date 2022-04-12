package database.firebase;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import database.entity.TaskEntity;

public class TaskListLiveData extends LiveData<List<TaskEntity>> {
    public TaskListLiveData(DatabaseReference reference) {
    }
}
