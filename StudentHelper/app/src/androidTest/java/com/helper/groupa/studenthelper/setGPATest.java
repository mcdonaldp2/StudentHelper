package com.helper.groupa.studenthelper;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


/**
 * Created by William on 9/27/2015.
 */
public class setGPATest extends ActivityInstrumentationTestCase2<GPAActivity> {



    public setGPATest() {

        super(GPAActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Scenario 1: Add Semester Displayed
    public void testsetGPATestDoubleInput() {
        String semesterTitleColumn = DBHelper.COLUMN_SEMESTER_TITLE;
        String semesterTitle = "cursorAttempt";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.setGPAButton)).perform(click());
        onView(withId(R.id.setGPAEditText)).perform(typeText("3.5"));
        onView(withId(R.id.saveGPAButton)).perform(click());
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        deleteSemesterInTest(semesterTitle);


    }



    //Scenario 2: Improper GPA Input
    public void testImproperDoubleInput() {
        String semesterTitle = "ImproperDouble";
        String badGPAInput = "3.p";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.setGPAButton)).perform(click());
        onView(withId(R.id.setGPAEditText)).perform(typeText(badGPAInput));
        onView(withId(R.id.saveGPAButton)).perform(click());
        onView(withText("Input a decimal value!")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    //Scenario 3: Empty setGPAEditText
    public void testEmptyGPAEditText() {
        String semesterTitle = "EmptyDecimalField";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.setGPAButton)).perform(click());
        onView(withId(R.id.saveGPAButton)).perform(click());
        onView(withText("Input a decimal value!")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    //deletes the added semester
    public void deleteSemesterInTest(String semesterTitle) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteSemesterString(semesterTitle);
    }






}