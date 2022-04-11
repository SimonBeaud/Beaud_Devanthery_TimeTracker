package database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class TaskEntity implements Comparable{


    private Long id;
    private String Taskname;
    private String Description;
    private int StartTime;
    private int EndTime;
    private String Date;
    private long idEmployee;

    public TaskEntity() {
    }

    public TaskEntity(
            String Taskname,
            String Description,
            int StartTime,
            int EndTime,
            String Date,
            long idEmployee
    ) {
        this.Taskname = Taskname;
        this.Description = Description;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Date = Date;
        this.idEmployee = idEmployee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(long idEmployee) {
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

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskname", Taskname);
        result.put("description", Description);
        return result;
    }
}