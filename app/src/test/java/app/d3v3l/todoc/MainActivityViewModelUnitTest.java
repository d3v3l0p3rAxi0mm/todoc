package app.d3v3l.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import app.d3v3l.todoc.model.Task;
import app.d3v3l.todoc.ui.MainActivityViewModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();
    private MainActivityViewModel mainActivityViewModel;

    @Before
    public void setup() {

    }

    @Test
    public void createTaskInRepositoryIncreaseByOneNumberOfTask() {
        Task task = new Task(1, "Task demo", new Date().getTime());

    }

    @Test
    public void removeTaskDecreaseByOneNumberOfTask() {

    }
}