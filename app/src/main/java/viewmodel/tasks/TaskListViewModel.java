package viewmodel.tasks;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import baseapp.BaseApp;
import database.entity.TaskEntity;
import database.pojo.EmployeeWithTask;
import database.repository.EmployeeRepository;
import database.repository.TaskRepository;
import util.OnAsyncEventListener;


public class TaskListViewModel extends AndroidViewModel {

    private static final String TAG = "AccountListViewModel";

    private TaskRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<EmployeeWithTask>> mObservableEmployeeTasks;
    private final MediatorLiveData<List<TaskEntity>> mObservableTasks;

    public TaskListViewModel(@NonNull Application application,
                                final String ownerId, EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        super(application);

        mRepository = taskRepository;

        mObservableEmployeeTasks = new MediatorLiveData<>();
        mObservableTasks = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableEmployeeTasks.setValue(null);
        mObservableTasks.setValue(null);

        LiveData<List<EmployeeWithTask>> employeeTasks = employeeRepository.getOtherEmployeesWithTasks(ownerId);
        LiveData<List<TaskEntity>> ownTasks = mRepository.getTasksOfEmployee(ownerId);

        // observe the changes of the entities from the database and forward them
        mObservableEmployeeTasks.addSource(employeeTasks, mObservableEmployeeTasks::setValue);
        mObservableTasks.addSource(ownTasks, mObservableTasks::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mOwnerId;

        private final EmployeeRepository mEmployeeRepository;

        private final TaskRepository mTaskRepository;

        public Factory(@NonNull Application application, String ownerId) {
            mApplication = application;
            mOwnerId = ownerId;
            mEmployeeRepository = ((BaseApp) application).getEmployeeRepository();
            mTaskRepository = ((BaseApp) application).getTaskRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TaskListViewModel(mApplication, mOwnerId, mEmployeeRepository, mTaskRepository);
        }
    }

    /**
     * Expose the LiveData ClientWithAccounts query so the UI can observe it.
     */
    public LiveData<List<EmployeeWithTask>> getEmployeeTasks() {
        return mObservableEmployeeTasks;
    }

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     */
    public LiveData<List<TaskEntity>> getOwnTasks() {
        return mObservableTasks;
    }

    public void deleteTask(TaskEntity task, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getTaskRepository()
                .delete(task, callback);
    }

}