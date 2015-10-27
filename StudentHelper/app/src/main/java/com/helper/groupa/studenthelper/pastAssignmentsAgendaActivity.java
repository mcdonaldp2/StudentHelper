package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class pastAssignmentsAgendaActivity extends Activity {
    // private static final String LOG_TAG = "debugger";
    private static final int REQUEST_CODE_ADD = 0;
    private static final int REQUEST_CODE_EDIT = 1;


    DBHelper db = new DBHelper((this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_assignments_agenda);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //loads all previous entries in the agenda
        if (db.getAllAssignments().getCount() != 0) {

            loadAssignments();

            Context context = getApplicationContext();
            CharSequence text = "Click on the assignment you would like to re-assign/delete";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else {


            Context context = getApplicationContext();
            CharSequence text = "You have no past assignments";
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
        inflater.inflate(R.menu.menu_past_assignments_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    //takes input in the form of strings for a class, assignment, and a due date, and creates a textview on the page with that info to display the assignment
    public void publishAssignment(String classStr, String assignmentStr, String dueDateStr, final int id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.past_assignments_layout);
        TextView assignView = new TextView(this);
        assignView.setText(Html.fromHtml("<b> <u> " + classStr + " </b> </u> <br /> " + assignmentStr + " <br /> Due: " + dueDateStr));
        assignView.setId(id);


        // makes each Assignment added to the agenda page clickable, and if clicked, goes to an edit page for that assignment
        assignView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDeleteOrReassign(id);

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
            if (cursor.getInt(4) == 0) {
                publishAssignment(classStr, assignmentStr, dueDateStr, id);
            }
            if (count != (rowCount - 1)) {
                cursor.moveToNext();
            }
            count++;


        }


    }

    public void startDeleteOrReassign(final int id) {
        final Cursor c = db.getAssignment(id);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    // delete was clicked
                    case DialogInterface.BUTTON_POSITIVE:
                        db.deleteAssignmentById(id);
                        setResult(78);
                        finish();
                        break;

                    //reassign was clicked
                    case DialogInterface.BUTTON_NEGATIVE:
                        db.updateAssignment(id, c.getString(1), c.getString(2), c.getString(3), 1);
                        setResult(77);
                        finish();
                        break;
                    // cancel was clicked 
                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };

        //sets message of Alert dialog
        //sets title of negative and positive buttons
        //displays Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Choose to Either Delete or Reassign the assignment or cancel");
        builder.setNegativeButton(R.string.dialog_reassign, dialogClickListener);
        builder.setPositiveButton(R.string.dialog_delete, dialogClickListener);
        builder.setNeutralButton(R.string.cancel, dialogClickListener);

        builder.show();
    }
}
