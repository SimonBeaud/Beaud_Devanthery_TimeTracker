package ui.mgmt.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;


import com.example.beaud_devanthery_timetracker.R;
import com.example.beaud_devanthery_timetracker.databinding.FragmentHistoryBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.TaskAdapter;
import baseapp.BaseApp;
import database.entity.TaskEntity;
import database.repository.EmployeeRepository;
import database.repository.TaskRepository;
import ui.mgmt.LoginActivity;
import ui.mgmt.MainActivity;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private View root;
    ListView list;
    private TaskAdapter myAdapter;

    //list to store the tasks of the users
    List<TaskEntity> myListOfTasks;

    private TaskRepository repository;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //get the link with the DB
        repository = ((BaseApp)getActivity().getApplication()).getTaskRepository();

        //link with the xml file view
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        //get the list view
        list = (ListView)root.findViewById(R.id.myListViewHistory);


        myListOfTasks = new ArrayList<>();
        myAdapter = new TaskAdapter(getActivity().getBaseContext(),R.layout.history_task_fragment,myListOfTasks,inflater,getActivity().getApplication(),root.getContext(),getParentFragmentManager());
        list.setAdapter(myAdapter);

        //request the list of tasks of the employee in the db
        repository.getTasksOfEmployee(getActivity().getApplication(),LoginActivity.LOGGED_EMPLOYEE.getId()).observe(getActivity(), taskEntities -> {
            if (taskEntities != null) {
                //free the list to avoid double values
                myListOfTasks.clear();


                for(int i=0;i<taskEntities.size();i++)
                {

                    myListOfTasks.add(taskEntities.get(i));
                }

                //set an adapter to the list to display the tasks, and make them clickable
                list.setAdapter(myAdapter);

            } else {
                Log.i("history","no tasks in database!");
            }
        });
        return root;
    }

}