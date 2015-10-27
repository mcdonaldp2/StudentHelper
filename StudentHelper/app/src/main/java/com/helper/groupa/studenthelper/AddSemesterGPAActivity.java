package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSemesterGPAActivity extends Activity {
    public final static String TAG = "AddSemesterGPAActivity";
    private DBHelper db;
    EditText semesterTitleEditText;
    Button saveSemesterButton;
    Button setGPABtn, chooseClassesBtn;
    String semester_title;



    int semesterID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //semesterID = getIntent().getIntExtra(GPAActivity.KEY_EXTRA_SEMESTER_ID, 0);
        //Log.d(TAG, "semesterID = " + semesterID);
        //String semester_title;
        semesterTitleEditText = (EditText) findViewById(R.id.semesterTitleTextEdit);

        db = new DBHelper(this);

        setGPABtn = (Button)findViewById(R.id.setGPAButton);
        setGPABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semester_title = semesterTitleEditText.getText().toString();

                if (db.CheckDataAlreadyInDB(DBHelper.TABLE_SEMESTERS, DBHelper.COLUMN_SEMESTER_TITLE, semester_title))
                {//checks for already existing semester
                    Toast.makeText(getApplication(), "This semester already exists", Toast.LENGTH_SHORT).show();
                }
                else if (semester_title.equals(""))
                {//checks for null semester
                    Toast.makeText(getApplicationContext(), "Enter a title.", Toast.LENGTH_LONG).show();
                } else
                {//saves the semester
                    Intent intent = new Intent(AddSemesterGPAActivity.this, setGPAActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("TitleId", semester_title);
                    extras.putString("setGPA", "true");
                    intent.putExtras(extras);
                    startActivity(intent);
                }


            }
        });

        //goes to the course/gpa calculation screen
        chooseClassesBtn = (Button)findViewById(R.id.chooseClassesButton);
        chooseClassesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semester_title = semesterTitleEditText.getText().toString();
                if (db.CheckDataAlreadyInDB(DBHelper.TABLE_SEMESTERS, DBHelper.COLUMN_SEMESTER_TITLE, semester_title))
                {//checks already existing semester
                    Toast.makeText(getApplication(), "This semester already exists", Toast.LENGTH_SHORT).show();
                }
                else if (semester_title.equals(""))
                {//checks null title
                    Toast.makeText(getApplicationContext(), "Enter a title.", Toast.LENGTH_LONG).show();
                }
                else
                {//saves the semester, and sets its setGPA column value to false
                    db.insertSemester(semester_title, "false", 0.0 );

                    //sends the created semesterId to the courses/gpa activity for
                    //courses to be added based on semester id
                    int id = db.findID(semester_title);
                    Log.d("setGPAorClasses", "int id found = " + id);
                    Intent intent = new Intent(AddSemesterGPAActivity.this, courseSemesterGPAActivity.class);
                    Bundle extras = new Bundle();

                    extras.putString("StringId", semester_title);
                    extras.putInt("IntId", id);
                    intent.putExtras(extras);

                    startActivity(intent);
                }

            }
        });




    }





}
