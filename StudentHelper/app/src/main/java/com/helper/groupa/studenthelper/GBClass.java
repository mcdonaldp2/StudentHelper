package com.helper.groupa.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class GBClass extends Activity {

    // Field variables declared
    private GBClassDAO classDAO; // Allows interaction with database table
    private List<GBClassUnit> listClassUnit; // Uploads table to list
    private ListView listClassView; // Layout of classes
    private Button addClassButton; // Layout of button
    private ArrayAdapter<GBClassUnit> classAdapter; // Adapter that updates list
    private EditText className; // Add/change class name
    private int pos; // Used for position of item in list

    private TextView homeScreen;

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gb_class);

        // Field variables initialized
        classDAO = new GBClassDAO(this);
        listClassUnit = classDAO.getCompleteClassList();
        listClassView = (ListView) findViewById(R.id.gb_class_list);
        addClassButton = (Button) findViewById(R.id.gb_add_class_button);
        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listClassUnit);
        listClassView.setAdapter(classAdapter); // Setting layout of classes to adapter

        homeScreen = (TextView) findViewById(R.id.gb_title_home);

        backNavi();

        // Adding a new class
        addClass();

        // Changing a class
        changeClass();

        // Go to class syllabus
        classSyllabus();
    }

    public void backNavi() {
        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GBClass.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Adding a new class after clicking the add class button
     */
    private void addClass() {
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                // Dialog is built with layout
                v = (LayoutInflater.from(GBClass.this)).inflate(R.layout.gb_add_class, null);
                AlertDialog.Builder addClassPopUp = new AlertDialog.Builder(GBClass.this);
                addClassPopUp.setView(v);

                // Sets up the fields to allow user interaction and input
                className = (EditText) v.findViewById(R.id.gb_class_name);
                className.setHint("Class Name");

                // "ADD" button is added that adds a class to list and database
                addClassPopUp.setCancelable(true).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // New class object is created with variables set
                        GBClassUnit classUnit = new GBClassUnit();
                        String className = GBClass.this.className.getText().toString();
                        classUnit.setName(className);
                        classUnit.setGrade(0.0);
                        // Class object then added to database
                        classUnit = classDAO.createClass(classUnit);
                        // Update the database and view
                        classDAO.updateClass(classUnit);
                        classAdapter.add(classUnit);
                    }
                });
                Dialog dialog = addClassPopUp.create();
                dialog.show();
            }
        });
    }

    /**
     * Changing/adding a current class after clicking on the class-item in list
     */
    private void changeClass() {
        listClassView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Dialog is built with layout
                view = (LayoutInflater.from(GBClass.this)).inflate(R.layout.gb_add_class, null);
                AlertDialog.Builder addClassPopUp = new AlertDialog.Builder(GBClass.this);
                addClassPopUp.setView(view);
                // EditText with class name is added to dialog
                className = (EditText) view.findViewById(R.id.gb_class_name);
                className.setText(listClassUnit.get(position).getName());
                // Takes position of the long-clicked item
                pos = position;
                // "EDIT" button is added that updates a class to list and database
                addClassPopUp.setCancelable(true).setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finds the class' position, changes name, update adapter, and update
                        // database
                        listClassUnit.get(pos).setName(className.getText().toString());
                        classAdapter.notifyDataSetChanged();
                        classDAO.updateClass(listClassUnit.get(pos));
                    }
                });
                // "DELETE" deletes a class from the list and database
                addClassPopUp.setCancelable(true).setNegativeButton("DELETE", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finds the class' position, deletes the class, and update adapter
                        classDAO.deleteClass(listClassUnit.get(pos));
                        listClassUnit.remove(pos);
                        classAdapter.notifyDataSetChanged();
                    }
                });
                Dialog dialog = addClassPopUp.create();
                dialog.show();
                return true;
            }
        });
    }

    /**
     * Goes to class syllabus with classID to display only that class' syllabus
     */
    private void classSyllabus() {
        listClassView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Sends an intent containing classID that allows only the syllabus having the
                // same classID to show in the syllabus list
                Intent myIntent = new Intent(GBClass.this, GBSyllabus.class);
                myIntent.putExtra("classID", listClassUnit.get(position).getId());
                startActivity(myIntent);
            }
        });
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
}
