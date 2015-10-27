package com.helper.groupa.studenthelper;

import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.view.WindowManager;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.allOf;

// ************************SPECIAL NOTE PREFACE*****************************
// SUPPORT MATCHER METHODS BELOW TEST SCENARIOS
// THERE IS SOME JUNK CODE KEPT AT THE VERY BOTTOM FOR BOOKKEEPING AND FUTURE USE.
// ************************SPECIAL NOTE PREFACE*****************************
public class GBAddGrades extends ActivityInstrumentationTestCase2<GBClass> {

    public GBAddGrades() {
        super(GBClass.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();

        getActivity().getApplicationContext().deleteDatabase("StudentHelperDB2.db");

        GBClassDAO GBClassDAO = new GBClassDAO(getActivity().getApplicationContext());
        GBSyllabusDAO GBSyllabusDAO = new GBSyllabusDAO(getActivity().getApplicationContext());
        GBGradeDAO GBGradeDAO = new GBGradeDAO(getActivity().getApplicationContext());

        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 355");
        GBClassUnit.setGrade(87.5);
        GBClassDAO.createClass(GBClassUnit);

        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Exams");
        GBSyllabusUnit.setWeight(50);
        GBSyllabusUnit.setGrade(82.5);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);

        GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Projects");
        GBSyllabusUnit.setWeight(50);
        GBSyllabusUnit.setGrade(92.5);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);

        GBGradeUnit GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Exam 1");
        GBGradeUnit.setPointsEarned(80);
        GBGradeUnit.setPointsPossible(100);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(1));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(1).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Exam 2");
        GBGradeUnit.setPointsEarned(85);
        GBGradeUnit.setPointsPossible(100);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(1));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(1).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Project 1");
        GBGradeUnit.setPointsEarned(90);
        GBGradeUnit.setPointsPossible(100);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(2));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(2).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Project 2");
        GBGradeUnit.setPointsEarned(95);
        GBGradeUnit.setPointsPossible(100);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(2));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(2).getId());
        GBGradeDAO.createGrade(GBGradeUnit);
    }


    // ************************SPECIAL NOTE*****************************
    // TESTING SCENARIOS BELOW.
    // ************************SPECIAL NOTE*****************************

    /**
     * SCENARIO: ADD EXAM GRADE (HAPPY PATH)
     */
    public void testAddExamGrade() {
        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 355");
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Exams");
        GBGradeUnit GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Exam 3");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withId(R.id.gb_add_grade_button)).perform(click());
        onView(withId(R.id.gb_grade_name)).perform(typeText("Exam 3"));
        onView(withId(R.id.gb_grade_earned)).perform(typeText("70"));
        onView(withId(R.id.gb_grade_possible)).perform(typeText("100"));
        onView(withText("ADD")).perform(click());
        onData(allOf(instanceOf(GBGradeUnit.class), withGradeContent(GBGradeUnit))).check(matches(isDisplayed()));
    }

    /**
     * SCENARIO: ADD EXTRA CREDIT (SAD PATH)
     */
    public void testAddExtraCredit() {
        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 355");
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Projects");
        GBGradeUnit GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("EC");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withId(R.id.gb_add_grade_button)).perform(click());
        onView(withId(R.id.gb_grade_name)).perform(typeText("EC"));
        onView(withId(R.id.gb_grade_earned)).perform(typeText("10"));
        onView(withId(R.id.gb_grade_possible)).perform(typeText("0"));
        onView(withText("ADD")).perform(click());
        onView(withText("Point Possible cannot be 0!"))
                .inRoot(isToast())
                .check(matches(isDisplayed()));
    }

    /**
     * SCENARIO: ADDING DECIMAL GRADES (SAD PATH)
     */
    public void testAddingDecimalGrades() {
        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 355");
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("HW");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onView(withId(R.id.gb_add_syllabus_button)).perform(click());
        onView(withId(R.id.gb_syllabus_name)).perform(typeText("HW"));
        onView(withId(R.id.gb_syllabus_weight)).perform(typeText("10.5"));
        onView(withText("ADD")).perform(click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).check(matches
                (isDisplayed()));
    }


    // ************************SPECIAL NOTE*****************************
    // SUPPORT MATCHER METHODS FOR TESTING
    // ************************SPECIAL NOTE*****************************

    public static Matcher<Object> withScheduleContent(GBClassUnit GBClassUnit) {
        return withScheduleContent(equalTo(GBClassUnit));
    }

    private static Matcher<Object> withScheduleContent(final Matcher<GBClassUnit> schedule) {
        return new BoundedMatcher<Object, GBClassUnit>(GBClassUnit.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Returned");
            }

            @Override
            protected boolean matchesSafely(GBClassUnit item) {
                if (schedule.matches(item)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    public static Matcher<Object> withSyllabusContent(GBSyllabusUnit GBSyllabusUnit) {
        return withSyllabusContent(equalTo(GBSyllabusUnit));
    }

    private static Matcher<Object> withSyllabusContent(final Matcher<GBSyllabusUnit> syllabus) {
        return new BoundedMatcher<Object, GBSyllabusUnit>(GBSyllabusUnit.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Returned");
            }

            @Override
            protected boolean matchesSafely(GBSyllabusUnit item) {
                if (syllabus.matches(item)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    public static Matcher<Object> withGradeContent(GBGradeUnit GBGradeUnit) {
        return withGradeContent(equalTo(GBGradeUnit));
    }

    private static Matcher<Object> withGradeContent(final Matcher<GBGradeUnit> grade) {
        return new BoundedMatcher<Object, GBGradeUnit>(GBGradeUnit.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Returned");
            }

            @Override
            protected boolean matchesSafely(GBGradeUnit item) {
                if (grade.matches(item)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        return true;
                    }
                }
                return false;
            }
        };
    }


    // ************************SPECIAL NOTE*****************************
    // PLEASE IGNORE. CODE BELOW IS KEPT FOR BOOKKEEPING AND FUTURE USE
    // ************************SPECIAL NOTE*****************************
    /*
    onView(withId(R.id.add_class_button)).perform(click());
    onView(withId(R.id.class_popup_name)).perform(typeText("CMSC 355"));
    onView(withText("OK")).perform(click());
    Schedule schedule = new Schedule();
    schedule.setName("CMSC 355");
    onData(allOf(instanceOf(Schedule.class), withScheduleContent(schedule))).perform
            (click());
    onView(withId(R.id.add_assign_button)).perform(click());
    onView(withId(R.id.assignment_popup_name)).perform(typeText("Exams"));
    onView(withId(R.id.weight_possible_popup)).perform(typeText("50"));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.add_assign_button)).perform(click());
    onView(withId(R.id.assignment_popup_name)).perform(typeText("Projects"));
    onView(withId(R.id.weight_possible_popup)).perform(typeText("50"));
    onView(withText("OK")).perform(click());
    Syllabus syllabus = new Syllabus();
    syllabus.setName("Exams");
    onData(allOf(instanceOf(Syllabus.class), withSyllabusContent(syllabus))).perform
            (click());
    onView(withId(R.id.add_grade_button)).perform(click());
    onView(withId(R.id.grade_popup_name)).perform(typeText("Exam 1"));
    onView(withId(R.id.point_earned_popup)).perform(typeText("85"));
    onView(withId(R.id.point_possible_popup)).perform(typeText("100"));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.add_grade_button)).perform(click());
    onView(withId(R.id.grade_popup_name)).perform(typeText("Exam 2"));
    onView(withId(R.id.point_earned_popup)).perform(typeText("88"));
    onView(withId(R.id.point_possible_popup)).perform(typeText("100"));
    onView(withText("OK")).perform(click());
    pressBack();
    syllabus.setName("Projects");
    onData(allOf(instanceOf(Syllabus.class), withSyllabusContent(syllabus))).perform
            (click());
    onView(withId(R.id.add_grade_button)).perform(click());
    onView(withId(R.id.grade_popup_name)).perform(typeText("Project 1"));
    onView(withId(R.id.point_earned_popup)).perform(typeText("80"));
    onView(withId(R.id.point_possible_popup)).perform(typeText("100"));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.add_grade_button)).perform(click());
    onView(withId(R.id.grade_popup_name)).perform(typeText("Project 2"));
    onView(withId(R.id.point_earned_popup)).perform(typeText("90"));
    onView(withId(R.id.point_possible_popup)).perform(typeText("100"));
    onView(withText("OK")).perform(click());
    pressBack();
    pressBack();
    */

}
