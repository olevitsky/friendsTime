package com.friendstime.apps.calex.activities;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by David on 9/2/2015.
 */
@ParseClassName("Action")
public class Action extends ParseObject implements Parcelable {
    
    public String action;
    public String nameOfPerson;

    public Action() {
        super();

    }

    public void setAction(String action, String name) {

        put("action", action);
        put("name", name);
        this.action = action;
        this.nameOfPerson = name;
    }

   // public Action(String action, String name){
    //    this.action = action;
     //   this.nameOfPerson = name;
    //}

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(action);
        out.writeString(nameOfPerson);
    }

    private Action(Parcel in) {
        action = in.readString();
        nameOfPerson = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Action> CREATOR
            = new Parcelable.Creator<Action>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };


}
