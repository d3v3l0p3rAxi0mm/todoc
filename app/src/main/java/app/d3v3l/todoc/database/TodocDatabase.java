package app.d3v3l.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import app.d3v3l.todoc.database.dao.ProjectDao;
import app.d3v3l.todoc.database.dao.TaskDao;
import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;

import java.util.concurrent.Executors;

@Database(entities = {Project.class, Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    private static volatile TodocDatabase INSTANCE;

    // --- DAO ---
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    // --- INSTANCE ---
    // We should never used '.allowMainThreadQueries()'
    //TODO remove allowMainThreadQueries() in the next release

    public static TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                               TodocDatabase.class, "database.db")
                            .addCallback(prepopulateDatabase())
                            .allowMainThreadQueries()
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
                    Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao()
                            .createProject(new Project(project.getId(), project.getName(), project.getColor())));
                }

            }
        };
    }

}
