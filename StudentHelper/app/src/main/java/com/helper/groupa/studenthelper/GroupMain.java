package com.helper.groupa.studenthelper;

import android.app.Activity;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class GroupMain extends Activity {

    public static final String debugHeading = "S H GroupMain";

    private final StudentHelper helper = StudentHelper.getInstance();
    private User user;
    private Group_Fragment_Active activeGroups;
    private Group_Fragment_UserInfo userInfo;
    private Group_Fragment_New newGroupFragment;
    private Group_Fragment_Find findGroups;
    // Get HTTP (Volley) helper from Singleton class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_dashboard);
        user = helper.getUser();
        Log.v(debugHeading, "User fetched: "+ user.getUserId() );
        activeGroups = new Group_Fragment_Active();
        // Begin the transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
                ft.replace(R.id.fragment_place, activeGroups);

        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        getFragmentManager().executePendingTransactions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_dashboard, menu);
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
        if(id== R.id.group_view_user)
        {
            userInfo = new Group_Fragment_UserInfo();
            //((TextView) fm.findFragmentById(userInfo.getId()).getView().findViewById(R.id.user_info)).setText("Hello");
            ft.replace(R.id.fragment_place, userInfo);
            Toast.makeText(GroupMain.this, "Display User Info", Toast.LENGTH_SHORT).show();
        }

        if(id== R.id.group_view_active)
        {
            activeGroups = new Group_Fragment_Active();
            ft.replace(R.id.fragment_place, activeGroups);
            Toast.makeText(GroupMain.this, "Display Active Groups", Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.group_find_button)
        {
            findGroups = new Group_Fragment_Find();
            ft.replace(R.id.fragment_place, findGroups);
            Toast.makeText(GroupMain.this, "Display Group Finder", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.group_create_new_button) {
            newGroupFragment = new Group_Fragment_New();
            ft.replace(R.id.fragment_place, newGroupFragment);
            Toast.makeText(GroupMain.this, "Display New Group Form", Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.group_view_archive)
        {
            ft.replace(R.id.fragment_place, new Group_Fragment_Archive());
            Toast.makeText(GroupMain.this, "Display Group Archive", Toast.LENGTH_SHORT).show();
        }

        ft.commit();
        return super.onOptionsItemSelected(item);
    }

    public void createGroup(View v)
    {
        //Toast.makeText(GroupMain.this, "Group Created!", Toast.LENGTH_SHORT).show();

        if(newGroupFragment.validExpirationDate()) {

            StudentGroup newGroup = newGroupFragment.fetchGroupInfo();

            if(newGroup.getGroupName().isEmpty() || newGroup.getGroupClass().isEmpty())
            {
                Toast.makeText(GroupMain.this, "Group Name and Class are required!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String gName ="", gClass="", expires="";
                // Prep values for url
                try{
                    gName = URLEncoder.encode(newGroup.getGroupName(), "utf-8");
                    gClass = URLEncoder.encode(newGroup.getGroupClass(), "utf-8");
                    expires = URLEncoder.encode(newGroup.getExpires(), "utf-8");

                }catch(UnsupportedEncodingException e)
                {
                    //TODO
                }

                String url = "http://steiner.solutions/studentHelper/newGroup/" + StudentHelper.getUser().getUserId() + "/"+gName+"/"+gClass+"/"+expires;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(GroupMain.this, "Group Successfully Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), GroupSingle.class);
                                User u = user;
                                u.addActiveGroup(response);
                                StudentGroup g = u.getActiveGroups().get( (u.getActiveGroups().size()-1) );
                                u.setCurrentGroup(g.getGroup_id());
                                StudentHelper.setUser(u);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO Failed response from server
                    }
                });
                // Add the request to the RequestQueue.
                helper.add(stringRequest);
            }
        }
        else
        {
            Toast.makeText(GroupMain.this, "Date must be later than today!", Toast.LENGTH_SHORT).show();
        }


    }

    public void updateUserName(View v)
    {

        FragmentTransaction trans = getFragmentManager().beginTransaction();

        if(userInfo.changeUsername())
        {

            // Encode Username for url. ex Test Droid -> Test+Droid
            String username = "";
            try{
                username = URLEncoder.encode(user.getUserName(), "utf-8");
            }catch(UnsupportedEncodingException e)
            {
                //TODO
            }

            String url = "http://steiner.solutions/studentHelper/updateUserName/" + user.getUserId() + "/" + username;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("1")) {
                                response = "Username updated!";
                            } else {
                                response = "Update error";
                            }
                            Toast.makeText(GroupMain.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO Failed response from server
                    }
            });
            // Add the request to the RequestQueue.
            helper.add(stringRequest);
        }
        else
        {
            Toast.makeText(GroupMain.this, "No changes detected", Toast.LENGTH_SHORT).show();
        }

        trans.replace(R.id.fragment_place, userInfo);
        trans.commit();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //TODO save user changes
    }


    public void findGroup(View v)
    {
        StudentGroup search = findGroups.gatherInput();

    helper.getRequestQueue();
        boolean ready = false;
        boolean alreadyJoined = false;
        String gId = "";
        String gName = " ";
        String gClass = " ";
        String url ="";


        if(((search.getGroupName() != null) && (search.getGroupClass() != null )))
        {
            ready = true;
            try {

                gName = URLEncoder.encode(search.getGroupName(), "utf-8");

                gClass = URLEncoder.encode(search.getGroupClass(), "utf-8");
                url = "http://steiner.solutions/studentHelper/joinGroup/"+Integer.toString(user.getUserId())+"/0/" + gName + "/" + gClass;
            } catch (UnsupportedEncodingException e) {
                //TODO
            }

        }
        if(search.getGroup_id() != 0) {
            ready = true;
            try {
                gId = URLEncoder.encode(Integer.toString(search.getGroup_id()), "utf-8");
                url = "http://steiner.solutions/studentHelper/joinGroup/"+Integer.toString(user.getUserId())+"/" + gId;
            } catch (UnsupportedEncodingException e) {
                //TODO
            }
        }
        User usr = StudentHelper.getUser();

        StudentGroup g = usr.getGroup(search.getGroup_id());





        Log.v(debugHeading, "GNAME: "+g.getGroupName());

        if(g.getGroupName() != null) {

            alreadyJoined = true;
            ready = false;
        }

        if(search.getGroupName() != null) {
            StudentGroup gTwo = usr.getGroupByName(search.getGroupName());

            if(gTwo.getGroupClass() != null) {

                if (gTwo.getGroupClass().equals(search.getGroupClass())) {
                    alreadyJoined = true;
                    ready = false;
                }
            }
        }

        if(ready){
            // Encode Username for url. ex Test Droid -> Test+Droid
            Log.v(debugHeading, "submitting request: "+url);
            StringRequest stringReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.v(debugHeading, response);

                            if(response.equals("No Group Found"))
                            {
                                Toast.makeText(GroupMain.this, response, Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(GroupMain.this, "Group Successfully Joined", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), GroupSingle.class);
                                User u = StudentHelper.getUser();
                                u.addActiveGroup(response);
                                StudentGroup g = u.getActiveGroups().get( (u.getActiveGroups().size()-1) );
                                u.setCurrentGroup(g.getGroup_id());
                                StudentHelper.setUser(u);
                                startActivity(intent);
                            }
                            /*

                            */
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //TODO Failed response from server
                }
            });
            // Add the request to the RequestQueue.
            helper.add(stringReq);
        }
        else
        {
            if(alreadyJoined)
                Toast.makeText(GroupMain.this, "Already a member!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(GroupMain.this, "Please insert group information.", Toast.LENGTH_SHORT).show();
        }



    }
}
