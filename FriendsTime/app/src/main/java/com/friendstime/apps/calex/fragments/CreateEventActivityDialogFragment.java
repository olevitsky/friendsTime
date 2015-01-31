package com.friendstime.apps.calex.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.friendstime.apps.calex.R;

public class CreateEventActivityDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;
    private String m_title;
    public interface inHonorOfDialogFragmentListener {
        void onFinishEditDialog(String inputText);
    }
    public CreateEventActivityDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            inHonorOfDialogFragmentListener listener = (inHonorOfDialogFragmentListener) getActivity();
            String returnString = m_title + " " + mEditText.getText().toString();

            listener.onFinishEditDialog(returnString);
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

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        return view;
    }
}