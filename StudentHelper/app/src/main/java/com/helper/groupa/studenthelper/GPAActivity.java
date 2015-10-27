package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GPAActivity extends Activity {
    public final static String KEY_EXTRA_SEMESTER_ID = "KEY_EXTRA_SEMESTER_ID";
    Double averageGPA;
    TextView averageGPATextView;
    private ListView listView;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        Button button = (Button) findViewById(R.id.addSemesterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GPAActivity.this, AddSemesterGPAActivity.class);
                Bundle extra = new Bundle();
                extra.putString("StringId", "fromGPAActivity");
                extra.putInt("IntId", 0);
                intent.putExtras(extra);
                startActivity(intent);
            }
        });

        averageGPATextView = (TextView)findViewById(R.id.gpaDecimalTextView);

        db = new DBHelper(this);
        //db.deleteAllData();


        //Determines if there are semesters within
        //the displayed listView
        averageGPA = calculateTotalGPA();
        if (averageGPA > -1) {
            averageGPATextView.setText("" + averageGPA);
        } else {
            averageGPATextView.setText("N/A");
        }

        //cursor retrieves all the data from semester table
        final Cursor cursor = db.getAllSemesters();
        String [] columns = new String[] {
                "_id",
                DBHelper.COLUMN_SEMESTER_TITLE,
                DBHelper.COLUMN_SEMESTER_SET_GPA,
                DBHelper.COLUMN_SEMESTER_GPA
        };
        int[] widgets = new int[] {
                R.id.semesterID,
                R.id.semesterName,
                R.id.setGPA,
                R.id.GPA
        };

        //populates the listView with semesters and their GPA's
        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.semester_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.gpaListView);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView semesterTitle = (TextView) view.findViewById(R.id.semesterName);
                TextView semesterIdTextView = (TextView) view.findViewById(R.id.semesterID);
                TextView setGPATextView = (TextView) view.findViewById(R.id.setGPA);
                TextView semesterGPATextView = (TextView) view.findViewById(R.id.GPA);

                final String title = semesterTitle.getText().toString();
                final String semester_id =  semesterIdTextView.getText().toString();
                final String setGPA = setGPATextView.getText().toString();
                final String semesterGPA = semesterGPATextView.getText().toString();

                Log.d("GPAActivity", "semester title = " + title);
                final int pos = position;
                //editDeleteDialog(title);
                //final String title = semesterTitle;


                AlertDialog alertbox = new AlertDialog.Builder(GPAActivity.this)
                        .setMessage("What would you like to do?")

                                //starts the editing activity
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(getApplicationContext(), EditSemesterGPAActivity.class);

                                //info from clicked semester to be sent
                                Bundle extra = new Bundle();
                                extra.putString("semesterTitle", title);
                                extra.putString("semesterGPA", semesterGPA);
                                extra.putString("setGPA", setGPA);
                                extra.putString("semesterID", semester_id);
                                intent.putExtras(extra);

                                startActivity(intent);
                            }
                        })

                                //deletes the semester and connected courses
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            //take delete method form courseSemesterGPAActivity
                            //and add it to DBHelper to use here


                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {

                                db.deleteSemesterString(title);
                                Cursor cursor = db.getAllSemesters();
                                cursorAdapter.changeCursor(cursor);
                                averageGPA = calculateTotalGPA();

                                if (averageGPA > -1) {
                                    averageGPATextView.setText("" + averageGPA);
                                } else {
                                    averageGPATextView.setText("N/A");
                                }
                                /*Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                                startActivity(intent);*/
                            }
                        })
                        .show();

            }
        });


    }



    //DELETE THIS AT SOME POINT NOT IN USE
    //calculates the average gpa of the semesters within the listview
    public double calculateTotalGPA() {
        Cursor cursor2 = db.getAllSemesters();
        if (cursor2 != null && cursor2.getCount() > 0) {
            double sumGPA = 0;
            double postDivideGPA;
            int counter = 0;
            cursor2.moveToFirst();

            //calculates the total sum of the stored GPA's
            //and keeps track of how many rows of data there was for averaging
            do
            {
                double gpa = cursor2.getDouble(cursor2.getColumnIndexOrThrow(DBHelper.COLUMN_SEMESTER_GPA));
                Log.d("GPAActivity", "single gpa = " + gpa);

                sumGPA = sumGPA + gpa;
                counter++;
            }
            while(cursor2.moveToNext());

            postDivideGPA = sumGPA / counter;
            postDivideGPA = (double)Math.round(postDivideGPA * 100d) / 100d;
            cursor2.close();
            Log.d("GPAActivity", "Total GPA = " + postDivideGPA);
            return postDivideGPA;
        } //-1 is only returned when there are no semesters within the semester table
        else
        {
            return -1;
        }

    }


    //Edit and delete dialog for ListViewOnclick
    protected void editDeleteDialog(String semesterTitle) {

        final String title = semesterTitle;
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("What would you like to do?")

                //starts the editing activity
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                })

                //deletes the semester and connected courses
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    //take delete method form courseSemesterGPAActivity
                    //and add it to DBHelper to use here


                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        db.deleteSemesterString(title);
                        Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }







}
