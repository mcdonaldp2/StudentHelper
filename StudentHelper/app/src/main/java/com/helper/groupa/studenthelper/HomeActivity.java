package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.helper.groupa.studenthelper.Calendar.ScheduleMainActivity;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(HomeActivity.this, ScheduleMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    // Take user to Group Dashboard

    public void openGroups(View view)
    {
        Intent intent = new Intent(this, GroupMain.class);
        startActivity(intent);
    }

    // starts agenda activity when Agenda button is clicked calling this method using an onClick handler in the xml
   public void viewAgenda(View view){
       Intent intent = new Intent(this, AgendaActivity.class);
       startActivity(intent);
   }


   public void gpaButtonClick(View view) {
        Intent gpaIntent = new Intent(this, GPAActivity.class);
        startActivity(gpaIntent);

    }


    public void gradebookPage(View view) {
        Intent intent = new Intent(this, GBClass.class);
        startActivity(intent);
    }

}
