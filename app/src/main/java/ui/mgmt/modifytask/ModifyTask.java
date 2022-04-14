package ui.mgmt.modifytask;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beaud_devanthery_timetracker.R;
import com.example.beaud_devanthery_timetracker.databinding.ModifyTaskFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

import baseapp.BaseApp;
import database.entity.TaskEntity;
import database.repository.EmployeeRepository;
import database.repository.TaskRepository;
import ui.mgmt.LoginActivity;
import ui.mgmt.history.HistoryFragment;
import util.OnAsyncEventListener;

public class ModifyTask extends Fragment {

    private ModifyTaskFragmentBinding binding;
    private TaskRepository repository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        repository = ((BaseApp) getActivity().getApplication()).getTaskRepository();

        //get the arguments that were sent in input
        Bundle arguments = getArguments();
        String id = (String)arguments.get("idTask");
        String title = (String)arguments.get("title");
        String description = (String)arguments.get("description");
        String date = (String)arguments.get("date");
        int start = (int)arguments.get("start");
        int end = (int)arguments.get("end");

        //link with the xml file view
        binding = ModifyTaskFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //set the texts in all textboxes. the user just modifies the line he wants
        binding.txtTitlemodify.setText(title);
        binding.txtDescriptionmodify.setText(description);
        //format of dates with 2 digits HH:MM
        binding.txtStartTimemodify.setText(String.format("%02d:%02d",start/60,start%60));
        binding.txtEndTimemodify.setText(String.format("%02d:%02d",end/60,end%60));


        //when button "cancel" is clicked
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchToHistory();
            }
        });


        //when button "modify" is clicked
        binding.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get modifications that were made
                String description = binding.txtDescriptionmodify.getText().toString();
                String endTime = binding.txtEndTimemodify.getText().toString();
                String startTime = binding.txtStartTimemodify.getText().toString();
                String title = binding.txtTitlemodify.getText().toString();

                //time values management to store it in integer variables
                String[] startSplit = startTime.split(":");
                String[] endSplit = endTime.split(":");
                int endHour = Integer.parseInt(endSplit[0]);
                int endMinute = Integer.parseInt(endSplit[1]);
                int startHour = Integer.parseInt(startSplit[0]);
                int startMinute = Integer.parseInt(startSplit[1]);

                int endInt = endHour*60 + endMinute;
                int startInt = startHour*60 + startMinute;


                //create new task entity , with the same id as the one we are modifying
                TaskEntity task = new TaskEntity(title,description,startInt,endInt,date, FirebaseAuth.getInstance().getCurrentUser().getUid());
                task.setId(id);



                //modify in db
                repository.update(task,new OnAsyncEventListener() {

                    @Override
                    public void onSuccess() {
                        Log.i("ModifyTask" , "successful");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.w("ModifyTask" , "failed");
                    }
                });

                switchToHistory();

            }
        });

        return root;

    }

    private void switchToHistory()
    {
        //change the fragment back to the history fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.nav_host_fragment_activity_main, HistoryFragment.class,null);
        transaction.commit();
    }

}