package com.helper.groupa.studenthelper.CalendarTests;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.helper.groupa.studenthelper.Calendar.ClassSchedule;
import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.HomeActivity;
import com.helper.groupa.studenthelper.R;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Matt on 10/24/2015.
 */
public class UpdateEditScheduleTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    DBHelper.Schedule dbHelper;

    public UpdateEditScheduleTest() {
        super(HomeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        dbHelper = db.new Schedule();
        dbHelper.insertClassSchedule("class1", 4, 0, 6, 0, "monday");
        getActivity();
    }
    @Override
    public void tearDown(){
        clearDb();  //clear db after each test
    }

    /**
     * Scenario 1: Update class on schedule (Happy path)
     * Test the ui's ability to update a class on the schedule
     */
    public void testScenario1(){
        onView(ViewMatchers.withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
        onView(withId(R.id.updateStartTimeButton)).perform(click());

        onView(withId(R.id.updateNameInput)).perform(clearText());
        onView(withId(R.id.updateNameInput)).perform(typeText("class2"));
        onView(withId(R.id.updateClassButton)).perform(click());

        onView(withText("Class updated: class1" )).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Scenario 2: Update a class with time conflict (Sad path)
     * Test the ui's ability to handle a time conflict on update
     */
    public void testScenario2(){
        dbHelper.insertClassSchedule("class2", 7, 0, 9, 0, "monday");
        onView(withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
        onView(withId(R.id.updateStartTimeButton)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(8, 0));
        onView(withId(R.id.updateEndTimeButton)).perform(click());
        onView(withId(R.id.updateClassButton)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Schedule conflicts with one already on the calender")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Scenario 3: Update a class with a name conflict (Sad path)
     * Test the ui's ability to handle a name conflict on update
     */
    public void testScenario3(){
        dbHelper.insertClassSchedule("class2", 7, 0, 9, 0, "monday");
        onView(withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
        onView(withId(R.id.updateStartTimeButton)).perform(click());

        onView(withId(R.id.updateNameInput)).perform(clearText());
        onView(withId(R.id.updateNameInput)).perform(typeText("class2"));
        onView(withId(R.id.updateClassButton)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Class already exists on schedule")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Clear any schedules in the database
     */
    private void clearDb(){
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        DBHelper.Schedule dbHelper = db.new Schedule();

        ArrayList<ClassSchedule> schedules = dbHelper.getAllClassSchedules();
        for(ClassSchedule schedule:schedules){
            dbHelper.deleteClassSchedule(schedule.getId());
        }
    }
}
