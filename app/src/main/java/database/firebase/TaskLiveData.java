package database.firebase;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;

import database.entity.TaskEntity;

public class TaskLiveData extends LiveData<TaskEntity> {
    public TaskLiveData(DatabaseReference reference) {
    }
}
