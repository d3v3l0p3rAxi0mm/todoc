package app.d3v3l.todoc.repositories;

import androidx.lifecycle.LiveData;

import app.d3v3l.todoc.database.dao.TaskDao;
import app.d3v3l.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) { this.taskDao = taskDao; }

    // --- GET ---
    public LiveData<List<Task>> getTasks(){
        return this.taskDao.getTasks();
    }

    public LiveData<List<Task>> getTasksByProjectId(long projectId){
        return this.taskDao.getTasksByProject(projectId);
    }

    // --- CREATE ---
    public void createTask(Task task){ taskDao.createTask(task); }

    // --- DELETE ---
    public void deleteTask(Task task){ taskDao.deleteTask(task); }

    // --- UPDATE ---
    public void updateTask(Task task){ taskDao.updateTask(task); }

}

