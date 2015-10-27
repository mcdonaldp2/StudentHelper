package com.helper.groupa.studenthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GBGradeDAO {

    public static final String TAG = "GradeDAO";
    private SQLiteDatabase mDatabase;
    private DBHelper mGradebook;
    private Context mContext;
    private String[] mAllColumns = {
            DBHelper.GRADE_COLUMN_ID,
            DBHelper.GRADE_COLUMN_NAME,
            DBHelper.GRADE_COLUMN_GRADE_EARNED,
            DBHelper.GRADE_COLUMN_GRADE_POSSIBLE,
            DBHelper.GRADE_COLUMN_EXTRA_CREDIT,
            DBHelper.GRADE_COLUMN_SYLLABUSID,
    };

    public GBGradeDAO(Context context) {
        this.mContext = context;
        mGradebook = new DBHelper(context);

        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLExeception on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mGradebook.getWritableDatabase();
    }

    public void close() {
        mGradebook.close();
    }

    public GBGradeUnit createGrade(GBGradeUnit gradeUnit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.GRADE_COLUMN_NAME, gradeUnit.getName());
        values.put(DBHelper.GRADE_COLUMN_GRADE_EARNED, gradeUnit.getPointsEarned());
        values.put(DBHelper.GRADE_COLUMN_GRADE_POSSIBLE, gradeUnit.getPointsPossible());
        values.put(DBHelper.GRADE_COLUMN_EXTRA_CREDIT, gradeUnit.getExtraCredit());
        values.put(DBHelper.GRADE_COLUMN_SYLLABUSID, gradeUnit.getGBSyllabusUnit().getId());
        long insertID = mDatabase.insert(DBHelper.GRADE_TABLE,
                null,
                values);
        Cursor cursor = mDatabase.query(DBHelper.GRADE_TABLE,
                mAllColumns,
                DBHelper.GRADE_COLUMN_ID + " = " + insertID,
                null, null, null, null);
        cursor.moveToFirst();
        GBGradeUnit newGBGradeUnit = cursorToGrade(cursor);
        cursor.close();
        return newGBGradeUnit;
    }

    public GBGradeUnit getGradeByID(long id) {
        Cursor cursor = mDatabase.query(DBHelper.GRADE_TABLE, mAllColumns,
                DBHelper.GRADE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursorToGrade(cursor);
    }

    public List<GBGradeUnit> getAllGrade() {
        List<GBGradeUnit> listGrade = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBHelper.GRADE_TABLE,
                mAllColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GBGradeUnit gradeUnit = cursorToGrade(cursor);
            listGrade.add(gradeUnit);
            cursor.moveToNext();
        }
        cursor.close();
        return listGrade;
    }

    public List<GBGradeUnit> getGradeBySyllabus(long syllabusID) {
        List<GBGradeUnit> listGrade = new ArrayList<>();
        Cursor cursor = mDatabase.query(DBHelper.GRADE_TABLE,
                mAllColumns,
                DBHelper.GRADE_COLUMN_SYLLABUSID + " = ?",
                new String[]{String.valueOf(syllabusID)},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GBGradeUnit gradeUnit = cursorToGrade(cursor);
            listGrade.add(gradeUnit);
            cursor.moveToNext();
        }
        cursor.close();
        return listGrade;
    }

    public int updateGrade(GBGradeUnit gradeUnit) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.GRADE_COLUMN_NAME, gradeUnit.getName());
        values.put(DBHelper.GRADE_COLUMN_GRADE_EARNED, gradeUnit.getPointsEarned());
        values.put(DBHelper.GRADE_COLUMN_GRADE_POSSIBLE, gradeUnit.getPointsPossible());
        values.put(DBHelper.GRADE_COLUMN_EXTRA_CREDIT, gradeUnit.getExtraCredit());
        values.put(DBHelper.GRADE_COLUMN_SYLLABUSID, gradeUnit.getGBSyllabusUnit().getId());
        return mDatabase.update(DBHelper.GRADE_TABLE,
                values,
                DBHelper.GRADE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(gradeUnit.getId())});
    }

    public void deleteGrade(GBGradeUnit gradeUnit) {
        long id = gradeUnit.getId();
        mDatabase.delete(DBHelper.GRADE_TABLE,
                DBHelper.SYLLABUS_COLUMN_ID + " = " + id,
                null);
    }

    private GBGradeUnit cursorToGrade(Cursor cursor) {
        GBGradeUnit gradeUnit = new GBGradeUnit();
        gradeUnit.setId(cursor.getLong(0));
        gradeUnit.setName(cursor.getString(1));
        gradeUnit.setPointsEarned(cursor.getDouble(2));
        gradeUnit.setPointsPossible(cursor.getDouble(3));
        gradeUnit.setExtraCredit(cursor.getInt(4));

        long syllabusID = cursor.getLong(5);
        GBSyllabusDAO syllabusDAO = new GBSyllabusDAO(mContext);
        GBSyllabusUnit syllabusUnit = syllabusDAO.getSyllabusByID(syllabusID);
        if (syllabusUnit != null) {
            gradeUnit.setGBSyllabusUnit(syllabusUnit);
        }
        return gradeUnit;
    }
}
