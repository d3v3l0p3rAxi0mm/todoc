package app.d3v3l.todoc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static app.d3v3l.todoc.model.Project.getAllProjects;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;
import app.d3v3l.todoc.repositories.ProjectDataRepository;
import app.d3v3l.todoc.repositories.TaskDataRepository;
import app.d3v3l.todoc.ui.MainActivityViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelUnitTest {

    //--------------------------------------------------
    // Fields
    //--------------------------------------------------

    //MainViewModel LiveDatas from repo to be mocked
    private final MutableLiveData<List<Project>> mListOfProjectMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> mListOfTaskMutableLiveData = new MutableLiveData<>();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    //set Task  :
    Task testTask0 = new Task(1, "iii task 0", new Date().getTime());
    Task testTask1 = new Task(1, "hhh task 1", new Date().getTime() + 1);
    Task testTask2 = new Task(2,  "ggg task 2", new Date().getTime() + 2);
    Task testTask3 = new Task(2,  "fff task 3", new Date().getTime() + 3);
    Task testTask4 = new Task(3,  "eee task 4", new Date().getTime() + 4);
    Task testTask5 = new Task(3,  "ddd task 5", new Date().getTime() + 5);
    Task testTask6 = new Task(3,  "ccc task 6", new Date().getTime() + 6);
    Task testTask7 = new Task(2,  "bbb task 7", new Date().getTime() + 7);
    Task testTask8 = new Task(1, "aaa task 8", new Date().getTime() + 8);
    Task testTask9 = new Task(3, "zzz task 9", new Date().getTime() + 9);



    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();
    @Mock // ProjectRepository to use on test
    private ProjectDataRepository mockProjectRepository;
    @Mock // TaskRepository to use on test
    private TaskDataRepository mockTaskRepository;
    // ViewModel to use on test
    private MainActivityViewModel underTestMainActivityViewModel;
    // Executor to use on test
    private final Executor testExecutor = MainActivityViewModel.getExecutor();

    @Before
    public void setup() {
        //Mock LiveDatas from repositories
        mocking_mListOfProjectMutableLiveData();
        mocking_mListOfTaskMutableLiveData();
        //Instantiate MainViewModel for testing, passing mocked repositories
        underTestMainActivityViewModel = new MainActivityViewModel(mockProjectRepository, mockTaskRepository, testExecutor);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void insertTask() {
        // When : insert Task 9
        underTestMainActivityViewModel.createTask(testTask9);
        // Then : check if repository is called 1 time when we insert the task
        verify(mockTaskRepository, times(1)).createTask(testTask9);
    }

    @Test
    public void removeTask() {
        // When : remove Task 8
        underTestMainActivityViewModel.deleteTask(testTask8);
        // Then : check if repository is called 1 time when we insert the task
        verify(mockTaskRepository, times(1)).deleteTask(testTask8);
    }

    @Test
    public void getTasks() {
        // When : remove Task 8
        underTestMainActivityViewModel.getTasks();
        // Then : check if repository is called 1 time when we insert the task
        verify(mockTaskRepository, times(1)).getTasks();
    }



    private void mocking_mListOfProjectMutableLiveData() {
        mListOfProjectMutableLiveData.setValue(getAllProjectsForTest());
        doReturn(mListOfProjectMutableLiveData).when(mockProjectRepository).getAllProjects();
    }

    private void mocking_mListOfTaskMutableLiveData() {
        List<Task> listOfTask = Arrays.asList(
                testTask0, testTask1, testTask2, testTask3,
                testTask4, testTask5, testTask6, testTask7, testTask8
        );
        mListOfTaskMutableLiveData.setValue(listOfTask);
        doReturn(mListOfTaskMutableLiveData).when(mockTaskRepository).getTasks();
    }

    // Setting Project Tasks
    private List<Project> getAllProjectsForTest() {
        return new ArrayList<>(Arrays.asList(getAllProjects()));
    }


}