package app.d3v3l.todoc.dao;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import app.d3v3l.todoc.database.TodocDatabase;
import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;
import app.d3v3l.todoc.utils.LiveDataTestUtil;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {


    private TodocDatabase database;

    // DATA SET FOR TEST
    private final int TARTAMPION_PROJECT_ID = 1;
    private final int LUCIDIA_PROJECT_ID = 2;
    private final int CIRCUS_PROJECT_ID = 3;
    private final Project projet1 = new Project(1, "Projet Tartampion", 0xFFEADAD1);
    private final Project projet2 = new Project(2, "Projet Lucidia", 0xFFB4CDBA);
    private final Project projet3 = new Project(3, "Projet Circus", 0xFFA3CED2);
    private final Task NEW_TASK_A = new Task(LUCIDIA_PROJECT_ID, "NEW TASK A, TS 10000", 10000);
    private final Task NEW_TASK_B = new Task(LUCIDIA_PROJECT_ID, "NEW TASK B, TS 10500", 10500);
    private final Task NEW_TASK_C = new Task(LUCIDIA_PROJECT_ID, "NEW TASK C (Lucidia), TS 10200", 10200);
    private final Task NEW_TASK_D = new Task(TARTAMPION_PROJECT_ID, "NEW TASK D (Tartampion), TS 10400", 10400);
    private final Task NEW_TASK_E = new Task(CIRCUS_PROJECT_ID, "NEW TASK E (Circus), TS 10300", 10300);
    private final Task NEW_TASK_F = new Task(LUCIDIA_PROJECT_ID, "NEW TASK F (Lucidia), TS 10800", 10800);
    private final Task NEW_TASK_G = new Task(CIRCUS_PROJECT_ID, "NEW TASK G (Circus), TS 10600", 10600);
    private final Task NEW_TASK_H = new Task(CIRCUS_PROJECT_ID, "NEW TASK H (Circus), TS 11000", 11000);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), TodocDatabase.class)
                .allowMainThreadQueries().build();
        this.database.projectDao().createProject(projet1);
        this.database.projectDao().createProject(projet2);
        this.database.projectDao().createProject(projet3);
    }

    @After
    public void closeDb() {
        this.database.close();
    }

    @Test
    public void getTasksAtStart() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(0,tasks.size());
    }

    @Test
    public void getTasksByProject() throws InterruptedException {
        // Test if no task in DB
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(0, tasks.size());
        // BEFORE : Adding 3 Tasks in each project
        this.database.taskDao().createTask(NEW_TASK_C);
        this.database.taskDao().createTask(NEW_TASK_D);
        this.database.taskDao().createTask(NEW_TASK_E);
        this.database.taskDao().createTask(NEW_TASK_F);
        this.database.taskDao().createTask(NEW_TASK_G);
        this.database.taskDao().createTask(NEW_TASK_H);
        // TEST if 3 tasks have been created
        // Test if no task in DB
        List<Task> tasksAfterTasksCreating = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(6, tasksAfterTasksCreating.size());
        // TEST Number of Tasks FOR EACH PROJECT
        List<Task> tartampionTasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasksByProject(TARTAMPION_PROJECT_ID));
        assertEquals(1, tartampionTasks.size());
        List<Task> lucidiaTasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasksByProject(LUCIDIA_PROJECT_ID));
        assertEquals(2, lucidiaTasks.size());
        List<Task> circusTasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasksByProject(CIRCUS_PROJECT_ID));
        assertEquals(3, circusTasks.size());
    }

    @Test
    public void insertTasksAndVerify() throws InterruptedException {
        // BEFORE : Adding 3 Tasks
        this.database.taskDao().createTask(NEW_TASK_A);
        this.database.taskDao().createTask(NEW_TASK_B);
        this.database.taskDao().createTask(NEW_TASK_C);
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(3, tasks.size());
    }

    @Test
    public void insertAndDeleteTasks() throws InterruptedException {
        // BEFORE : Adding 3 Tasks
        this.database.taskDao().createTask(NEW_TASK_A);
        this.database.taskDao().createTask(NEW_TASK_B);
        this.database.taskDao().createTask(NEW_TASK_C);
        //TEST 1 must have 3 tasks
        List<Task> tasksForTest1 = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(3, tasksForTest1.size());
        // BEFORE : Remove 2 Tasks
        this.database.taskDao().deleteTask(tasksForTest1.get(0)); // don't call NEW_TASK_A because Task Id in database is autogenerate
        this.database.taskDao().deleteTask(tasksForTest1.get(1));
        //TEST 2 must have 1 task left
        List<Task> tasksForTest2 = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(1, tasksForTest2.size());
    }

}
