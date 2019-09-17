package com.example.taskmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskmanager.Models.Collaborator;
import com.example.taskmanager.Models.SpinnerDetails;
import com.example.taskmanager.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItemAdapter extends ArrayAdapter<Collaborator> {
    public SpinnerItemAdapter(@NonNull Context context, List<Collaborator> spinnerDetailsList) {
        super(context, 0, spinnerDetailsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_spinner_user_row, parent,false);
        }

        TextView email = (TextView) convertView.findViewById(R.id.spinner_email);

        Collaborator c = (Collaborator) getItem(position);
        email.setText(c.mail);

        return convertView;
    }
}
