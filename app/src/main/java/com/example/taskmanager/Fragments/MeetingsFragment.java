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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.taskmanager.Adapters.MeetingListAdapter;
import com.example.taskmanager.CreateMeetingActivity;
import com.example.taskmanager.Models.Meeting;
import com.example.taskmanager.Models.User;
import com.example.taskmanager.Models.UserMeeting;
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
 * {@link MeetingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int totalNumberOfMeetings;
    private int numberOfReads;

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private LinearLayout noMeetingsFragment;
    private LinearLayout progressbarLayout;

    //DATABASE STUFF

    private FirebaseAuth firebaseAuthInstance;
    private FirebaseUser currentUser;
    private DatabaseReference userMeetingsDbReference;
    private DatabaseReference currentUserDatabaseReference;
    private DatabaseReference mDatabaseCurrentUser;

    private List<Meeting> meetings;
    private MeetingListAdapter adapter;

    public MeetingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingsFragment newInstance(String param1, String param2) {
        MeetingsFragment fragment = new MeetingsFragment();
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
        firebaseAuthInstance = FirebaseAuth.getInstance();
        currentUser = firebaseAuthInstance.getCurrentUser();
        userMeetingsDbReference = FirebaseDatabase.getInstance().getReference().child("meetings");
        currentUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        numberOfReads = 0;
        totalNumberOfMeetings = 0;

        meetings = new ArrayList<>();

        currentUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMeetingsForCurrentUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMeetingsForCurrentUser() {
        meetings = new ArrayList<>();


        currentUserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                totalNumberOfMeetings = currentUser.meetingList.size();

                if(totalNumberOfMeetings == 0){
                    setAdapter();
                }
                else {
                    progressbarLayout.setVisibility(View.VISIBLE);
                }
                for (UserMeeting userMeeting : currentUser.getMeetingList()){
                    DatabaseReference currentMeeting = FirebaseDatabase.getInstance().getReference()
                            .child("meetings").child(userMeeting.meetingId);
                    currentMeeting.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Meeting meeting = dataSnapshot.getValue(Meeting.class);
                            if(meeting != null){
                                meetings.add(meeting);
                                numberOfReads++;
                                if(numberOfReads == totalNumberOfMeetings) {
                                    setAdapter();
                                }
                            }

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

    public void setAdapter(){

        totalNumberOfMeetings = 0;
        numberOfReads = 0;

        progressbarLayout.setVisibility(View.GONE);

        if(meetings.size() == 0 ){
            noMeetingsFragment.setVisibility(View.VISIBLE);
        }
        else {
            noMeetingsFragment.setVisibility(View.GONE);
        }
        adapter = new MeetingListAdapter(meetings);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_meetings, container, false);
        floatingActionButton = view.findViewById(R.id.meetings_floating_action_button);
        recyclerView = view.findViewById(R.id.fragment_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        noMeetingsFragment = (LinearLayout) view.findViewById(R.id.no_meeting_layout);
        progressbarLayout = (LinearLayout) view.findViewById(R.id.progress_bar_layout);
        progressbarLayout.setVisibility(View.VISIBLE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateMeetingActivity.class);

                startActivityForResult(intent,2);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
//            getMeetingsForCurrentUser();
        }

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
}
