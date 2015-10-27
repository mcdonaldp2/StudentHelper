package com.helper.groupa.studenthelper;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by willsteiner on 9/17/15.
 *
 * User object to pass around user attributes with minimal calls to DB.
 * Note, this does not interface directly with DB. It is meant to be populated
 * with data from DB and used as a local refrence.
 *
 */
public class User extends Application {

    public static final String debugHeading = "S H User";

    // User's attributes

    // Identity
    private int userId; // Primary key
    private String phoneNumber; // Unique key
    private String userName; // Alternate identity

    private JSONArray groupsArray;

    // Actively manipulating group
    private int currentGroup;
    // Groups user is in:
    private ArrayList<StudentGroup> userGroupsActive = new ArrayList<StudentGroup>();
    private ArrayList<StudentGroup> userGroupsArchive = new ArrayList<StudentGroup>();

    // Initialize user object and add phone number
    public User()
    {
        Context context = StudentHelper.getInstance();
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.phoneNumber = tm.getLine1Number();
    }

    // Set's user's id, typically after a HTTP request to DB
    // @param int id
    public void setUserId(int id)
    {
        this.userId = id;
    }

    // Returns User's primary key "user_id"
    public int getUserId() {
        return this.userId;
    }

    // Returns a string representation of line 1 number
    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    // @param String name
    public void setUserName(String name)
    {
        this.userName = name;

    }
    // Returns String of user's userName
    public String getUserName()
    {
        return userName;
    }




    public static void sync()
    {
        // Reference https://stackoverflow.com/questions/28656865/send-a-jsonarray-post-request-with-android-volley-library

        // Build current user object into JSONObject { user, userName, phone, [ groups ] }

        // Post to web api


    }

    public void setCurrentGroup(int groupId)
    {
        Log.v(debugHeading, "Setting current group:" +groupId);
        this.currentGroup = groupId;
    }

    public StudentGroup getCurrentGroup()
    {
        int index = this.currentGroup;
        return getGroup(index);
    }

    public void addActiveGroup(String newGroupJSONString)
    {
        JSONObject g;
        try {
            g = new JSONObject(newGroupJSONString);
            addActiveGroup(g);

        }catch (JSONException e)
        {
            //TODO
        }

    }

    public void addActiveGroup(JSONObject newGroup)
    {
        StudentGroup g = new StudentGroup();
        try
        {
            String gId = newGroup.getString("group_id");
            String gName = newGroup.getString("groupName");
            String gClass = newGroup.getString("groupClass");
            String gExpires = newGroup.getString("expires");
            String groupActive = newGroup.getString("active");

            g.setGroup_id(Integer.parseInt(gId));
            g.setGroupName(gName);
            g.setGroupClass(gClass);
            g.setExpires(gExpires);


            this.getActiveGroups().add(g);
        }catch (JSONException e)
        {
            //TODO error
        }




    }


    public void setGroupsArray(JSONArray groupsJSONObject) {

        JSONArray groupArray = groupsJSONObject;

        int groupCount = 0;
        try {

            groupCount = groupArray.length();
            for (int i = 0; i < groupCount; i++) {
                JSONObject group = groupArray.getJSONObject(i);

                String gId = group.getString("group_id");
                String gName = group.getString("groupName");
                String gClass = group.getString("groupClass");
                String gExpires = group.getString("expires");
                String groupActive = group.getString("active");

                StudentGroup newGroup =  new StudentGroup();

                newGroup.setGroup_id(gId);
                newGroup.setGroupName(gName);
                newGroup.setGroupClass(gClass);
                newGroup.setExpires(gExpires);


                Log.v(debugHeading, "Group "+newGroup.getGroup_id()+" fetched from DB");

                if(groupActive.equals("1"))
                {
                    userGroupsActive.add(newGroup);
                }
                else
                {
                    userGroupsArchive.add(newGroup);
                }

                // If group active = false, put in archive else add to active

            }
        }
        catch (JSONException e)
        {
            //TODO
            Log.v(debugHeading, "Setting groupArray FAIL");
        }
    }

    public ArrayList<StudentGroup> getActiveGroups()
    {
        return this.userGroupsActive;
    }

    public ArrayList<StudentGroup> getArchivedGroups()
    {
        return this.userGroupsArchive;

    }

    public StudentGroup getGroup(int groupId)
    {
        StudentGroup g = new StudentGroup();
        boolean found = false;
        Log.v(debugHeading, "searching active groups for id: "+groupId+"...");

        //Check if id matches in active array
        for(int i = 0 ; i < this.userGroupsActive.size(); i++)
        {
            if((this.userGroupsActive.get(i).getGroup_id() == groupId))
            {
                Log.v(debugHeading, "match");
                g = this.userGroupsActive.get(i);
                i = this.userGroupsActive.size();
                found = true;
            }
        }

        //If not in active array, check archived groups
        if(!found)
        {
            for(int i = 0 ; i < this.userGroupsArchive.size(); i++)
            {
                if((this.userGroupsArchive.get(i).getGroup_id() == groupId))
                {
                    Log.v(debugHeading, "match");
                    g = this.userGroupsArchive.get(i);
                    i = this.userGroupsArchive.size();

                }
            }

        }


        return  g;
    }

    public StudentGroup getGroup(String groupId)
    {
        StudentGroup g = new StudentGroup();

        int gId = Integer.parseInt(groupId);

        Log.v(debugHeading, "Get group id from string: "+gId);

        g = getGroup(gId);



        return  g;
    }


    public StudentGroup getGroupByName(String name)
    {
        StudentGroup g = new StudentGroup();

        for(int i = 0 ; i < this.userGroupsActive.size(); i++)
        {
            if((this.userGroupsActive.get(i).getGroupName().equals(name)))
            {
                Log.v(debugHeading, "Group Name Match");
                g = this.userGroupsActive.get(i);
                i = this.userGroupsActive.size();
            }
        }

        return g;
    }




    // Simply return a string with respective user information
    @Override
    public String toString()
    {
        String userDetails;
        userDetails = "User Details\n";
        userDetails += "Id: "+getUserId()+"\n";
        userDetails += "UserName: "+getUserName()+"\n";
        userDetails += "Phone number: "+getPhoneNumber()+"\n";

        if(this.getActiveGroups().size() > 0) {
            userDetails+= "Groups: ";
            for (int g = 0; g < this.getActiveGroups().size(); g++) {
                userDetails += this.getActiveGroups().get(g).getGroup_id();
                if((this.getActiveGroups().size() - 1) != g)
                {
                    userDetails+= ", ";
                }
            }
        }else{
            userDetails += "No active groups. \n";
        }



        return userDetails;
    }


    public boolean hasActiveGroup(int gId)
    {
        ArrayList<StudentGroup> groups = this.getActiveGroups();
        for(int i = 0; i < groups.size() ; i++ )
        {
            if(groups.get(i).getGroup_id() == gId)
                return true;
        }


        return false;
    }

}
