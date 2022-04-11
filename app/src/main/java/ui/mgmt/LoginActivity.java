package ui.mgmt;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beaud_devanthery_timetracker.R;

import java.security.NoSuchAlgorithmException;

import baseapp.BaseApp;
import database.entity.EmployeeEntity;
import database.repository.EmployeeRepository;
import java.security.MessageDigest;


public class LoginActivity extends AppCompatActivity {

    //Attributes
    public static EmployeeEntity LOGGED_EMPLOYEE;
    private AutoCompleteTextView usernameView;
    private AutoCompleteTextView passwordView;
    private Button buttonLogin;
    private EditText Username, Password;
    private ProgressBar progressBar;
    private AppDataBase database;

    private EmployeeRepository repository;

    //On create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = AppDataBase.getInstance(this.getBaseContext());
        repository = ((BaseApp)getApplication()).getEmployeeRepository();
        Username =  findViewById(R.id.editTextTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);


    }

    //Button back to register page
    public void GoToRegister(View view){
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    //Login method
    public void Login(View view) {

        Username.setError(null);
        Password.setError(null);
        buttonLogin= findViewById(R.id.btnLogin);
        String stUsername = Username.getText().toString();
        String encryptedPassword = "";

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(Password.getText().toString().getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = s.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        boolean cancel = false;
        View focusView = null;


        //Check if the username is valid
        if(TextUtils.isEmpty(stUsername)){
            Username.setError(getString(R.string.error_username_required));
            focusView = Username;
            cancel=true;
        }else if(!isUsernameValid(stUsername)){
            Username.setError(getString(R.string.error_invalid_username));
            focusView = Username;
            cancel=true;
        }

        //login action
        if(cancel){
            focusView.requestFocus();
        }else{
            String finalEncryptedPassword = encryptedPassword;
            repository.getEmployee(stUsername, getApplication()).observe(LoginActivity.this, employeeEntity -> {
                if (employeeEntity != null) {
                    if (employeeEntity.getPassword().equals(finalEncryptedPassword)) {
                        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
                        editor.putString(MainActivity.PREFS_USER, employeeEntity.getUsername());
                        editor.apply();

                        LOGGED_EMPLOYEE = employeeEntity;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Username.setText("");
                        Password.setText("");
                    } else {
                        Password.setError(getString(R.string.error_incorrect_password));
                        Password.requestFocus();
                        Password.setText("");
                    }
                } else {
                    Username.setError(getString(R.string.error_invalid_username));
                    Username.requestFocus();
                    Username.setText("");
                }
            });
        }
    }

    //Check if username is valid
    private boolean isUsernameValid(String username){
        return true;
    }

    //Check if password is valid
    private boolean isPasswordValid(String password){
        return password.length()>=4;
    }

    //Show error on app
    private void showError(EditText input, String s){
        input.setError(s);
    }
}