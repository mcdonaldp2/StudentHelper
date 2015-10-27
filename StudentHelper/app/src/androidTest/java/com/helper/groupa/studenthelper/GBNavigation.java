package com.helper.groupa.studenthelper;

import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;

// ************************SPECIAL NOTE PREFACE*****************************
// SUPPORT MATCHER METHODS BELOW TEST SCENARIOS
// ************************SPECIAL NOTE PREFACE*****************************
public class GBNavigation extends ActivityInstrumentationTestCase2<GBClass> {

    public GBNavigation() {
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
        GBClassUnit.setName("CMSC 256");
        GBClassUnit.setGrade(86.00);
        GBClassDAO.createClass(GBClassUnit);

        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("HW");
        GBSyllabusUnit.setWeight(25.00);
        GBSyllabusUnit.setGrade(80.00);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);

        GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Iteration");
        GBSyllabusUnit.setWeight(50.00);
        GBSyllabusUnit.setGrade(97.00);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);

        GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Exam");
        GBSyllabusUnit.setWeight(25.00);
        GBSyllabusUnit.setGrade(81.00);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);

        GBGradeUnit GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("HW 1");
        GBGradeUnit.setPointsEarned(82.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(1));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(1).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("HW 2");
        GBGradeUnit.setPointsEarned(70.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(1));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(1).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("HW 3");
        GBGradeUnit.setPointsEarned(88.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(1));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(1).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Iteration 1");
        GBGradeUnit.setPointsEarned(100.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(2));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(2).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Iteration 2");
        GBGradeUnit.setPointsEarned(94.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(2));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(2).getId());
        GBGradeDAO.createGrade(GBGradeUnit);

        GBGradeUnit = new GBGradeUnit();
        GBGradeUnit.setName("Midterm");
        GBGradeUnit.setPointsEarned(70.00);
        GBGradeUnit.setPointsPossible(100.00);
        GBGradeUnit.setGBSyllabusUnit(GBSyllabusDAO.getSyllabusByID(3));
        GBGradeUnit.setScheduleID(GBSyllabusDAO.getSyllabusByID(3).getId());
        GBGradeDAO.createGrade(GBGradeUnit);
    }

    // ************************SPECIAL NOTE*****************************
    // TESTING SCENARIOS BELOW.
    // ************************SPECIAL NOTE*****************************

    /**
     * SCENARIO: NAVIGATE BACK HOME (HAPPY PATH)
     */
    public void testNaviBackHome() {
        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 256");
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Exam");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withId(R.id.grade_title_class)).perform(click());
        onView(withId(R.id.gb_title_home)).perform(click());
        onView(withText("Student Helper")).check(matches(isDisplayed()));
    }

    /**
     * SCENARIO: NAVIGATE EXTRA CREDIT (SAD PATH)
     */
    public void testNaviEC() {
        GBClassDAO GBClassDAO = new GBClassDAO(getActivity().getApplicationContext());
        GBSyllabusDAO GBSyllabusDAO = new GBSyllabusDAO(getActivity().getApplicationContext());
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Class Curve");
        GBSyllabusUnit.setExtraCredit(1);
        GBSyllabusUnit.setWeight(5.4321);
        GBSyllabusUnit.setClassUnit(GBClassDAO.getClassByID(1));
        GBSyllabusUnit.setScheduleID(GBClassDAO.getClassByID(1).getId());
        GBSyllabusDAO.createSyllabus(GBSyllabusUnit);


        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 256");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withText("Extra Credit cannot have grades!"))
                .inRoot(isToast())
                .check(matches(isDisplayed()));
    }

    /**
     * SCENARIO: NAVIGATE BETWEEN GRADES (HAPPY PATH)
     */
    public void testNaviBetweenGrades() {
        GBClassUnit GBClassUnit = new GBClassUnit();
        GBClassUnit.setName("CMSC 256");
        GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
        GBSyllabusUnit.setName("Exam");

        onData(allOf(instanceOf(GBClassUnit.class), withScheduleContent(GBClassUnit))).perform
                (click());
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withId(R.id.grade_title_syllabus)).perform(click());
        GBSyllabusUnit.setName("Iteration");
        onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform
                (click());
        onView(withId(R.id.grade_title_class)).perform(click());
        onView(withText("Gradebook: Classes")).check(matches(isDisplayed()));
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
}
