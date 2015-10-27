package com.helper.groupa.studenthelper;

import java.text.DecimalFormat;

public class GBSyllabusUnit {

    private long id;
    private String name;
    private double weight;
    private double grade;
    private int extraCredit;
    private long scheduleID;
    private GBClassUnit classUnit;

    public GBSyllabusUnit() {
        super();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double getGrade() {
        return grade;
    }

    public int getExtraCredit() {
        return extraCredit;
    }

    public long getScheduleID() {
        return scheduleID;
    }

    public GBClassUnit getClassUnit() {
        return classUnit;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setExtraCredit(int extraCredit) {
        this.extraCredit = extraCredit;
    }

    public void setScheduleID(long scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setClassUnit(GBClassUnit classUnit) {
        this.classUnit = classUnit;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00#");
        if (extraCredit == 1) {
            return name +
                    "\nExtra Credit %: " + df.format(weight);
        }
        return name +
                "\nWeight %: " +  df.format(weight) +
                "\nGrade: " +  df.format(grade) +
                "\nWeighted Grade: " + df.format((weight * grade) / 100);
    }

    public boolean equals(Object obj) {
        return obj instanceof GBSyllabusUnit && ((this.name.equals(((GBSyllabusUnit) obj).name)));
    }
}
