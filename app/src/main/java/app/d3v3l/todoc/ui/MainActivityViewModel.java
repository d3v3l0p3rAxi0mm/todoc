package app.d3v3l.todoc.ui;

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

    private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    // DATA
    @Nullable
    private LiveData<List<Project>> currentProjects;
    @Nullable
    private LiveData<List<Task>> currentTasks;

    public MainActivityViewModel(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        this.projectDataSource = projectDataSource;
        this.taskDataSource = taskDataSource;
        this.executor = executor;
    }

    public void init() {
        if (this.currentProjects != null) {
            return;
        }
        currentProjects = projectDataSource.getAllProjects();
    }

    public LiveData<List<Project>> getProjects() {
        return projectDataSource.getAllProjects();
    }

    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void createTask(Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskDataSource.deleteTask(task));
    }

}
