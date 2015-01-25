package com.friendstime.apps.calex.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
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
import com.friendstime.apps.calex.fragments.CreateEventActivityDialogFragment;
import com.friendstime.apps.calex.model.EventData;

public class CreateEventActivity extends ActionBarActivity
        implements CreateEventActivityDialogFragment.inHonorOfDialogFragmentListener {
    private EditText tvEventName;
    private Spinner svInHonorOf;
    private Spinner svOccasion;
    private EditText tvEventDescription;
    private EditText tvDateFrom;
    private EditText tvDateTo;
    private EditText tvTimeFrom;
    private EditText tvTimeTo;
    private ImageButton btAddInHonor;
    private CheckBox cbAllDay;
    private String actions = "";
    private String notes = "";

    private Button btActions;
    private Button btNotes;
    private Button bSave;

    private EventData eventData = new EventData();

    private String[] inHonorOfNameStr = {"Dinesh"};
    private String[]  occasionsStr= {"birthday" , "friday" , "eve beer"};
    private ArrayAdapter<String> inHonorOfNameAdapter;
    private ArrayAdapter<String> occasionAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        tvEventName = (EditText) findViewById(R.id.tvEventName);
        svInHonorOf = (Spinner) findViewById(R.id.svInHonorOf);
        svOccasion = (Spinner) findViewById(R.id.svOccasion);
        tvEventDescription = (EditText) findViewById(R.id.tvEventDescription);
        tvDateFrom = (EditText) findViewById(R.id.tvDateFrom);
        tvDateTo = (EditText) findViewById(R.id.tvDateTo);
        tvTimeFrom = (EditText) findViewById(R.id.tvTimeFrom);
        tvTimeTo = (EditText) findViewById(R.id.tvTimeTo);
        btAddInHonor = (ImageButton) findViewById(R.id.btAddInHonor);


        btActions  = (Button)  findViewById(R.id.btActions);
        btNotes  = (Button)  findViewById(R.id.btNotes);
        bSave =  (Button) findViewById(R.id.btSave);
        cbAllDay=  (CheckBox)  findViewById(R.id.cbAllDay);
        //inHonorOfNameAdapter = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_spinner_item, inHonorOfNameStr);
        inHonorOfNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        inHonorOfNameAdapter.add("Dinesh");
        // Specify the layout to use when the list of choices appears
        inHonorOfNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        svInHonorOf.setAdapter(inHonorOfNameAdapter);

        occasionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, occasionsStr);
        // Specify the layout to use when the list of choices appears
        occasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        svOccasion.setAdapter(occasionAdapter);

        btAddInHonor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("New Person");
                editNameDialog.show(fm, "fragment_edit_name");
            }
        });
        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventData.setEventData(tvEventName.getText().toString(), svInHonorOf.getSelectedItem().toString(),
                        svOccasion.getSelectedItem().toString(), tvDateFrom.getText().toString(),
                        tvDateTo.getText().toString(), tvTimeFrom.getText().toString(), tvTimeTo.getText().toString(),
                        actions, notes, cbAllDay.isChecked());
                eventData.save(getBaseContext());
            };
        });

        btActions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("More Actions");

                editNameDialog.show(fm, "fragment_edit_name");
            }
        });

        btNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateEventActivityDialogFragment editNameDialog = CreateEventActivityDialogFragment.newInstance("Add Notes");

                editNameDialog.show(fm, "fragment_edit_name");
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
            inHonorOfNameAdapter.add(content);
            inHonorOfNameAdapter.notifyDataSetChanged();
            int sel = inHonorOfNameAdapter.getPosition(content);
            svInHonorOf.setSelection(sel);

        } else if (title.equals("MoreActions")) {
            actions = content;
        } else if(title.equals("AddNotes")) {
            notes = content;
        }
        else { //should never come here
            Toast.makeText(this, "ERROR uknown title" + " " + title + " " + content, Toast.LENGTH_SHORT).show();
        }
    }
}
