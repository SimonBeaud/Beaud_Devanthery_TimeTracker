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

public class EmployeeListLiveData extends LiveData<List<EmployeeEntity>> {


        private static final String TAG = "AccountListLiveData";

        private final DatabaseReference reference;
        private final String owner;
        private final MyValueEventListener listener = new MyValueEventListener();

        public EmployeeListLiveData(DatabaseReference ref, String owner) {
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
                setValue(toAccounts(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
            }
        }

        private List<TaskEntity> toTasks(DataSnapshot snapshot) {
            List<TaskEntity> tasks = new ArrayList<>();
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                EmployeeEntity entity = childSnapshot.getValue(EmployeeEntity.class);
                entity.setId(childSnapshot.getKey());
                entity.setOwner(owner);
                employee.add(entity);
            }
            return accounts;
        }
}
