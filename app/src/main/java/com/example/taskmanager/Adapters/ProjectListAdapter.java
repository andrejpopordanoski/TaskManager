package com.example.taskmanager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Activities.EditProjectInfoActivity;
import com.example.taskmanager.Fragments.ProjectsFragment;
import com.example.taskmanager.Models.Project;
import com.example.taskmanager.R;
import com.example.taskmanager.Activities.TasksActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder>{
    private static final String TAG = "ProjectListAdapter";
    private List<Project> mProjectNames;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private Activity parentActivity;
    private ProjectsFragment projectsFragment;
//    private Context mContext;

    public ProjectListAdapter(ArrayList<Project> mProjectNames, Activity received, ProjectsFragment pf) {
        this.mProjectNames = mProjectNames;
//        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        this.parentActivity = received;
        this.projectsFragment = pf;




    }

    public void setProjectList(ArrayList<Project> mProjectNames){
        this.mProjectNames = mProjectNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item, parent, false);


        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder called");
        Project project = mProjectNames.get(position);

        holder.projectName.setText(project.name);

        if(project.isUserFromType(mCurrentUser.getUid(), "Project Manager")){
            holder.starImage.setVisibility(View.VISIBLE);
        }
        else {
            holder.starImage.setVisibility(View.INVISIBLE);
        }



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click on the field with the name " + mProjectNames.get(position).name);
                Intent intent = new Intent(v.getContext(), TasksActivity.class);
                intent.putExtra("currentProject", mProjectNames.get(position));

                v.getContext().startActivity(intent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return mProjectNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView projectName;
        LinearLayout parentLayout;
        ImageView starImage;
        ImageView moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            projectName = (TextView) itemView.findViewById(R.id.project_name);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
            starImage = (ImageView) itemView.findViewById(R.id.star);
            moreButton = (ImageView) itemView.findViewById(R.id.more_icon);
            parentLayout.setOnCreateContextMenuListener(this);
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentActivity.openContextMenu(parentLayout);
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            ImageView star = (ImageView) v.findViewById(R.id.star);
            if(star.getVisibility() == View.VISIBLE) {

                menu.setHeaderTitle("Choose an option:");
                menu.add(this.getAdapterPosition(), R.id.open_tasks, 0, "Open tasks");
                menu.add(this.getAdapterPosition(), R.id.edit_settings, 1, "Edit project settings");
            }
            else {
                menu.add(this.getAdapterPosition(), R.id.open_tasks, 0, "Open tasks");
            }
        }

    }

    public void openTask(int position){
        Intent intent = new Intent(parentActivity, TasksActivity.class);
        intent.putExtra("currentProject", mProjectNames.get(position));

        parentActivity.startActivity(intent);
    }

    public void openEditActivity(int position) {
        projectsFragment.startEditSettingsActivity(mProjectNames.get(position));

    }
}
