package ui.mgmt.createtask;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.beaud_devanthery_timetracker.databinding.FragmentCreatetaskBinding;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import baseapp.BaseApp;
import database.entity.TaskEntity;
import database.repository.TaskRepository;
import ui.mgmt.LoginActivity;
import util.OnAsyncEventListener;

public class CreateTaskFragment extends Fragment {

    private FragmentCreatetaskBinding binding;
    private TaskRepository repository;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        repository = ((BaseApp) getActivity().getApplication()).getTaskRepository();
        //link with the xml file view
        binding = FragmentCreatetaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //when button "create" is clicked
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////
                //// get infos written by the user
                ////////////////////////////////////////////////////////////////////
                String description = binding.txtDescription.getText().toString();
                String endTime = binding.txtEndTime.getText().toString();
                String startTime = binding.txtStartTime.getText().toString();
                String title = binding.txtTitle.getText().toString();


                ////////////////////////////////////////////////////////////////////
                //// test if the end is not before start?
                ////////////////////////////////////////////////////////////////////






                if(checkConditions())
                {
                    ////////////////////////////////////////////////////////////////////
                    //// set the text boxes free
                    ////////////////////////////////////////////////////////////////////
                    binding.txtTitle.setText("");
                    binding.txtDescription.setText("");
                    binding.txtEndTime.setText("");
                    binding.txtStartTime.setText("");

                    ////////////////////////////////////////////////////////////////////
                    //// create a new task object
                    ////////////////////////////////////////////////////////////////////
                    TaskEntity newTask = new TaskEntity();
                    newTask.setDescription(description);
                    //splitting time in min/hour
                    String[] startSplit = startTime.split(":");
                    String[] endSplit = endTime.split(":");
                    int endHour = Integer.parseInt(endSplit[0]);
                    int endMinute = Integer.parseInt(endSplit[1]);
                    int startHour = Integer.parseInt(startSplit[0]);
                    int startMinute = Integer.parseInt(startSplit[1]);

                    newTask.setEndTime(endHour*60 + endMinute);
                    newTask.setStartTime(startHour*60 + startMinute);
                    newTask.setTaskname(title);
                    Date today = Calendar.getInstance().getTime();
                    String myDate =String.format("%02d.%02d.%04d", today.getDate() , today.getMonth()+1 , today.getYear()+1900);
                    newTask.setDate(myDate);
                    newTask.setIdEmployee(LoginActivity.LOGGED_EMPLOYEE.getId());



                    ////////////////////////////////////////////////////////////////////
                    //// add the task in database
                    ////////////////////////////////////////////////////////////////////
                    repository.insert(newTask ,new OnAsyncEventListener() {

                        @Override
                        public void onSuccess() {
                            Log.i("createTask" , "successful");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.w( "createTask" , "failed");
                        }
                    }
                    );
                }



            }
        });

        return root;
    }
    public boolean checkConditions() {
        boolean anyisempty = true;

        if (binding.txtTitle.getText().toString().equals("")) {
            showError(binding.txtTitle, "Can not be empty");
            return false;
        }
        if (binding.txtDescription.getText().toString().equals("")) {
            showError(binding.txtDescription, "Can not be empty");
            return false;
        }

        if (binding.txtStartTime.getText().toString().equals("")) {
            showError(binding.txtStartTime, "Can not be empty");
            return false;
        }
        else
        {
            if(!checkHourFormat(binding.txtStartTime.getText().toString()))
            {
                showError(binding.txtStartTime,"date format must be HH:MM");
                return false;
            }
        }
        if (binding.txtEndTime.getText().toString().equals("")) {
            showError(binding.txtEndTime, "Can not be empty");
            return false;
        }
        else
        {
            if(!checkHourFormat(binding.txtEndTime.getText().toString()))
            {
                showError(binding.txtEndTime,"date format must be HH:MM");
                return false;
            }
        }

        return true;
    }

    public boolean checkHourFormat(String time)
    {
        try {
            String[] split = time.split(":");
            if(time.length()>5)
            {
                return false;
            }
            Integer.parseInt(split[0]);
            Integer.parseInt(split[1]);


        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    //Show error on app
    private void showError(EditText input, String s) {
        input.setError(s);
    }
}