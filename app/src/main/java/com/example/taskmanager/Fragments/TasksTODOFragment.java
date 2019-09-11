package com.example.taskmanager.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.Adapters.ProjectListAdapter;
import com.example.taskmanager.Adapters.TasksListAdapter;
import com.example.taskmanager.CreateTaskActivity;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TasksTODOFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TasksTODOFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksTODOFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Project currentProject;
    private RecyclerView recyclerView;
    private List<Task> taskList;
    private DatabaseReference mDatabaseCurrentProject;


    private FloatingActionButton fab;
    private TasksListAdapter adapter;

    public TasksTODOFragment() {
        // Required empty public constructor
    }

    public TasksTODOFragment(Project currentProject) {

        this.currentProject = currentProject;
        mDatabaseCurrentProject = FirebaseDatabase.getInstance().getReference().child("projects").child(this.currentProject.projectId);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksTODOFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksTODOFragment newInstance(String param1, String param2) {
        TasksTODOFragment fragment = new TasksTODOFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        taskList = currentProject.getAllTasksFromState("TODO");

        setAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tasks_todo, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_new_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateTaskActivity.class);
                intent.putExtra("currentProject", currentProject);
                startActivityForResult(intent, 2);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public void openCreateTaskActivity(View view) {
        Intent intent = new Intent(view.getContext(), CreateTaskActivity.class);
        startActivityForResult(intent, 2);

    }

    private void getTasksForCurrentUser (){
        mDatabaseCurrentProject.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                currentProject = project;
                taskList = project.getAllTasksFromState("TODO");

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            getTasksForCurrentUser();
        }
    }

    public void setAdapter() {
        adapter = new TasksListAdapter(taskList);
        recyclerView.setAdapter(adapter);
    }
}
