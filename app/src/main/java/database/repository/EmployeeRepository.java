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

import java.util.List;

import baseapp.BaseApp;
import database.async.employee.CreateEmployee;
import database.async.employee.DeleteEmployee;
import database.async.employee.UpdateEmployee;
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

    public LiveData<EmployeeEntity> getAccount(final String accountId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("accounts")
                .child(accountId);
        return new EmployeeLiveData(reference);
    }

    public LiveData<List<EmployeeEntity>> getByOwner(final String owner) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(owner)
                .child("accounts");
        return new EmployeeListLiveData(reference, owner);
    }

    public void insert(final EmployeeEntity employee, final OnAsyncEventListener callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(employee.getOwner())
                .child("accounts");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(employee.getOwner())
                .child("accounts")
                .child(key)
                .setValue(employee, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final EmployeeEntity account, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .updateChildren(account.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final EmployeeEntity account, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
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
