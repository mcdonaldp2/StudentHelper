package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by willsteiner on 10/8/15.
 */
public class Group_Fragment_newPost extends Fragment {


    final static String debugHeading = "S H Group newPost";

    LinearLayout datePicker, description, location, link, messageHeading, messageContent;
    Spinner typesSpinner;
    ArrayAdapter<CharSequence> adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        User user = StudentHelper.getUser();
        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_singlegroup_newpost, container, false);
        Context ctx = v.getContext();

        typesSpinner = (Spinner) v.findViewById(R.id.group_post_type_spinner);
        datePicker = (LinearLayout)v.findViewById(R.id.group_post_datePicker);
        description = (LinearLayout) v.findViewById(R.id.group_post_description);
        messageHeading = (LinearLayout) v.findViewById(R.id.group_post_message_heading);
        messageContent = (LinearLayout) v.findViewById(R.id.group_post_message_content);
        location = (LinearLayout) v.findViewById(R.id.group_post_location);
        link = (LinearLayout) v.findViewById(R.id.group_post_link);

        adapter = ArrayAdapter.createFromResource(ctx, R.array.group_spinner_post_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        messageHeading.setVisibility(LinearLayout.VISIBLE);
        messageContent.setVisibility(LinearLayout.VISIBLE);

        typesSpinner.setAdapter(adapter);

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                hideAll();

                // your code here
                if (position == 0) {
                    Log.v(debugHeading, "Message chosen");
                    messageHeading.setVisibility(LinearLayout.VISIBLE);
                    messageContent.setVisibility(LinearLayout.VISIBLE);
                }

                if (position == 1) {
                    Log.v(debugHeading, "Task Suggestion chosen");
                    datePicker.setVisibility(LinearLayout.VISIBLE);
                    description.setVisibility(LinearLayout.VISIBLE);
                }

                if (position == 2) {
                    Log.v(debugHeading, "Link Share chosen");
                    link.setVisibility(LinearLayout.VISIBLE);
                }

                if (position == 2) {
                    Log.v(debugHeading, "Meet Up chosen");
                    location.setVisibility(LinearLayout.VISIBLE);
                    datePicker.setVisibility(LinearLayout.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }


        });

        return v;
    }

    public void hideAll()
    {
        
        //datePicker, description, message, location, link;
        datePicker.setVisibility(LinearLayout.INVISIBLE);
        description.setVisibility(LinearLayout.INVISIBLE);
        messageContent.setVisibility(LinearLayout.INVISIBLE);
        messageHeading.setVisibility(LinearLayout.INVISIBLE);
        location.setVisibility(LinearLayout.INVISIBLE);
        link.setVisibility(LinearLayout.INVISIBLE);
    }


}
