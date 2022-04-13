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
import database.repository.TaskRepository;
import util.OnAsyncEventListener;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<TaskEntity> mObservableTask;

    public TaskViewModel(@NonNull Application application,
                            final String taskId, TaskRepository taskRepository) {
        super(application);

        mRepository = taskRepository;

        mObservableTask = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableTask.setValue(null);

        if (taskId != null) {
            LiveData<TaskEntity> task = mRepository.getTask(taskId);

            // observe the changes of the account entity from the database and forward them
            mObservableTask.addSource(task, mObservableTask::setValue);
        }
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mTaskId;

        private final TaskRepository mRepository;

        public Factory(@NonNull Application application, String taskId) {
            mApplication = application;
            mTaskId = taskId;
            mRepository = ((BaseApp) application).getTaskRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TaskViewModel(mApplication, mTaskId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<TaskEntity> getTask() {
        return mObservableTask;
    }

    public void createTask(TaskEntity task, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getTaskRepository()
                .insert(task, callback);
    }

    public void updateTask(TaskEntity task, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getTaskRepository()
                .update(task, callback);
    }
}