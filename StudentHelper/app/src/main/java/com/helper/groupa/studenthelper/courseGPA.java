package com.helper.groupa.studenthelper;

/**
 * Created by William on 9/17/2015.
 */
public class courseGPA {
    private long id;
    private String semesterTitle;
    private String title;
    private String letterGrade;
    private int creditHours;

    private String courseOverview = title + "\nGrade: "
            + letterGrade
            + "\nCredits: "
            + creditHours;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;

    }

    public String getSemesterTitle() {
        return semesterTitle;
    }

    public void setSemesterTitle(String semesterTitle) {
        this.semesterTitle = semesterTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        return courseOverview;
    }
}
