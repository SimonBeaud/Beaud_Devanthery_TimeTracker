package database.entity;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class TaskEntity {
    private String id;
    private String Taskname;
    private String Description;
    private int StartTime;
    private int EndTime;
    private String Date;
    private String idEmployee;

    public TaskEntity() {
    }

    public TaskEntity(
            String Taskname,
            String Description,
            int StartTime,
            int EndTime,
            String Date,
            String idEmployee
    ) {
        this.Taskname = Taskname;
        this.Description = Description;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Date = Date;
        this.idEmployee = idEmployee;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskname() {
        return Taskname;
    }

    public void setTaskname(String taskname) {
        Taskname = taskname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getEndTime() {
        return EndTime;
    }

    public void setEndTime(int endTime) {
        EndTime = endTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof TaskEntity)) return false;
        TaskEntity o = (TaskEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return Taskname;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();


        result.put("taskname", Taskname);
        result.put("description", Description);
        result.put("startTime", StartTime);
        result.put("endTime", EndTime);
        result.put("date", Date);
        result.put("idEmployee", idEmployee);


        return result;
    }
}