package app.d3v3l.todoc;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static app.d3v3l.todoc.utils.TestUtils.withRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import app.d3v3l.todoc.ui.MainActivity;
import app.d3v3l.todoc.utils.DeleteFirstTaskInTasksList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    //public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addAndRemoveTask() {

        MainActivity activity = rule.getActivity();
        TextView lblNoTask = activity.findViewById(R.id.lbl_no_task);
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);
        ViewAction deleteFirstTaskInTasksList = new DeleteFirstTaskInTasksList();

        // empty the tasks
        int numberOfViewHolder = Objects.requireNonNull(listTasks.getAdapter()).getItemCount();
        for (int i = 0; i < numberOfViewHolder; i++) {
            onView(ViewMatchers.withId(R.id.list_tasks))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, deleteFirstTaskInTasksList));
        }


        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        // Check that lblTask is not displayed anymore
        assertThat(lblNoTask.getVisibility(), equalTo(View.GONE));
        // Check that recyclerView is displayed
        assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
        // Check that it contains one element only
        assertThat(listTasks.getAdapter().getItemCount(), equalTo(1));

        onView(withId(R.id.img_delete)).perform(click());

        // Check that lblTask is displayed
        assertThat(lblNoTask.getVisibility(), equalTo(View.VISIBLE));
        // Check that recyclerView is not displayed anymore
        assertThat(listTasks.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void sortTasks() {
        MainActivity activity = rule.getActivity();
        ViewAction deleteFirstTaskInTasksList = new DeleteFirstTaskInTasksList();
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);

        // empty the tasks
        int numberOfViewHolder = Objects.requireNonNull(listTasks.getAdapter()).getItemCount();
        for (int i = 0; i < numberOfViewHolder; i++) {
            onView(ViewMatchers.withId(R.id.list_tasks))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, deleteFirstTaskInTasksList));
        }


        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_order)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_order)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Sort old first
        onView(withId(R.id.action_order)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_order)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }


    @Test
    public void filterTasks() {
        MainActivity activity = rule.getActivity();
        ViewAction deleteFirstTaskInTasksList = new DeleteFirstTaskInTasksList();
        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);

        // empty the tasks
        int numberOfViewHolder = Objects.requireNonNull(listTasks.getAdapter()).getItemCount();
        for (int i = 0; i < numberOfViewHolder; i++) {
            onView(ViewMatchers.withId(R.id.list_tasks))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, deleteFirstTaskInTasksList));
        }

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa.Task Tartampion"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Tartampion")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("bbb.Task Lucidia"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Lucidia")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("ccc.Task Circus"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Circus")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.all_projects)).perform(click());

        onView(withId(R.id.action_order)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());


        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa.Task Tartampion")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("bbb.Task Lucidia")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("ccc.Task Circus")));

        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.tartampion)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa.Task Tartampion")));


        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.lucidia)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("bbb.Task Lucidia")));

        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.circus)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("ccc.Task Circus")));


    }


}
