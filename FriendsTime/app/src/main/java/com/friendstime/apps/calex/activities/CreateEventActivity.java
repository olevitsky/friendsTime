package com.friendstime.apps.calex.activities;

import android.content.Context;
import android.content.Intent;
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
import com.friendstime.apps.calex.adapters.ContactsAdapter;
import com.friendstime.apps.calex.fragments.CreateEventActivityDialogFragment;
import com.friendstime.apps.calex.model.Business;
import com.friendstime.apps.calex.model.Contact;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.EventDataStore;
import com.friendstime.apps.calex.model.YelpClient;
import com.friendstime.apps.calex.utils.ContactFetcher;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;
import com.parse.ui.ParseLoginBuilder;

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
    private Button mBClear;

    private String date;
    private String eventDescription;

    private final int PARSE_LOGIN_REQUEST_CODE = 0;

    private String[] m_occasions = {"birthday" , "friday" , "eve beer"};
    //private ArrayAdapter<String> mInHonorOfNameAdapter;
    private ArrayList<Contact> mListContacts;
    ArrayList<CreatedEventDisplay> eventsArray;
    private ArrayAdapter<String> mOccasionAdapter;

    public static final String EVENT_DATE = "event_date";

    public static Intent newIntent(Context packageContext, String eventDate) {
        Intent i = new Intent(packageContext, CreateEventActivity.class);
        i.putExtra(EVENT_DATE, eventDate);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mTvEventName = (EditText) findViewById(R.id.tvEventName);
        mSvInHonorOf = (Spinner) findViewById(R.id.svInHonorOf);
        mSvOccasion = (Spinner) findViewById(R.id.svOccasion);
        mTvEventDescription = (EditText) findViewById(R.id.tvEventName);
        mTvDateFrom = (EditText) findViewById(R.id.tvDateFrom);
        mTvDateTo = (EditText) findViewById(R.id.tvDateTo);
        String eventDate = (String)getIntent().getSerializableExtra(EVENT_DATE);
        if (eventDate != null && !eventDate.isEmpty()) {
            mTvDateFrom.setText(eventDate);
            mTvDateTo.setText(eventDate);
        }
        mTvTimeFrom = (EditText) findViewById(R.id.tvTimeFrom);
        mTvTimeTo = (EditText) findViewById(R.id.tvTimeTo);
        mBtAddInHonor = (ImageButton) findViewById(R.id.btAddInHonor);


        mBtActions = (Button)  findViewById(R.id.btActions);
        mBtNotes = (Button)  findViewById(R.id.btNotes);
        mBSave =  (Button) findViewById(R.id.btSave);
        mCbAllDay =  (CheckBox)  findViewById(R.id.cbAllDay);
        mBtSurpriseMe = (Button) findViewById(R.id.btSurpriseMe);
        mBtCancel = (Button) findViewById(R.id.btCancel);
        mBClear = (Button) findViewById(R.id.btClear);
        //mInHonorOfNameAdapter = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_spinner_item, inHonorOfNameStr);
        //mInHonorOfNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        //mInHonorOfNameAdapter.add("Dinesh");
        // Specify the layout to use when the list of choices appears
        //mInHonorOfNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //mSvInHonorOf.setAdapter(mInHonorOfNameAdapter);





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
                View sv = mSvInHonorOf.getSelectedView();
                ParseUser user = ParseUser.getCurrentUser();
                if(user==null) {
                    //login
                    ParseLoginBuilder builder = new ParseLoginBuilder(getBaseContext());
                    startActivityForResult(builder.build(), PARSE_LOGIN_REQUEST_CODE);

                } else {
                    saveEventData();
                }



                //DAVID HACK

                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("date",mTvDateFrom.getText().toString());
                data.putExtra("description",  mTvEventDescription.getText().toString());
                 data.putExtra("name", mTvEventName.getText().toString());// ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
                /*getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                date = mTvDateFrom.getText().toString();
                eventDescription = mTvEventDescription.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                bundle.putString("description", eventDescription);
                Fragment fragInfo = new Fragment();
                fragInfo.setArguments(bundle);
                Toast toast = Toast.makeText(getApplicationContext(), "tttt", Toast.LENGTH_SHORT);
                toast.show();*/




            };
        });

        mBClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventDataStore.debugPrint(getBaseContext());
                //EventData.clearDebug(getBaseContext());
            }
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

        //setup Contact fetcher
        mListContacts = new ContactFetcher(this).fetchAll();
        ContactsAdapter adapterContacts = new ContactsAdapter(this, mListContacts);
        mSvInHonorOf.setAdapter(adapterContacts);

        //PushService.subscribe(this, getString(R.string.parse_msg_channel), CreateEventActivity.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mItems to the action bar if it is present.
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
            //mInHonorOfNameAdapter.add(content);
            //mInHonorOfNameAdapter.notifyDataSetChanged();
            //int sel = mInHonorOfNameAdapter.getPosition(content);
            //.setSelection(sel);

        } else if (title.equals("MoreActions")) {
            m_actions = content;
        } else if(title.equals("AddNotes")) {
            m_notes = content;
        }
        else { //should never come here
            Toast.makeText(this, "ERROR unknown title" + " " + title + " " + content, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEventData() {
        int pos = mSvInHonorOf.getSelectedItemPosition();
        Contact contact = (Contact) (mSvInHonorOf.getItemAtPosition(pos));
        //String cname = ((TextView) sv.findViewById(R.id.tvContactName)).getText().toString();
        EventData eventData = new EventData();
        Contact savedContact =
                EventDataStore.getInstance().getContactFromMap(contact.getContactLookupURI());
        if(savedContact == null) {
            savedContact = contact;
            contact.saveContact(getBaseContext());
        }
        eventData.setEventData(getBaseContext(), mTvEventName.getText().toString(),savedContact ,
                mSvOccasion.getSelectedItem().toString(), mTvDateFrom.getText().toString(),
                mTvTimeFrom.getText().toString(), mTvTimeTo.getText().toString(),
                "location", "foodPref", m_actions, m_notes);

        eventData.save(getBaseContext());
        EventDataStore.getInstance().addEventData(eventData);
        eventData.printDebug(getBaseContext());
        JSONObject jobj;
        try {
            jobj = new JSONObject();
            jobj.put("alert" , "New Event");
            jobj.put("action", getString(R.string.parse_message_action));
            jobj.put("username", contact.getFirstEmail().getAddress());
            ParsePush parsePush = new ParsePush();
            ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query

            // !! For debugging purposes. For production, uncomment line below and comment one after!!!
            //pQuery.whereEqualTo("username", contact.getFirstEmail().getAddress());
            pQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());


            parsePush.setData(jobj);
            //parsePush.setMessage("TESTMSG");
            //parsePush.setChannel(getString(R.string.parse_msg_channel));
            parsePush.setQuery(pQuery);
            parsePush.sendInBackground(new SendCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            });




        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == PARSE_LOGIN_REQUEST_CODE) {
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("username", ParseUser.getCurrentUser().getUsername());
            installation.saveEventually();
            saveEventData();
        }
    }
}
