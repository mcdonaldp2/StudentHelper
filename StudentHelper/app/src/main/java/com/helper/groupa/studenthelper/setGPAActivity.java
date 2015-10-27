package com.helper.groupa.studenthelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Double.valueOf;

public class setGPAActivity extends Activity {
    String semesterTitle, setGPA;
    Double GPADouble;
    EditText setGPAEditText;
    Button saveGPABtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_gpa);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle getBundle = getIntent().getExtras();
        semesterTitle = getBundle.getString("TitleId");
        setGPA = getBundle.getString("setGPA");


        setGPAEditText = (EditText)findViewById(R.id.setGPAEditText);
        saveGPABtn = (Button)findViewById(R.id.saveGPAButton);
        saveGPABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tries parsing the setGPAEditText if it throws an exception
                //it displays a toast
                try {
                    GPADouble = valueOf(setGPAEditText.getText().toString());
                    if(db.insertSemester(semesterTitle, setGPA, GPADouble))
                    {
                        Toast.makeText(getBaseContext(), "Semester Saved!", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), GPAActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Input a decimal value!", Toast.LENGTH_LONG).show();
                }


            }
        });

        db = new DBHelper(this);
    }

}
