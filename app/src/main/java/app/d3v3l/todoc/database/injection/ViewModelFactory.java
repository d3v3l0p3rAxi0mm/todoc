package app.d3v3l.todoc.database.injection;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import app.d3v3l.todoc.database.TodocDatabase;
import app.d3v3l.todoc.repositories.ProjectDataRepository;
import app.d3v3l.todoc.repositories.TaskDataRepository;
import app.d3v3l.todoc.ui.MainActivityViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        TodocDatabase database = TodocDatabase.getInstance(context);
        this.projectDataSource = new ProjectDataRepository(database.projectDao());
        this.taskDataSource = new TaskDataRepository(database.taskDao());
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(taskDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}