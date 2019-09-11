package com.example.taskmanager.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.taskmanager.R;

public class DescriptionDialog extends AppCompatDialogFragment {


    public interface DescriptionDialogListener {
        void applyText(String desc);
    }
    private EditText description;
    public DescriptionDialogListener descriptionDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.description_dialog_layout, null);
        description = (EditText) view.findViewById(R.id.description);

        builder.setView(view)
                .setTitle("Add description")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String desc = description.getText().toString();
                        descriptionDialogListener.applyText(desc);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            descriptionDialogListener = (DescriptionDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ExampleDialogListener");
        }

    }
}
