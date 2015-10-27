package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class editAgendaActivity extends Activity {
    DBHelper db = new DBHelper(this);
    int assignId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agenda);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // gets Intent and Id of assignment clicked in Agenda
        Intent intent = getIntent();
        assignId = intent.getIntExtra("assignmentId", 0);
        determineAssignment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAssignment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // using the id number of the assignment passed in the intent from the agenda page,
    // gathers assignment using that id number and fills the fields of the form with that data
    public void determineAssignment() {

        Cursor cursor = db.getAssignment(assignId);
        String classStr = cursor.getString(1);
        String assignStr = cursor.getString(2);
        String dueDateStr = cursor.getString(3);


        EditText classEdit = (EditText) findViewById(R.id.edit_class_editText);
        classEdit.setText(classStr);

        EditText assignEdit = (EditText) findViewById(R.id.edit_assignment_editText);
        assignEdit.setText(assignStr);

        int[] date = parseDate(dueDateStr);
        DatePicker picker = (DatePicker) findViewById(R.id.agenda_edit_datePicker);

        //year, month, day of month
        picker.updateDate(date[2], date[0]-1, date[1]);

    }

    //parses date string from database to an integer array
    public int[] parseDate(String dateStr) {

        int[] dateArray = {0, 0, 0};

        String[] dateStrArray = dateStr.split("/");
        Log.d("debugger", dateStrArray[1]);
        //month; they use 0-11 for the 12 months which is extremely dumb
        dateArray[0] = Integer.parseInt(dateStrArray[0]);

        //day of month
        dateArray[1] = Integer.parseInt(dateStrArray[1]);

        //year
        dateArray[2] = Integer.parseInt(dateStrArray[2]);

        return dateArray;
    }

    // onClick handler for  finish edit button
    public void finishEditAssignment(View v) {
        Intent data = new Intent();

        boolean dueBool = false;
        // get the class name
        EditText className = (EditText) findViewById(R.id.edit_class_editText);
        String classStr = className.getText().toString();
        // get the assignment
        EditText assignment = (EditText) findViewById(R.id.edit_assignment_editText);
        String assignmentStr = assignment.getText().toString();
        // get the date from the datepicker and create a string of that date
        int[] userDate = getDateTimeFromPickers();
        String dueDateStr = userDate[0]+1 + "/" + userDate[1] + "/" + userDate[2];
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        // reuse the calendar to set user specified date
        c.set(Calendar.YEAR, userDate[2]);
        c.set(Calendar.MONTH, userDate[0]);
        c.set(Calendar.DAY_OF_MONTH, userDate[1]);

        // get as a Date
        Date dateSpecified = c.getTime();
        //compare the user specified date to the current date to make sure it's not before the current
        //date
        if (dateSpecified.before(today)) {
            dueBool = true;
        }


        boolean classBool = "".equals(classStr);

        boolean assignBool = "".equals(assignmentStr);

        if (classBool || assignBool || dueBool) {

            if (classBool) {
                Context context = getApplicationContext();
                CharSequence text = "No Class, try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            if (assignBool) {
                Context context = getApplicationContext();
                CharSequence text = "No assignment, try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            if (dueBool) {
                Context context = getApplicationContext();
                CharSequence text = "That date is before the current date, try again";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        // if all fields have valid inputs, pass the input back to the agenda page to be published to the page
        } else {

            db.updateAssignment(assignId, classStr, assignmentStr, dueDateStr, 1);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    // gets the user entered date from the DatePicker
    public int[] getDateTimeFromPickers() {
        DatePicker dp = (DatePicker) findViewById(R.id.agenda_edit_datePicker);

        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        int year = dp.getYear();

        return new int[]{month, day, year};
    }

    // handles the click on the delete button// trash icon in nav bar
    // prompts user to make sure they are sure of the delete before it goes through
    public void deleteAssignment() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked

                        //deletes assignment and finishes the activity and goes back to agenda page
                        //db.deleteAssignmentById(assignId);
                        Cursor cursor = db.getAssignment(assignId);

                        db.updateAssignment(assignId, cursor.getString(1), cursor.getString(2), cursor.getString(3), 0);
                        Intent intent = new Intent();
                        setResult(86, intent);
                        finish();
                        break;
                }
            }
        };

        //sets message of Alert dialog
        //sets title of negative and positive buttons
        //displays Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove the assignment from the agenda?");
        builder.setNegativeButton(R.string.delete_dialog_yes, dialogClickListener);
        builder.setPositiveButton(R.string.delete_dialog_no, dialogClickListener);

        builder.show();


    }
}

