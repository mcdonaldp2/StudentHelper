package com.helper.groupa.studenthelper;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowDouble;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Double.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by William on 10/21/2015.
 */
public class GPAEditSemesterTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    String semesterTitle;
    String newGPA;
    public GPAEditSemesterTest() {

        super(HomeActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();

        //deletes all data from TABLE_SEMESTERS and TABLE_COURSES before running tests
        deleteSemesterData();

    }

    //scenario 1: Edit GPA (happy path)
    public void testEditSemesterGPA() {
        semesterTitle = "ChangeGPASemester";
        newGPA = "4.0";
        addSemester(semesterTitle);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        onView(withId(R.id.editSemesterGPAEditText)).perform(clearText()).perform(typeText(newGPA));
        onView(withId(R.id.updateButton)).perform(click());

        onData(allOf(withRowDouble(DBHelper.COLUMN_SEMESTER_GPA, valueOf(newGPA))
                .withStrictColumnChecks(false))).perform(click());

        deleteSemesterData();
    }

    //Scenario 2: Improper GPA Edit (sad path)
    public void testImproperGPAChange() {
        semesterTitle = "ImproperGPASemester";
        newGPA = "4.g";
        addSemester(semesterTitle);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        onView(withId(R.id.editSemesterGPAEditText)).perform(clearText()).perform(typeText(newGPA));
        onView(withId(R.id.updateButton)).perform(click());

        onView(withText("Input a decimal value!")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

        deleteSemesterData();
    }

    //Scenario 3: Delete a Semester (neutral path)
    public void testDeleteSemester() {
        semesterTitle = "DeleteSemester";
        addSemester(semesterTitle);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Delete")).perform(click());

        //checks for the course not being there
        onView(withId(R.id.gpaListView)).check(matches(not(withStringContent(semesterTitle))));

        deleteSemesterData();

    }

    ///custom matcher for deleteTest
    public static Matcher<Object> withStringContent(String semTitle) {
        return withStringContent(equalTo(semTitle));
    }

    private static Matcher<Object> withStringContent(final Matcher<String> semTitle) {
        return new BoundedMatcher<Object, String>(String.class) {

            @Override
            protected boolean matchesSafely(String item) {
                if (semTitle.matches(item)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Returned");
            }


        };
    }

    public void addSemester(String semesterTitle) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());

        db.insertSemester(semesterTitle, "true", 3.0);

    }

    public void deleteSemesterData() {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteAllGPAData();
    }
}
