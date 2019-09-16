package com.example.taskmanager.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.Activities.EditProjectInfoActivity;
import com.example.taskmanager.Activities.ProfileProjectsMeetingsActivity;
import com.example.taskmanager.Adapters.ProjectListAdapter;
import com.example.taskmanager.Activities.CreateProjectActivity;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserProject;
import com.example.taskmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {
    private static final String TAG = "ProjectsFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton fabAddProject;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseProjects;
    private ArrayList<Project> userProjects;
    ProjectListAdapter adapter;

    private List<View> projectManagerViews;

    private boolean semaphore = true;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreate called");


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseProjects = FirebaseDatabase.getInstance().getReference().child("projects");
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());
        userProjects = new ArrayList<>();

//        getProjectsForCurrectUser();


//        mDatabaseProjects.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    getProjectsForCurrectUser();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        mDatabaseCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                getProjectsForCurrectUser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getProjectsForCurrectUser() {
        Log.i(TAG, "getProjectForCurrentUser called");
        userProjects = new ArrayList<>();
        mDatabaseCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                for(UserProject up:currentUser.getProjectList()){
                    DatabaseReference currProject = mDatabaseProjects.child(up.projectID);
                    currProject.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Project project = dataSnapshot.getValue(Project.class);
                            if(project != null) {
                                userProjects.add(project);
                                Log.i(TAG, "adding projects" + project.name);
                            }
                            setAdapter();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void setAdapter() {
        Log.i(TAG, "Adapter is setting up, userprojects look like " + userProjects.toString());
        ProfileProjectsMeetingsActivity ppma = (ProfileProjectsMeetingsActivity) getActivity();

        adapter = new ProjectListAdapter(userProjects, this.getActivity(), this);
        recyclerView.setAdapter(adapter);
        ppma.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        recyclerView = (RecyclerView)  view.findViewById(R.id.recycler_view);
        Log.i(TAG, "onCreateView called");
        setAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        fabAddProject = (FloatingActionButton) view.findViewById(R.id.fab_projects);

        /**
         *
         */


        fabAddProject.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), CreateProjectActivity.class);

                Log.i(TAG, "STARTS FOR ACTIVITY");
                startActivityForResult(intent, 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult" + (requestCode == Activity.RESULT_OK));
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 3){
            getProjectsForCurrectUser();
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

    public void startEditSettingsActivity(Project p) {
        Intent intent = new Intent(getContext(), EditProjectInfoActivity.class);
        intent.putExtra("currentProject", p);
        startActivityForResult(intent, 3);
    }


}
