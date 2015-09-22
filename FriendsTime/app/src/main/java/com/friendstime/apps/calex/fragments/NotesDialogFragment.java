package com.friendstime.apps.calex.fragments;

/**
 * Created by David on 8/29/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.friendstime.apps.calex.R;


public class NotesDialogFragment extends android.support.v4.app.DialogFragment implements TextView.OnEditorActionListener {

    private TextView tvNote;
    private ImageButton ibDelete;
    private int position;
    private Button save;
    private NotesListener listener;

    public interface NotesListener {
        void onNotesDelete(int position);
        void onNotesSave(String note, int position);
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
        View view = inflater.inflate(R.layout.fragment_notes_display_onclick, container);
        // Setup handles to view objects here
        tvNote = (TextView) view.findViewById(R.id.tvNote);
        ibDelete = (ImageButton) view.findViewById(R.id.ibDeleteNote);
        ibDelete.setBackground(null);
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotesDelete(position);
                tvNote.setText("");
                dismiss();
            }
        });
        save = (Button) view.findViewById(R.id.bSaveEditedNote);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNotesDelete(position);
                listener.onNotesSave(tvNote.getText().toString(), position);
                hideSoftKeyboard(getView());
                dismiss();
            }
        });
        getDialog().setTitle("Note");
        String someNote = getArguments().getString("someNote");
        tvNote.setText(someNote);
        tvNote.setTextColor(Color.BLACK);

        position = getArguments().getInt("position");


        return view;
    }




    public static NotesDialogFragment newInstance(String someNote, Integer position) {
        NotesDialogFragment fragmentDemo = new NotesDialogFragment();
        Bundle args = new Bundle();
        args.putString("someNote", someNote);
        args.putInt("position", position);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof NotesListener) {
            listener = (NotesListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}


