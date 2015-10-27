package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class addCourseGPAActivity extends Activity {
    public final static String EXTRA_KEY_SENT_BACK = "EXTRA_KEY_SENT_BACK";
    String TAG= "addCourseGPAACtivityLog";
    private DBHelper db;
    EditText classTitleEditText;
    TextView gradeTextView, creditHoursTextView;
    Spinner gradeSpinnerView, creditHoursSpinnerView;
    Button saveCourseButton;

    String course_title;
    String grade;
    String creditHours;

    double qualityPoints;
    double gradeDouble;

    int creditHoursInt;
    int semester_id;

    String fromActivity;
    String semesterTitle;
    int semesterID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle getBundle = getIntent().getExtras();

        fromActivity = getBundle.getString("fromActivity");
        semesterTitle = getBundle.getString("StringId");
        semesterID = getBundle.getInt("IntId");
        Log.d(TAG, "extra id = " + semesterID);
        Log.d(TAG, "fromActivity: " + fromActivity);

        classTitleEditText = (EditText)findViewById(R.id.courseTitleEditText);
        gradeTextView = (TextView)findViewById(R.id.gradeTextView);
        creditHoursTextView = (TextView)findViewById(R.id.creditHoursTextView);
        gradeSpinnerView = (Spinner)findViewById(R.id.gradeSpinnerView);
        creditHoursSpinnerView = (Spinner)findViewById(R.id.creditHoursSpinner);
        saveCourseButton = (Button)findViewById(R.id.courseSaveButton);

        db = new DBHelper(this);


        saveCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_title = classTitleEditText.getText().toString();
                grade = gradeSpinnerView.getSelectedItem().toString();
                creditHours = creditHoursSpinnerView.getSelectedItem().toString();

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
                    creditHoursInt = Integer.parseInt(creditHours);
                    gradeDouble = convertGrade(grade);
                    qualityPoints = creditHoursInt * gradeDouble;
                    semester_id = semesterID;

                    Log.d(TAG, "gradeDouble = " + gradeDouble);
                    Log.d(TAG, "qaulityPoints = " + qualityPoints);

                    if (db.insertCourse(course_title, creditHoursInt, grade, gradeDouble, qualityPoints, semester_id))
                    {
                        Toast.makeText(getBaseContext(), "Course Saved!", Toast.LENGTH_LONG).show();
                    }

                    if ("courseSemesterGPAActivity".equals(fromActivity)) {
                        /*Intent intent = new Intent(getApplicationContext(), courseSemesterGPAActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle extra = new Bundle();
                        extra.putString("StringId", semesterTitle);
                        extra.putInt("IntId", semester_id);
                        intent.putExtras(extra);
                        startActivity(intent);*/

                        String sentBackTo = "Result sent back to: " + fromActivity;
                        Intent intent = new Intent();
                        intent.putExtra(TAG, sentBackTo);
                        setResult(3, intent);
                        finish();

                    } else if (EditSemesterGPAActivity.TAG.equals(fromActivity)) {
                        String sentBackTo = "Result sent back to: " + fromActivity;
                        Intent intent = new Intent();
                        intent.putExtra(TAG, sentBackTo);
                        setResult(2, intent);
                        finish();

                    }

                }



            }
        });


    }

    //changes the letter grade to its respective double
    //representation
    protected static double convertGrade(String grade) {
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

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if ("courseSemesterGPAActivity".equals(fromActivity)) {
                String sentBackTo = "Result sent back to: " + fromActivity;
                Intent intent = new Intent();
                intent.putExtra(TAG, sentBackTo);
                setResult(3, intent);
                finish();
            } else if ("EditSemesterGPAActivity".equals(fromActivity)) {
                String sentBackTo = "Result sent back to: " + fromActivity;
                Intent intent = new Intent();
                intent.putExtra(TAG, sentBackTo);
                setResult(2, intent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
