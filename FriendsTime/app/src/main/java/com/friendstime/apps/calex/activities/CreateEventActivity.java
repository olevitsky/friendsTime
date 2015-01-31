package com.friendstime.apps.calex.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.YelpClientApp;
import com.friendstime.apps.calex.fragments.CreateEventActivityDialogFragment;
import com.friendstime.apps.calex.model.Business;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.YelpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateEventActivity extends ActionBarActivity
        implements CreateEventActivityDialogFragment.inHonorOfDialogFragmentListener {
    private EditText mTvEventName;
    private Spinner mSvInHonorOf;
    private Spinner mSvOccasion;
    private EditText mTvEventDescription;
    private EditText mTvDateFrom;
    private EditText mTvDateTo;
    private EditText mTvTimeFrom;
    private EditText mTvTimeTo;
    private ImageButton mBtAddInHonor;
    private CheckBox mCbAllDay;
    private String m_actions = "";
    private String m_notes = "";

    private Button mBtActions;
    private Button mBtNotes;
    private Button mBSave;
    private Button mBtSurpriseMe;
    private Button mBtCancel;

    private EventData mEventData = new EventData();


    private String[] m_occasions = {"birthday" , "friday" , "eve beer"};
    private ArrayAdapter<String> mInHonorOfNameAdapter;
    private ArrayAdapter<String> mOccasionAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mTvEventName = (EditText) findViewById(R.id.tvEventName);
        mSvInHonorOf = (Spinner) findViewById(R.id.svInHonorOf);
        mSvOccasion = (Spinner) findViewById(R.id.svOccasion);
        mTvEventDescription = (EditText) findViewById(R.id.tvEventDescription);
        mTvDateFrom = (EditText) findViewById(R.id.tvDateFrom);
        mTvDateTo = (EditText) findViewById(R.id.tvDateTo);
        mTvTimeFrom = (EditText) findViewById(R.id.tvTimeFrom);
        mTvTimeTo = (EditText) findViewById(R.id.tvTimeTo);
        mBtAddInHonor = (ImageButton) findViewById(R.id.btAddInHonor);


        mBtActions = (Button)  findViewById(R.id.btActions);
        mBtNotes = (Button)  findViewById(R.id.btNotes);
        mBSave =  (Button) findViewById(R.id.btSave);
        mCbAllDay =  (CheckBox)  findViewById(R.id.cbAllDay);
        mBtSurpriseMe = (Button) findViewById(R.id.btSurpriseMe);
        mBtCancel = (Button) findViewById(R.id.btCancel);
        //mInHonorOfNameAdapter = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_spinner_item, inHonorOfNameStr);
        mInHonorOfNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        mInHonorOfNameAdapter.add("Dinesh");
        // Specify the layout to use when the list of choices appears
        mInHonorOfNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSvInHonorOf.setAdapter(mInHonorOfNameAdapter);

        mOccasionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, m_occasions);
        // Specify the layout to use when the list of choices appears
        mOccasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSvOccasion.setAdapter(mOccasionAdapter);

        mBtAddInHonor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("New Person");
                editNameDialog.show(fm, "fragment_edit_name");
            }
        });
        mBSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEventData.setEventData(mTvEventName.getText().toString(), mSvInHonorOf.getSelectedItem().toString(),
                        mSvOccasion.getSelectedItem().toString(), mTvDateFrom.getText().toString(),
                        mTvDateTo.getText().toString(), mTvTimeFrom.getText().toString(), mTvTimeTo.getText().toString(),
                        m_actions, m_notes, mCbAllDay.isChecked());
                mEventData.save(getBaseContext());
            }

            ;
        });

        mBtActions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("More Actions");

                editNameDialog.show(fm, "fragment_edit_name");
            }
        });

        mBtNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("Add Notes");

                editNameDialog.show(fm, "fragment_edit_name");
            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        mBtSurpriseMe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                YelpClient client = YelpClientApp.getRestClient();
                client.search("food", "san francisco", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int code, JSONObject body) {
                        try {
                            JSONArray businessesJson = body.getJSONArray("businesses");
                            ArrayList<Business> businesses = Business.fromJson(businessesJson);
                            Toast.makeText(CreateEventActivity.this, "Success1" + businesses.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("DEBUG", body.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        Toast.makeText(CreateEventActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        String title = inputText.substring(0,inputText.indexOf(" "));
        String content = inputText.substring(inputText.indexOf(" ")+1);
        if(title.equals("NewPerson")) {
            mInHonorOfNameAdapter.add(content);
            mInHonorOfNameAdapter.notifyDataSetChanged();
            int sel = mInHonorOfNameAdapter.getPosition(content);
            mSvInHonorOf.setSelection(sel);

        } else if (title.equals("MoreActions")) {
            m_actions = content;
        } else if(title.equals("AddNotes")) {
            m_notes = content;
        }
        else { //should never come here
            Toast.makeText(this, "ERROR unknown title" + " " + title + " " + content, Toast.LENGTH_SHORT).show();
        }
    }
}
