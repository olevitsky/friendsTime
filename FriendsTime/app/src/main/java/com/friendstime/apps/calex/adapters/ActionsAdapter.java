package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.activities.Action;

import java.util.ArrayList;

/**
 * Created by David on 9/2/2015.
 */
public class ActionsAdapter extends ArrayAdapter<Action> {

    public ActionsAdapter(Context context, ArrayList<Action> actions){
        super(context, 0, actions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Action action = getItem(position);
        if (action == null){
            return convertView;
        }
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actions_layout_for_adapter, parent, false);
        }
        // Lookup view for data population
        TextView tvAction = (TextView) convertView.findViewById(R.id.tvAdapterActionName);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvAdapterNameOfPerson);

        // Populate the data into the template view using the data object
        tvAction.setText(action.action);
        tvName.setText(action.nameOfPerson);
        // Return the completed view to render on screen
        return convertView;
    }

}
