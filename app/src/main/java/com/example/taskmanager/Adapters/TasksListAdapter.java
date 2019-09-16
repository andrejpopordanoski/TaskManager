package com.example.taskmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Activities.TaskDetails;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder>{
    private static final String TAG = "TasksListAdapter";
    private List<Task> mTasks;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseCurrentProject;
    private Project currentProject;
//    private Context mContext;

    public TasksListAdapter(List<Task> tasks, Project currentProject) {
//        this.mContext = mContext;
        this.mTasks = tasks;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        this.currentProject  = currentProject;
    }

    public void setProjectList(ArrayList<Task> mTasks){
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);


        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder called");
        final Task task = mTasks.get(position);

        holder.taskName.setText(task.name);
        holder.dateTextView.setText(task.getDateFormatted());

        if (mTasks.get(position).inProgress)
            holder.inProgressView.setVisibility(View.VISIBLE);
        else
            holder.inProgressView.setVisibility(View.INVISIBLE);


        if(mTasks.get(position).priority.equals("High")){
            holder.priorityLayout.setBackgroundColor(Color.parseColor("#e91e63"));
        }

        if(mTasks.get(position).priority.equals("Medium")){
            holder.priorityLayout.setBackgroundColor(Color.parseColor("#66bb6a"));
        }
        if(mTasks.get(position).priority.equals("Low")){
            holder.priorityLayout.setBackgroundColor(Color.parseColor("#e2e619"));
        }

        if(mTasks.get(position).testFailed){

            holder.inProgressView.setBackgroundResource(R.drawable.ic_indeterminate_check_box_black_24dp);
            holder.inProgressView.setVisibility(View.VISIBLE);
        }

        holder.parentLayout.setClickable(true);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click on the field with the name " + mTasks.get(position).name);
                Intent intent = new Intent(v.getContext(), TaskDetails.class);
                intent.putExtra("currentTask", mTasks.get(position));
                intent.putExtra("currentProject", currentProject);

                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        LinearLayout parentLayout;
        TextView dateTextView;
        LinearLayout priorityLayout;
        ImageView inProgressView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = (TextView) itemView.findViewById(R.id.taskName);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
            dateTextView = (TextView) itemView.findViewById(R.id.taskDueDate);
            priorityLayout = (LinearLayout) itemView.findViewById(R.id.taskPriority);
            inProgressView = (ImageView) itemView.findViewById(R.id.in_progress_view);
        }
    }
}
