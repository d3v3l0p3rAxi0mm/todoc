package app.d3v3l.todoc.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import app.d3v3l.todoc.database.dao.ProjectDao;
import app.d3v3l.todoc.database.dao.TaskDao;
import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;

import java.security.Timestamp;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    private static volatile TodocDatabase INSTANCE;

    // --- DAO ---
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    // --- INSTANCE ---
    public static TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                               TodocDatabase.class, "database.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Project[] projects = Project.getAllProjects();
                for (Project project : projects) {
                    Log.d("Project",project.getName());
                    Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao()
                            .createProject(new Project(project.getId(), project.getName(), project.getColor())));

                    /* Must wait end of Execution before creating Tasks below
                    //TODO How to do that ????
                    Log.d("Task","Project " + project.getId() + "Task 01");
                    Executors.newSingleThreadExecutor().execute(() -> INSTANCE.taskDao()
                            .createTask(new Task(project.getId(), "Task 01 ("+ project.getName() +")", 100000)));
                    Log.d("Task","Project " + project.getId() + "Task 02");
                    Executors.newSingleThreadExecutor().execute(() -> INSTANCE.taskDao()
                            .createTask(new Task(project.getId(), "Task 02 ("+ project.getName() +")", 100020)));
                    Log.d("Task","Project " + project.getId() + "Task 03");
                    Executors.newSingleThreadExecutor().execute(() -> INSTANCE.taskDao()
                            .createTask(new Task(project.getId(), "Task 03 ("+ project.getName() +")", 100030)));
                    */
                }

            }
        };
    }

}
