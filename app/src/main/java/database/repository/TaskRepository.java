package database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import database.entity.TaskEntity;
import database.firebase.TaskListLiveData;
import database.firebase.TaskLiveData;
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
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////
//////////////////A DISCUTER////////////////////////
/////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////

//    public void signIn(final String email, final String password,
//                       final OnCompleteListener<AuthResult> listener) {
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(listener);
//    }

    public LiveData<TaskEntity> getTask(final String taskId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("tasks")
                .child(taskId);

        return new TaskLiveData(reference);

    }

    public LiveData<List<TaskEntity>> getTasks(){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("tasks");

        return new TaskListLiveData(reference);
    }

    public LiveData<List<TaskEntity>> getTasksOfEmployee( String employeeId){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("tasks");
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////
        //modify to have only the tasks of one employee
        return new TaskListLiveData(reference);

    }

    //Insertion d'un employée
    public void insert(final TaskEntity task, OnAsyncEventListener callback){
        String id = FirebaseDatabase.getInstance().getReference("tasks").push().getKey();
        FirebaseDatabase.getInstance().getReference("tasks")
                .child(id)
                .setValue(task, (databaseError,databaseReference)->{
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
    public void delete(final TaskEntity task, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance().getReference("employees")
                .child(task.getId())
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

    //Update d'un employée
    public void update(final TaskEntity task, OnAsyncEventListener callback){
        FirebaseDatabase.getInstance().getReference("employees").child(task.getId())
                .updateChildren(task.toMap(),(databaseError,databaseReference)-> {
                    if(databaseError!= null)
                    {
                        callback.onFailure(databaseError.toException());
                    }
                    else{
                        callback.onSuccess();
                    }
                });
    }
}
