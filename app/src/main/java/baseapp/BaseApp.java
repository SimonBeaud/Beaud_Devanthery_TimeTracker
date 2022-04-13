package baseapp;

import android.app.Application;

import database.repository.EmployeeRepository;
import database.repository.TaskRepository;

public class BaseApp extends Application {

    public EmployeeRepository getEmployeeRepository(){
        return EmployeeRepository.getInstance();
    }

    public TaskRepository getTaskRepository(){
        return TaskRepository.getInstance();
    }

}
