package com.helper.groupa.studenthelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by willsteiner on 9/26/15.
 */
public class Group_Fragment_Active extends Fragment {

    public static final String debugHeading = "S H GroupActive";

    ListView listView ;
    Context ctx;
    LinearLayout.LayoutParams linearBlock = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    ViewGroup.LayoutParams blockElement = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    //StudentHelper helper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View v = new View(getActivity());
        v = inflater.inflate(R.layout.fragment_group_active, container, false);

        LinearLayout activeGroupList = (LinearLayout) v.findViewById(R.id.activeGroupsList);


        ctx = this.getActivity().getApplicationContext();

        // For each group in  StudentGroup.user.groups build a linearLayout:
        // Group Name, Class, Group Code

        ArrayList<StudentGroup> activeGroups = StudentHelper.getUser().getActiveGroups();

            for(int i=0; i < activeGroups.size(); i++) {
                StudentGroup group = activeGroups.get(i);

                String gId = (String) Integer.toString(group.getGroup_id());
                String gName = group.getGroupName();
                String gClass = group.getGroupClass();


                Log.v(debugHeading, "Load group: " + gId);

                LinearLayout sampleGroup = makeGroupOption(gName, gClass, gId);
                    sampleGroup.setId(activeGroupList.indexOfChild(sampleGroup));
                    activeGroupList.addView(sampleGroup, linearBlock);
                    sampleGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String id = v.getTag().toString();
                            StudentHelper.getUser().setCurrentGroup(Integer.parseInt(id));
                            Log.v(debugHeading, "Clicked group: "+id);
                            Intent intent = new Intent(getActivity(), GroupSingle.class);
                            startActivity(intent);
                        }
                    });

            }


        return v;

    }


    private LinearLayout makeGroupOption(String groupName, String groupClass, String groupId)
    {
        // ViewGroup Params reference: https://developer.android.com/reference/android/view/ViewGroup.LayoutParams.html
        //ViewGroup.LayoutParams blockElement = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearBlock.setMargins(5,5,5,5);
        //ViewGroup.LayoutParams optionTitle = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Linear layout parameters reference: https://developer.android.com/reference/android/widget/LinearLayout.LayoutParams.html
        LinearLayout sampleGroup = new LinearLayout(ctx);
        sampleGroup.setLayoutParams(linearBlock);
        sampleGroup.setPadding(5, 5, 5, 5);
        sampleGroup.setTag(groupId);

            TextView groupInfo = new TextView(ctx);
            groupInfo.setLayoutParams(blockElement);
            groupInfo.setPadding(5, 5, 5, 5);
            groupInfo.setBackgroundColor(Color.parseColor("#666A73"));
            groupInfo.setTextColor(Color.parseColor("#D9D1C7"));
            groupInfo.setText(groupName + "\n" + groupClass);

        sampleGroup.addView(groupInfo, blockElement);



        return sampleGroup;

    }


    private int pixelsToDp(int pixelValue)
    {
        float scale = getResources().getDisplayMetrics().density;
        int dpConversion = (int) (pixelValue*scale + 0.5f);
        return dpConversion;
    }


}
