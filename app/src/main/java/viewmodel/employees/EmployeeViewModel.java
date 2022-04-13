package viewmodel.employees;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import baseapp.BaseApp;
import database.entity.EmployeeEntity;
import database.repository.EmployeeRepository;
import util.OnAsyncEventListener;


public class EmployeeViewModel extends AndroidViewModel {

    private static final String TAG = "AccountViewModel";

    private EmployeeRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<EmployeeEntity> mObservableEmployee;

    public EmployeeViewModel(@NonNull Application application,
                           final String employeeId, EmployeeRepository employeeRepository) {
        super(application);

        repository = employeeRepository;

        mObservableEmployee = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableEmployee.setValue(null);

        LiveData<EmployeeEntity> employee = repository.getEmployee(employeeId);

        // observe the changes of the client entity from the database and forward them
        mObservableEmployee.addSource(employee, mObservableEmployee::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String employeeId;

        private final EmployeeRepository repository;

        public Factory(@NonNull Application application, String employeeId) {
            this.application = application;
            this.employeeId = employeeId;
            repository = ((BaseApp) application).getEmployeeRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new EmployeeViewModel(application, employeeId, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<EmployeeEntity> getEmployee() {
        return mObservableEmployee;
    }

    public void updateEmployee(EmployeeEntity employee, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getEmployeeRepository()
                .update(employee, callback);
    }

    public void deleteEmployee(EmployeeEntity employee, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getEmployeeRepository()
                .delete(employee, callback);
    }
}