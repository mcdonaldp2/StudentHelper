package com.helper.groupa.studenthelper.CalendarTests;

import android.test.ActivityInstrumentationTestCase2;

import com.helper.groupa.studenthelper.Calendar.ClassSchedule;
import com.helper.groupa.studenthelper.Calendar.ScheduleMainActivity;
import com.helper.groupa.studenthelper.DBHelper;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by Matt on 9/29/2015.
 */
public class ViewScheduleTest extends ActivityInstrumentationTestCase2<ScheduleMainActivity> {


    public ViewScheduleTest() {
        super(ScheduleMainActivity.class);
    }

    @Override
    public void setUp() throws Exception {

        super.setUp();
        getActivity();
    }

    public void testScenario1(){


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testScenario2(){
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("Day view")).check(matches(isDisplayed())).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void testScenario3(){
        onView(isRoot()).perform(pressMenuKey());
        //onView(withText("Week view")).check(matches(isDisplayed())).perform(click());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
    }

    private void deleteAddedObject(String name){
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        DBHelper.Schedule dbHelper = db.new Schedule();

        ArrayList<ClassSchedule> schedules = dbHelper.getAllClassSchedules();
        for(ClassSchedule schedule:schedules){
            if(schedule.getName().equals(name)){
                dbHelper.deleteClassSchedule(schedule.getId());
            }
        }
    }
}
