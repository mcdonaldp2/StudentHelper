package com.helper.groupa.studenthelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GBSyllabus extends Activity {

    private GBClassDAO classDAO;
    private GBSyllabusDAO syllabusDAO;
    private TextView classTitle;
    private List<GBSyllabusUnit> listSyllabusUnit;
    private ListView listSyllabusView;
    private Button addSyllabusButton;
    private ArrayAdapter<GBSyllabusUnit> syllabusAdapter;
    private EditText syllabusName;
    private EditText weight;
    private CheckBox extraCredit;
    private long classID;
    private GBClassUnit classUnit;
    private GBSyllabusUnit syllabus;


    private int ecFlag;
    private double weightedGradeEarned;
    private double weightedGradePossible;
    private double weightEC;

    private int pos;


    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gb_syllabus);

        classDAO = new GBClassDAO(this);
        classID = getIntent().getLongExtra("classID", 0);
        classUnit = classDAO.getClassByID(classID);
        syllabusDAO = new GBSyllabusDAO(this);
        classTitle = (TextView) findViewById(R.id.syllabus_title_class);
        classTitle.setText(classUnit.toString());
        listSyllabusUnit = syllabusDAO.getSyllabusByClass(classID);
        listSyllabusView = (ListView) findViewById(R.id.gb_syllabus_list);
        addSyllabusButton = (Button) findViewById(R.id.gb_add_syllabus_button);
        syllabusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listSyllabusUnit);
        listSyllabusView.setAdapter(syllabusAdapter);


        backNavi();

        addSyllabus();


        changeSyllabus();


        syllabusGrade(classID);
    }


    public void backNavi() {
        classTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GBSyllabus.this, GBClass.class);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Adds a new syllabus item after clicking on the add syllabus button
     */
    private void addSyllabus() {
        addSyllabusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog is built with layout and custom ADD button
                @SuppressLint("InflateParams") View view = (LayoutInflater.from(GBSyllabus.this)).inflate(R.layout.gb_add_syllabus, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(GBSyllabus.this);
                builder.setCancelable(true).setView(view)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                // Sets up fields to allow user interaction and input
                syllabusName = (EditText) view.findViewById(R.id.gb_syllabus_name);
                weight = (EditText) view.findViewById(R.id.gb_syllabus_weight);
                extraCredit = (CheckBox) view.findViewById(R.id.gb_syllabus_ec);

                // Sets fields to display certain hints
                syllabusName.setHint("Syllabus Item");
                weight.setHint("Weight %");
                ecFlag = 0;

                // Checks if there are other items.  If there isn't, then the first item cannot
                // be extra credit, so the checkbox is disabled
                weightedGradePossible = 0;
                for (GBSyllabusUnit s : listSyllabusUnit) {
                    weightedGradePossible += s.getWeight();
                }
                listSyllabusUnit.size();
                if (weightedGradePossible != 0) {
                    extraCredit.setEnabled(true);
                } else {
                    extraCredit.setEnabled(false);
                }

                // Custom ADD button
                class CustomListener implements View.OnClickListener {
                    private final Dialog dialog;

                    public CustomListener(Dialog dialog) {
                        this.dialog = dialog;
                    }

                    public void onClick(View v) {
                        // Saves inputs into string tokens
                        String syllabusName = GBSyllabus.this.syllabusName.getText().toString();
                        String weightRawToken = weight.getText().toString();

                        // If weight is empty, tells user that is not allowed.
                        if (weightRawToken.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "% cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, saves string token and converts into double token
                        else {
                            double weightToken = Double.valueOf(weightRawToken);

                            // If weight is zero, tells user that is not allowed.
                            if (weightToken == 0.0) {
                                Toast.makeText(getApplicationContext(),
                                        "% cannot be 0!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Else, save item into table, calculate, and update
                            else {
                                // Syllabus object created and given values
                                syllabus = new GBSyllabusUnit();
                                syllabus.setClassUnit(classUnit);
                                syllabus.setName(syllabusName);
                                syllabus.setWeight(weightToken);
                                syllabus.setGrade(0);
                                syllabus.setExtraCredit(ecFlag);

                                // Syllabus object is saved into table, and view is updated
                                syllabus = syllabusDAO.createSyllabus(syllabus);
                                syllabusAdapter.add(syllabus);

                                // Calculates then updates class grade
                                weightedGradeEarned = 0;
                                weightedGradePossible = 0;
                                weightEC = 0;
                                for (GBSyllabusUnit s : listSyllabusUnit) {
                                    if (s.getExtraCredit() == 0) {
                                        weightedGradeEarned += ((s.getWeight() * s.getGrade()) / 100);
                                        weightedGradePossible += s.getWeight();
                                    } else if (s.getExtraCredit() == 1) {
                                        weightEC += s.getWeight();
                                    }
                                }

                                classUnit.setGrade(((weightedGradeEarned / weightedGradePossible) *
                                        100) + weightEC);
                                classDAO.updateClass(classUnit);
                                classTitle.setText(classUnit.toString());

                                // Dialog closes
                                dialog.dismiss();
                            }
                        }
                    }
                }

                // Extra Credit checkbox
                extraCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // If checked, sets integer flag to 1 and changes hint
                        if (isChecked) {
                            ecFlag = 1;
                            weight.setHint("Extra Credit %");
                        }

                        // Else, retain previous state
                        else {
                            ecFlag = 0;
                            weight.setHint("Weight %");
                        }
                    }

                });

                // Built dialog is displayed
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button addButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                addButton.setOnClickListener(new CustomListener(alertDialog));
            }
        });
    }


    /**
     * Edit or delete a current item after long-clicking an item in list
     */
    private void changeSyllabus() {
        listSyllabusView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Dialog is built with layout and custom EDIT
                view = (LayoutInflater.from(GBSyllabus.this)).inflate(R.layout.gb_add_syllabus, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(GBSyllabus.this);
                builder.setCancelable(true).setView(view)
                        .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                // Holds position of item in list and size of list
                pos = position;
                final int size = listSyllabusUnit.size();

                // Sets up the fields to allow user interaction and input
                syllabusName = (EditText) view.findViewById(R.id.gb_syllabus_name);
                weight = (EditText) view.findViewById(R.id.gb_syllabus_weight);
                extraCredit = (CheckBox) view.findViewById(R.id.gb_syllabus_ec);

                // Sets fields to display current item's values
                syllabusName.setText(listSyllabusUnit.get(pos).getName());
                weight.setText(String.valueOf(listSyllabusUnit.get(pos)
                        .getWeight()));
                ecFlag = listSyllabusUnit.get(pos).getExtraCredit();

                // Sets up the extra credit checkbox depending on circumstances
                // If item had extra credit as true (1), then the checkbox is checked
                if (ecFlag == 1) {
                    extraCredit.setChecked(true);
                }
                // Else, checkbox is not checked
                else {
                    extraCredit.setChecked(false);
                }

                // Certain events are checked to prevent items becoming extra credit, which
                // could result in a list of nothing but extra credit.  Items are needed, while
                // extra credit are "attachments"; extra credits are additions onto current
                // items, not a standalone.
                int counter = 0;
                // Since the extra credit integer is (1), a counter tracks the number of extra
                // credit in the grade list
                for (GBSyllabusUnit s : listSyllabusUnit) {
                    counter += s.getExtraCredit();
                }
                // If there are other items, then enable the extra credit checkbox
                if (size - counter > 1) {
                    extraCredit.setEnabled(true);
                }
                // Else, do not allow an item to become extra credit.  Extra credit item's
                // checkbox is still enabled, however.
                else {
                    if (ecFlag == 1) {
                        extraCredit.setEnabled(true);
                    } else {
                        extraCredit.setEnabled(false);
                    }
                }

                // Custom EDIT button
                class CustomEdit implements View.OnClickListener {
                    private final Dialog dialog;

                    public CustomEdit(Dialog dialog) {
                        this.dialog = dialog;
                    }

                    public void onClick(View v) {
                        String syllabusName = GBSyllabus.this.syllabusName.getText().toString();
                        String weightRawToken = weight.getText().toString();

                        // If weight is empty, tells user that is not allowed.
                        if (weightRawToken.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "% cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, saves string token and converts into double token
                        else {
                            double weightToken = Double.valueOf(weightRawToken);

                            // If weight is zero, tells user that is not allowed.
                            if (weightToken == 0.0) {
                                Toast.makeText(getApplicationContext(),
                                        "% cannot be 0!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Else, save item into table, calculate, and update
                            else {
                                // Syllabus object at position in list has updated values
                                listSyllabusUnit.get(pos).setName(syllabusName);
                                listSyllabusUnit.get(pos).setWeight(weightToken);
                                listSyllabusUnit.get(pos).setExtraCredit(ecFlag);


                                // Syllabus object is saved into table, and view is updated
                                syllabusAdapter.notifyDataSetChanged();
                                syllabusDAO.updateSyllabus(listSyllabusUnit.get(pos));


                                // Calculates then updates class grade
                                weightedGradeEarned = 0;
                                weightedGradePossible = 0;
                                weightEC = 0;
                                for (GBSyllabusUnit s : listSyllabusUnit) {
                                    if (s.getExtraCredit() == 0) {
                                        weightedGradeEarned += ((s.getWeight() * s.getGrade()) / 100);
                                        weightedGradePossible += s.getWeight();
                                    } else if (s.getExtraCredit() == 1) {
                                        weightEC += s.getWeight();
                                    }
                                }
                                classUnit.setGrade(((weightedGradeEarned / weightedGradePossible) *
                                        100) + weightEC);
                                classDAO.updateClass(classUnit);
                                classTitle.setText(classUnit.toString());

                                // Dialog closes
                                dialog.dismiss();
                            }
                        }
                    }
                }

                // Building custom DELETE button
                builder.setCancelable(true).setView(view)
                        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                // Used as a check to prevent deletion of grade
                final int finalCounter = size - counter;

                // Custom DELETE button
                class CustomDelete implements View.OnClickListener {
                    private final Dialog dialog;

                    public CustomDelete(Dialog dialog) {
                        this.dialog = dialog;
                    }

                    @Override
                    public void onClick(View v) {
                        // An item cannot be deleted if there remains extra credit items.
                        // Notifies user if that event happens.
                        if (size >= 2 && listSyllabusUnit.get(pos).getExtraCredit() == 0 &&
                                finalCounter == 1) {
                            Toast.makeText(getApplicationContext(),
                                    "Last item cannot be deleted!\nExtra credit cannot be alone!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, deletes the grade and updates syllabus and class
                        else {
                            syllabusDAO.deleteSyllabus(listSyllabusUnit.get(pos));
                            listSyllabusUnit.remove(pos);
                            syllabusAdapter.notifyDataSetChanged();

                            // Calculates new grade after deletion
                            weightedGradeEarned = 0;
                            weightedGradePossible = 0;
                            weightEC = 0;
                            for (GBSyllabusUnit s : listSyllabusUnit) {
                                if (s.getExtraCredit() == 0) {
                                    weightedGradeEarned += ((s.getWeight() * s.getGrade()) / 100);
                                    weightedGradePossible += s.getWeight();
                                } else if (s.getExtraCredit() == 1) {
                                    weightEC += s.getWeight();
                                }
                            }

                            // If the deleted item was not the last item, update accordingly
                            if (weightedGradePossible != 0) {
                                classUnit.setGrade(((weightedGradeEarned / weightedGradePossible) *
                                        100) + weightEC);
                                classDAO.updateClass(classUnit);
                                classTitle.setText(classUnit.toString());

                            }
                            // Else, set class grade to zero and update accordingly
                            else {
                                classUnit.setGrade(0);
                                classDAO.updateClass(classUnit);
                                classTitle.setText(classUnit.toString());
                            }

                            dialog.dismiss();
                        }
                    }
                }

                // Extra Credit checkbox
                extraCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // If checked, sets integer flag to 1 and changes hint
                        if (isChecked) {
                            ecFlag = 1;
                            weight.setHint("Extra Credit %");
                        }

                        // Else, retain previous state
                        else {
                            ecFlag = 0;
                            weight.setHint("Weight %");
                        }
                    }

                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button editButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button deleteButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                editButton.setOnClickListener(new CustomEdit(alertDialog));
                deleteButton.setOnClickListener(new CustomDelete(alertDialog));
                return true;
            }
        });
    }


    private void syllabusGrade(final long classID) {


        class CustomAdapt extends ArrayAdapter {

            private Context context;
            private int resource;

            public CustomAdapt(Context context, int resource) {
                super(context, resource);
                this.context = context;
                this.resource = resource;
            }

            public boolean areAllItemsEnabled() {
                return false;
            }

            public boolean isEnabled(int position) {
                return false;
            }
        }


        final CustomAdapt adapt = new CustomAdapt(this, R.id.gb_syllabus_list);

        listSyllabusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (listSyllabusUnit.get(position).getExtraCredit() == 1) {
                    adapt.isEnabled(position);
                    Toast.makeText(getApplicationContext(),
                            "Extra Credit cannot have grades!",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Intent myIntent = new Intent(GBSyllabus.this, GBGrade.class);
                    myIntent.putExtra("syllabusID", listSyllabusUnit.get(position).getId());
                    myIntent.putExtra("classID", classID);
                    startActivity(myIntent);
                }
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
