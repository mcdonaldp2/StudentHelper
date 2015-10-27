package com.helper.groupa.studenthelper.CalendarTests;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.helper.groupa.studenthelper.Calendar.ClassSchedule;
import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.HomeActivity;
import com.helper.groupa.studenthelper.R;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
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
public class ViewDeleteScheduleTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    DBHelper.Schedule dbHelper;

    public ViewDeleteScheduleTest() {
        super(HomeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //add a schedule to the calendar
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        dbHelper = db.new Schedule();
        dbHelper.insertClassSchedule("class1", 4, 0, 6, 0, "monday");
        getActivity();
    }

    @Override
    public void tearDown(){
        //clear db after each test
        clearDb();
    }

    /**
     * Scenario 1: Delete a class from the calendar (Happy path)
     * Test the ui's ability to delete a schedule from the details screen
     * Fails randomly because it checks a toast message, but still acts correctly
     */
    public void testScenario1(){
        onView(ViewMatchers.withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
        onView(withId(R.id.detailsDeleteButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Class deleted from schedule")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Scenario 2: Cancel deleting class from calendar (Sad path)
     * Test the ui's ability to back out of deleting/details screen
     */
    public void testScenario2(){
        onView(withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
        pressBack();
        pressBack();
    }

    /**
     * Scenario 3: View details about class from calendar (Happy path)
     * Test the ui's ability to view details about a class on the schedule
     */
    public void testScenario3(){
        onView(withId(R.id.scheduleButton)).perform(click());
        onView(withId(R.id.simulateClickButton)).perform(click());
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
