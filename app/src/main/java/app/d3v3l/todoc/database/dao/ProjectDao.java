package app.d3v3l.todoc.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import app.d3v3l.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProject(Project project);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getProjects();

    @Query("SELECT * FROM Project")
    Cursor getProjectsWithCursor();

    @Query("SELECT * FROM Project WHERE id = :project_id")
    LiveData<Project> getProjectById(int project_id);

    @Query("SELECT * FROM Project WHERE id = :project_id")
    Cursor getProjectByIdWithCursor(int project_id);

    @Update
    void updateProject(Project project);

    @Delete
    void deleteProject(Project project);

}
