package app.d3v3l.todoc.repositories;

import androidx.lifecycle.LiveData;

import app.d3v3l.todoc.database.dao.ProjectDao;
import app.d3v3l.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET ---
    public LiveData<List<Project>> getAllProjects(){ return this.projectDao.getProjects(); }
    public LiveData<Project> getProjectById(int projectId){ return this.projectDao.getProjectById(projectId); }

    // --- CREATE ---
    public void createProject(Project project){ projectDao.createProject(project); }

    // --- DELETE ---
    public void deleteProject(Project project) { projectDao.deleteProject(project); }

    // --- UPDATE ---
    public void updateProject(Project project){ projectDao.updateProject(project); }

}
