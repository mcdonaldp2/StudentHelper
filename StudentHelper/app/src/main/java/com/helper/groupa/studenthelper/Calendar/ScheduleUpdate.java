package com.helper.groupa.studenthelper.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleUpdate extends Activity {

    private EditText addClassNameInput;
    private CheckBox mondayCheckBox;
    private CheckBox tuesdayCheckBox;
    private CheckBox wednesdayCheckBox;
    private CheckBox thursdayCheckBox;
    private CheckBox fridayCheckBox;
    private TimePicker scheduleTimePicker;
    private Button setClassStartButton;
    private Button setClassEndButton;
    private TextView classStartTextField;
    private TextView classEndTextField;
    private Button updateClassButton;
    private ClassSchedule schedule;

    private Map<String, Integer> classStartTime;
    private Map<String, Integer> classEndTime;

    private DBHelper.Schedule dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_update);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setVariables();

        String sId = getIntent().getExtras().getString("scheduleId");
        int id = Integer.parseInt(sId);
        schedule = getClassScheduleById(id);

        classStartTime = new HashMap<String, Integer>();
        classStartTime.put("hour", schedule.getStartHour());
        classStartTime.put("minute", schedule.getStartMinute());

        classEndTime = new HashMap<String, Integer>();
        classEndTime.put("hour", schedule.getEndHour());
        classEndTime.put("minute", schedule.getEndMinute());


        setCurrentTimeOnView();
        this.addClassNameInput.setText(schedule.getName());

        for(String day:schedule.getDays()){
            switch(day){
                case "monday":
                    mondayCheckBox.setChecked(true);
                    break;
                case "tuesday":
                    tuesdayCheckBox.setChecked(true);
                    break;
                case "wednesday":
                    wednesdayCheckBox.setChecked(true);
                    break;
                case "thursday":
                    thursdayCheckBox.setChecked(true);
                    break;
                case "friday":
                    fridayCheckBox.setChecked(true);
                    break;
            }
        }

        classStartTextField.setText(convertTime(schedule.getStartHour(), schedule.getStartMinute()));
        classEndTextField.setText(convertTime(schedule.getEndHour(), schedule.getEndMinute()));

        setClassStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = scheduleTimePicker.getCurrentHour();
                int minute = scheduleTimePicker.getCurrentMinute();

                classStartTime = new HashMap<String, Integer>();
                classStartTime.put("hour", hour);
                classStartTime.put("minute", minute);

                classStartTextField.setText(convertTime(hour, minute));
            }
        });

        setClassEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = scheduleTimePicker.getCurrentHour();
                int minute = scheduleTimePicker.getCurrentMinute();

                classEndTime = new HashMap<String, Integer>();
                classEndTime.put("hour", hour);
                classEndTime.put("minute", minute);

                classEndTextField.setText(convertTime(hour, minute));
            }
        });

        updateClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String className = String.valueOf(addClassNameInput.getText());
                    int startHour = classStartTime.get("hour");
                    int startMinute = classStartTime.get("minute");
                    int endHour = classEndTime.get("hour");
                    int endMinute = classEndTime.get("minute");
                    List<String> selectedDays = getSelectedDays();

                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, classStartTime.get("hour"));
                    startTime.set(Calendar.MINUTE, classStartTime.get("minute"));

                    //collect the days and put them in a format we can use
                    String stringDays = new String();
                    for (String day : selectedDays) {
                        stringDays += day + ",";
                    }

                    //remove the comma at the end
                    stringDays = stringDays.substring(0, stringDays.length() - 1);

                    //build a schedule to compare to
                    ClassSchedule newSchedule = new ClassSchedule(className, startHour, startMinute, endHour, endMinute, stringDays);

                    //get all the schedules to check for conflicts
                    ArrayList<ClassSchedule> schedules = dbHelper.getAllClassSchedules();

                    boolean hasConflict = false;
                    boolean sameName = false;
                    for (ClassSchedule oldSchedule : schedules) {
                        if (newSchedule.hasConflict(oldSchedule) && oldSchedule.getId() != schedule.getId()) {
                            hasConflict = true;
                        }
                        if (newSchedule.getName().equals(oldSchedule.getName()) && oldSchedule.getId() != schedule.getId()) {
                            sameName = true;
                        }
                    }
                    if (hasConflict) {
                        showDialogue("scheduleConflict");
                    } else if (sameName) {
                        showDialogue("sameName");
                    } else {
                        //if it makes it here, there are no conflicts and we can save the schedule
                        dbHelper.updateClassSchedule(schedule.getId(), className, startHour, startMinute, endHour, endMinute, stringDays);
                        Intent intent;
                        intent = new Intent(ScheduleUpdate.this, ScheduleMainActivity.class);
                        startActivity(intent);
                        Toast.makeText(ScheduleUpdate.this, "Class updated: " + schedule.getName() , Toast.LENGTH_SHORT).show();

                    }
                } catch (NullPointerException e) {
                    showDialogue("missingData");
                }
            }
        });
    }

    //shows a dialogue popup on the screen with a custom message

    /**
     * Creates and shows a unique popup for various errors, etc...
     * @param type Type of dialogue to be shown
     */
    private void showDialogue(String type){
        AlertDialog alertDialog = new AlertDialog.Builder(ScheduleUpdate.this).create();
        alertDialog.setTitle("Error: Schedule not Saved");
        switch(type){
            case "missingData":
                alertDialog.setMessage("One or more fields not set");
                break;
            case "scheduleConflict":
                alertDialog.setMessage("Schedule conflicts with one already on the calender");
                break;
            case "sameName":
                alertDialog.setMessage("Class already exists on schedule");

        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }

    /**
     * Get the selected days from the ui and put them in a list object
     * @return List of days (strings)
     */
    private List<String> getSelectedDays(){
        List<String> dayList = new ArrayList<String>();

        if(mondayCheckBox.isChecked()){
            dayList.add("monday");
        }
        if(tuesdayCheckBox.isChecked()){
            dayList.add("tuesday");
        }
        if(wednesdayCheckBox.isChecked()){
            dayList.add("wednesday");
        }
        if(thursdayCheckBox.isChecked()){
            dayList.add("thursday");
        }
        if(fridayCheckBox.isChecked()){
            dayList.add("friday");
        }
        if(dayList.size() == 0){
            throw new NullPointerException();
        }
        return dayList;
    };

    /**
     * Convert time so that ui shows 12 hour time with am/pm
     * @param hour
     * @param minute
     * @return Correctly formatted time as a string
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

    /**
     * Sets the timePicker to the current time. Used on page load
     */
    private void setCurrentTimeOnView(){
        scheduleTimePicker.setCurrentHour(this.schedule.getStartHour());
        scheduleTimePicker.setCurrentMinute(this.schedule.getStartMinute());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_add_class, menu);
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
        }else if(id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A single method to set all the variable and grab all of the components in the activity
     */
    private void setVariables(){
        DBHelper db = new DBHelper(this);
        dbHelper = db.new Schedule();
        addClassNameInput = (EditText) findViewById(R.id.updateNameInput);
        mondayCheckBox = (CheckBox) findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = (CheckBox) findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = (CheckBox) findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = (CheckBox) findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = (CheckBox) findViewById(R.id.fridayCheckBox);
        scheduleTimePicker = (TimePicker) findViewById(R.id.scheduleTimePicker);
        setClassStartButton = (Button) findViewById(R.id.updateStartTimeButton);
        setClassEndButton = (Button) findViewById(R.id.updateEndTimeButton);
        classStartTextField = (TextView) findViewById(R.id.classStartTextField);
        classEndTextField = (TextView) findViewById(R.id.classEndTextField);
        updateClassButton = (Button) findViewById(R.id.updateClassButton);
    }

    private ClassSchedule getClassScheduleById(int id){
        ArrayList<ClassSchedule> schedules = dbHelper.getAllClassSchedules();

        for(int i=0; i<schedules.size(); i++){
            if(id == schedules.get(i).getId()){
                return schedules.get(i);
            }
        }

        return null;
    }
}
