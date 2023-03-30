package app.d3v3l.todoc.utils;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import app.d3v3l.todoc.R;

public class DeleteFirstTaskInTasksList implements ViewAction {

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public void perform(UiController uiController, View view) {
        View clickZone = view.findViewById(R.id.img_delete);
        clickZone.performClick();
    }

    @Override
    public String getDescription() {
        return "Click on a Meeting in the RecyclerView";
    }

}
