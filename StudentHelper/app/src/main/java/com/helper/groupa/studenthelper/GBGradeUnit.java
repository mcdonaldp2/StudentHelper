package com.helper.groupa.studenthelper;


import java.text.DecimalFormat;

public class GBGradeUnit {

    private long id;
    private String name;
    private double pointsEarned;
    private double pointsPossible;
    private int extraCredit;
    private long scheduleID;
    private GBSyllabusUnit GBSyllabusUnit;

    public GBGradeUnit() {
        super();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPointsEarned() {
        return pointsEarned;
    }

    public double getPointsPossible() {
        return pointsPossible;
    }

    public int getExtraCredit() {
        return extraCredit;
    }

    public long getScheduleID() {
        return scheduleID;
    }

    public GBSyllabusUnit getGBSyllabusUnit() {
        return GBSyllabusUnit;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPointsEarned(double pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public void setPointsPossible(double pointsPossible) {
        this.pointsPossible = pointsPossible;
    }

    public void setExtraCredit(int extraCredit) {
        this.extraCredit = extraCredit;
    }

    public void setScheduleID(long scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setGBSyllabusUnit(GBSyllabusUnit GBSyllabusUnit) {
        this.GBSyllabusUnit = GBSyllabusUnit;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00#");
        if (extraCredit == 1) {
            return name +
                    "\nExtra Credit: " + df.format(pointsEarned);
        }
        return name
                + "\nEarned: " + df.format(pointsEarned)
                + "\nPossible: " + df.format(pointsPossible)
                + "\nGrade: " + df.format((pointsEarned / pointsPossible) * 100);
    }

    public boolean equals(Object obj) {
        return obj instanceof GBGradeUnit && ((this.name.equals( ((GBGradeUnit) obj).name)));
    }
}
