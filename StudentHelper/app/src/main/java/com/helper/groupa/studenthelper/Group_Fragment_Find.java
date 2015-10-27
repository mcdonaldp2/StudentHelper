package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by willsteiner on 9/26/15.
 */
public class Group_Fragment_Find extends Fragment {

    public static final String debugHeading = "S H Group_Find";

    private EditText gName;
    private EditText cName;

    private EditText gID;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_group_find, container, false);
        //Inflate layout for fragment

        gName = (EditText) v.findViewById(R.id.group_find_name);
        cName = (EditText) v.findViewById(R.id.group_find_class);
        gID = (EditText) v.findViewById(R.id.group_find_id);



        return v;

    }


    public StudentGroup gatherInput()
    {
        StudentGroup inputGroup;
        inputGroup = new StudentGroup();

        if(!gID.getText().toString().matches(""))
            inputGroup.setGroup_id(gID.getText().toString());

        if(!gName.getText().toString().matches(""))
            inputGroup.setGroupName(gName.getText().toString());

        if(!cName.getText().toString().matches(""))
            inputGroup.setGroupClass(cName.getText().toString());

        return inputGroup;
    }


}
