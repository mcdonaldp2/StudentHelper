package com.helper.groupa.studenthelper;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by willsteiner on 9/30/15.
 */
public class GroupUserTest extends ActivityInstrumentationTestCase2<GroupMain> {


    public GroupUserTest() {

        super(GroupMain.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        getActivity();
    }

    // Happy path test
    /*  User wants to uniquely identify himself.
        Until manually set UI references to the user are printed: "student user_id"
        Note: My laptop always identifies by the number 15555215554
         To replicate new users, the record is deleted from the db prior to test run
    */

    // Happy Paths - User identity modification

    // Sets Username
    public void testFirstUserModification() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("User Info")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.user_info_username))).perform(clearText()).perform(typeText("TestDroid"));
        onView(withId(R.id.save_user_info_button)).perform(click());
    }
    // Sad Paths - User identity modification


    // User uses same username
    public void testSecondUserModification() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("User Info")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.save_user_info_button)).perform(click());
    }
    // User clears field and saves
    public void testThirdUserModification() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("User Info")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.user_info_username))).perform(clearText());
        onView(withId(R.id.save_user_info_button)).perform(click());
    }

    // User enters multiple words
    public void testFifthUserModification() {
        onView(isRoot()).perform(pressMenuKey());
        onView(withText("User Info")).check(matches(isDisplayed())).perform(click());
        onView((withId(R.id.user_info_username))).perform(clearText()).perform(typeText("Test Droid One"));
        onView(withId(R.id.save_user_info_button)).perform(click());
    }


}