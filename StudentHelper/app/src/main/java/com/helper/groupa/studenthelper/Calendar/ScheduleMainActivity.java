package com.helper.groupa.studenthelper.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.helper.groupa.studenthelper.DBHelper;
import com.helper.groupa.studenthelper.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleMainActivity extends Activity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener{

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private DBHelper.Schedule dbHelper;
    ArrayList<ClassSchedule> allSchedules;
    private Button simulateClickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        DBHelper db = new DBHelper(this);
        dbHelper = db.new Schedule();

        this.simulateClickButton = (Button) findViewById(R.id.simulateClickButton);
        simulateClickButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ScheduleMainActivity.this, ScheduleDetails.class);
                intent.putExtra("scheduleId", "1");
                startActivity(intent);
            }
        });

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.addClassToScheduleButton:
                Intent intent;
                intent = new Intent(ScheduleMainActivity.this, ScheduleAddClassActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with evnets from db.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        //get schedules from db
        allSchedules = dbHelper.getAllClassSchedules();
        Calendar testDate = Calendar.getInstance();

        //use test date to iterate through each day of each month so that calendar updates correctly
        //when scrolling horizontally
        testDate.set(Calendar.MONTH, newMonth-1);
        testDate.set(Calendar.YEAR, newYear);

        /*Since the schedule only uses names of days(monday, tuesday, etc..), we need to loop through
        * each day of the month to determine which day it is and if it needs to place a schedule there*/
        for(int i=1; i<32; i++){
            testDate.set(Calendar.DAY_OF_MONTH, i);
            String dayLongName = testDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

            for(int j=0; j<allSchedules.size(); j++){
                ClassSchedule schedule = allSchedules.get(j);
                String[] days = schedule.getDays();

                for(int k=0; k<days.length; k++){
                    if(days[k].equals(dayLongName.toLowerCase())){
                        /*if it makes it here, then an instance has been found where we need to put
                        * a saved schedule on the calendar*/

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, schedule.getStartHour());
                        startTime.set(Calendar.MINUTE, schedule.getStartMinute());
                        startTime.set(Calendar.DAY_OF_MONTH, i);
                        startTime.set(Calendar.MONTH, newMonth - 1);
                        startTime.set(Calendar.YEAR, newYear);

                        Calendar endTime = Calendar.getInstance();
                        endTime.set(Calendar.HOUR_OF_DAY, schedule.getEndHour());
                        endTime.set(Calendar.MINUTE, schedule.getEndMinute());
                        endTime.set(Calendar.DAY_OF_MONTH, i);
                        endTime.set(Calendar.YEAR, newYear);
                        endTime.set(Calendar.MONTH, newMonth-1);

                        WeekViewEvent event = new WeekViewEvent(schedule.getId(), schedule.getName(), startTime, endTime);

                        event.setColor(Color.RED);
                        events.add(event);
                    }
                }
            }
        }

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        //setting custom layout to dialog
        //dialog.setContentView(R.layout.activity_schedule_edit);
        //dialog.setTitle("Custom Dialog");

        //ClassSchedule schedule = getScheduleById((int) event.getId());

        Intent intent;
        intent = new Intent(ScheduleMainActivity.this, ScheduleDetails.class);

        String id = String.valueOf((event.getId()));

        intent.putExtra("scheduleId", id);
        startActivity(intent);
        System.out.println("matt: " + event.getId());
    }

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {


        Toast.makeText(ScheduleMainActivity.this, "Class deleted from schedule: ", Toast.LENGTH_SHORT).show();
    }



}
