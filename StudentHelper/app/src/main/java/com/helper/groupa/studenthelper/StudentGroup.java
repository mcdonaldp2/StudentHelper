package com.helper.groupa.studenthelper;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by willsteiner on 9/17/15.
 */
public class StudentGroup extends Object {


    private String groupName;
    private String groupClass;
    private String expires;
    private ArrayList<User> members;
    private int group_id;
    private boolean active;

    public StudentGroup()
    {

    }


    public void setGroup_id(int gId)
    {
        this.group_id = gId;
    }

    public void setGroup_id(String gId)
    {
        this.group_id = Integer.parseInt(gId);
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroupName(String name)
    {
        this.groupName = name;
    }

    public String getGroupName(){return this.groupName; }

    public void setGroupClass(String className)
    {
        this.groupClass = className;
    }

    public String getGroupClass() { return this.groupClass; }

    public void setExpires(int month, int day, int year)
    {
        String date = Integer.toString(year) + "-" +Integer.toString(month) + "-" + Integer.toString(day);
        this.expires = date;
    }

    public void setExpires(String dayFormat)
    {
        //TODO add validation
        this.expires = dayFormat;
    }


    public String getExpires() { return this.expires; }


    public static int findGroup(String groupName)
    {
        // initialize id, return -1 if not found
        int  groupId = -1;

        // return group Id
        return groupId;
    }

    public boolean insertGroup()
    {
        JSONArray groupArrayJson = new JSONArray();

        for(int i = 0; i < this.members.size() ; i++)
        {
            groupArrayJson.put(this.members.get(i));
        }
        return true;
    }

    public String shortDescription()
    {
        return "\nGroup "+this.group_id+"" +
                "\n"+this.getGroupName()+
                "\n"+this.groupClass;
    }


    public String toString()
    {
        return "Name: "+this.groupName+"" +
                "\nClass: "+this.groupClass+
                "\nExpires: "+this.expires;
    }

}
