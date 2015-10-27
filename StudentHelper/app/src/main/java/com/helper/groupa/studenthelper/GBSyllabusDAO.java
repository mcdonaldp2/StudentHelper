package com.helper.groupa.studenthelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GBSyllabusDAO {

    private static final String TAG = "SyllabusDAO";
    private SQLiteDatabase mDatabase;
    private final DBHelper mGradebook;
    private final Context mContext;
    private final String[] mAllcolumns = {
            DBHelper.SYLLABUS_COLUMN_ID,
            DBHelper.SYLLABUS_COLUMN_NAME,
            DBHelper.SYLLABUS_COLUMN_WEIGHT,
            DBHelper.SYLLABUS_COLUMN_GRADE,
            DBHelper.SYLLABUS_COLUMN_EXTRA_CREDIT,
            DBHelper.SYLLABUS_COLUMN_CLASSID};

    public GBSyllabusDAO(Context context) {
        this.mContext = context;
        mGradebook = new DBHelper(context);

        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void open() throws SQLException {
        mDatabase = mGradebook.getWritableDatabase();
    }

// --Commented out by Inspection START (10/20/2015 12:07 PM):
//    public void close() {
//        mGradebook.close();
//    }
// --Commented out by Inspection STOP (10/20/2015 12:07 PM)


    public GBSyllabusUnit createSyllabus(GBSyllabusUnit syllabusUnit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.SYLLABUS_COLUMN_NAME, syllabusUnit.getName());
        values.put(DBHelper.SYLLABUS_COLUMN_WEIGHT, syllabusUnit.getWeight());
        values.put(DBHelper.SYLLABUS_COLUMN_GRADE, syllabusUnit.getGrade());
        values.put(DBHelper.SYLLABUS_COLUMN_EXTRA_CREDIT, syllabusUnit.getExtraCredit());
        values.put(DBHelper.SYLLABUS_COLUMN_CLASSID, syllabusUnit.getClassUnit().getId());
        long insertID = mDatabase.insert(DBHelper.SYLLABUS_TABLE,
                null,
                values);
        Cursor cursor = mDatabase.query(DBHelper.SYLLABUS_TABLE,
                mAllcolumns,
                DBHelper.SYLLABUS_COLUMN_ID + " = " + insertID,
                null, null, null, null);
        cursor.moveToFirst();
        GBSyllabusUnit newSyllabusUnit = cursorToSyllabus(cursor);
        cursor.close();
        return newSyllabusUnit;
    }

    public GBSyllabusUnit getSyllabusByID(long id) {
        Cursor cursor = mDatabase.query(DBHelper.SYLLABUS_TABLE, mAllcolumns,
                DBHelper.SYLLABUS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursorToSyllabus(cursor);
    }

// --Commented out by Inspection START (10/20/2015 12:09 PM):
//    public List<GBSyllabusUnit> getAllSyllabus() {
//        List<GBSyllabusUnit> listSyllabus = new ArrayList<>();
//        Cursor cursor = mDatabase.query(DBHelper.SYLLABUS_TABLE,
//                mAllcolumns, null, null, null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            GBSyllabusUnit syllabusUnit = cursorToSyllabus(cursor);
//            listSyllabus.add(syllabusUnit);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return listSyllabus;
//    }
// --Commented out by Inspection STOP (10/20/2015 12:09 PM)

    public List<GBSyllabusUnit> getSyllabusByClass(long classID) {
        List<GBSyllabusUnit> listSyllabus = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.SYLLABUS_TABLE,
                mAllcolumns,
                DBHelper.SYLLABUS_COLUMN_CLASSID + " = ?",
                new String[]{String.valueOf(classID)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GBSyllabusUnit syllabusUnit = cursorToSyllabus(cursor);
            listSyllabus.add(syllabusUnit);
            cursor.moveToNext();
        }
        cursor.close();
        return listSyllabus;
    }


    public int updateSyllabus(GBSyllabusUnit syllabusUnit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.SYLLABUS_COLUMN_NAME, syllabusUnit.getName());
        values.put(DBHelper.SYLLABUS_COLUMN_WEIGHT, syllabusUnit.getWeight());
        values.put(DBHelper.SYLLABUS_COLUMN_GRADE, syllabusUnit.getGrade());
        values.put(DBHelper.SYLLABUS_COLUMN_EXTRA_CREDIT, syllabusUnit.getExtraCredit());
        values.put(DBHelper.SYLLABUS_COLUMN_CLASSID, syllabusUnit.getClassUnit().getId());
        return mDatabase.update(DBHelper.SYLLABUS_TABLE,
                values,
                DBHelper.SYLLABUS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(syllabusUnit.getId())});
    }


    public void deleteSyllabus(GBSyllabusUnit syllabusUnit) {
        long id = syllabusUnit.getId();

        GBGradeDAO gradeDAO = new GBGradeDAO(mContext);
        List<GBGradeUnit> listGradeUnit = gradeDAO.getGradeBySyllabus(id);
        if (listGradeUnit != null && !listGradeUnit.isEmpty()) {
            for (GBGradeUnit g : listGradeUnit) {
                gradeDAO.deleteGrade(g);
            }
        }


        mDatabase.delete(DBHelper.SYLLABUS_TABLE,
                DBHelper.CLASS_COLUMN_ID + " = " + id,
                null);
    }


    private GBSyllabusUnit cursorToSyllabus(Cursor cursor) {
        GBSyllabusUnit syllabusUnit = new GBSyllabusUnit();
        syllabusUnit.setId(cursor.getLong(0));
        syllabusUnit.setName(cursor.getString(1));
        syllabusUnit.setWeight(cursor.getDouble(2));
        syllabusUnit.setGrade(cursor.getDouble(3));
        syllabusUnit.setExtraCredit(cursor.getInt(4));

        long classID = cursor.getLong(5);
        GBClassDAO classDAO = new GBClassDAO(mContext);
        GBClassUnit classUnit = classDAO.getClassByID(classID);
        if (classUnit != null) {
            syllabusUnit.setClassUnit(classUnit);
        }
        return syllabusUnit;
    }
}
