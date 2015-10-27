package com.helper.groupa.studenthelper;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by willsteiner on 10/1/15.
 */
public class GroupNewGroupTest extends ActivityInstrumentationTestCase2<GroupMain> {


    public GroupNewGroupTest() {

        super(GroupMain.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }



    // Happy Path - User inserts all information into form correctly
    public void testFirstGroupAddition() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("Create")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.group_new_groupName))).perform(clearText()).perform(typeText("Group_A"));
        onView((withId(R.id.group_new_groupClass))).perform(clearText()).perform(typeText("CMSC 355"));
        onView((withId(R.id.group_new_datePicker))).perform(setDate(2015, 12, 18));
        onView((withId(R.id.group_create_new_button))).perform(scrollTo());
        onView((withId(R.id.group_create_new_button))).perform(click());
    }

    // Sad Path - User inserts due date prior to today
    public void testSecondGroupAddition() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("Create")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.group_new_groupName))).perform(clearText()).perform(typeText("Group_A"));
        onView((withId(R.id.group_new_groupClass))).perform(clearText()).perform(typeText("CMSC 355"));
        onView((withId(R.id.group_new_datePicker))).perform(setDate(2015, 3, 2));
        onView((withId(R.id.group_create_new_button))).perform(scrollTo());
        onView((withId(R.id.group_create_new_button))).perform(click());
    }

    // Sad Path - User leaves all fields blank and clicks create
    public void testThirdGroupAddition() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("Create")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.group_create_new_button))).perform(scrollTo());
        onView((withId(R.id.group_create_new_button))).perform(click());
    }

    // Sad Path - User inputs name and date but forgets class
    public void testFourthGroupAddition() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("Create")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.group_new_groupName))).perform(clearText()).perform(typeText("Group_A"));
        onView((withId(R.id.group_new_datePicker))).perform(setDate(2015, 11, 24));
        onView((withId(R.id.group_create_new_button))).perform(scrollTo());
        onView((withId(R.id.group_create_new_button))).perform(click());
    }


}