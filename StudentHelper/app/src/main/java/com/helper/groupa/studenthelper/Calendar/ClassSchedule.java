package com.helper.groupa.studenthelper.Calendar;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Matt on 9/17/2015.
 */
public class ClassSchedule {
    private int id;
    private String name;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String[] days;
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();

    public ClassSchedule() {

    }

    public Calendar getStartTime() {
        return startTime;
    }

    public ClassSchedule(int id, String name, int startHour, int startMinute, int endHour, int endMinute, String days){
        this.id = id;
        this.name = name;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.days = days.split(",");

        this.startTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.startTime.set(Calendar.MINUTE, startMinute);
        this.endTime.set(Calendar.HOUR_OF_DAY, endHour);

        this.endTime.set(Calendar.MINUTE, endMinute);
    }

    public ClassSchedule(String name, int startHour, int startMinute, int endHour, int endMinute, String days) {
        this.name = name;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.days = days.split(",");

        this.startTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.startTime.set(Calendar.MINUTE, startMinute);
        this.endTime.set(Calendar.HOUR_OF_DAY, endHour);

        this.endTime.set(Calendar.MINUTE, endMinute);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    private int convertDayToInt(String day){
        switch (day){
            case "monday":
                return 1;
            case "tuesday":
                return 2;
            case "wednesday":
                return 3;
            case "thursday":
                return 4;
            case "friday":
                return 5;
            default:
                return -1;
        }
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public boolean hasConflict(ClassSchedule classB) {
        ClassSchedule classA = this;
        boolean sameDays = false;

        for(String dayA:classA.days){
            for(String dayB:classB.days){
                if(dayA.equals(dayB)){
                    sameDays = true;
                }
            }
        }
        //if the two have no days in common then they can't be in conflict
        if(sameDays == false){
            return false;
        }else if(classA.startTime.compareTo(classB.startTime) > 0 && classA.startTime.compareTo(classB.endTime) < 0){
            return true;
        }else if(classA.endTime.compareTo(classB.startTime) > 0 && classA.endTime.compareTo(classB.endTime) < 0){
            return true;
        }else if(classA.startTime.compareTo(classB.startTime) < 0 && classA.endTime.compareTo(classB.endTime) > 0 ){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                ", days=" + Arrays.toString(days) +
                '}';
    }

    public String getDaysString(){
        String text = "";

        if(days.length > 0){
            for(String day:this.days){
                text += day + ", ";
            }
            String newText = text.substring(0, text.length() - 2);
            return newText;
        }else{
            return null;
        }
    }
}
