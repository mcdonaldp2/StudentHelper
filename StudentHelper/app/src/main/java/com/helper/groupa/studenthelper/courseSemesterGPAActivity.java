package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class courseSemesterGPAActivity extends Activity {
    String TAG = "courseSemesterGPAActivity Log";
    public final static String KEY_EXTRA_COURSE_ID = "KEY_EXTRA_COURSE_ID";
    TextView semesterTitle;
    Button calculateGPA; 
    private ListView listView;
    DBHelper db;

    Intent intent;
    CursorAdapter cursorAdapter;

    double semesterGPA;
    int semesterID;
    int cursorAdapterCount;
    String semesterTitleString;
    String setGPA = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_semester_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null) {

            semesterTitleString = getBundle.getString("StringId");
            semesterID = getBundle.getInt("IntId");
        }


        //semesterID = getIntent().getIntExtra(GPAActivity.KEY_EXTRA_SEMESTER_ID, 0);

        Log.d(TAG, "semesterID is " + semesterID);

        Button addCourseButton = (Button)findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(courseSemesterGPAActivity.this, addCourseGPAActivity.class);
                Bundle extras = new Bundle();
                extras.putString("fromActivity", "courseSemesterGPAActivity");
                extras.putString("StringId", semesterTitleString);
                extras.putInt("IntId", semesterID);
                //intent.putExtra(KEY_EXTRA_COURSE_ID, semesterID);
                intent.putExtras(extras);
                startActivityForResult(intent, 3);
            }
        });

        db = new DBHelper(this);
        //showGPA = (TextView)findViewById(R.id.semesterGPATextView);
        
        calculateGPA = (Button)findViewById(R.id.calculateGPAButton);
        calculateGPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Cursor cursorGPA = db.getSemesterCoursesDoubles(semesterID);
                Log.d(TAG, "cursor count = " + cursorGPA.getCount());
                if (cursorGPA != null && cursorGPA.getCount() > 0) {
                    semesterGPA = calculateSemesterGPA(cursorGPA);
                    db.updateGPA(semesterID, semesterGPA);
                    Toast.makeText(getApplicationContext(), "GPA Calculated and Saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Add Courses to Calculate!", Toast.LENGTH_LONG).show();
                }
                
            }
        });

        semesterTitle = (TextView)findViewById(R.id.semesterTitleTextView);
        semesterTitle.setText(semesterTitleString);
        //Log.d(TAG, "GPA = " + semesterGPA);
        //showGPA.setText("Semester GPA: " + semesterGPA);

        final Cursor cursor = db.getAllSemesterCourses(semesterID);
        String [] columns = new String[] {
                DBHelper.COLUMN_COURSE_ID,
                DBHelper.COLUMN_COURSE_TITLE,
                DBHelper.COLUMN_COURSE_CREDIT_HOURS,
                DBHelper.COLUMN_COURSE_GRADE
        };

        int[] widgets = new int[] {
                R.id.courseID,
                R.id.courseName,
                R.id.courseCredits,
                R.id.courseGrade
        };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.course_info,
                cursor, columns, widgets, 0);
        cursorAdapterCount = cursorAdapter.getCount();
        listView = (ListView)findViewById(R.id.courseListView);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Initiates widgets from course_info
                final TextView courseIDTextView = (TextView) view.findViewById(R.id.courseID);
                final TextView courseTitleTextView = (TextView) view.findViewById(R.id.courseName);
                final TextView courseCreditsTextView = (TextView) view.findViewById(R.id.courseCredits);
                final TextView courseGradeTextView = (TextView) view.findViewById(R.id.courseGrade);

                AlertDialog alertbox = new AlertDialog.Builder(courseSemesterGPAActivity.this)
                        .setMessage("What would you like to do?")

                                //starts the editing activity
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {


                                //retrieves information from widgets above
                                String courseID = courseIDTextView.getText().toString();
                                String courseTitle = courseTitleTextView.getText().toString();
                                String courseCredits = courseCreditsTextView.getText().toString();
                                String courseGrade = courseGradeTextView.getText().toString();

                                Intent intent = new Intent(getApplicationContext(), EditCourseSemesterGPAActivity.class);

                                //puts the strings from above into a bundle and some other values needed for updating courses
                                Bundle extra = new Bundle();
                                extra.putString("fromActivity", TAG);
                                extra.putString("courseID", courseID);
                                extra.putString("courseTitle", courseTitle);
                                extra.putString("courseCredits", courseCredits);
                                extra.putString("courseGrade", courseGrade);
                                extra.putInt("semesterID", semesterID);
                                intent.putExtras(extra);

                                Log.d(TAG, "semesterID = " + semesterID + "\n" +
                                        "courseID = " + courseID);

                                startActivityForResult(intent, 3);
                                Log.d(TAG, "Ran startActivityForResult");

                            }
                        })

                                //deletes the semester and connected courses
                        .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                            //take delete method form courseSemesterGPAActivity
                            //and add it to DBHelper to use here


                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                String courseTitle = courseTitleTextView.getText().toString();
                                db.deleteCourseString(courseTitle);
                                Cursor postDeleteCursor = db.getAllSemesterCourses(semesterID);
                                cursorAdapter.changeCursor(postDeleteCursor);

                                //updateGPA(semester_id);

                            }
                        })
                        .show();

            }
        });


    }

    public double calculateSemesterGPA(Cursor cursor) {
        double qualityPointSum = 0;
        double creditHoursSum = 0;
        double GPA = 0;
        do {
            double qualityPoint = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_QUALITY_POINTS));
            Log.d(TAG, "single quality point = " + qualityPoint);
            double creditHours = Double.parseDouble(cursor.getString(
                    cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_CREDIT_HOURS)));
            qualityPointSum = qualityPointSum + qualityPoint;
            creditHoursSum = creditHoursSum + creditHours;
        }while(cursor.moveToNext());

        cursor.close();
        Log.d(TAG, "qualitPoints = " + qualityPointSum + "\ncreditHours = " + creditHoursSum);
        GPA = qualityPointSum / creditHoursSum;
        GPA = (double)Math.round(GPA * 100d) / 100d;
        return GPA;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home && cursorAdapterCount > 0) {
            backDialog();
            return true;
        }
        db.deleteSemesterString(semesterTitleString);
        return super.onOptionsItemSelected(item);
    }

    //Dialog for clicking the menu back button
    protected void backDialog() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("You will lose all courses added to this semester, do you wish to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        db.deleteSemesterString(semesterTitleString);
                        //deleteConnectedCourses(semesterID);
                        Log.d("courseSemActivity", "Delete was ran!");
                        Intent intent = new Intent(getApplicationContext(), AddSemesterGPAActivity.class);
                        startActivity(intent);

                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //updates the coursesListView
        if (requestCode == 3) {


            Cursor cursor = db.getAllSemesterCourses(semesterID);
            cursorAdapterCount = cursor.getCount();
            cursorAdapter.changeCursor(cursor);
        }
    }


    public void deleteConnectedCourses(int semesterId) {

        Cursor tempCursor = db.getAllSemesterCourses(semesterId);
        if (tempCursor != null && tempCursor.getCount() > 0){
            do{
                String title = tempCursor.getString(tempCursor.getColumnIndex(DBHelper.COLUMN_COURSE_TITLE));
                db.deleteCourseString(title);
            }while(tempCursor.moveToNext());
        }
        tempCursor.close();
    }







}