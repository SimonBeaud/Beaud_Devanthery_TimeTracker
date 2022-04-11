package baseapp;

import android.app.Application;

import database.repository.EmployeeRepository;
import database.repository.TaskRepository;

public class BaseApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public AppDataBase getDatabase(){
        return AppDataBase.getInstance(this);
    }

    public EmployeeRepository getEmployeeRepository(){
        return EmployeeRepository.getInstance();
    }

    public TaskRepository getTaskRepository(){
        return TaskRepository.getInstance();
    }
}
