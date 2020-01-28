package com.example.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.taskmanager.Activities.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import org.hamcrest.Description;
import org.hamcrest.EasyMock2Matchers;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.util.concurrent.Executor;

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
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);


    FirebaseAuth firebaseAuth;

    @Before
    public void setup() {

    }


    @Test
    public void signInWithEmailAndPassword() {

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.taskmanager", appContext.getPackageName());
    }

    @Test
    public void useCanEnterEmail() {
        onView(withId(R.id.login_user_email)).perform(typeText("username"));
    }

    @Test
    public void userCanEnterPassword() {
        onView(withId(R.id.login_user_password)).perform(typeText("password"));
    }

    @Test
    public void emailIsEmpty() {
        onView(withId(R.id.login_user_email)).perform(clearText());
        onView(withId(R.id.login_user_button)).perform(click());
        onView(withId(R.id.login_user_email)).check(matches(withError("Email field is empty")));
    }

    @Test
    public void passwordIsEmpty() {
        onView(withId(R.id.login_user_email)).perform(typeText("example@email.com"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(clearText());
        onView(withId(R.id.login_user_button)).perform(click());
        onView(withId(R.id.login_user_password)).check(matches(withError("Password field is empty")));
    }

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.login_user_email)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(typeText("123456789"), closeSoftKeyboard());

        onView(withId(R.id.login_user_button)).perform(click());
        onView(withId(R.id.login_user_email)).check(matches(withError("Email address is badly formatted")));
    }

    @Test
    public void passwordIsTooShort() {
        onView(withId(R.id.login_user_email)).perform(typeText("email@email.com"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.login_user_button)).perform(click());
        onView(withId(R.id.login_user_password)).check(matches(withError("Password is too short. Must be more than 8 characters")));
    }

    @Test
    public void loginFailedByIncorrectAddress() {
        onView(withId(R.id.login_user_email)).perform(typeText("incorrect@.com"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(typeText("123456789"), closeSoftKeyboard());
        onView(withId(R.id.login_user_button)).perform(click());
        onView(withText("The email address is badly formatted."))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void loginFailedByIncorrectCredentials() {
//
//
//        onView(withId(R.id.login_user_email)).perform(typeText("incorrect@123.com"), closeSoftKeyboard());
//        onView(withId(R.id.login_user_password)).perform(typeText("123456789"), closeSoftKeyboard());
//        onView(withId(R.id.login_user_button)).perform(click());
//        onView(withText("The email address is badly formatted."))
//                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
//                .check(matches(isDisplayed()));
//    }


    @Test
    public void loginSuccessfully_shouldShowToast() {


//
//        firebaseAuth = Mockito.mock(FirebaseAuth.class);
//        Mockito.when(firebaseAuth.signInWithEmailAndPassword("123@123.com", "123123123")).thenReturn(new Task<AuthResult>() {
//            @Override
//            public boolean isComplete() {
//                return true;
//            }
//
//            @Override
//            public boolean isSuccessful() {
//                return true;
//            }
//
//            @Override
//            public boolean isCanceled() {
//                return false;
//            }
//
//            @Nullable
//            @Override
//            public AuthResult getResult() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Exception getException() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
//                return null;
//            }
//        });



        onView(withId(R.id.login_user_email)).perform(typeText("123@123.com"), closeSoftKeyboard());
        onView(withId(R.id.login_user_password)).perform(typeText("12345678"), closeSoftKeyboard());
        onView(withId(R.id.login_user_button)).perform(click());
        onView(withText("Login was successful"))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }



    private String getString(@StringRes int resourceId) {
        return activityTestRule.getActivity().getString(resourceId);
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
