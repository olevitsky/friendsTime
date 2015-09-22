package com.friendstime.apps.calex.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.YelpClientApp;
import com.friendstime.apps.calex.adapters.ActionsAdapter;
import com.friendstime.apps.calex.adapters.ParticipantsAdapter;
import com.friendstime.apps.calex.fragments.ActionsDialogFragment;
import com.friendstime.apps.calex.fragments.ActionsFragmentOnClick;
import com.friendstime.apps.calex.fragments.AddParticipantsDialogFragment;
import com.friendstime.apps.calex.fragments.CreateEventActivityDialogFragment;
import com.friendstime.apps.calex.fragments.NotesDialogFragment;
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
        implements CreateEventActivityDialogFragment.NotesDialogFragmentListener, NotesDialogFragment.NotesListener, ActionsDialogFragment.ActionsListener, ActionsFragmentOnClick.ActionOnClickListener {
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
    private ImageButton mBtAddParticipants;
    private ListView mLvNotes;
    private ListView mLvParticipants;
    private ListView mlvActions;
    private ArrayAdapter<Action> mActionAdapter;
    private ArrayList<Action> mArrayActions;

    private final int PARSE_LOGIN_REQUEST_CODE = 0;

    private String[] m_occasions = {"birthday" , "friday" , "eve beer"};
    //private ArrayAdapter<String> mInHonorOfNameAdapter;
    public ArrayList<Contact> mSelectedContactArray;
    private ArrayAdapter<String> mOccasionAdapter;

    private ArrayList<String> arrayOfNotes;
    private ArrayAdapter<String> mNotesAdapter;
    private ArrayList<String> mArrayOfParticipants;
    public static ArrayList<Contact> allContacts;
    private ArrayList<Contact> mFilteredContactsArray;
    private ParticipantsAdapter mParticipantsAdapter;

    private String mSelectedDateForActivityPurposes; // used for on click for edit button to redraw view after editing item

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
        mSelectedContactArray = new ArrayList<Contact>();
        mTvEventName = (EditText) findViewById(R.id.tvEventName);
        //mSvInHonorOf = (Spinner) findViewById(R.id.svInHonorOf);
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
       // mBtAddInHonor = (ImageButton) findViewById(R.id.btAddInHonor);
        allContacts = new ContactFetcher(getApplicationContext()).fetchAll();
        Toast toast = Toast.makeText(this, Integer.toString(allContacts.size()), Toast.LENGTH_LONG);
        toast.show();

        mBtActions = (Button)  findViewById(R.id.btActions);
        mBtNotes = (Button)  findViewById(R.id.btNotes);
        mBSave =  (Button) findViewById(R.id.btSave);
        mCbAllDay =  (CheckBox)  findViewById(R.id.cbAllDay);
        mBtSurpriseMe = (Button) findViewById(R.id.btSurpriseMe);
        mBtCancel = (Button) findViewById(R.id.btCancel);
        mBClear = (Button) findViewById(R.id.btClear);
        mBtAddParticipants = (ImageButton) findViewById(R.id.ibAddParticipants);
        mLvNotes = (ListView) findViewById(R.id.lvNotes);
        mArrayOfParticipants = new ArrayList<>();
        mLvParticipants = (ListView) findViewById(R.id.lvParticipantsCreateActivity);
        mFilteredContactsArray = new ArrayList<Contact>();

        ArrayList<Contact> editedContacts = getIntent().getParcelableArrayListExtra("participants");
        if (editedContacts != null){
            mFilteredContactsArray = editedContacts;
        }

        mParticipantsAdapter = new ParticipantsAdapter(this, mFilteredContactsArray);
        mLvParticipants.setAdapter(mParticipantsAdapter);
        mLvParticipants.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        String name = getIntent().getStringExtra("name");
        String dateFrom = getIntent().getStringExtra("dateFrom");
        String dateTo = getIntent().getStringExtra("dateTo");
        //String startTime = getIntent().getStringExtra("startTime");
        //String endTime = getIntent().getStringExtra("endTime");
        if (name != null){
            mTvEventName.setText(name);
        }
        if (dateFrom != null) {
            mTvDateFrom.setText(dateFrom);
            mTvDateTo.setText(dateTo);
        }
        arrayOfNotes = new ArrayList<String>();
        ArrayList<String> arrayOfNotesEdited = getIntent().getStringArrayListExtra("notes");
        if (arrayOfNotesEdited != null){
            arrayOfNotes = arrayOfNotesEdited;
        }


        mNotesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayOfNotes);
        mLvNotes.setAdapter(mNotesAdapter);
        mLvNotes.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        mOccasionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, m_occasions);
        // Specify the layout to use when the list of choices appears
        mOccasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSvOccasion.setAdapter(mOccasionAdapter);

        mlvActions = (ListView)findViewById(R.id.lvActions);
        mArrayActions = new ArrayList<Action>();
        ArrayList<Action> editedActions = getIntent().getParcelableArrayListExtra("actions");
        if (editedActions != null){
            mArrayActions = editedActions;
        }
        mActionAdapter = new ActionsAdapter(this, mArrayActions);
        mlvActions.setAdapter(mActionAdapter);
        mlvActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> testArray = new ArrayList<String>();
                for (Contact contact : mFilteredContactsArray){
                    testArray.add(contact.name);
                }
                Action someAction = mArrayActions.get(position);
                FragmentManager fm = getSupportFragmentManager();
                ActionsFragmentOnClick frag = ActionsFragmentOnClick.newInstance(someAction.action, someAction.nameOfPerson, testArray, position);
                frag.show(fm, "fragment_edit_name");

            }
        });
        mlvActions.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

/*        mBtAddInHonor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("New Person");
                editNameDialog.show(fm, "fragment_edit_name");
            }
        });*/

        mLvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String note = arrayOfNotes.get(position);
                NotesDialogFragment notesFragment = NotesDialogFragment.newInstance(note, position);
                FragmentManager fm = getSupportFragmentManager();
                notesFragment.show(fm, note);

            }
        });

        mBSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                View sv = mSvInHonorOf.getSelectedView();
                ParseUser user = ParseUser.getCurrentUser();
                if(user==null) {
                    //login
                    ParseLoginBuilder builder = new ParseLoginBuilder(getBaseContext());
                    startActivityForResult(builder.build(), PARSE_LOGIN_REQUEST_CODE);

                } else {
                    saveEventData();
                    //DAVID HACK
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    // NULL POINTER EXCEPTIONS?
                    data.putExtra("dateFrom", mTvDateFrom.getText().toString());
                    data.putExtra("description", mTvEventDescription.getText().toString());
                    data.putExtra("name", mTvEventName.getText().toString());// ints work too
                    data.putExtra("dateTo", mTvDateTo.getText().toString());
                    data.putExtra("startTime", mTvTimeFrom.getText().toString());
                    data.putExtra("endTime", mTvTimeTo.getText().toString());
                    data.putExtra("occasion", mSvOccasion.getSelectedItem().toString());
                    data.putExtra("selectedDate",  mTvDateFrom.getText().toString()); // change this to ACTUAL selected date
                    data.putStringArrayListExtra("notes", arrayOfNotes);
                    data.putParcelableArrayListExtra("actions", mArrayActions);
                    data.putParcelableArrayListExtra("participants", mFilteredContactsArray);
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
                }


            };
        });

        mBClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //EventDataStore.debugPrint(getBaseContext());
                //EventData.clearDebug(getBaseContext());
                mTvEventName.setText("");
                mTvEventDescription.setText("");
                mTvDateFrom.setText("");
                mTvDateTo.setText("");
                mTvTimeFrom.setText("");
                mTvTimeTo.setText("");

            }
        });



        mBtActions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ArrayList<String> testArray = new ArrayList<String>();
                for (Contact contact : mFilteredContactsArray){
                    testArray.add(contact.name);
                }
                ActionsDialogFragment actionsDialog = ActionsDialogFragment.newInstance(testArray);
               actionsDialog.show(fm, "fragment_edit_name");
            }
        });

        mBtAddParticipants.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager fm = getSupportFragmentManager();
                AddParticipantsDialogFragment addParticipantsDialog = AddParticipantsDialogFragment.newInstance(mFilteredContactsArray);

              addParticipantsDialog.show(fm, "fragment_edit_name");
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
        //*** DAVID HACK - changing spinner -> listview to be utilized by dialog fragment.
        /*mListContacts = new ContactFetcher(this).fetchAll();
        ContactsAdapter adapterContacts = new ContactsAdapter(this, mListContacts);
        mSvInHonorOf.setAdapter(adapterContacts);*/

        //*** END DAVID HACK

        //PushService.subscribe(this, getString(R.string.parse_msg_channel), CreateEventActivity.class);

        // getting intent from event planner fragment upon edit button click


    }

    public void onCheck(View v){
        CheckBox checkbox = (CheckBox) v.findViewById(R.id.cbCheckBox);
        int position = (int) v.getTag();
        Contact contact = allContacts.get(position);
        //contact.setContact("contentLookupURI", contact.getContactName());

        if (checkbox.isChecked()){
            contact.checked = 1;
            //contact.setContact("contentLookupURI", contact.name);
            contact.setContact(1);
            mParticipantsAdapter.add(contact);
            // Needed for PARSE?
        }
        else{
            contact.checked = 0;
            mParticipantsAdapter.remove(contact);
            mParticipantsAdapter.notifyDataSetChanged();
            contact.setContact(0); // Needed for PARSE?
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }


    @Override
    public void onNotesDelete(int position){
        arrayOfNotes.remove(position);
        mNotesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNotesSave(String note, int position){
        if (arrayOfNotes.size() > 0){
        arrayOfNotes.add(position, note);}
        else{
            arrayOfNotes.add(note);
        }
        mNotesAdapter.notifyDataSetChanged();
        InputMethodManager imm =(InputMethodManager)this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTvEventName.getWindowToken(), 0);

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
    public void onAddAction(String action, String name){
       Action newAction = new Action();
        newAction.setAction(action, name);
        mArrayActions.add(newAction);
        mActionAdapter.notifyDataSetChanged();

    }


    public void onActionDelete(int position) {
        //Action action = mArrayActions.get(position);
        mArrayActions.remove(position);
        mActionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActionSave(String action, String name, Integer position){
        Action newAction = new Action();
        newAction.setAction(action, name);
        if (mArrayActions.size() > 0){
        mArrayActions.add(position, newAction);}
        else{
            mArrayActions.add(newAction);
        }
        mActionAdapter.notifyDataSetChanged();
    }


   @Override
    public void onFinishEditDialog(String inputText) {
        /*String title = inputText.substring(0,inputText.indexOf(" "));
        String content = inputText.substring(inputText.indexOf(" ") + 1);
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
        }*/
       arrayOfNotes.add(inputText);
       mNotesAdapter.notifyDataSetChanged();
       //Toast toast = Toast.makeText(this, Integer.toString(arrayOfNotes.size()), Toast.LENGTH_LONG);
       //toast.show();
    }

    private void saveEventData() {
//        int pos = mSvInHonorOf.getSelectedItemPosition();
  //      Contact contact = (Contact) (mSvInHonorOf.getItemAtPosition(pos));
        //String cname = ((TextView) sv.findViewById(R.id.tvContactName)).getText().toString();
        EventData eventData = new EventData();
       /* Contact savedContact =
                EventDataStore.getInstance().getContactFromMap(contact.getContactLookupURI());
        if(savedContact == null) {
            savedContact = contact;
            contact.saveContact(getBaseContext());
        }
        */
        ArrayList<String> actionName = new ArrayList<String>();
        ArrayList<String> actionPerson = new ArrayList<String>();
        ArrayList<String> participantName = new ArrayList<String>();
        for (Action action : mArrayActions){
            actionName.add(action.action);
            actionPerson.add(action.nameOfPerson);
        }
        for (Contact contact : mFilteredContactsArray){
            participantName.add(contact.name);
        }
        eventData.setEventData(getBaseContext(), mTvEventName.getText().toString(),
                mSvOccasion.getSelectedItem().toString(), mTvDateFrom.getText().toString(),
                mTvTimeFrom.getText().toString(), mTvTimeTo.getText().toString(), mTvEventDescription.getText().toString(),
                "location", "foodPref", arrayOfNotes, actionName, actionPerson, participantName);

        eventData.save(getBaseContext());
        EventDataStore.getInstance().addEventData(eventData);
        //eventData.printDebug(getBaseContext());
        JSONObject jobj;
        try {
            jobj = new JSONObject();
            jobj.put("alert" , "New Event");
            jobj.put("action", getString(R.string.parse_message_action));
            //jobj.put("username", contact.getFirstEmail().getAddress());
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
        if (resultCode == RESULT_OK && requestCode == 200){

        }
    }
}
