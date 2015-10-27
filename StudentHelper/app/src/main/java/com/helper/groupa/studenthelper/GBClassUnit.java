package com.helper.groupa.studenthelper;

import java.text.DecimalFormat;

/**
 * Creates a class with a name and grade
 */
public class GBClassUnit {

    // Field variables declared
    private long id;
    private String name;
    private double grade;

    /**
     * Default constructor
     */
    public GBClassUnit() {
        super();
    }

    /**
     * Get class ID
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Get class name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get class grade
     *
     * @return grade
     */
    public double getGrade() {
        return grade;
    }

    /**
     * Set class ID
     *
     * @param id new ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Set class name
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set class grade
     *
     * @param grade new grade
     */
    public void setGrade(double grade) {
        this.grade = grade;
    }

    /**
     * Format output
     *
     * @return output of class object
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00#");
        return "Class:   " + this.name +
                "\nGrade:  " + df.format(grade);
    }

    /**
     * Checks if class name is equal to passed argument
     *
     * @param obj an object
     * @return if object is a class-type and matches name
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof GBClassUnit &&
                ((this.name.equals(((GBClassUnit) obj).name)));
    }
}

