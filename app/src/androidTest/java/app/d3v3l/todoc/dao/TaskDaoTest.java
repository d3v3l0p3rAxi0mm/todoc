package app.d3v3l.todoc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    private final Project projet4 = new Project(4, "Projet for Test To remove", 0xFFB4DAE7);
    private final Task NEW_TASK_A = new Task(LUCIDIA_PROJECT_ID, "NEW TASK A, TS 10000", 10000);
    private final Task NEW_TASK_B = new Task(LUCIDIA_PROJECT_ID, "NEW TASK B, TS 10500", 10500);
    private final Task NEW_TASK_C = new Task(LUCIDIA_PROJECT_ID, "NEW TASK C, TS 10200", 10200);
    private final Task NEW_TASK_D = new Task(LUCIDIA_PROJECT_ID, "NEW TASK D, TS 10100", 10100);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb()  throws InterruptedException {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), TodocDatabase.class)
                .allowMainThreadQueries().build();
    }

    @After
    public void closeDb() {
        this.database.close();
    }

    /*
    @Test
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.userDao().createUser(USER_DEMO);
        // TEST
        User user = LiveDataTestUtil.getValue(this.database.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }
*/

        /*
    @Test
    public void testCrudOnProjectTable() throws InterruptedException {
        // Init : should be 0
        List<Project> projectsInit = LiveDataTestUtil.getValue(this.database.projectDao().getProjects());
        assertEquals(0,projectsInit.size());
        this.database.projectDao().createProject(projet1);
        this.database.projectDao().createProject(projet2);
        this.database.projectDao().createProject(projet3);
        this.database.projectDao().createProject(projet4);
        // After adding 4 projects : should be 4
        List<Project> projectsAfterCreate = LiveDataTestUtil.getValue(this.database.projectDao().getProjects());
        assertEquals(4,projectsAfterCreate.size());
        // After remove the last inserted project : should be 3
        this.database.projectDao().deleteProject(projet4);
        List<Project> projectsAfterDelete = LiveDataTestUtil.getValue(this.database.projectDao().getProjects());
        assertEquals(3,projectsAfterDelete.size());
    }
         */


    @Test
    public void getTasksAtStart() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasksByProject(LUCIDIA_PROJECT_ID));
        assertEquals(0,tasks.size());
    }
/*
    @Test
    public void insertAndGetItems() throws InterruptedException {
        // BEFORE : Adding demo user & demo items

        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        this.database.itemDao().insertItem(NEW_ITEM_IDEA);
        this.database.itemDao().insertItem(NEW_ITEM_RESTAURANTS);

        // TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID)).get(0);
        itemAdded.setSelected(true);
        this.database.itemDao().updateItem(itemAdded);

        //TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 1 && items.get(0).getSelected());
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID)).get(0);
        this.database.itemDao().deleteItem(itemAdded.getId());

        //TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }

     */

}
