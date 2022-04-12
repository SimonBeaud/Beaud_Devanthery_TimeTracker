package database.firebase;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import database.entity.EmployeeEntity;

public class EmployeeListLiveData extends LiveData<List<EmployeeEntity>> {
    public EmployeeListLiveData(DatabaseReference reference) {
    }
}
