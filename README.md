# Group_A-app_project_355
app_project_355 created for Group_A

Overview of Student Helper:

Student Helper is an app that consists of 5 parts: an Agenda, schedule, GPA calculator, Gradebook, and a group communication tool.
- The agenda allows students to store assignments on their phone so they can reference it later and finish the assignmment before it is due.
- The Schedule allows students to enter classes and their class times, so they can reference it later and make sure they go to each class.
- The GPA calculator allows students to calculate their current GPA by adding classes or adding semester GPAs.
- The Gradebook allows students to keep track of their current grades.
- The Group tool allows students to communicate with other students on homework, or group projects.

# Iteration 2 Difficulties

Agenda difficulties
 - I, Paul McDonald, think the hardest part I had to finish in the second iteration was making each assignment that was dynamically created, clickable, and editable.
 - The second hardest thing I had to do was make sure that the agenda was updated to reflect what the database stores after an edit or removal

GPA difficulties
- I, William Crump, think the hardest part was getting the correct logic for reverting all changes made to a semester with courses attached because of how much database interaction was involved all within the same activity. 
- The second hardest thing was making sure that the listviews with semesters or courses was updated dynamically when it was edited or deleted. 

Gradebook
 - I, Thomas Seo, finished all the user stories that was originally planned.  After the first iteration, app-building and development became easier.  Taking what we learned in class, I tried to be more mindful about commenting, refactoring, and organization/structure.  All that helped and made this iteration all the easier.  There's still a large room to improve in terms of code structure.  The groundwork laid here will help implement a future user story that I hope to accomplish.
 - The difficulty of this iteration wasn't so much in the app-development but rather the logic.  I had to make sure my one of my feature - Extra Credit - worked in a certain behavior.  I did not want EC to be added if there were no other grades (i.e. by itself).  Throw in certain edit/delete and navigation behvaiors, EC became quite a challenge.
 - I did a little bit of app aesthetics.  Even though I did a little bit of it, I could already feel it being a whole new level of difficulty.  I think the key difference here versus coding, XML, or Android is that it is much more visible to see your errors.  You can tell if something is off by x-number of pixels or a different color, and so, it becomes easier and faster to learn and pinpoint your errors.  I look forward to making my app more eye-pleasing.

# Iteration 1 Difficulties

Agenda difficulties
 - I, Paul McDonald, Think the hardest part I had to finish in this iteration was coming up with Espresso tests that were consistent.  I kept having tests that would pass sometimes and fail othertimes.
 - The second hardest thing I had to do was implementing the sqlite database functionality. It took a lot of work to make my assignments store to a database and load from their to the agenda to be displayed.

GPA difficulties
 - I, William Crump, had the most trouble espresso testing listviews with onData(), as well as just getting espresso running, overall it was the most frustrating. 
 - The second hardest was also implementing sqlite functionality mainly because my implementaion required two tables where one relied on the other so making the cursor sort data properly between the semester table and courses table was fairly difficult. 
 
Schedule difficulties
 - I, Matthew McGee, enjoyed working on this application. I think my summer internship gave me a headstart in some areas of this app (implementing databases, version control) and I tried to help out my team to the best of my ability in these areas. Implementing the calendar was rather fun, as I prefer working on the front end of applications, where it's much easier to see your work progressing. 
 - The most diffucult part of this iteration for me was the testing. I've learned java and picked up the rest of android rather quickly, but the majority of my time was spent on just trying to implement tests correctly. UI testing in Espresso is easy if you are using standard buttons and textfields, but once you get into testing dynamic objects it gets a little tricky(at least to me it did). 
 - The one thing I believe I failed on is testing the schedule view better. I couldn't figure out how to get the onData() method to work in Espresso in order to grab the calendar or objects on it. I instead had to be satisfied with testing the different views.

Gradebook
 - I, Thomas Seo, did not finish the "Future Grade" user story.  The app-building took a lot time.  Looking back, the basic foundation wasn't that difficult, but since I've never done app development, it took time to learn the basics.  Keeping that in mind, I realize adding a feature that predicts grades needed will be a big endeavor.
 - Everything about building the app was difficult.  From the get-go, learning how to combine XML, SQLite, and Android programming into a functioning app is incredibly difficult.  On there own, it was easy to pick up XML, SQLite, and Android, but when you have to integrate and make it all work together, that is a challenge.
 - Espresso testing was more frustrating than difficult like combining different aspects.  Getting help from my group members and surprisingly finding working solutions, Espresso became do-able.

Groups
- I, Will Steiner, Had the most difficuly figuring out how to use HTTP requests. The examples in Android's documentation expressed the main idea, but only provide overview details on the prerequisites like to how to get Volley running.
- The android environment has a steep learning curve. Looking back, displaying views and fragments seems simple, but the tasks were frustrating at first. 
- Espresso provided it's own basket of problems. I struggled with getting to the correct fragemnt and editing the respecitve views. With help from my team I was pointed in the right direction and was able to get all of my tests runnning.

# Help Resources
If there was any resources that you think could help the team out, you can post it down below here.

To set-up your cellphone to run your app: http://developer.android.com/tools/device.html  .  It's very easy and takes a few minutes to do.  I highly recommend it, because it's faster to start up,
helps you with XML formatting, and gives you an idea of how users would interact.  Additionally, you can run Espresso test.

To make sure only positive decimal numbers can be inputted: android:inputType="number|numberDecimal"  .  Placing that attribute ensures only a certain type of input is allowed.  I showed Ricky how it looked in real-time.
Very helpful if you want this particular input.


If you use ListView for Espresso testing, here's a general guideline to help you.
```
// You call this method in your Espresso.  Notice it passes a GBSyllabus object as a parameter.
public static Matcher<Object> withSyllabusContent(GBSyllabusUnit GBSyllabusUnit) {
    // This return statement will call the private customer matcher, 
    // which passes equalTo method on the the parameter GBSyllabus object
    return withSyllabusContent(equalTo(GBSyllabusUnit)); 
}

// This is our private custom matcher.  Again, notice our parameter GBSyllabus object,
// which now has a Matcher datatype (thanks to the equalTo).  
// Our parameter Matcher is what we use to check against other GBSyllabus objects
private static Matcher<Object> withSyllabusContent(final Matcher<GBSyllabusUnit> syllabus) {
    // This return statement will call an abstract BonundedMatcher.  
    // We parameterized BoundedMatcher: Object comes from the private Matcher and 
    // indicates we want to look for an object (can work for other things like View),
    // and GBSyllabusUnit is the type of object.  This is further emphasized 
    // when the BoundedMatcher constructor passes the expected type (i.e. GBSyllabus.class).
    return new BoundedMatcher<Object, GBSyllabusUnit>(GBSyllabusUnit.class) {

        // Part of the implementation.  You can use any description.
        @Override
        public void describeTo(Description description) {
            description.appendText("Returned");
        }

        // This is where the actual matching occurs.
        // If our "item" matches the Matcher, we turn true.
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
```
```
// I overrode the equals method in my GBSyllabusUnit class.
// This is required if you are comparing objects.
// Notice I only care about the name of object.
public boolean equals(Object obj) {
    return obj instanceof GBSyllabusUnit && ((this.name.equals(((GBSyllabusUnit) obj).name)));
}
```
```
// This snippet is from my Espresso test.
// I created a new GBSyllabusUnit object and set its name.
GBSyllabusUnit GBSyllabusUnit = new GBSyllabusUnit();
GBSyllabusUnit.setName("Projects");

// I only care about objects that are instances of GBSyllabusUnit.
// My matcher now only returns true if the given instance is not only a GBSyllabusUnit,
// but an instance that matches the name of my object (the one I made above).
// Then, it will perform a long-click.
onData(allOf(instanceOf(GBSyllabusUnit.class), withSyllabusContent(GBSyllabusUnit))).perform(longClick());
```
