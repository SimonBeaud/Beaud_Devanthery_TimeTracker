package ui.mgmt.modifyemployee;

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
import com.example.beaud_devanthery_timetracker.databinding.FragmentProfileBinding;
import com.example.beaud_devanthery_timetracker.databinding.ModifyEmployeeFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

import baseapp.BaseApp;
import database.entity.EmployeeEntity;
import database.repository.EmployeeRepository;
import database.repository.TaskRepository;
import ui.mgmt.LoginActivity;
import ui.mgmt.history.HistoryFragment;
import ui.mgmt.profile.ProfileFragment;
import util.OnAsyncEventListener;

public class ModifyEmployee extends Fragment {

    private ModifyEmployeeFragmentBinding binding;
    private EmployeeRepository repository;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        repository = ((BaseApp) getActivity().getApplication()).getEmployeeRepository();

        //link with the xml file view

        binding = ModifyEmployeeFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get the arguments that were sent in input
        Bundle arguments = getArguments();
        String id = (String)arguments.get("id");
        String username = (String)arguments.get("username");
        //String password = (String)arguments.get("password");
        String email = (String)arguments.get("email");
        String firstname = (String)arguments.get("firstname");
        String name = (String)arguments.get("name");
        String number = (String)arguments.get("number");
        String address = (String)arguments.get("address");
        String function = (String)arguments.get("function");
        String npa = (String)arguments.get("npa");
        boolean admin = (boolean)arguments.get("admin");


        //set the texts in all textboxes. the user just modifies the line he wants
        binding.createAccountUsername.setText(username);
        //binding.createAccountPassword.setText(password);
        binding.createAccountEmail.setText(email);
        binding.createAccountFirstname.setText(firstname);
        binding.createAccountLastname.setText(name);
        binding.createAccountPhone.setText(number);
        binding.createAccountAddress.setText(address);
        binding.createAccountFunction.setText(function);
        binding.createAccountNpa.setText(npa);


        //when "modify" button is clicked
        binding.modifyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get modifications that were made
                EmployeeEntity employee = new EmployeeEntity();
                employee.setId(id);
                employee.setUsername(binding.createAccountUsername.getText().toString());
                //employee.setPassword(binding.createAccountPassword.getText().toString());
                employee.setEmail(binding.createAccountEmail.getText().toString());
                employee.setFirstName(binding.createAccountFirstname.getText().toString());
                employee.setName(binding.createAccountLastname.getText().toString());
                employee.setTelnumber(binding.createAccountPhone.getText().toString());
                employee.setAddress(binding.createAccountAddress.getText().toString());
                employee.setFunction(binding.createAccountFunction.getText().toString());
                employee.setNPA(binding.createAccountNpa.getText().toString());
                employee.setAdmin(admin);

                //////////////////////////////////////////////
                //////////////////////////////////////////////
                //////////////////////////////////////////////

                // maybe not useful anymore with firebase


                //change the logged employee values to keep them correct

//                LoginActivity.LOGGED_EMPLOYEE = employee;




                //modify in db
                repository.update(employee, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("UpdateEmployee","sucessful");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.w("UpdateEmployee","failed");

                    }
                });

                switchToProfile();


            }
        });

        //when "back" button is clicked
        binding.modifyProfileBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchToProfile();
            }
        });

        return root;
    }

    private void switchToProfile()
    {
        //change the fragment back to the Profile fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.nav_host_fragment_activity_main, ProfileFragment.class,null);
        transaction.commit();
    }

}