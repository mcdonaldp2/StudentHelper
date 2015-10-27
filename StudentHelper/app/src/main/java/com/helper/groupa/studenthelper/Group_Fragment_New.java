package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by willsteiner on 9/26/15.
 */
public class Group_Fragment_New extends Fragment{

    private EditText groupNameField;
    private EditText groupClassField;
    private DatePicker datePick;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_group_new, container, false);
        //Inflate layout for fragment

        groupNameField = (EditText) v.findViewById(R.id.group_new_groupName);
        groupClassField = (EditText) v.findViewById(R.id.group_new_groupClass);
        datePick = (DatePicker) v.findViewById(R.id.group_new_datePicker);


        return v;

    }


    public boolean validExpirationDate()
    {

        Calendar today = Calendar.getInstance();

        int day = today.get(Calendar.DAY_OF_MONTH);
        int inputDay = datePick.getDayOfMonth();

        int month = today.get(Calendar.MONTH);
        int inputMonth = datePick.getMonth();

        int year = today.get(Calendar.YEAR);
        int inputYear = datePick.getYear();


        if(inputYear < year)
            return false;

        if(inputMonth < month && inputYear == year)
            return false;

        if(inputDay < day && inputMonth == month)
            return false;

        return true;
    }



    public StudentGroup fetchGroupInfo()
    {
        StudentGroup retrieved = new StudentGroup();

        retrieved.setGroupName(groupNameField.getText().toString());
        retrieved.setGroupClass(groupClassField.getText().toString());
        retrieved.setExpires((datePick.getMonth()+1), datePick.getDayOfMonth(), datePick.getYear());

        //Log.v("Student Helper", "EXPIRES: " +  + "/" +  + "/" + );
        Log.v("Student Helper", "Fetched New Group:\n" + retrieved.toString());


        return retrieved;
    }
}
