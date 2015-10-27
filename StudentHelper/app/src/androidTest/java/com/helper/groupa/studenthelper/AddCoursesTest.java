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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by William on 9/28/2015.
 */
public class AddCoursesTest extends ActivityInstrumentationTestCase2<GPAActivity> {

    public AddCoursesTest() {

        super(GPAActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    //Scenario 1: Added Course Displayed
    public void testAddingCourseSuccessful() {
        String semesterTitleColumn = DBHelper.COLUMN_SEMESTER_TITLE;
        String semesterTitle = "cursorAttempt";
        String courseTitle = "cursorAttemptCourses";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.chooseClassesButton)).perform(click());
        onView(withId(R.id.addCourseButton)).perform(click());
        onView(withId(R.id.courseTitleEditText)).perform(typeText(courseTitle));

        onView(withId(R.id.gradeSpinnerView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        onView(withId(R.id.creditHoursSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("4"))).perform(click());

        onView(withId(R.id.courseSaveButton)).perform(click());
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_TITLE, courseTitle)
                .withStrictColumnChecks(false))).perform(click());

        deleteSemesterInTest(semesterTitle);
        deleteCourseInTest(courseTitle);

    }


    //Scenario 2: Course Title Error Message
    public void testImproperCourseFieldInputs() {
        String semesterTitle = "ImproperCourseFields";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.chooseClassesButton)).perform(click());
        onView(withId(R.id.addCourseButton)).perform(click());
        onView(withId(R.id.courseSaveButton)).perform(click());
        onView(withText("Create a title")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

        deleteSemesterInTest(semesterTitle);
    }

    //Scenario 3: Caution Dialog Displayed
    public void testComfirmationDialogBackPressed() {
        String courseTitle = "new course";
        String semesterTitle = "PressBack";
        String dialogText = "You will lose all courses added to this semester, do you wish to continue?";
        onView(withId(R.id.addSemesterButton)).perform(click());
        onView(withId(R.id.semesterTitleTextEdit)).perform(typeText(semesterTitle));
        onView(withId(R.id.chooseClassesButton)).perform(click());
        onView(withId(R.id.addCourseButton)).perform(click());
        onView(withId(R.id.courseTitleEditText)).perform(typeText(courseTitle));

        onView(withId(R.id.gradeSpinnerView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        onView(withId(R.id.creditHoursSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("4"))).perform(click());

        onView(withId(R.id.courseSaveButton)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText(dialogText)).check(matches(isDisplayed()));

        deleteSemesterInTest(semesterTitle);
        deleteCourseInTest(courseTitle);
    }

    //deletes the created course from database
    public void deleteCourseInTest(String courseTitle) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteCourseString(courseTitle);
    }

    //deletes the created semester from database
    public void deleteSemesterInTest(String semesterTitle) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteSemesterString(semesterTitle);
    }

}
