package database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import database.entity.EmployeeEntity;
import database.entity.TaskEntity;
import database.pojo.EmployeeWithTask;

public class TaskEmployeeListLiveData extends LiveData<List<EmployeeWithTask>> {
    private static final String TAG = "ClientAccountsLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final TaskEmployeeListLiveData.MyValueEventListener listener =
            new TaskEmployeeListLiveData.MyValueEventListener();

    public TaskEmployeeListLiveData(DatabaseReference ref, String owner) {
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
            setValue(toClientWithAccountsList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<EmployeeWithTask> toClientWithAccountsList(DataSnapshot snapshot) {
        List<EmployeeWithTask> clientWithAccountsList = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            if (!childSnapshot.getKey().equals(owner)) {
                EmployeeWithTask employeeWithTask = new EmployeeWithTask();
                employeeWithTask.task = childSnapshot.getValue(TaskEntity.class);
                employeeWithTask.task.setId(childSnapshot.getKey());
                employeeWithTask.employee = toAccounts(childSnapshot.child("accounts"),
                        childSnapshot.getKey());
                clientWithAccountsList.add(employeeWithTask);
            }
        }
        return clientWithAccountsList;
    }

    private List<EmployeeEntity> toAccounts(DataSnapshot snapshot, String ownerId) {
        List<EmployeeEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            EmployeeEntity entity = childSnapshot.getValue(EmployeeEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setOwner(ownerId);
            accounts.add(entity);
        }
        return accounts;
    }
}
