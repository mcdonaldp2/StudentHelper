package com.helper.groupa.studenthelper.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.R;

import java.util.ArrayList;

public class ScheduleDetails extends Activity {

    TextView name;
    TextView days;
    TextView startTime;
    TextView endTime;
    Button editButton;
    Button deleteButton;
    DBHelper db;
    DBHelper.Schedule dbHelper;
    ClassSchedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //grab ui objects
        name = (TextView) findViewById(R.id.detailsNameOutput);
        days = (TextView) findViewById(R.id.detailsDaysOutput);
        startTime = (TextView) findViewById(R.id.detailsStartTimeOutput);
        endTime = (TextView) findViewById(R.id.detailsEndTimeOutput);
        editButton = (Button) findViewById(R.id.updateStartTimeButton);
        deleteButton = (Button) findViewById(R.id.detailsDeleteButton);
        db = new DBHelper(this);
        dbHelper = db.new Schedule();

        //get id of schedule passed from previous activity
        String sId = "";
        try{
            sId = getIntent().getExtras().getString("scheduleId");
        }catch (Exception e){
            return;
        }
        int id = Integer.parseInt(sId);

        //get schedule passed
        schedule = getClassScheduleById(id);
        //set the fields to correct schedule details
        name.setText(schedule.getName());
        days.setText(schedule.getDaysString());
        startTime.setText(convertTime(schedule.getStartHour(), schedule.getStartMinute()));
        endTime.setText(convertTime(schedule.getEndHour(), schedule.getEndMinute()));

        //go to update screen
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ScheduleDetails.this, ScheduleUpdate.class);
                String id = String.valueOf(schedule.getId());
                intent.putExtra("scheduleId", id);
                startActivity(intent);
            }
        });

        //delete schedule
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) schedule.getId();
                                // continue with delete
                                dbHelper.deleteClassSchedule(id);
                                Intent intent;
                                intent = new Intent(ScheduleDetails.this, ScheduleMainActivity.class);
                                startActivity(intent);
                Toast.makeText(ScheduleDetails.this, "Class deleted from schedule", Toast.LENGTH_SHORT).show();

                //dialog had to be removed because I couldnt't push the buttons in espresso

//                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleDetails.this)
//                        .setTitle("Delete Class")
//                        .setMessage("Are you sure you want to delete this class?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                int id = (int) schedule.getId();
//                                // continue with delete
//                                dbHelper.deleteClassSchedule(id);
//                                Intent intent;
//                                intent = new Intent(ScheduleDetails.this, ScheduleMainActivity.class);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert);
//                AlertDialog dialog = builder.create();
//                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//                    @Override
//                    public void onShow(DialogInterface dialog) {
//                        something = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
//                    }
//                });
//
//                dialog.show();
            }
        });
    }

    /**
     * Get schedule from db by id
     * @param id Id of schedule
     * @return ClassSchedule
     */
    private ClassSchedule getClassScheduleById(int id){
        ArrayList<ClassSchedule> schedules = dbHelper.getAllClassSchedules();

        for(int i=0; i<schedules.size(); i++){
            if(id == schedules.get(i).getId()){
                return schedules.get(i);
            }
        }

        return null;
    }

    /**
     * Converts time to a more readable 12 hour format with am/pm
      * @param hour
     * @param minute
     * @return
     */
    private String convertTime(int hour, int minute){
        if(hour == 0){ //will display as 0 so change to 12
            hour = 12;
            if(minute < 10){
                return hour + ":0" + minute + " A.M.";
            }else{
                return hour + ":" + minute + " A.M.";
            }

        }else if(hour >= 12){
            hour = hour - 12;
            if(hour == 0){hour = 12;}

            if(minute < 10){
                return hour + ":0" + minute + " P.M.";
            }else{
                return hour + ":" + minute + " P.M.";
            }
        }else {

            if (minute < 10) {
                return hour + ":0" + minute + " A.M.";
            }else {
                return hour + ":" + minute + " A.M.";
            }
        }
    }
}
