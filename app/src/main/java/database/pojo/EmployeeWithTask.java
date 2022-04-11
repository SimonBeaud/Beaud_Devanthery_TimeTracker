package database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import database.entity.EmployeeEntity;
import database.entity.TaskEntity;

public class EmployeeWithTask {

    public EmployeeEntity employee;

    public List<TaskEntity> tasks;

}
