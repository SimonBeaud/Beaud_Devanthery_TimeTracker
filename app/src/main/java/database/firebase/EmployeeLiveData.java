package database.firebase;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;

import database.entity.EmployeeEntity;

public class EmployeeLiveData extends LiveData<EmployeeEntity> {
    public EmployeeLiveData(DatabaseReference reference) {
    }
}
