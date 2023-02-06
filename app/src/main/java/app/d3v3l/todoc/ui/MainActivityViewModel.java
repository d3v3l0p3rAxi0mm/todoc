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
    private Executor executor;

    // DATA
    @Nullable
    private LiveData<List<Project>> currentProjects;
    @Nullable
    private LiveData<List<Task>> currentTasks;

    private long currentProjectIdFilter = 0;

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
        currentProjectIdFilter = 0;
        return taskDataSource.getTasks();
    }

    public LiveData<List<Task>> getTasksByProject(long projectId) {
        currentProjectIdFilter = projectId;
        return taskDataSource.getTasksByProject(projectId);
    }

    public long getCurrentProjectIdFilter() {
        return this.currentProjectIdFilter;
    }

    public void createTask(Task task) {
        currentProjectIdFilter = 0;
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskDataSource.deleteTask(task));
    }

}
