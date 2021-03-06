package database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.os.AsyncTask;

import java.util.concurrent.Executors;

import database.dao.EmployeeDao;
import database.dao.TaskDao;
import database.entity.EmployeeEntity;
import database.entity.TaskEntity;

//DataBase
@Database(entities = {EmployeeEntity.class, TaskEntity.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {


    private static final String TAG ="AppDatabase";
    private static AppDataBase instance;
    private static final String DATABASE_NAME ="bank-database";
    public abstract EmployeeDao employeeDao();
    public abstract TaskDao taskDao();
    private final MutableLiveData<Boolean> mIsDatabaseCreated= new MutableLiveData<>();


    public static AppDataBase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDataBase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                    instance.updateDatabaseCreated(context.getApplicationContext());
                    //instance.initializeDemoData(instance);
                }
            }
        }

        return instance;
    }

    //DataBAse building
    private static AppDataBase buildDatabase(final Context appContext) {
        Log.i(TAG, "Database will be initialized.");
        return Room.databaseBuilder(appContext, AppDataBase.class, DATABASE_NAME).fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDataBase database = AppDataBase.getInstance(appContext);
                            //database.initializeDemoData(database);

                            database.setDatabaseCreated();
                        });
                    }
                }).build();

    }

    public void initializeDemoData(final AppDataBase database) {
        System.out.println("INITIALIZING DATA");
        Executors.newSingleThreadExecutor().execute(() -> {
            database.runInTransaction(() -> {
                Log.i(TAG, "Wipe database.");
                database.employeeDao().deleteAll();
                database.taskDao().deleteAll();

                DatabaseInitializer.populateDatabase(database);
            });
        });
    }

    //Update DataBase
    private void updateDatabaseCreated(final Context context){
        if(context.getDatabasePath(DATABASE_NAME).exists()){
            Log.i(TAG, "Database initialized.");
            setDatabaseCreated();
        }
    }

    //Setting DataBAse
    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
        System.out.println("LA BASE DE DONNEE A BIEN ETE CREE !!!!!!!!!!");
    }


    public LiveData<Boolean> getDatabaseCreated(){
        return mIsDatabaseCreated;
    }
}
