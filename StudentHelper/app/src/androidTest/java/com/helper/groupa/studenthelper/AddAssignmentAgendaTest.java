package com.helper.groupa.studenthelper;

/** TESTS FOR  create new Assignment-Agenda scenario tests
 * Created by paulmcdonaldjr on 9/29/15.
 */
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.test.ActivityInstrumentationTestCase2;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class AddAssignmentAgendaTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public AddAssignmentAgendaTest() {

        super(HomeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Scenario 1, Happy Path
    public void testAddAssignmentSuccess() {
        String className = "Math 151";
        String assignment = "Pages 30-32 #1,2,3,4,5,6,7,8,9,10";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        onView(withId(R.id.agenda_scroll)).check(matches(( isDisplayed() )));
    }
    //Scenario 2, Sad Path
    public void testEmptyInput() {
        String className = "CHEM 101";
        String assignment = "";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2016, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        onView(withId(R.id.addPageLayout)).check(matches((isDisplayed())));
    }
    //Scenario 3, Sad Path
    public void testOldDateAssignment() {
        String className = "CMSC 355";
        String assignment = "1st iteration";
        onView(withId(R.id.agenda)).perform(click());
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.class_editText)).perform(typeText(className));
        onView(withId(R.id.assignment_editText)).perform(typeText(assignment));
        onView(withId(R.id.agenda_add_datePicker)).perform(PickerActions.setDate(2014, 10, 31));
        onView(withId(R.id.action_add_button)).perform(click());
        onView(withId(R.id.addPageLayout)).check(matches((isDisplayed())));
         deleteAddedAssignment("Math 151");
    }
    private void deleteAddedAssignment(String className){
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteAssignment(className);
    }



}
