package com.helper.groupa.studenthelper.CalendarTests;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.helper.groupa.studenthelper.Calendar.ClassSchedule;
import com.helper.groupa.studenthelper.Calendar.ScheduleAddClassActivity;
import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.R;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
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
 * Created by Matt on 9/29/2015.
 */
public class AddScheduleTest extends ActivityInstrumentationTestCase2<ScheduleAddClassActivity> {

    public AddScheduleTest() {

        super(ScheduleAddClassActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    /**
     * Scenario 1: Add a class to the apps calendar (Happy path)
     * Test the ui's ability to add a schedule to the apps calendar
     */
    public void testScenario1() {
        String testClassName = "Some Random Class";
        onView(ViewMatchers.withId(R.id.updateNameInput)).perform(typeText(testClassName));

        onView(withId(R.id.mondayCheckBox)).perform(click());
        onView(withId(R.id.wednesdayCheckBox)).perform(click());
        onView(withId(R.id.fridayCheckBox)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(6, 15));
        onView(withId(R.id.addStartTimeButton)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(8, 0));
        onView(withId(R.id.addEndTimeButton)).perform(click());

        onView(withId(R.id.addClassButton)).perform(click());

        //testScenario3 will use and delete this schedule
    }

    /**
     * Scenario 2: Add a class to the apps calendar and leaving an input blank. (Sad path)
     * Test the ui's ability to account for an empty field when user tries to add a schedule
     */
    public void testScenario2() {
        String testClassName = "Some Random Class";
        onView(withId(R.id.updateNameInput)).perform(typeText(testClassName));

        onView(withId(R.id.mondayCheckBox)).perform(click());
        onView(withId(R.id.wednesdayCheckBox)).perform(click());
        onView(withId(R.id.fridayCheckBox)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(6, 15));
        onView(withId(R.id.addStartTimeButton)).perform(click());

        //leave a field blank
        //onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(8, 0));
        //onView(withId(R.id.setClassEndButton)).perform(click());

        onView(withId(R.id.addClassButton)).perform(click());
        onView(withText("One or more fields not set")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    /**
     * Scenario 3: Adding a class schedule that conflicts with one already on the calendar. (Sad path)
     * Test the ui's ability to account for conflicts on the apps calendar. Adds a second schedule
     * that conflicts with the one added in testScenario1
     */
    public void testScenario3() {
        //add first
        String testClassName = "Some Random Class";
        String testClassName2 = "Another Random Class";
        onView(withId(R.id.updateNameInput)).perform(typeText(testClassName2));

        onView(withId(R.id.mondayCheckBox)).perform(click());
        onView(withId(R.id.wednesdayCheckBox)).perform(click());
        onView(withId(R.id.fridayCheckBox)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(7, 15));
        onView(withId(R.id.addStartTimeButton)).perform(click());

        onView(withId(R.id.scheduleTimePicker)).perform(PickerActions.setTime(9, 0));
        onView(withId(R.id.addEndTimeButton)).perform(click());

        onView(withId(R.id.addClassButton)).perform(click());
        onView(withText("Schedule conflicts with one already on the calender")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

        //delete schedules so tests can be repeated
        deleteAddedObject(testClassName);
        deleteAddedObject(testClassName2);
    }

    /**
     * To make the tests repeatable, any objects being added to the database need to be removed
     * @param name The name of the schedule to delete
     */
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
