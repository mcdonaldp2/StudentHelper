package com.helper.groupa.studenthelper;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

public class addPageActivity extends Activity {
    //private static final String LOG_TAG = "debugger";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //Log.d(LOG_TAG, "Started Add Page Activity");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // when the finish button is clicked, this method is ran using an onclick handler in the xml
    // First it gets the class and assignment from their respective EditText views
    // then it gets the date from the DatePicker
    // checks that the class and assignment inputs are not empty, and if they are, it will display
    // a toast telling you to not leave a certain field blank and to try again with valid input
    // checks that the date entered is not before the current date, and if it is, it will display a
    // toast saying that the date is in the past and to try again with a valid date
    // if all conditions are met for the input it passes the class, assignment, and date as strings
    // back to the agenda page through an intent
    public void finishAddAssignment(View view) {
        Intent intent = new Intent();

        boolean dueBool =false;
        // get the class name
        EditText className = (EditText) findViewById(R.id.class_editText);
        String classStr = className.getText().toString();
        // get the assignment
        EditText assignment = (EditText) findViewById(R.id.assignment_editText);
        String assignmentStr = assignment.getText().toString();
        // get the date from the datepicker and create a string of that date
        int[] userDate = getDateTimeFromPickers();
        String dueDateStr = userDate[0]+1 +"/"+userDate[1]+"/"+userDate[2];
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

            intent.putExtra("classStr",classStr);
            intent.putExtra("assignmentStr",assignmentStr);
            intent.putExtra("dueDateStr", dueDateStr);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    // gets the user entered date from the DatePicker
    public int[] getDateTimeFromPickers(){
        DatePicker dp = (DatePicker) findViewById(R.id.agenda_add_datePicker);

        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        int year = dp.getYear();

        int[] dateTime = {month,day,year};
        return dateTime;
    }
}
