package database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import database.entity.EmployeeEntity;
import database.firebase.EmployeeListLiveData;
import database.firebase.EmployeeLiveData;
import util.OnAsyncEventListener;

public class EmployeeRepository {

    private static EmployeeRepository instance;

    public static EmployeeRepository getInstance() {
        if (instance == null) {
            synchronized (EmployeeRepository.class) {
                if (instance == null) {
                    instance = new EmployeeRepository();
                }
            }
        }
        return instance;
    }


    //Get des employées
    public LiveData<EmployeeEntity> getEmployee(final String employeeId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("employees")
                .child(employeeId);

        return new EmployeeLiveData(reference);
    }

    public LiveData<List<EmployeeEntity>> getEmployees(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("employees");

        return new EmployeeListLiveData(reference);
    }

    //Insertion d'un employée
    public void insert(final EmployeeEntity employee, OnAsyncEventListener callback){
        String id = FirebaseDatabase.getInstance().getReference("employees").push().getKey();
        FirebaseDatabase.getInstance().getReference("employees")
                .child(id)
                .setValue(employee, (databaseError,databaseReference)->{
                    if(databaseError!= null) {
                        callback.onFailure(databaseError.toException());
                    }
                    else
                    {
                        callback.onSuccess();
                    }
                });

    }

    //Delete d'un employée
    public void delete(final EmployeeEntity employee, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance().getReference("employees")
                .child(employee.getId())
                .removeValue((databaseError,databaseReference)-> {
                    if(databaseError!= null)
                    {
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });
    }
    public void transaction(final EmployeeEntity sender, final EmployeeEntity recipient,
                            OnAsyncEventListener callback) {
        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.runTransaction(new Transaction.Handler() {
            @NonNull
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                rootReference
                        .child("clients")
                        .child(sender.getOwner())
                        .child("accounts")
                        .child(sender.getId())
                        .updateChildren(sender.toMap());

                rootReference
                        .child("clients")
                        .child(recipient.getOwner())
                        .child("accounts")
                        .child(recipient.getId())
                        .updateChildren(recipient.toMap());

                return Transaction.success(mutableData);
            }

    //Update d'un employée
    public void update(final EmployeeEntity employee, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance().getReference("employees").child(employee.getId())
                .updateChildren(employee.toMap(),(databaseError,databaseReference)-> {
                    if(databaseError!= null)
                    {
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    callback.onFailure(databaseError.toException());
                } else {
                    callback.onSuccess();
                }
            }
        });
    }


}
