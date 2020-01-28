package com.example.taskmanager;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.taskmanager.Activities.CreateProjectActivity;
import com.example.taskmanager.Activities.LoginActivity;
import com.example.taskmanager.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




@RunWith(AndroidJUnit4.class)
public class CreateProjectTest {

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<CreateProjectActivity> activityTestRule2 =
            new ActivityTestRule<>(CreateProjectActivity.class);

    @Before
    public void setup() {
        onView(withId(R.id.login_user_email)).perform(typeText("123@123.com"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(typeText("12345678"), closeSoftKeyboard());
        onView(withId(R.id.login_user_button)).perform(click());

        onView(isRoot()).perform( waitFor(2000));

        onView(withId(R.id.fab_projects)).perform(click());

        onView(isRoot()).perform( waitFor(1500));

        onView(withId(R.id.project_name)).perform(typeText("Project name"),  closeSoftKeyboard());
        onView(withId(R.id.collab_address)).perform(typeText("andrej@artistika.com"), closeSoftKeyboard());

    }


    @Test
    public void checkEmptyProjectName() {

        onView(withId(R.id.project_name)).perform(clearText());

        onView(withId(R.id.add_collab_button)).perform(click());

        onView(withId(R.id.fab_new_collaborator)).perform(click());

        onView(withId(R.id.project_name)).check(matches(withError("Must not be left blank!" )));

    }

    @Test
    public void checkOwnCollabAddress() {

        onView(withId(R.id.collab_address)).perform(clearText());

        onView(withId(R.id.collab_address)).perform(typeText("123@123.com"), closeSoftKeyboard());

        onView(withId(R.id.add_collab_button)).perform(click());

        onView(withId(R.id.fab_new_collaborator)).perform(click());



        onView(withText("You cannot add yourself as a collaborator"))
                .inRoot(withDecorView(not(activityTestRule2.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }

    @Test
    public void addCollaboratorTwice() {


        onView(withId(R.id.add_collab_button)).perform(click());

        onView(withId(R.id.collab_address)).perform(typeText("andrej@artistika.com"), closeSoftKeyboard());

        onView(withId(R.id.add_collab_button)).perform(click());

        onView(isRoot()).perform( waitFor(2500));


        onView(withText("You have already added that collaborator"))
                .inRoot(withDecorView(not(activityTestRule2.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }

    @Test
    public void addNonExistantCollaborator() {


        onView(withId(R.id.collab_address)).perform(clearText());


        onView(withId(R.id.collab_address)).perform(typeText("fake@none.com"), closeSoftKeyboard());

        onView(withId(R.id.add_collab_button)).perform(click());



        onView(withText("There is no user with that e-mail in the database, please try again"))
                .inRoot(withDecorView(not(activityTestRule2.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

    }

    @Test
    public void successfullCreatingProject() {

        onView(withId(R.id.fab_new_collaborator)).perform(click());

    }


    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof EditText) {
                    return ((EditText)item).getError().toString().equals(expected);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message" + expected + ", find it!");
            }
        };
    }
}
