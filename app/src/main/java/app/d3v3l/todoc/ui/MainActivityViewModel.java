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
    private long currentProjectIdFilter = 0;

    // DATA
    @Nullable
    private LiveData<List<Project>> currentProjects;

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

    public LiveData<List<Task>> getTasks() {
        if (currentProjectIdFilter==0) {
            // No filter > return all tasks
            return taskDataSource.getTasks();
        } else {
            // filter on Project Id
            return taskDataSource.getTasksByProjectId(currentProjectIdFilter);
        }
    }

    public void setCurrentProjectIdFilter(long idProject) {
        this.currentProjectIdFilter = idProject;
    }

    public void createTask(Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskDataSource.deleteTask(task));
    }

}
