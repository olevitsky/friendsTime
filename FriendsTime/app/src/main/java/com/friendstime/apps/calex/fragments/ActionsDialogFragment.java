package com.friendstime.apps.calex.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.friendstime.apps.calex.R;

import java.util.ArrayList;

/**
 * Created by David on 9/2/2015.
 */
public class ActionsDialogFragment extends android.support.v4.app.DialogFragment implements TextView.OnEditorActionListener {
    private EditText tvAction;
    private Spinner spinner;
    private Button saveButton;
    private int position;
    private ActionsListener listener;
    ArrayList<String> arrayOfSelectedParticipants;

    public interface ActionsListener {
        void onAddAction(String action, String name);

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            return true;}
        return false;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_action_layout, container);
        // Setup handles to view objects here
        tvAction = (EditText) view.findViewById(R.id.tvFragmentAction);
        spinner = (Spinner) view.findViewById(R.id.sFragmentNameOfPerson);
        arrayOfSelectedParticipants = getArguments().getStringArrayList("participantsArray");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayOfSelectedParticipants);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        getDialog().setTitle("Create Action");
        saveButton = (Button)view.findViewById(R.id.bFragmentActionSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsListener listener = (ActionsListener)getActivity();
                String action = tvAction.getText().toString();
                String name = spinner.getSelectedItem().toString();
                listener.onAddAction(action, name);
                dismiss();

            }
        });
        return view;
    }




    public static ActionsDialogFragment newInstance(ArrayList<String> arrayOfSelectedParticipants) {
        ActionsDialogFragment fragmentDemo = new ActionsDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("participantsArray", arrayOfSelectedParticipants);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActionsListener) {
            listener = (ActionsListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

}

