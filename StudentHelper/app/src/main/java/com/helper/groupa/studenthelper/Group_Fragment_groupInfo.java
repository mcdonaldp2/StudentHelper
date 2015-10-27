package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by willsteiner on 10/8/15.
 */
public class Group_Fragment_groupInfo extends Fragment{

    EditText groupName , groupClass;
    Button archiveGroup, restoreGroup;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_singlegroup_groupinfo, container, false);
        //Link views to params
        TextView gId = (TextView) v.findViewById(R.id.group_id);
        groupName = (EditText) v.findViewById(R.id.group_name);
        groupClass = (EditText) v.findViewById(R.id.group_className);
        archiveGroup = (Button) v.findViewById(R.id.group_singleGroup_archiveButton);
        restoreGroup = (Button) v.findViewById(R.id.group_singleGroup_restoreButton);
        // Display Group's ID
        StudentGroup group = StudentHelper.getUser().getCurrentGroup();
        gId.setText(Integer.toString(group.getGroup_id()));
        // Display Group's Name and class
        groupName.setText(group.getGroupName());
        groupClass.setText(group.getGroupClass());

        if(StudentHelper.getUser().hasActiveGroup(group.getGroup_id()))
        {
            archiveGroup.setVisibility(LinearLayout.VISIBLE);

        }
        else
        {
            restoreGroup.setVisibility(LinearLayout.VISIBLE);
        }



        return v;
    }


}
