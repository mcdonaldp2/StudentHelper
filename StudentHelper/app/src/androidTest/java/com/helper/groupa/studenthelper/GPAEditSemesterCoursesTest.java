package com.helper.groupa.studenthelper;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by William on 10/20/2015.
 */
public class GPAEditSemesterCoursesTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    String semesterTitle;
    String courseTitle;
    String courseTitle2;

    public GPAEditSemesterCoursesTest() {

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

    //Scenario 1: Grade Edit (Happy path)
    public void testChangeCourseGrade() {

        semesterTitle = "SemesterWithClasses";
        courseTitle = "SemesterClass1015";
        /*semesterTitleReset = "SemesterToBeReset";
        coursetTitleReset = "CourseToBeReset";*/
        addSemesterAndCourse(semesterTitle, courseTitle);
       /* addSemesterAndCourse(semesterTitleReset, coursetTitleReset);*/


        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //clicks on course
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_TITLE, courseTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //changes grade from B to A
        onView(withId(R.id.editGradeSpinnerView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        //updates course
        onView(withId(R.id.editCourseApplyButton)).perform(click());

        //Asserts course grade has been changed to A
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_GRADE, "A")
                .withStrictColumnChecks(false))).perform(click());

        deleteSemesterData();
    }

    //Scenario 2: Improper Grade input (sad path)
    public void testImproperGradeEdit() {
        semesterTitle = "SemesterWithClasses";
        courseTitle = "ImproperGradeInput";
        addSemesterAndCourse(semesterTitle, courseTitle);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //clicks on course
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_TITLE, courseTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //changes grade from B to Grade (improper)
        onView(withId(R.id.editGradeSpinnerView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Grade"))).perform(click());

        //updates course
        onView(withId(R.id.editCourseApplyButton)).perform(click());

        //Checks for Toast
        onView(withText("Choose a grade!")).inRoot(withDecorView(not(is(getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));

        deleteSemesterData();

    }

    //Scenario 3: Cancel Changes to Semester (neutral path)
    public void testResetEditedClasses() {
        semesterTitle = "ResetCourseEdits";
        courseTitle = "SemesterClassReset";
        addSemesterAndCourse(semesterTitle, courseTitle);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //clicks on course
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_TITLE, courseTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //changes grade from B to F
        onView(withId(R.id.editGradeSpinnerView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("F"))).perform(click());

        //updates course
        onView(withId(R.id.editCourseApplyButton)).perform(click());

        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText("Yes")).perform(click());
        //onView(withText("3.0")).perform(click());

        deleteSemesterData();

    }

    //Scenario 4: Delete Course (neutral)
    public void testDeleteCourse() {
        semesterTitle = "DeleteCourseEdits";
        courseTitle = "DeleteSemesterClass";
        courseTitle2 = "WontBeDeletedClass";
        addSemesterAndCourse(semesterTitle, courseTitle, courseTitle2);

        onView(withId(R.id.gpaCalc)).perform(click());

        //clicks on semester
        onData(allOf(withRowString(DBHelper.COLUMN_SEMESTER_TITLE, semesterTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Edit")).perform(click());

        //clicks on course
        onData(allOf(withRowString(DBHelper.COLUMN_COURSE_TITLE, courseTitle)
                .withStrictColumnChecks(false))).perform(click());
        onView(withText("Delete")).perform(click());

        //checks for the course not being there
        onView(withId(R.id.editSemesterCoursesListView)).check(matches(not(withStringContent(courseTitle))));

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


    //Just deletes all the semester data after tests are ran
    /*public void testDeleteAll() {
        deleteSemesterData();
    }*/

    public void addSemesterAndCourse(String semesterTitle, String courseTitle) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());

        db.insertSemester(semesterTitle, "false", 3.0);
        int semesterID = db.findID(semesterTitle);
        db.insertCourse(courseTitle, 3, "B", 3.0, 9.0, semesterID);
    }

    public void addSemesterAndCourse(String semesterTitle, String courseTitle, String courseTitle2) {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());

        db.insertSemester(semesterTitle, "false", 3.0);
        int semesterID = db.findID(semesterTitle);
        db.insertCourse(courseTitle, 3, "B", 3.0, 9.0, semesterID);
        db.insertCourse(courseTitle2, 3, "B", 3.0, 9.0, semesterID);
    }

    public void deleteSemesterData() {
        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        db.deleteAllGPAData();
    }
}
