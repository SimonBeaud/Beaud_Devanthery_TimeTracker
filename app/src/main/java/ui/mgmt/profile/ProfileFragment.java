package ui.mgmt.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.beaud_devanthery_timetracker.R;
import com.example.beaud_devanthery_timetracker.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

import baseapp.BaseApp;
import database.entity.EmployeeEntity;
import database.repository.EmployeeRepository;
import ui.mgmt.LoginActivity;
import ui.mgmt.MyAlertDialog;
import ui.mgmt.modifyemployee.ModifyEmployee;
import ui.mgmt.modifytask.ModifyTask;
import viewmodel.employees.EmployeeViewModel;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private EmployeeRepository repository;
    private EmployeeViewModel viewModel;
    private EmployeeEntity employee;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //link with the xml file view
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        repository = ((BaseApp) getActivity().getApplication()).getEmployeeRepository();
        //get the employee that is logged
        EmployeeViewModel.Factory factory = new EmployeeViewModel.Factory(
                getActivity().getApplication(),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(EmployeeViewModel.class);
        viewModel.getEmployee().observe(getViewLifecycleOwner(), employeeEntity -> {
            if (employeeEntity != null) {
                employee = employeeEntity;
            }
        });

        //set all the labels with the infos of the user
        binding.lblEmail.setText(employee.getEmail());
        binding.lblFirstName.setText(employee.getFirstName());
        binding.lblName.setText(employee.getName());
        binding.lblNumber.setText(employee.getTelnumber());


        //when button "logout" is clicked
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open an alert dialog to confirm choice
                MyAlertDialog ad = new MyAlertDialog(getContext(), "Logout ? ", "are you sure you want to log out", "log out");
                ad.backToLoginPage();

            }
        });


        //when edit profile is clicked
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the fragment to Modify Employee
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.setReorderingAllowed(true);

                //give all the user infos in argument
                Bundle args = new Bundle();
                args.putString("id", employee.getId());
                args.putString("username", employee.getUsername());
                args.putString("password", employee.getPassword());
                args.putString("email", employee.getEmail());
                args.putString("firstname", employee.getFirstName());
                args.putString("name", employee.getName());
                args.putString("number", employee.getTelnumber());
                args.putString("address", employee.getAddress());
                args.putString("function", employee.getFunction());
                args.putString("npa", employee.getNPA());
                args.putBoolean("admin", employee.getAdmin());

                //change the fragment to Modify Employee
                transaction.replace(R.id.nav_host_fragment_activity_main, ModifyEmployee.class, args);

                transaction.commit();
            }
        });

        //when button "delete" is clicked
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open alert dialog to confirm choice<
                MyAlertDialog ad = new MyAlertDialog(getContext(), "Delete account ? ", "are you sure you want to delete this account?", "delete");
                ad.deleteAccount(employee, getActivity().getApplication());
            }
        });
        return root;
    }
}