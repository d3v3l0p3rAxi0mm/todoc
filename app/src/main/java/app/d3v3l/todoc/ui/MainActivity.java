package app.d3v3l.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.d3v3l.todoc.R;
import app.d3v3l.todoc.injection.ViewModelFactory;
import app.d3v3l.todoc.model.Project;
import app.d3v3l.todoc.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    private MainActivityViewModel viewModel;

    /**
     * List of all projects available in the application
     */
    private final Project[] allProjects = Project.getAllProjects();

    /**
     * List of all current tasks of the application
     */
    @NonNull
    private List<Task> tasks = new ArrayList<>();
    /**
     * List of filtered tasks by project of the application
     */
    @NonNull
    private List<Task> filteredtasks = new ArrayList<>();

    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, this);

    /**
     * The sort method to be used to display tasks
     */
    @NonNull
    private SortMethod sortMethod = SortMethod.OLD_FIRST;


    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;

    private TextView lblProjectName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureViewModel();
        getTasks();

        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        lblProjectName = findViewById(R.id.lbl_optional_name_of_project);

        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    private void configureViewModel() {
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainActivityViewModel.class);
    }

    private void getTasks() {
        viewModel.getTasks().observe(this, this::updateTasks);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If user click on sub-menu item
        if (!item.toString().equals("Filter") && !item.toString().equals("Order")) {
            int id = item.getItemId();

            if (id == R.id.filter_alphabetical ||
                id == R.id.filter_alphabetical_inverted ||
                id == R.id.filter_oldest_first ||
                id == R.id.filter_recent_first)
            {
                if (id == R.id.filter_alphabetical) {
                    sortMethod = SortMethod.ALPHABETICAL;
                } else if (id == R.id.filter_alphabetical_inverted) {
                    sortMethod = SortMethod.ALPHABETICAL_INVERTED;
                } else if (id == R.id.filter_oldest_first) {
                    sortMethod = SortMethod.OLD_FIRST;
                } else { // if (id == R.id.filter_recent_first)
                    sortMethod = SortMethod.RECENT_FIRST;
                }
                updateTasksGui();
            } else {
                if (id == R.id.filter_allProjects) {
                    viewModel.setCurrentProjectIdFilter(0);
                    updateTasksByProjectGui(0);
                } else if (id == R.id.filter_tartampion) {
                    viewModel.setCurrentProjectIdFilter(1);
                    updateTasksByProjectGui(1);
                } else if (id == R.id.filter_lucidia) {
                    viewModel.setCurrentProjectIdFilter(2);
                    updateTasksByProjectGui(2);
                } else if (id == R.id.filter_circus) {
                    viewModel.setCurrentProjectIdFilter(3);
                    updateTasksByProjectGui(3);
                } else if (id == R.id.filter_alibaba) {
                    viewModel.setCurrentProjectIdFilter(4);
                    updateTasksByProjectGui(4);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {
        viewModel.deleteTask(task);
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                addTask(task);
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        populateDialogSpinner();
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        viewModel.createTask(task);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<Task> tasksList) {
        this.tasks = tasksList;
        // if current project is 0 (all projects) updateTasksByProjectGui will go on updateTasksGui
        updateTasksByProjectGui(viewModel.getCurrentProjectIdFilter());
    }

    private void updateTasksGui(){
        List<Task> listOfTask;
        if (viewModel.getCurrentProjectIdFilter()==0) {
            listOfTask = tasks;
        } else {
            listOfTask = filteredtasks;
        }
        if (listOfTask.size() == 0) {
            lblProjectName.setText(messageNoTask());
            lblNoTasks.setVisibility(View.VISIBLE);
            lblProjectName.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            lblProjectName.setVisibility(View.GONE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(listOfTask, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(listOfTask, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(listOfTask, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(listOfTask, new Task.TaskOldComparator());
                    break;
            }
            adapter.updateTasks(listOfTask);
        }
    }

    private void updateTasksByProjectGui(long projectId){

        if (tasks.size() == 0) {
            lblProjectName.setText(messageNoTask());
            lblProjectName.setVisibility(View.VISIBLE);
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            lblProjectName.setVisibility(View.GONE);
            filteredtasks = new ArrayList<>();
            if (projectId != 0) {
                for (Task task: tasks) {
                    if (task.getProjectId() == projectId) {
                        filteredtasks.add(task);
                    }
                }
            }
            updateTasksGui();
        }
    }

    private String messageNoTask() {
        String txt;
        if (viewModel.currentProjectIdFilter == 0) {
            txt = "";
        }
        else {
            txt = Objects.requireNonNull(Project.getProjectById(viewModel.currentProjectIdFilter)).getName();
        }
        return txt;
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });
        dialog = alertBuilder.create();
        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST
    }

}
