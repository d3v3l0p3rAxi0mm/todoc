package app.d3v3l.todoc.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Nullable;

import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;
import app.d3v3l.todoc.repositories.ProjectDataRepository;
import app.d3v3l.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class MainActivityViewModel extends ViewModel {

    //private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    //Executor
    private static Executor mExecutor;
    public long currentProjectIdFilter;

    // DATA
    //@Nullable
    //private LiveData<List<Project>> currentProjects;

    public MainActivityViewModel(TaskDataRepository taskDataSource, Executor executor) {
        //this.projectDataSource = projectDataSource;
        this.taskDataSource = taskDataSource;
        mExecutor = executor;
        currentProjectIdFilter = 0;
    }

    /**
     * Gets executor.
     * @return the executor
     */
    public static Executor getExecutor() {
        return mExecutor;
    }


    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void setCurrentProjectIdFilter(long idProject) {
        currentProjectIdFilter = idProject;
    }

    public long getCurrentProjectIdFilter() {
        return currentProjectIdFilter;
    }

    public void createTask(Task task) {
        taskDataSource.createTask(task);
    }

    public void deleteTask(Task task) {
        taskDataSource.deleteTask(task);
    }

}
