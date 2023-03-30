package app.d3v3l.todoc.repositories;

import androidx.lifecycle.LiveData;

import app.d3v3l.todoc.database.dao.ProjectDao;
import app.d3v3l.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET ---
    /**
     * Gets all projects live data.
     *
     * @return the all projects live data
     */
    public LiveData<List<Project>> getAllProjects(){ return this.projectDao.getProjects(); }


}
