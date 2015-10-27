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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GBGrade extends Activity {
    // Class variables used to display/update class title
    private GBClassDAO classDAO;
    private long classID;
    private GBClassUnit classUnit;
    private TextView classTitle;

    // Syllabus variables used to display/update syllabus title
    private GBSyllabusDAO syllabusDAO;
    private GBSyllabusUnit syllabusUnit;
    private TextView syllabusTitle;
    // Syllabus list holds syllabus table.  Used to update syllabus title.
    private List<GBSyllabusUnit> listSyllabusUnit;

    // Grade variables used for various purposes
    private GBGradeDAO gradeDAO; // Allows CRUD interaction with grade table
    private List<GBGradeUnit> listGradeUnit;
    private Button addGradeButton;
    private ArrayAdapter<GBGradeUnit> gradeAdapter;

    // Misc variables
    // Grade object
    private GBGradeUnit grade;
    // Used mainly to set up fields in dialog
    private EditText gradeName;
    private EditText gradeEarned;
    private EditText gradePossible;
    private CheckBox extraCredit;
    // Used mainly to calculate/update class and syllabus grades
    private double possibleTotal;
    private double earnedTotal;
    private double syllabusEC;
    // Flag check if grade is extra credit
    private int ecFlag;
    // Position in list view
    private int pos;

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gb_grade);

        // Class variables
        classDAO = new GBClassDAO(this);
        classID = getIntent().getLongExtra("classID", 0);
        classUnit = classDAO.getClassByID(classID);
        classTitle = (TextView) findViewById(R.id.grade_title_class);
        classTitle.setText(classUnit.toString());

        // Syllabus variables
        syllabusDAO = new GBSyllabusDAO(this);
        long syllabusID = getIntent().getLongExtra("syllabusID", 0);
        syllabusUnit = syllabusDAO.getSyllabusByID(syllabusID);
        syllabusTitle = (TextView) findViewById(R.id.grade_title_syllabus);
        syllabusTitle.setText(syllabusUnit.toString());
        // Syllabus list holds syllabus table.  Used to update syllabus title.
        listSyllabusUnit = syllabusDAO.getSyllabusByClass(classID);

        // Grade variables used for various purposes
        gradeDAO = new GBGradeDAO(this);
        listGradeUnit = gradeDAO.getGradeBySyllabus(syllabusID);
        ListView listGradeView = (ListView) findViewById(R.id.gb_grade_list);
        addGradeButton = (Button) findViewById(R.id.gb_add_grade_button);
        gradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listGradeUnit);
        listGradeView.setAdapter(gradeAdapter);

        backNavi();

        addGrade();

        changeGrade(listGradeView);
    }

    /**
     * Allows backward navigation via clicking on title
     */
    private void backNavi() {
        // Class title sends user back to class screen
        classTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GBGrade.this, GBClass.class);
                startActivity(myIntent);
            }
        });

        // Syllabus title sends user back to syllabus screen
        syllabusTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GBGrade.this, GBSyllabus.class);
                myIntent.putExtra("classID", classID);
                startActivity(myIntent);
            }
        });
    }

    /**
     * Add a new grade after clicking the add grade button
     */
    private void addGrade() {
        addGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog is built with layout and custom ADD button
                @SuppressLint("InflateParams") View view = (LayoutInflater.from(GBGrade.this)).inflate(R.layout.gb_add_grade, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(GBGrade.this);
                builder.setCancelable(true).setView(view)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                // Sets up the fields to allow user interaction and input
                gradeName = (EditText) view.findViewById(R.id.gb_grade_name);
                gradeEarned = (EditText) view.findViewById(R.id.gb_grade_earned);
                gradePossible = (EditText) view.findViewById(R.id.gb_grade_possible);
                extraCredit = (CheckBox) view.findViewById(R.id.gb_grade_ec);

                // Sets fields to display certain hints
                gradeName.setHint("Grade Name");
                gradeEarned.setHint("Point Earned");
                gradePossible.setHint("Point Possible");
                ecFlag = 0;

                // Checks if there are other grades.  If there isn't, then the first grade cannot
                // be extra credit, so the checkbox is disabled
                possibleTotal = 0.0;
                for (GBGradeUnit g : listGradeUnit) {
                    possibleTotal += g.getPointsPossible();
                }
                if (possibleTotal != 0) {
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

                    @Override
                    public void onClick(View v) {
                        // Saves inputs into string tokens
                        String gradeName = GBGrade.this.gradeName.getText().toString();
                        String gradeEarned = GBGrade.this.gradeEarned.getText().toString();
                        String gradePossible = GBGrade.this.gradePossible.getText().toString();

                        // If grade earned is empty, tells user that is not allowed.
                        if (gradeEarned.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Point Earned cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else if grade possible is empty and not extra credit, tells user that is not allowed.
                        else if (gradePossible.equals("") && ecFlag == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Point Possible cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, saves string tokens and converts into double tokens
                        else {
                            double pEarn = Double.valueOf(gradeEarned);
                            double pPossible;

                            // Checks if grade is extra credit.  If so, set grade possible to
                            // zero; else, save into double token
                            if (ecFlag == 1) {
                                pPossible = 0.0;
                            } else {
                                pPossible = Double.valueOf(gradePossible);
                            }

                            // If grade possible is zero and not extra credit, tells user that is
                            // not allowed.
                            if (pPossible == 0.0 && ecFlag == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Point Possible cannot be 0!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Else, save grade into table, calculate, and update
                            else {
                                // Grade object created and given values
                                grade = new GBGradeUnit();
                                grade.setGBSyllabusUnit(syllabusUnit);
                                grade.setName(gradeName);
                                grade.setPointsEarned(pEarn);
                                grade.setPointsPossible(pPossible);
                                grade.setExtraCredit(ecFlag);

                                // Grade object is saved into table, and view is updated
                                grade = gradeDAO.createGrade(grade);
                                gradeAdapter.add(grade);

                                // Calculates then updates syllabus grade
                                earnedTotal = 0;
                                possibleTotal = 0;
                                for (GBGradeUnit g : listGradeUnit) {
                                    earnedTotal += g.getPointsEarned();
                                    possibleTotal += g.getPointsPossible();
                                }
                                syllabusUnit.setGrade((earnedTotal / possibleTotal) * 100);
                                syllabusDAO.updateSyllabus(syllabusUnit);
                                syllabusTitle.setText(syllabusUnit.toString());

                                // Calculates then updates class grade
                                listSyllabusUnit = syllabusDAO.getSyllabusByClass(classID);
                                earnedTotal = 0;
                                possibleTotal = 0;
                                syllabusEC = 0;
                                for (GBSyllabusUnit s : listSyllabusUnit) {
                                    if (s.getExtraCredit() == 0) {
                                        earnedTotal += ((s.getWeight() * s.getGrade()) / 100);
                                        possibleTotal += s.getWeight();
                                    } else if (s.getExtraCredit() == 1) {
                                        syllabusEC += s.getWeight();
                                    }
                                }
                                classUnit.setGrade(((earnedTotal / possibleTotal) * 100) +
                                        syllabusEC);
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
                        // If checked, sets integer flag to 1, disables grade possible, and
                        // changes hint
                        if (isChecked) {
                            ecFlag = 1;
                            gradePossible.setEnabled(false);
                            gradeEarned.setHint("EC Earned");
                            gradePossible.setHint("");
                        }

                        // Else, retain previous state
                        else {
                            ecFlag = 0;
                            gradePossible.setEnabled(true);
                            gradeEarned.setHint("Point Earned");
                            gradePossible.setHint("Point Possible");
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
     * Edit or delete a current grade after long-clicking a grade in list
     *
     * @param listGradeView view of the grade list
     */
    private void changeGrade(ListView listGradeView) {
        listGradeView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Dialog is built with layout and custom EDIT
                view = (LayoutInflater.from(GBGrade.this)).inflate(R.layout.gb_add_grade, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(GBGrade.this);
                builder.setCancelable(true).setView(view)
                        .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                // Holds position of grade in list and size of list
                pos = position;
                final int size = listGradeUnit.size();

                // Sets up the fields to allow user interaction and input
                gradeName = (EditText) view.findViewById(R.id.gb_grade_name);
                gradeEarned = (EditText) view.findViewById(R.id.gb_grade_earned);
                gradePossible = (EditText) view.findViewById(R.id.gb_grade_possible);
                extraCredit = (CheckBox) view.findViewById(R.id.gb_grade_ec);

                // Sets fields to display current grade's values
                gradeName.setText(listGradeUnit.get(pos).getName());
                gradeEarned.setText(String.valueOf((listGradeUnit.get(pos).getPointsEarned())));
                gradePossible.setText(String.valueOf((listGradeUnit.get(pos).getPointsPossible())));
                ecFlag = listGradeUnit.get(pos).getExtraCredit();

                // Sets up the extra credit checkbox depending on circumstances
                // If grade had extra credit as true (1), then the checkbox is checked and grade
                // possible is disabled
                if (ecFlag == 1) {
                    extraCredit.setChecked(true);
                    gradePossible.setEnabled(false);
                }
                // Else, checkbox is not checked and grade possible is enabled
                else {
                    extraCredit.setChecked(false);
                    gradePossible.setEnabled(true);
                }

                // Certain events are checked to prevent grades becoming extra credit, which
                // could result in a list of nothing but extra credit.  Grades are needed, while
                // extra credit are "attachments"; extra credits are additions onto current
                // grades, not a standalone.
                int counter = 0;
                // Since the extra credit integer is (1), a counter tracks the number of extra
                // credit in the grade list
                for (GBGradeUnit g : listGradeUnit) {
                    counter += g.getExtraCredit();
                }
                // If there are other grades, then enable the extra credit checkbox
                if (size - counter > 1) {
                    extraCredit.setEnabled(true);
                }
                // Else, do not allow a grade to become extra credit.  Extra credit grade's
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

                    @Override
                    public void onClick(View v) {
                        // Saves inputs into string tokens
                        String gradeName = GBGrade.this.gradeName.getText().toString();
                        String gradeEarned = GBGrade.this.gradeEarned.getText().toString();
                        String gradePossible = GBGrade.this.gradePossible.getText().toString();

                        // If grade earned is empty, tells user that is not allowed.
                        if (gradeEarned.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Point Earned cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else if grade possible is empty and not extra credit, tells user that is not allowed.
                        else if (gradePossible.equals("") && ecFlag == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Point Possible cannot be empty!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, saves string tokens and converts into double tokens
                        else {
                            double pEarn = Double.valueOf(gradeEarned);
                            double pPossible;

                            // Checks if grade is extra credit.  If so, set grade possible to
                            // zero; else, save into double token
                            if (ecFlag == 1) {
                                pPossible = 0.0;
                            } else {
                                pPossible = Double.valueOf(gradePossible);
                            }

                            // If grade possible is zero and not extra credit, tells user that is
                            // not allowed.
                            if (pPossible == 0.0 && ecFlag == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Point Possible cannot be 0!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Else, save grade into table, calculate, and update
                            else {
                                // Grade object at position in list has updated values
                                listGradeUnit.get(pos).setName(gradeName);
                                listGradeUnit.get(pos).setPointsEarned(pEarn);
                                listGradeUnit.get(pos).setPointsPossible(pPossible);
                                listGradeUnit.get(pos).setExtraCredit(ecFlag);

                                // Grade is updated in view and table
                                gradeAdapter.notifyDataSetChanged();
                                gradeDAO.updateGrade(listGradeUnit.get(pos));

                                // Calculates and then updates syllabus grade
                                earnedTotal = 0;
                                possibleTotal = 0;
                                for (GBGradeUnit g : listGradeUnit) {
                                    earnedTotal += g.getPointsEarned();
                                    possibleTotal += g.getPointsPossible();
                                }
                                syllabusUnit.setGrade((earnedTotal / possibleTotal) * 100);
                                syllabusDAO.updateSyllabus(syllabusUnit);
                                syllabusTitle.setText(syllabusUnit.toString());

                                // Calculates then updates class grade
                                listSyllabusUnit = syllabusDAO.getSyllabusByClass(classID);
                                earnedTotal = 0;
                                possibleTotal = 0;
                                syllabusEC = 0;
                                for (GBSyllabusUnit s : listSyllabusUnit) {
                                    if (s.getExtraCredit() == 0) {
                                        earnedTotal += ((s.getWeight() * s.getGrade()) / 100);
                                        possibleTotal += s.getWeight();
                                    } else if (s.getExtraCredit() == 1) {
                                        syllabusEC += s.getWeight();
                                    }
                                }
                                classUnit.setGrade(((earnedTotal / possibleTotal) * 100) +
                                        syllabusEC);
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
                        // A grade cannot be deleted if there remains extra credit grades.
                        // Notifies user if that event happens.
                        if (size >= 2 && listGradeUnit.get(pos).getExtraCredit() == 0 &&
                                finalCounter == 1) {
                            Toast.makeText(getApplicationContext(),
                                    "Last grade cannot be deleted!\nExtra credit cannot be alone!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Else, deletes the grade and updates syllabus and class
                        else {
                            // Grade is delete from table and removed/updated in list view
                            gradeDAO.deleteGrade(listGradeUnit.get(pos));
                            listGradeUnit.remove(pos);
                            gradeAdapter.notifyDataSetChanged();

                            // Calculates new grade after deletion
                            earnedTotal = 0;
                            possibleTotal = 0;
                            for (GBGradeUnit g : listGradeUnit) {
                                earnedTotal += g.getPointsEarned();
                                possibleTotal += g.getPointsPossible();
                            }

                            // If the deleted grade was not the last grade, update accordingly
                            if (possibleTotal != 0) {
                                syllabusUnit.setGrade((earnedTotal / possibleTotal) * 100);
                                syllabusDAO.updateSyllabus(syllabusUnit);
                                syllabusTitle.setText(syllabusUnit.toString());
                            }

                            // Else, set syllabus grade to zero and update accordingly
                            else {
                                syllabusUnit.setGrade(0);
                                syllabusDAO.updateSyllabus(syllabusUnit);
                                syllabusTitle.setText(syllabusUnit.toString());
                            }

                            // Calculates then updates class grade
                            listSyllabusUnit = syllabusDAO.getSyllabusByClass(classID);
                            earnedTotal = 0;
                            possibleTotal = 0;
                            syllabusEC = 0;
                            for (GBSyllabusUnit s : listSyllabusUnit) {
                                if (s.getExtraCredit() == 0) {
                                    earnedTotal += ((s.getWeight() * s.getGrade()) / 100);
                                    possibleTotal += s.getWeight();
                                } else if (s.getExtraCredit() == 1) {
                                    syllabusEC += s.getWeight();
                                }
                            }
                            classUnit.setGrade(((earnedTotal / possibleTotal) * 100) +
                                    syllabusEC);
                            classDAO.updateClass(classUnit);
                            classTitle.setText(classUnit.toString());

                            // Dialog closes
                            dialog.dismiss();
                        }
                    }
                }

                // Extra Credit checkbox
                extraCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // If checked, sets integer flag to 1, disables grade possible, and
                        // changes hint
                        if (isChecked) {
                            ecFlag = 1;
                            gradePossible.setEnabled(false);
                            gradeEarned.setHint("EC Earned");
                            gradePossible.setHint("");
                        }

                        // Else, retain previous state
                        else {
                            ecFlag = 0;
                            gradePossible.setEnabled(true);
                            gradeEarned.setHint("Point Earned");
                            gradePossible.setHint("Point Possible");
                        }
                    }
                });

                // Built dialog is displayed
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
