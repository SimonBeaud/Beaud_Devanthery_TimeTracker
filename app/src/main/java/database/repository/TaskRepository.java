package database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import baseapp.BaseApp;
import database.async.task.CreateTask;
import database.async.task.DeleteTask;
import database.async.task.UpdateTask;
import database.entity.TaskEntity;
import database.firebase.TaskEmployeeListLiveData;
import database.firebase.TaskLiveData;
import database.pojo.EmployeeWithTask;
import util.OnAsyncEventListener;

public class TaskRepository {

    private static final String TAG = "ClientRepository";

    private static TaskRepository instance;

    private TaskRepository() {
    }

    public static TaskRepository getInstance() {
        if (instance == null) {
            synchronized (TaskRepository.class) {
                if (instance == null) {
                    instance = new TaskRepository();
                }
            }
        }
        return instance;
    }

    public void signIn(final String email, final String password,
                       final OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public LiveData<TaskEntity> getClient(final String clientId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(clientId);
        return new TaskLiveData(reference);
    }

    public LiveData<List<EmployeeWithTask>> getOtherClientsWithAccounts(final String owner) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients");
        return new TaskEmployeeListLiveData(reference, owner);
    }

    public void register(final TaskEntity client, final OnAsyncEventListener callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                client.getEmail(),
                client.getPassword()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                client.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(client, callback);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    private void insert(final TaskEntity client, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(client, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onFailure(null);
                                        Log.d(TAG, "Rollback successful: User account deleted");
                                    } else {
                                        callback.onFailure(task.getException());
                                        Log.d(TAG, "Rollback failed: signInWithEmail:failure",
                                                task.getException());
                                    }
                                });
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final TaskEntity client, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(client.getId())
                .updateChildren(client.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(client.getPassword())
                .addOnFailureListener(
                        e -> Log.d(TAG, "updatePassword failure!", e)
                );
    }

    public void delete(final TaskEntity client, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(client.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

}
