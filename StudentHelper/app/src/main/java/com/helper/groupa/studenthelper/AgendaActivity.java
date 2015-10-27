package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AgendaActivity extends Activity {
   // private static final String LOG_TAG = "debugger";
    private static final int REQUEST_CODE_ADD = 0;
    private static final int REQUEST_CODE_EDIT = 1;
    private static final int REQUEST_CODE_PAST = 2;


    DBHelper db = new DBHelper((this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //loads all previous entries in the agenda
        if (db.getAllAssignments().getCount() != 0) {
            loadAssignments();

            Context context = getApplicationContext();
            CharSequence text = "Loaded the previous assignments";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        //db.deleteAllData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                openAddPage();
                return true;
            case R.id.action_past_folder:
                openPastPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //  return true;
        //}

        //return super.onOptionsItemSelected(item);
    }

    // handles data from the add assignment page and then adds it to the list of assignments on the page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(LOG_TAG, "Passed strings back from add page");

        // executes code below if an Assignment is added
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD) {
            if (data != null) {
                String classStr = data.getStringExtra("classStr");
                String assignmentStr = data.getStringExtra("assignmentStr");
                String dueDateStr = data.getStringExtra("dueDateStr");
                // inserts the assignment in the database in the agenda table
                db.insertAssignment(classStr, assignmentStr, dueDateStr);
                Cursor cursor = db.getAllAssignments();
                cursor.moveToLast();
                publishAssignment(classStr, assignmentStr, dueDateStr, cursor.getInt(0));
                Context context = getApplicationContext();
                CharSequence text = "Success";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
            // Executes code below if an assignment was edited
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            if (data != null) {

                //refresh layout to show changes
                LinearLayout layout = (LinearLayout) findViewById(R.id.agenda_layout);
                layout.removeAllViews();
                loadAssignments();

                Context context = getApplicationContext();
                CharSequence text = "Successful Edit";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
        }
        // Executes code below if an assignment was deleted
        else if (resultCode == 86 && requestCode == REQUEST_CODE_EDIT) {
            if (data != null) {

                //refresh layout to show changes
                LinearLayout layout = (LinearLayout) findViewById(R.id.agenda_layout);
                layout.removeAllViews();
                loadAssignments();

                Context context = getApplicationContext();
                CharSequence text = "Assignment has been removed from agenda and added to Past Assignment folder";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        // executes code below if an assignment was reassigned from past assignments folder
        else if (resultCode == 77 && requestCode == REQUEST_CODE_PAST) {


                //refresh layout to show changes
                LinearLayout layout = (LinearLayout) findViewById(R.id.agenda_layout);
                layout.removeAllViews();
                loadAssignments();

                Context context = getApplicationContext();
                CharSequence text = "Assignment has been added back to the agenda";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

        }
        // executes code below if an assignment was hard deleted in the past assignments folder
        else if (resultCode == 78 && requestCode == REQUEST_CODE_PAST) {


                //refresh layout to show changes
                LinearLayout layout = (LinearLayout) findViewById(R.id.agenda_layout);
                layout.removeAllViews();
                loadAssignments();

                Context context = getApplicationContext();
                CharSequence text = "All data pertaining to that assignment has been deleted";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

        }
    }

    // starts the add page activity
    public void openAddPage() {
        Intent intent = new Intent(this, addPageActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    // starts Past Assignments Page activity
    public void openPastPage() {
        Intent intent = new Intent(this, pastAssignmentsAgendaActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PAST);
    }

    // starts edit page activity
    public void openEditPage(int assignId) {

        Intent intent = new Intent(this, editAgendaActivity.class);
        intent.putExtra("assignmentId", assignId);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    //takes input in the form of strings for a class, assignment, and a due date, and creates a textview on the page with that info to display the assignment
    public void publishAssignment(String classStr, String assignmentStr, String dueDateStr, final int id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.agenda_layout);
        TextView assignView = new TextView(this);
        assignView.setText(Html.fromHtml("<b> <u> " + classStr + " </b> </u> <br /> " + assignmentStr + " <br /> Due: " + dueDateStr));
        assignView.setId(id);


        // makes each Assignment added to the agenda page clickable, and if clicked, goes to an edit page for that assignment
        assignView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openEditPage(id);

            }
        });
        layout.addView(assignView);

    }

    //loads all the previous assignments from the database and publishes them to the page so they are viewable
    public void loadAssignments() {
        Cursor cursor = db.getAllAssignments();
        cursor.moveToFirst();
        int rowCount = cursor.getCount();
        int count = 0;



        while (count < rowCount) {

            int id = cursor.getInt(0);
            String classStr = cursor.getString(1);
            String assignmentStr = cursor.getString(2);
            String dueDateStr = cursor.getString(3);
            if (cursor.getInt(4) == 1) {
                publishAssignment(classStr, assignmentStr, dueDateStr, id);
            }
            if (count != (rowCount - 1)) {
                cursor.moveToNext();
            }
            count++;


        }


    }

}
