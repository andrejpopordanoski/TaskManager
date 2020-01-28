package com.example.taskmanager;

import com.example.taskmanager.Activities.LoginActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class FirebaseMockigTest {

    static final String ANY_EMAIL = "email@email.com";

    static final String ANY_PASSWORD = "ANY_PASSWORD";

    static ArgumentCaptor<OnCompleteListener> testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);
    static ArgumentCaptor<OnSuccessListener> testOnSuccessListener = ArgumentCaptor.forClass(OnSuccessListener.class);
    static ArgumentCaptor<OnFailureListener> testOnFailureListener = ArgumentCaptor.forClass(OnFailureListener.class);
    static <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private Task<AuthResult> authResultTask;



    @Mock
    private AuthResult authResult;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        setupTask(authResultTask);

        when(firebaseAuth.signInWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD)).thenReturn(authResultTask);


//        when(firebaseAuth.signInWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD)).thenReturn(authResultTask)
    }

    @Test
    public void createUserWithEmailAndPassword() {
        TestObserver<AuthResult> authTestObserver = LoginActivity
                .createUserWithEmailAndPassword(firebaseAuth, ANY_EMAIL, ANY_PASSWORD);

        testOnSuccessListener.getValue().onSuccess(authResult);
        testOnCompleteListener.getValue().onComplete(authResultTask);

        verify(firebaseAuth).createUserWithEmailAndPassword(ANY_EMAIL, ANY_PASSWORD);

        authTestObserver.assertNoErrors()
                .assertValueCount(1)
                .assertValueSet(Collections.singletonList(authResult))
                .assertComplete()
                .dispose();
    }



}
