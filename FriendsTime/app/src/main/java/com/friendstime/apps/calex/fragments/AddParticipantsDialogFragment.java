package com.friendstime.apps.calex.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.activities.CreateEventActivity;
import com.friendstime.apps.calex.adapters.ContactsAdapter;
import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by David on 8/25/2015.
 */
public class AddParticipantsDialogFragment extends DialogFragment {


    private Button saveButton;
    public ArrayList<Contact> mContactArray;
    private EditText mEditText;
    private ListView lvParticipants;
    private ContactsAdapter mContactsAdapter;
    private ArrayList<Contact> checkedContacts;

    public interface ParticipantsListener {
        void onAddParticipant(String action, String name);

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {



            dismiss();
            return true;
        }
        return false;
    }

    public AddParticipantsDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static AddParticipantsDialogFragment newInstance(ArrayList<Contact> alreadyChecked) {
            AddParticipantsDialogFragment frag = new AddParticipantsDialogFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList("checkedContacts", alreadyChecked);
            frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_participants, container);
        saveButton = (Button) view.findViewById(R.id.bSaveParticipants);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ParticipantsListener listener = (ParticipantsListener)getActivity();
                lvParticipants.invalidateViews();
                mContactsAdapter = new ContactsAdapter(getActivity(), mContactArray);

                dismiss();


            }
        });

        lvParticipants = (ListView) view.findViewById(R.id.lvParticipants);
        mContactArray = CreateEventActivity.allContacts;
        checkedContacts = getArguments().getParcelableArrayList("checkedContacts");
        if (checkedContacts != null) {
            for (int i = 0; i < mContactArray.size(); i++)
                for (int j = 0; j<checkedContacts.size(); j++){
                if (mContactArray.get(i).name.equals(checkedContacts.get(j).name)) {
                    mContactArray.get(i).checked = 1; //set already selected contacts to checked
                }}
       }
        mContactsAdapter = new ContactsAdapter(getActivity(), mContactArray);
        //mSelectedContactArray = new ArrayList<Contact>();
        lvParticipants.setAdapter(mContactsAdapter);
        mEditText = (EditText) view.findViewById(R.id.etSearch);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContactsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getDialog().setTitle("Select Participants");
        // Show soft keyboard automatically
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
          //      WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

}
