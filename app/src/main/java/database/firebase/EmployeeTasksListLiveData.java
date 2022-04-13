package database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import database.entity.EmployeeEntity;
import database.entity.TaskEntity;
import database.pojo.EmployeeWithTask;


public class EmployeeTasksListLiveData extends LiveData<List<EmployeeWithTask>> {

    private static final String TAG = "ClientAccountsLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final MyValueEventListener listener =
            new MyValueEventListener();

    public EmployeeTasksListLiveData(DatabaseReference ref, String owner) {
        reference = ref;
        this.owner = owner;
    }


    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toEmployeeWithTasksList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<EmployeeWithTask> toEmployeeWithTasksList(DataSnapshot snapshot) {
        List<EmployeeWithTask> clientWithAccountsList = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            if (!childSnapshot.getKey().equals(owner)) {
                EmployeeWithTask employeeWithTask = new EmployeeWithTask();
                employeeWithTask.employee = childSnapshot.getValue(EmployeeEntity.class);
                employeeWithTask.employee.setId(childSnapshot.getKey());
                employeeWithTask.tasks = toTasks(childSnapshot.child("tasks"),
                        childSnapshot.getKey());
                clientWithAccountsList.add(employeeWithTask);
            }
        }
        return clientWithAccountsList;
    }

    private List<TaskEntity> toTasks(DataSnapshot snapshot, String ownerId) {
        List<TaskEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            TaskEntity entity = childSnapshot.getValue(TaskEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setIdEmployee(ownerId);
            accounts.add(entity);
        }
        return accounts;
    }
}