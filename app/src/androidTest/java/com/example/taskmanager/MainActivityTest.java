package com.example.taskmanager;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    FirebaseAuth firebaseAuth;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup() {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            if(!firebaseAuth.getCurrentUser().getEmail().equals("123@123.com")) {
                firebaseAuth.getCurrentUser().delete();
            }
        }


         onView(withId(R.id.reg_user_name)).perform(typeText("username"), closeSoftKeyboard());
        onView(withId(R.id.reg_user_address)).perform(typeText("Adress num 15"), closeSoftKeyboard());
        onView(withId(R.id.reg_user_phone)).perform(typeText("075522121"), closeSoftKeyboard());
        onView(withId(R.id.create_meeting_name)).perform(typeText("testing_email@fakeEmail.com"), closeSoftKeyboard());
        onView(withId(R.id.reg_user_password)).perform(typeText("12345678"), closeSoftKeyboard());
    }


    @Test
    public void checkEmptyUserName() {
        onView(withId(R.id.reg_user_name)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_name)).check(matches(withError("Name must not be empty")));
    }

    @Test
    public void checkEmptyAddress() {
        onView(withId(R.id.reg_user_address)).perform(clearText());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_address)).check(matches(withError("Address must not be empty")));
    }

    @Test
    public void checkEmptyPhoneNumber() {
        onView(withId(R.id.reg_user_phone)).perform(clearText());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_phone)).check(matches(withError("Phone must not be empty")));
    }


    @Test
    public void checkEmptyEmail() {
        onView(withId(R.id.create_meeting_name)).perform(clearText());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.create_meeting_name)).check(matches(withError("Email empty or not in correct format!")));
    }

    @Test
    public void checkEmptyPassword() {
        onView(withId(R.id.reg_user_password)).perform(clearText());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_password)).check(matches(withError("Password must be more than 8 characters")));
    }

    @Test
    public void checkLengthPassword() {
        onView(withId(R.id.reg_user_password)).perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.reg_user_password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_password)).check(matches(withError("Password must be more than 8 characters")));
    }


    @Test
    public void checkFormatPhoneNumber() {
        onView(withId(R.id.reg_user_phone)).perform(clearText());

        onView(withId(R.id.reg_user_phone)).perform(typeText("123123"), closeSoftKeyboard());
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.reg_user_phone)).check(matches(withError("Phone number should be a 9 digit number")));
    }

    @Test
    public void checkFormatEmail() {
        onView(withId(R.id.create_meeting_name)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.create_meeting_name)).perform(typeText("incorrect@format"), closeSoftKeyboard());

        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withId(R.id.create_meeting_name)).check(matches(withError("Email empty or not in correct format!")));
    }

    @Test
    public void checkSuccessfullRegister() {
        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withText("Registered successfully"))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkFailRegister() {
        onView(withId(R.id.create_meeting_name)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.create_meeting_name)).perform(typeText("123@123.com"), closeSoftKeyboard());

        onView(withId(R.id.reg_user_button)).perform(click());
        onView(withText("The email address is already in use by another account."))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
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
