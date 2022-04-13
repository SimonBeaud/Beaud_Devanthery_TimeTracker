package ui.mgmt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Delete;

import com.example.beaud_devanthery_timetracker.R;

import baseapp.BaseApp;
import database.entity.EmployeeEntity;
import database.entity.TaskEntity;
import database.repository.EmployeeRepository;
import ui.mgmt.modifytask.ModifyTask;
import util.OnAsyncEventListener;

public class MyAlertDialog {

    private int ThemeID;
    String dialogYesBtn, dialogNoBtn = "Cancel";
    Context context;
    AlertDialog.Builder myAlert;

    private EmployeeRepository repository;


    //constructor with all the text that will be displayes
    public MyAlertDialog(Context context, String title, String msg, String yesMsg) {
        myAlert = new AlertDialog.Builder(context, getThemeID());
        setDialogTitle(title);
        setDialogMsg(msg);
        setDialogYesBtn(yesMsg);
        this.context = context;
    }


    public void backToLoginPage() {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
            editor.remove(MainActivity.PREFS_USER);
            editor.apply();

            //logged employee is null now because he logged out
            LoginActivity.LOGGED_EMPLOYEE = null;

            //go to login activity
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public void aboutUs() {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
        });
        myAlert.show();
    }

    public void details(FragmentManager fragmentManager, TaskEntity taskEntity) {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
            editor.remove(MainActivity.PREFS_USER);
            editor.apply();
            Bundle arguments = new Bundle();
            arguments.putString("title", taskEntity.getTaskname());
            arguments.putString("description", taskEntity.getDescription());
            arguments.putString("date", taskEntity.getDate());
            arguments.putInt("start", taskEntity.getStartTime());
            arguments.putInt("end", taskEntity.getEndTime());
            arguments.putString("idTask", taskEntity.getId());


            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.setReorderingAllowed(true);

            transaction.replace(R.id.nav_host_fragment_activity_main, ModifyTask.class, arguments);
            transaction.commit();

        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public void deleteAccount(EmployeeEntity employee, Application application) {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
            editor.remove(MainActivity.PREFS_USER);
            editor.apply();
            EmployeeRepository repository = ((BaseApp)application).getEmployeeRepository();
            repository.delete(employee,new OnAsyncEventListener() {

                @Override
                public void onSuccess() {
                    System.out.println("L'employé a été supprimé");
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("L'employé n'a pas été supprimé");
                }
            });


            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }


    public int getThemeID() {
        return ThemeID;
    }

    public void setDialogTitle(String title) {
        myAlert.setTitle(title);
    }

    public void setDialogMsg(String msg) {
        myAlert.setMessage(msg);
    }


    public void setDialogYesBtn(String dialogYesBtn) {
        this.dialogYesBtn = dialogYesBtn;
    }
}


