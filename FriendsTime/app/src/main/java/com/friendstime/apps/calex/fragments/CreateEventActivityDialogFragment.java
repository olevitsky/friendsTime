package com.friendstime.apps.calex.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.friendstime.apps.calex.R;

public class CreateEventActivityDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;
    private String m_title;
    private NotesDialogFragmentListener listener;
    private Button mSaveButton;


    public interface NotesDialogFragmentListener {
        void onFinishEditDialog(String inputText);
    }
    public CreateEventActivityDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            NotesDialogFragmentListener listener = (NotesDialogFragmentListener) getActivity();
            String returnString = mEditText.getText().toString();

            listener.onFinishEditDialog(returnString);
            Toast toast = Toast.makeText(getActivity(), "editor action", Toast.LENGTH_SHORT);
            toast.show();
            dismiss();
            return true;
        }
        return false;
    }

    public static CreateEventActivityDialogFragment newInstance(String title) {
        CreateEventActivityDialogFragment frag = new CreateEventActivityDialogFragment();
        Bundle args = new Bundle();
        args.putString("m_title", title);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_create_event_activity, container);
        mEditText = (EditText) view.findViewById(R.id.etDialog);
        m_title = getArguments().getString("m_title", "Enter Name");
        getDialog().setTitle(m_title);
        m_title = m_title.replaceAll("\\s+","");
        if (m_title.equals("MoreActions")) {
            mEditText.setHint("Enter Actions");
        } else if(m_title.equals("AddNotes")) {
            mEditText.setHint("Enter Notes");
        }
        mSaveButton = (Button) view.findViewById(R.id.bDialogSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDialogFragmentListener listener = (NotesDialogFragmentListener) getActivity();
                String returnString = mEditText.getText().toString();

                listener.onFinishEditDialog(returnString);
                Toast toast = Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT);
                toast.show();
                dismiss();
            }
        });

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof NotesDialogFragmentListener) {
            listener = (NotesDialogFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }
}
