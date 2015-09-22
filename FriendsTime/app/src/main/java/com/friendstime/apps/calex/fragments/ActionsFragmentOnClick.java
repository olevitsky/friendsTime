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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.friendstime.apps.calex.R;

import java.util.ArrayList;

/**
 * Created by David on 9/2/2015.
 */
public class ActionsFragmentOnClick extends android.support.v4.app.DialogFragment implements TextView.OnEditorActionListener {

    private EditText etAction;
    private Spinner spinner;
    private ImageButton ibDelete;
    private ActionOnClickListener listener;
    private int position;
    private ArrayList<String> arrayOfSelectedParticipants;
    private Button save;

    public interface ActionOnClickListener {
        void onActionDelete(int position);
        void onActionSave(String action, String name, Integer position);
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
        View view = inflater.inflate(R.layout.actions_layout, container);
        // Setup handles to view objects here
        etAction = (EditText) view.findViewById(R.id.tvActionName);
        spinner = (Spinner) view.findViewById(R.id.svNameOfPerson);
        ibDelete = (ImageButton) view.findViewById(R.id.ibDeleteAction);
        ibDelete.setBackground(null);
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActionDelete(position);
                etAction.setText("");
                dismiss();
            }
        });
        save = (Button) view.findViewById(R.id.bSaveEditedAction);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActionDelete(position);
                listener.onActionSave(etAction.getText().toString(), spinner.getSelectedItem().toString(), position);
                dismiss();
            }
        });
        getDialog().setTitle("Action");
        String someAction = getArguments().getString("someAction");
       String name = getArguments().getString("name");
        arrayOfSelectedParticipants = getArguments().getStringArrayList("participants");
       etAction.setText(someAction);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayOfSelectedParticipants);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getPosition(name));
        position = getArguments().getInt("position");



        return view;
    }




    public static ActionsFragmentOnClick newInstance(String someAction, String name, ArrayList<String> arrayOfParticipants, Integer position) {
       ActionsFragmentOnClick fragmentDemo = new ActionsFragmentOnClick();
        Bundle args = new Bundle();
        args.putString("someAction", someAction);
        args.putString("name", name);
        args.putStringArrayList("participants", arrayOfParticipants);
        args.putInt("position", position);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActionOnClickListener) {
            listener = (ActionOnClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }
}
