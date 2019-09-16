package com.example.taskmanager.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taskmanager.DetailedMeetingActivity;
import com.example.taskmanager.Models.Meeting;
import com.example.taskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder> {

    private List<Meeting> meetings;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    public MeetingListAdapter(List<Meeting> meetings) {
        this.meetings = meetings;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Meeting meeting = meetings.get(position);

        holder.meetingName.setText(meeting.getName());
        holder.meetingDate.setText(meeting.getTime());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailedMeetingActivity.class);

                intent.putExtra("Meeting name", meeting.getName());
                intent.putExtra("Meeting description", meeting.getDescription());
                intent.putExtra("Meeting date", meeting.getTime());
                intent.putExtra("Meeting agenda", meeting.getAgenda());
                intent.putExtra("Meeting location", meeting.getLocation());
                intent.putExtra("Meeting id",meeting.getId());
                System.out.println("EVE KO CE KLIKNIS KOJ ID GO IMA" + meeting.getId());
                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView meetingName, meetingDate;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.meeting_item_parent_layout);
            meetingDate = itemView.findViewById(R.id.meeting_item_meeting_date);
            meetingName = itemView.findViewById(R.id.meeting_item_meeting_name);
        }
    }
}
