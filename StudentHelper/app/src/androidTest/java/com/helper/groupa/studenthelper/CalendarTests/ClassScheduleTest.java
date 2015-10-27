package com.helper.groupa.studenthelper.CalendarTests;

import com.helper.groupa.studenthelper.Calendar.ClassSchedule;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Created by Matt on 9/24/2015.
 */
public class ClassScheduleTest {
    @Test
    public void testCompareTo(){
        ClassSchedule classA = new ClassSchedule(1, "Class A", 2, 1, 4, 1, "monday");
        ClassSchedule classB = new ClassSchedule(2, "Class B", 3, 1, 5, 1, "monday");
        ClassSchedule classC = new ClassSchedule(3, "Class C", 5, 1, 6, 1, "monday");
        ClassSchedule classD = new ClassSchedule(4, "Class D", 3, 1, 5, 1, "tuesday");

        assertTrue(classA.hasConflict(classB));
        assertFalse(classA.hasConflict(classC));
        assertFalse(classA.hasConflict(classD));
    }

}
