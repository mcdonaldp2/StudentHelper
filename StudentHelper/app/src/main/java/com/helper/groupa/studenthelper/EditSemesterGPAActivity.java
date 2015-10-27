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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditSemesterGPAActivity extends Activity {
    public static final String TAG = "EditSemesterGPAActivity";
    private DBHelper db;
    Cursor cancelChangesCursor;
    SimpleCursorAdapter cursorAdapter;

    EditText semesterTitleEditText;
    EditText semesterGPAEditText;
    TextView semesterGPATextView;
    Button addCourseButton;
    Button updateOrRecalcButton;
    private ListView coursesListView;

    String semesterTitle;
    String gpaString;
    String setGPA;
    int semester_id;
    int cursorAdapterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semester_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //initiates database
        db = new DBHelper(this);

        //retrieves bundle from GPAAcitivty
        //with necessary data to fill UI fields
        Bundle getBundle = getIntent().getExtras();
        semesterTitle = getBundle.getString("semesterTitle");
        gpaString = getBundle.getString("semesterGPA");
        setGPA = getBundle.getString("setGPA");
        semester_id = Integer.parseInt(getBundle.getString("semesterID"));
        Log.d("EditSemesterGPAActivity", "semester id = " + semester_id);

        //Displays semester title in TextEdit
        semesterTitleEditText = (EditText) findViewById(R.id.editSemesterTitleEditText);
        semesterTitleEditText.setText(semesterTitle, TextView.BufferType.EDITABLE);

        //determines if user set the gpa themselves without attaching courses
        //UI depends on this
        if ("true".equals(setGPA)) {//creates UI without Courses showing
            semesterGPAEditText = (EditText) findViewById(R.id.editSemesterGPAEditText);
            semesterGPAEditText.setVisibility(View.VISIBLE);
            semesterGPAEditText.setText(gpaString, TextView.BufferType.EDITABLE);

            addCourseButton = (Button)findViewById(R.id.editAddCourseButton);
            addCourseButton.setVisibility(View.GONE);

            //creates the update button and makes it visible
            updateOrRecalcButton = (Button) findViewById(R.id.updateButton);
            updateOrRecalcButton.setVisibility(View.VISIBLE);
            updateOrRecalcButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        //retrieves title and gpa from edit texts
                        String semesterTitle = semesterTitleEditText.getText().toString();
                        Double gpaDouble = Double.valueOf(semesterGPAEditText.getText().toString());

                        //updates semester, if successful displays toast
                        if (db.updateSemester(semester_id, semesterTitle, setGPA, gpaDouble)) {
                            Toast.makeText(EditSemesterGPAActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                        }

                        //starts the GPAActivity
                        Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                        startActivity(intent);

                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Input a decimal value!", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        } else {//creates UI with attached courses showing


            semesterGPATextView = (TextView) findViewById(R.id.editSemesterGPATextView);
            semesterGPATextView.setVisibility(View.VISIBLE);
            semesterGPATextView.setText(gpaString);

            //creates recalculate button and makes it visible
            updateOrRecalcButton = (Button) findViewById(R.id.recalculateButton);
            updateOrRecalcButton.setVisibility(View.VISIBLE);
            updateOrRecalcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String semesterTitle = semesterTitleEditText.getText().toString();
                    Double gpaDouble = Double.valueOf(semesterGPATextView.getText().toString());

                    //updates semester, if successful displays toast
                    if (db.updateSemester(semester_id, semesterTitle, setGPA, gpaDouble)) {
                        Toast.makeText(EditSemesterGPAActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                    startActivity(intent);
                }
            });

            addCourseButton = (Button)findViewById(R.id.editAddCourseButton);
            addCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), addCourseGPAActivity.class);

                    Bundle extra = new Bundle();
                    extra.putString("fromActivity", TAG);
                    extra.putString("StringId", semesterTitle);
                    extra.putInt("IntId", semester_id);
                    intent.putExtras(extra);

                    startActivityForResult(intent, 2);
                }
            });

            //If the user makes changes but wants to cancel the changes made
            //this cursor houses the original state pre editing
            cancelChangesCursor = db.getAllSemesterCourses(semester_id);

            final Cursor cursor = db.getAllSemesterCourses(semester_id);
            String[] columns = new String[]{
                    DBHelper.COLUMN_COURSE_ID,
                    DBHelper.COLUMN_COURSE_TITLE,
                    DBHelper.COLUMN_COURSE_CREDIT_HOURS,
                    DBHelper.COLUMN_COURSE_GRADE
            };

            int[] widgets = new int[]{
                    R.id.courseID,
                    R.id.courseName,
                    R.id.courseCredits,
                    R.id.courseGrade
            };

            cursorAdapter = new SimpleCursorAdapter(this, R.layout.course_info,
                    cursor, columns, widgets, 0);
            cursorAdapterCount = cursorAdapter.getCount();
            coursesListView = (ListView) findViewById(R.id.editSemesterCoursesListView);
            coursesListView.setAdapter(cursorAdapter);
            coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Initiates widgets from course_info
                    final TextView courseIDTextView = (TextView) view.findViewById(R.id.courseID);
                    final TextView courseTitleTextView = (TextView) view.findViewById(R.id.courseName);
                    final TextView courseCreditsTextView = (TextView) view.findViewById(R.id.courseCredits);
                    final TextView courseGradeTextView = (TextView) view.findViewById(R.id.courseGrade);

                    AlertDialog alertbox = new AlertDialog.Builder(EditSemesterGPAActivity.this)
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
                                    extra.putInt("semesterID", semester_id);
                                    intent.putExtras(extra);

                                    Log.d(TAG, "semesterID = " + semester_id + "\n" +
                                            "courseID = " + courseID);

                                    startActivityForResult(intent, 2);
                                    Log.d(TAG, "Ran startActivityForResult");

                                }
                            })

                                    //deletes the semester and connected courses
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                //take delete method form courseSemesterGPAActivity
                                //and add it to DBHelper to use here


                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String courseTitle = courseTitleTextView.getText().toString();
                                    db.deleteCourseString(courseTitle);
                                    Cursor postDeleteCursor = db.getAllSemesterCourses(semester_id);
                                    cursorAdapter.changeCursor(postDeleteCursor);
                                    if(postDeleteCursor.getCount() > 0) {
                                        updateGPA(semester_id);
                                    } else {
                                        semesterGPATextView.setText("" + 0.0);
                                    }


                                }
                            })
                            .show();


                }
            });

        }

    }

    //same method from courseSemesterGPAActivity
    //calculates average GPA using a cursor
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

    private void reverseAllEditedCourses(Cursor cursor, Cursor itemsToDelete, int semesterID) {
        if (itemsToDelete.getCount() > 0) {
            do {
                String courseTitle = itemsToDelete.getString(itemsToDelete.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_TITLE));
                db.deleteCourseString(courseTitle);
            }while(itemsToDelete.moveToNext());
            itemsToDelete.close();
        }

        do {
            int courseID = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_ID)));
            String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_TITLE));
            int courseCredits = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_CREDIT_HOURS)));
            String courseGrade = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COURSE_GRADE));

            double gradeDouble = addCourseGPAActivity.convertGrade(courseGrade);
            double qualityPoints = gradeDouble * courseCredits;
            db.insertCourse(courseTitle, courseCredits, courseGrade, gradeDouble, qualityPoints, semesterID);


        }while(cursor.moveToNext());
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //updates the coursesListView
        if (requestCode == 2) {

            //Dynamically recalculates gpa and displays it
            updateGPA(semester_id);
//            Cursor cursorGPA = db.getSemesterCoursesDoubles(semester_id);
//            double newGPA = calculateSemesterGPA(cursorGPA);
//            semesterGPATextView.setText("" + newGPA);

            //Refreshes the courseListView with new data
            Cursor cursor = db.getAllSemesterCourses(semester_id);
            cursorAdapter.changeCursor(cursor);
        }
    }

    private void updateGPA(int semester_id) {
        int semesterIDHolder = semester_id;
        Cursor cursorGPA = db.getSemesterCoursesDoubles(semesterIDHolder);
        double newGPA = calculateSemesterGPA(cursorGPA);
        semesterGPATextView.setText("" + newGPA);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home && cursorAdapterCount > 0) {
            backDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backDialog() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("You will lose all changes unless apply is clicked. Do you wish to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {


                        reverseAllEditedCourses(cancelChangesCursor, cursorAdapter.getCursor(), semester_id);
                        Log.d(TAG, "all courses reverted!");
                        Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
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

}