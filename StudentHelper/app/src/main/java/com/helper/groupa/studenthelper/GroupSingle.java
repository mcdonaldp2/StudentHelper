package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

/**
 * Created by willsteiner on 10/4/15.
 */
public class GroupSingle extends Activity {

    public static final String debugHeading = "S H GroupSingle";

    private int groupId;
    private final StudentHelper helper = StudentHelper.getInstance();


    Fragment groupFeed, groupInfo, newPosts, members, archive;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_dashboard);


        //StudentGroup group = StudentHelper.getUser().getActiveGroups().get(gId);

        StudentGroup group = StudentHelper.getUser().getCurrentGroup();

        getActionBar().setTitle(group.getGroupName());



        groupFeed = new Group_Fragment_GroupFeed();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_place, groupFeed);
        Log.v(debugHeading, "Loading Current Group: "+group.toString());

        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        getFragmentManager().executePendingTransactions();
        //TextView info = (TextView) findViewById(R.id.group_single_info);

        //String text = group.toString();

        //info.setText(text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManif
        // est.xml.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (id == R.id.action_settings) {
            return true;
        }

        //TODO Implementation of fragments similar to GroupMain

        if(id== R.id.group_singleGroup_feed)
        {

            groupFeed = new Group_Fragment_GroupFeed();
            ft.replace(R.id.fragment_place, groupFeed);

            Toast.makeText(GroupSingle.this, "Display group feed", Toast.LENGTH_SHORT).show();
        }


        if(id== R.id.group_singleGroup_newPost)
        {

            newPosts = new Group_Fragment_newPost();
            ft.replace(R.id.fragment_place, newPosts);

            Toast.makeText(GroupSingle.this, "Display post form", Toast.LENGTH_SHORT).show();
        }
        if(id== R.id.group_singleGroup_viewInfo)
        {

            groupInfo = new Group_Fragment_groupInfo();
            //((TextView) fm.findFragmentById(userInfo.getId()).getView().findViewById(R.id.user_info)).setText("Hello");
            ft.replace(R.id.fragment_place, groupInfo);

            Toast.makeText(GroupSingle.this, "Display Group Info", Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.group_singleGroup_viewMembers)
        {
            /*
            members = new Group_Fragment_members();
            ft.replace(R.id.fragment_place, members);
            */
            Toast.makeText(GroupSingle.this, "Display member manager", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.group_singleGroup_archiveGroup) {
            /*
            archive = new Group_Fragment_groupArchive();
            ft.replace(R.id.fragment_place, archive);
            */
            Toast.makeText(GroupSingle.this, "Display group's archive", Toast.LENGTH_SHORT).show();
        }

        ft.commit();
        return super.onOptionsItemSelected(item);
    }


    public void archiveSingleGroup(View v)
    {
        final User u = StudentHelper.getUser();

        final StudentGroup curGroup = u.getCurrentGroup();

        final ArrayList<StudentGroup> activeGroups = u.getActiveGroups();

        String url = "http://steiner.solutions/studentHelper/archiveGroup/" + curGroup.getGroup_id() ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GroupSingle.this, response, Toast.LENGTH_SHORT).show();
                        for(int i=0 ; i < activeGroups.size() ; i++)
                        {
                            if(activeGroups.get(i).getGroup_id() == curGroup.getGroup_id())
                            {
                                activeGroups.remove(i);

                            }
                        }

                        ArrayList<StudentGroup> archivedGroups = u.getArchivedGroups();
                        archivedGroups.add(curGroup);
                        StudentHelper.setUser(u);
                        Toast.makeText(GroupSingle.this, "Group Archived", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(), GroupMain.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO Failed response from server
                Log.v(debugHeading, "Server Error: archiving group");
            }
        });
        // Add the request to the RequestQueue.
        helper.add(stringRequest);
    }


    public void restoreSingleGroup(View v)
    {
        final User u = StudentHelper.getUser();

        final StudentGroup curGroup = u.getCurrentGroup();

        final ArrayList<StudentGroup> archivedGroups = u.getActiveGroups();

        String url = "http://steiner.solutions/studentHelper/restoreGroup/" + curGroup.getGroup_id() ;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GroupSingle.this, response, Toast.LENGTH_SHORT).show();
                        for(int i=0 ; i < archivedGroups.size() ; i++)
                        {
                            if(archivedGroups.get(i).getGroup_id() == curGroup.getGroup_id())
                            {
                                archivedGroups.remove(i);

                            }
                        }

                        ArrayList<StudentGroup> activeGroups = u.getActiveGroups();
                        activeGroups.add(curGroup);
                        StudentHelper.setUser(u);
                        Toast.makeText(GroupSingle.this, "Group Restored", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(), GroupMain.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO Failed response from server
                Log.v(debugHeading, "Server Error: Restoring group");
            }
        });
        // Add the request to the RequestQueue.
        helper.add(stringRequest);
    }


}
