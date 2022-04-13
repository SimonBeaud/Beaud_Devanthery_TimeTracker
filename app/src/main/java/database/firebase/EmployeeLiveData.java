package database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import database.entity.EmployeeEntity;

public class EmployeeLiveData extends LiveData<EmployeeEntity> {
    private static final String TAG = "AccountLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final EmployeeLiveData.MyValueEventListener listener = new EmployeeLiveData.MyValueEventListener();

    public EmployeeLiveData(DatabaseReference ref) {
        reference = ref;
        owner = ref.getParent().getParent().getKey();
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
            EmployeeEntity entity = dataSnapshot.getValue(EmployeeEntity.class);
            entity.setId(dataSnapshot.getKey());
            entity.setOwner(owner);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
