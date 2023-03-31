package app.d3v3l.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import app.d3v3l.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM Task WHERE projectId = :project_id")
    LiveData<List<Task>> getTasksByProject(long project_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createTask(Task task);

    @Delete
    void deleteTask(Task task);

}
