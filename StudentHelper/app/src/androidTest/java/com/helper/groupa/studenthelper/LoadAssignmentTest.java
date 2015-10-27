package com.helper.groupa.studenthelper;

/** TESTS FOR SAVE ASSIGNMENTS SCENARIOS
 * Created by paulmcdonaldjr on 9/29/15.
 */
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.test.ActivityInstrumentationTestCase2;

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
import com.helper.groupa.studenthelper.DBHelper;

//import static android.support.test.espresso.contrib.PickerActions.setDate;
public class LoadAssignmentTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public LoadAssignmentTest() {

        super(HomeActivity.class);
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Scenario 1, Happy Path
    public void test1LoadAssignmentSuccess() {
        String className = "Math 151";
        String assignment = "Pages 30-32 #1,2,3,4,5,6,7,8,9,10";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.agenda_layout)).check(matches(( isDisplayed() )));
        onView(withId(getAssignId())).check(matches(isDisplayed()));



    }
    //Scenario 2, Sad Path
    public void test2EmptyInputAndLoad() {
        String className = "BIO 5000";
        String assignment = "";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        int id = getAssignId();
        Espresso.pressBack();
        Espresso.pressBack();
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(id)).check(matches(not(withText("BIO 5000\n" +
                "\n" +
                "Due: 10/31/2015"))));
    }
    //Scenario 3, Sad Path
    public void test3OldDateLoad() {
        String className = "CMSC 355";
        String assignment = "Test for Load";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2014, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        int id = getAssignId();
        Espresso.pressBack();
        Espresso.pressBack();
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(id)).check(matches(not(withText("CMSC 355\n" +
                "Test for Load\n" +
                "Due: "))));

        deleteAddedAssignment("Math 151");
    }

    // returns the id of the last entry in the database
    private int getAssignId() {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        Cursor cursor = db.getAllAssignments();

        if (cursor.getCount() == 0) {
            return 1;
        } else {
            cursor.moveToLast();
            int id = cursor.getInt(0);
                return id;
        }

    }
    private void deleteAddedAssignment(String className){
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteAssignment(className);
        //db.deleteAllData();
    }
}
