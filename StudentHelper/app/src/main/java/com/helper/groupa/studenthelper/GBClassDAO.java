package com.helper.groupa.studenthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A direct-access object class.  It creates an object that allows CRUD interaction with table
 */
public class GBClassDAO {

    // Field variables declared
    private static final String TAG = "ScheduleDAO";
    private SQLiteDatabase mDatabase;
    private final DBHelper mGradebook;
    private final Context mContext;
    private final String[] mAllColumns = {
            DBHelper.CLASS_COLUMN_ID,
            DBHelper.CLASS_COLUMN_NAME,
            DBHelper.CLASS_COLUMN_GRADE};

    /**
     * Constructor that creates new DAO object
     *
     * @param context activity screen
     */
    public GBClassDAO(Context context) {
        // Set this context to parameter context and set table to parameter context
        this.mContext = context;
        mGradebook = new DBHelper(context);
        // Try to open table
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens table
     *
     * @throws SQLException if there is no table
     */
    private void open() throws SQLException {
        mDatabase = mGradebook.getWritableDatabase();
    }

// --Commented out by Inspection START (10/19/2015 11:18 PM):
//    public void close() {
//        mGradebook.close();
//    }
// --Commented out by Inspection STOP (10/19/2015 11:18 PM)

    /**
     * Create a class
     *
     * @param classUnit an object of a class
     * @return classUnit in the table to indicate success
     */
    public GBClassUnit createClass(GBClassUnit classUnit) {
        // Class values placed into table
        ContentValues values = new ContentValues();
        values.put(DBHelper.CLASS_COLUMN_NAME, classUnit.getName());
        values.put(DBHelper.CLASS_COLUMN_GRADE, classUnit.getGrade());
        // Class is inserted into table, and an ID is retrieved
        long insertID = mDatabase.insert(DBHelper.CLASS_TABLE,
                null,
                values);
        // ID is used to get cursor to point to the class.  Then, it checks that it is not empty and
        // it was successfully added.
        Cursor cursor = mDatabase.query(DBHelper.CLASS_TABLE,
                mAllColumns,
                DBHelper.CLASS_COLUMN_ID + " = " + insertID,
                null, null, null, null);
        cursor.moveToFirst();
        GBClassUnit newClassUnit = cursorToClass(cursor);
        cursor.close();
        return newClassUnit;
    }

    /**
     * Retrieve class by ID
     *
     * @param id class ID
     * @return class
     */
    public GBClassUnit getClassByID(long id) {
        // Create class object and cursor set to table
        GBClassUnit classUnit = new GBClassUnit();
        Cursor cursor = mDatabase.query(DBHelper.CLASS_TABLE,
                mAllColumns,
                DBHelper.CLASS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        // Cursor finds ID match, and class object is set to found match
        if (cursor != null && cursor.moveToFirst()) {
            //cursor.moveToFirst();
            classUnit = cursorToClass(cursor);
            cursor.close();
        }
        // Schedule schedule = cursorToClass(cursor);
        return classUnit;
    }

    /**
     * Retrieve all classes
     *
     * @return class list
     */
    @SuppressWarnings("ConstantConditions")
    public List<GBClassUnit> getCompleteClassList() {
        // Create an array list of classes and cursor set to table
        List<GBClassUnit> listClass = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBHelper.CLASS_TABLE,
                mAllColumns,
                null, null, null, null, null);
        // As cursor moves through table, add class to array list
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GBClassUnit classUnit = cursorToClass(cursor);
                listClass.add(classUnit);
                cursor.moveToNext();
            }
        }
        cursor.close();// Close cursor
        return listClass;
    }

    /**
     * Update a class in table
     *
     * @param classUnit class that needs to be updated
     * @return updated class
     */
    public int updateClass(GBClassUnit classUnit) {
        // Update table columns
        ContentValues values = new ContentValues();
        values.put(DBHelper.CLASS_COLUMN_NAME, classUnit.getName());
        values.put(DBHelper.CLASS_COLUMN_GRADE, classUnit.getGrade());
        return mDatabase.update(
                DBHelper.CLASS_TABLE,
                values,
                DBHelper.CLASS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(classUnit.getId())});
    }

    /**
     * Deletes a class from the table
     *
     * @param classUnit class that needs to be deleted
     */
    public void deleteClass(GBClassUnit classUnit) {
        // Get the ID of the class and create new syllabus DAO set to current context
        long id = classUnit.getId();
        // Create syllabus DAO, retrieve syllabus item with matching class ID, and set to syllabus
        // list
        GBSyllabusDAO syllabusDAO = new GBSyllabusDAO(mContext);
        List<GBSyllabusUnit> listSyllabus = syllabusDAO.getSyllabusByClass(id);
        // Delete syllabus item from syllabus table
        if (listSyllabus != null && !listSyllabus.isEmpty()) {
            for (GBSyllabusUnit s : listSyllabus) {
                syllabusDAO.deleteSyllabus(s);
            }
        }
        // Delete the class from class table
        mDatabase.delete(DBHelper.CLASS_TABLE,
                DBHelper.CLASS_COLUMN_ID + " = " + id,
                null);
    }

    /**
     * Cursor reference to class
     *
     * @param cursor cursor
     * @return class object with set variables
     */
    private GBClassUnit cursorToClass(Cursor cursor) {
        GBClassUnit classUnit = new GBClassUnit();
        classUnit.setId(cursor.getLong(0));
        classUnit.setName(cursor.getString(1));
        classUnit.setGrade(cursor.getDouble(2));
        return classUnit;
    }
}
