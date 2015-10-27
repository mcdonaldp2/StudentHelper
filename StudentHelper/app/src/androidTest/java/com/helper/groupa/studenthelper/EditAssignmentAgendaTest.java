package com.helper.groupa.studenthelper;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/** TESTS FOR "Edit an Assignment" user story
 * Created by paulmcdonaldjr on 10/20/15.
 */
public class EditAssignmentAgendaTest extends ActivityInstrumentationTestCase2<HomeActivity> {


    public EditAssignmentAgendaTest() {

        super(HomeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Scenario 1, Happy Path
    public void testEditAssignmentSuccess() {
        String className = "Math 151";
        String assignment = "Pages 30-32 #1,2,3,4,5,6,7,8,9,10";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(scrollTo(),click());
        onView(withId(R.id.agenda_scroll)).check(matches((isDisplayed())));
        onView(withId(getAssignId())).perform(scrollTo(),click());
        onView(withId(R.id.edit_class_editText)).perform(clearText());
        onView(withId(R.id.edit_class_editText)).perform(typeText("EDITING 101"));
        onView(withId(R.id.action_edit_finish)).perform(scrollTo(),click());
        onView(withId(R.id.agenda_scroll)).check(matches((isDisplayed())));
        deleteAddedAssignment("EDITING 101");

    }
    //Scenario 2, Sad Path
    public void testEmptyInputOnEdit() {
        String className = "CHEM 101";
        String assignment = "TEST NEXT CLASS";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(scrollTo(), click());
        onView(withId(R.id.agenda_scroll)).check(matches((isDisplayed())));
        onView(withId(getAssignId())).perform(scrollTo(), click());
        onView(withId(R.id.edit_class_editText)).perform(clearText());
        onView(withId(R.id.edit_class_editText)).perform(typeText(""));
        onView(withId(R.id.action_edit_finish)).perform(scrollTo(), click());
        onView(withId(R.id.edit_agenda_scroll)).check(matches((isDisplayed())));
        deleteAddedAssignment("CHEM 101");
    }
    //Scenario 3, Sad Path
    public void testOldDateEdit() {
        String className = "CMSC 355";
        String assignment = "1st iteration";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(scrollTo(), click());
        onView(withId(R.id.agenda_scroll)).check(matches((isDisplayed())));
        onView(withId(getAssignId())).perform(scrollTo(), click());
        onView(withId(R.id.agenda_edit_datePicker)).perform(PickerActions.setDate(2014, 10, 31));
        onView(withId(R.id.action_edit_finish)).perform(scrollTo(), click());
        onView(withId(R.id.edit_agenda_scroll)).check(matches((isDisplayed())));
        deleteAddedAssignment("CMSC 355");

    }


    private void deleteAddedAssignment(String className){
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteAssignment(className);
    }

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



}

