package com.example.taskmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Models.Project;
import com.example.taskmanager.Models.Task;
import com.example.taskmanager.R;
import com.example.taskmanager.TasksActivity;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder>{
    private static final String TAG = "TasksListAdapter";
    private List<Task> mTasks;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
//    private Context mContext;

    public TasksListAdapter(List<Task> tasks) {
//        this.mContext = mContext;
        this.mTasks = tasks;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
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


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click on the field with the name " + mTasks.get(position).name);
//                Intent intent = new Intent(v.getContext(), TasksActivity.class);
//                intent.putExtra("currentProject", mProjectNames.get(position));

//                v.getContext().startActivity(intent);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = (TextView) itemView.findViewById(R.id.taskName);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
            dateTextView = (TextView) itemView.findViewById(R.id.taskDueDate);
        }
    }
}
