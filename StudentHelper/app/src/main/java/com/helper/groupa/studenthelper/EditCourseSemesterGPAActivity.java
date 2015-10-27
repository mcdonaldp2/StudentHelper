package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditCourseSemesterGPAActivity extends Activity {
    public static final String TAG = "EditCourseSemesterGPA";

    private DBHelper db;
    EditText courseTitleEditText;
    Spinner gradeSpinner;
    Spinner creditHoursSpinner;
    Button applyCourseEditButton;

    String fromActivity;
    int courseID;
    String courseTitle;
    String courseCredits;
    String courseGrade;
    int semesterID;

    int resultNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_semester_gpa);
        Log.d(TAG, "Started activity");
        db = new DBHelper(this);

        Bundle getBundle = getIntent().getExtras();
        fromActivity = getBundle.getString("fromActivity");
        courseID = Integer.parseInt(getBundle.getString("courseID"));
        courseTitle = getBundle.getString("courseTitle");
        courseCredits = getBundle.getString("courseCredits");
        courseGrade = getBundle.getString("courseGrade");
        semesterID = getBundle.getInt("semesterID");

        if (EditSemesterGPAActivity.TAG.equals(fromActivity)) {
            resultNumber = 2;
        } else {
            resultNumber = 3;
        }

        Log.d(TAG, "made it through bundle and if statement");

        //Sets the course title
        courseTitleEditText = (EditText)findViewById(R.id.editCourseTitleEditText);
        courseTitleEditText.setText(courseTitle, TextView.BufferType.EDITABLE);

        //creates the grade Spinner at sets it to an adapter
        gradeSpinner = (Spinner)findViewById(R.id.editGradeSpinnerView);
        ArrayAdapter<CharSequence> gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.gradeSpinner, android.R.layout.simple_spinner_item);
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeSpinnerAdapter);

        //sets the gradeSpinner value
        gradeSpinner.setSelection(gradeSpinnerAdapter.getPosition(courseGrade));

        //creates the credits spinner and sets it to an adapter
        creditHoursSpinner = (Spinner)findViewById(R.id.editCreditHoursSpinner);
        ArrayAdapter<CharSequence> creditHoursSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.creditSpinner, android.R.layout.simple_spinner_item);
        creditHoursSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditHoursSpinner.setAdapter(creditHoursSpinnerAdapter);

        //sets the credits spinner value
        creditHoursSpinner.setSelection(creditHoursSpinnerAdapter.getPosition(courseCredits));

        applyCourseEditButton = (Button)findViewById(R.id.editCourseApplyButton);
        applyCourseEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_title = courseTitleEditText.getText().toString();
                String grade = gradeSpinner.getSelectedItem().toString();
                String creditHours = creditHoursSpinner.getSelectedItem().toString();

                if (course_title.equals(""))
                {//checks for null title
                    Toast.makeText(getApplicationContext(), "Create a title", Toast.LENGTH_LONG).show();
                }
                else if (grade.equals("Grade"))
                {//checks for no grade input
                    Toast.makeText(getApplication(), "Choose a grade!", Toast.LENGTH_LONG).show();
                }
                else if (creditHours.equals("Credits"))
                {//checks for no credit hours input
                    Toast.makeText(getApplicationContext(), "Choose a credit hour!", Toast.LENGTH_LONG).show();
                }
                else
                {//if all inputs are displayed properly then course is saved to the database
                    int creditHoursInt= Integer.parseInt(creditHours);
                    Double gradeDouble = convertGrade(grade);
                    Double qualityPoints = creditHoursInt * gradeDouble;
                    int semester_id = semesterID;

                    Log.d(TAG, "gradeDouble = " + gradeDouble);
                    Log.d(TAG, "qaulityPoints = " + qualityPoints);

                    if (db.updateCourse(courseID, course_title, creditHoursInt, grade, gradeDouble, qualityPoints, semester_id))
                    {
                        Toast.makeText(getBaseContext(), "Course updated!", Toast.LENGTH_SHORT).show();
                    }
                }


                String sentBackTo = "Result sent back to: " + fromActivity;
                Intent intent = new Intent();
                intent.putExtra(TAG, sentBackTo);
                setResult(resultNumber, intent);
                finish();

            }

        });
    }

    //same converter from addCourseGPAActivity
    private double convertGrade(String grade) {
        String gradeHolder = grade;
        double gradeDouble = 0;
        switch(gradeHolder) {
            case "A":
                gradeDouble = 4.0;
                break;
            case "A-":
                gradeDouble = 3.7;
                break;
            case "B+":
                gradeDouble = 3.3;
                break;
            case "B":
                gradeDouble = 3.0;
                break;
            case "B-":
                gradeDouble = 2.7;
                break;
            case "C+":
                gradeDouble = 2.3;
                break;
            case "C":
                gradeDouble = 2.0;
                break;
            case "C-":
                gradeDouble = 1.7;
                break;
            case "D+":
                gradeDouble = 1.3;
                break;
            case "D":
                gradeDouble = 1.0;
                break;
            case "D-":
                gradeDouble = 0.7;
                break;
            case "F":
                gradeDouble = 0.0;
                break;

        }
        return gradeDouble;
    }




}
