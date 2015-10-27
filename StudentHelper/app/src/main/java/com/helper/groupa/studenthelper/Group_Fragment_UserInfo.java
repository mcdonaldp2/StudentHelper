package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by willsteiner on 9/26/15.
 */
public class Group_Fragment_UserInfo extends Fragment {


    private TextView userId;
    private TextView phone;
    private EditText userName;
    private User u;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        u = StudentHelper.getUser();
        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_group_userinfo, container, false);
        userId = (TextView) v.findViewById(R.id.user_info_id);
        phone = (TextView) v.findViewById(R.id.user_info_phone);
        userName = (EditText) v.findViewById(R.id.user_info_username);
        // Display User's ID
        userId.setText(Integer.toString(u.getUserId()));
        // Display User's Phone
        phone.setText(u.getPhoneNumber());
        // Something to do with username not being set yet?
        userName.setText(u.getUserName());
        return v;
    }

    public boolean changeUsername()
    {

        // Fetch value from editText
        String newName = userName.getText().toString();
        if(newName.isEmpty())
        {
            Log.v("Student Helper Update", "New Name: ISNULL");
           newName = "";
        }
        Log.v("Student Helper Update", "New Name: "+newName);
        if(StudentHelper.getUser().getUserName().toString().equals(newName))
        {
            Log.v("Student Helper Update", "No Change");
            // No change took place
            return false;
        }
        else {
            Log.v("Student Helper Update", "Change");
            // Update user field
            //User u = StudentHelper.getUser();
            u.setUserName(newName);
            //StudentHelper.setUser(u);
            // Verify editText shows new value
            //userName.setText(newName);

            return true;
        }
    }



}
