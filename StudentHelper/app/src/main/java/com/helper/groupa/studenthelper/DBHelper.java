package com.helper.groupa.studenthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Matt on 9/17/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "StudentHelperDB2.db";
    private static final int DATABASE_VERSION = 16;

    public static final String SCHEDULE_TABLE_NAME = "schedule";
    public static final String SCHEDULE_COLUMN_ID = "_id";
    public static final String SCHEDULE_COLUMN_CLASS_NAME = "className";
    public static final String SCHEDULE_COLUMN_START_HOUR = "startHour";
    public static final String SCHEDULE_COLUMN_START_MINUTE = "startMinute";
    public static final String SCHEDULE_COLUMN_END_HOUR = "endHour";
    public static final String SCHEDULE_COLUMN_END_MINUTE = "endMin";
    public static final String SCHEDULE_COLUMN_DAYS = "days";


    // Agenda Table Strings
    public static final String AGENDA_TABLE_NAME = "agenda";
    public static final String AGENDA_COLUMN_ID = "_id";
    public static final String AGENDA_COLUMN_CLASS_NAME = "className";
    public static final String AGENDA_COLUMN_ASSIGNMENT = "assignment";
    public static final String AGENDA_COLUMN_DUE_DATE = "dueDate";
    public static final String AGENDA_COLUMN_VISIBILITY_BOOL= "Visible";

    public static final String CREATE_TABLE_AGENDA = "CREATE TABLE " + AGENDA_TABLE_NAME + "("
            + AGENDA_COLUMN_ID + " INTEGER PRIMARY KEY, "
            + AGENDA_COLUMN_CLASS_NAME + " TEXT NOT NULL, "
            + AGENDA_COLUMN_ASSIGNMENT + " TEXT NOT NULL, "
            + AGENDA_COLUMN_DUE_DATE + " TEXT NOT NULL, "
            + AGENDA_COLUMN_VISIBILITY_BOOL + " INTEGER "
            +")";





    //GPA table strings
    //Semester Table Strings
    public static final String TABLE_SEMESTERS = "semesters";
    public static final String COLUMN_SEMESTER_TITLE = "SemesterTitle";
    public static final String COLUMN_SEMESTER_ID = "_id";
    public static final String COLUMN_SEMESTER_SET_GPA = "setGPA";
    public static final String COLUMN_SEMESTER_GPA = "GPA";

    //Course Table Strings
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = COLUMN_SEMESTER_ID;
    public static final String COLUMN_COURSE_TITLE = "courseTitle";
    public static final String COLUMN_COURSE_CREDIT_HOURS = "creditHoursInteger";
    public static final String COLUMN_COURSE_GRADE = "gradeInteger";
    public static final String COLUMN_COURSE_QUALITY_POINTS = "qualityPoints";
    public static final String COLUMN_COURSE_GRADE_DOUBLE = "gradeDouble";
    public static final String COLUMN_COURSE_SEMESTER_ID = "semester_id";

    //sql table creation strings
    public static final String CREATE_TABLE_SEMESTERS = "CREATE TABLE " + TABLE_SEMESTERS + "("
            + COLUMN_SEMESTER_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SEMESTER_TITLE + " TEXT NOT NULL, "
            + COLUMN_SEMESTER_SET_GPA + " TEXT, "
            + COLUMN_SEMESTER_GPA + " REAL "
            +")";

    public static final String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
            + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_COURSE_TITLE + " TEXT NOT NULL, "
            + COLUMN_COURSE_CREDIT_HOURS + " TEXT NOT NULL, "
            + COLUMN_COURSE_GRADE + " TEXT NOT NULL, "
            + COLUMN_COURSE_QUALITY_POINTS + " REAL NOT NULL, "
            + COLUMN_COURSE_GRADE_DOUBLE + " REAL NOT NULL, "
            + COLUMN_COURSE_SEMESTER_ID + " INTEGER NOT NULL "
            +")";

    public static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + SCHEDULE_TABLE_NAME + "(" +
            SCHEDULE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            SCHEDULE_COLUMN_CLASS_NAME + " TEXT, " +
            SCHEDULE_COLUMN_START_HOUR + " INTEGER, " +
            SCHEDULE_COLUMN_START_MINUTE + " INTEGER, " +
            SCHEDULE_COLUMN_END_HOUR + " INTEGER, " +
            SCHEDULE_COLUMN_END_MINUTE + " INTEGER, " +
            SCHEDULE_COLUMN_DAYS + " TEXT )";

    // Gradebook Tables
    // Class
    public static final String CLASS_TABLE = "classes";
    public static final String CLASS_COLUMN_ID = "_id";
    public static final String CLASS_COLUMN_NAME = "className";
    public static final String CLASS_COLUMN_GRADE = "classGrade";
    public static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE + "("
                    + CLASS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + CLASS_COLUMN_NAME + " TEXT, "
                    + CLASS_COLUMN_GRADE + " REAL" + ");";

    // Syllabus
    public static final String SYLLABUS_TABLE = "syllabi";
    public static final String SYLLABUS_COLUMN_ID = CLASS_COLUMN_ID;
    public static final String SYLLABUS_COLUMN_NAME = "syllabusName";
    public static final String SYLLABUS_COLUMN_WEIGHT = "syllabusWeight";
    public static final String SYLLABUS_COLUMN_GRADE = "syllabusGrade";
    public static final String SYLLABUS_COLUMN_EXTRA_CREDIT = "syllabusEC";
    public static final String SYLLABUS_COLUMN_CLASSID = "syllabus_id";
    public static final String CREATE_SYLLABUS_TABLE =
            "CREATE TABLE " + SYLLABUS_TABLE + "("
                    + SYLLABUS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + SYLLABUS_COLUMN_NAME + " TEXT, "
                    + SYLLABUS_COLUMN_WEIGHT + " REAL, "
                    + SYLLABUS_COLUMN_GRADE + " REAL, "
                    + SYLLABUS_COLUMN_EXTRA_CREDIT + " INT, "
                    + SYLLABUS_COLUMN_CLASSID + " INT" + ");";

    // Grade
    public static final String GRADE_TABLE = "assignments";
    public static final String GRADE_COLUMN_ID = SYLLABUS_COLUMN_ID;
    public static final String GRADE_COLUMN_NAME = "gradeName";
    public static final String GRADE_COLUMN_GRADE_EARNED = "gradeEarned";
    public static final String GRADE_COLUMN_GRADE_POSSIBLE = "gradePossible";
    public static final String GRADE_COLUMN_EXTRA_CREDIT = "gradeEC";
    public static final String GRADE_COLUMN_SYLLABUSID = "assignment_id";
    public static final String CREATE_ASSIGN_TABLE =
            "CREATE TABLE " + GRADE_TABLE + "("
                    + GRADE_COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + GRADE_COLUMN_NAME + " TEXT, "
                    + GRADE_COLUMN_GRADE_EARNED + " REAL, "
                    + GRADE_COLUMN_GRADE_POSSIBLE + " REAL, "
                    + GRADE_COLUMN_EXTRA_CREDIT + " INT, "
                    + GRADE_COLUMN_SYLLABUSID + " INT" + ");";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Second constructor
    // Used for Gradebook
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //creates gpa table
        db.execSQL(CREATE_TABLE_SEMESTERS);
        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_TABLE_AGENDA);
        db.execSQL(CREATE_SCHEDULE_TABLE);

        // Gradebook
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_SYLLABUS_TABLE);
        db.execSQL(CREATE_ASSIGN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + AGENDA_TABLE_NAME);

        // Gradebook
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SYLLABUS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GRADE_TABLE);
        onCreate(db);
    }

    public class Schedule {
        public boolean insertClassSchedule(String className, int startHour, int startMinute, int endHour, int endMinute, String days){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SCHEDULE_COLUMN_CLASS_NAME, className);
            contentValues.put(SCHEDULE_COLUMN_START_HOUR, startHour);
            contentValues.put(SCHEDULE_COLUMN_START_MINUTE, startMinute);
            contentValues.put(SCHEDULE_COLUMN_END_HOUR, endHour);
            contentValues.put(SCHEDULE_COLUMN_END_MINUTE, endMinute);
            contentValues.put(SCHEDULE_COLUMN_DAYS, days);
            db.insert(SCHEDULE_TABLE_NAME, null, contentValues);

            return true;
        }

        public boolean updateClassSchedule(Integer id, String className, int startHour, int startMinute, int endHour, int endMinute, String days){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SCHEDULE_COLUMN_CLASS_NAME, className);
            contentValues.put(SCHEDULE_COLUMN_START_HOUR, startHour);
            contentValues.put(SCHEDULE_COLUMN_START_MINUTE, startMinute);
            contentValues.put(SCHEDULE_COLUMN_END_HOUR, endHour);
            contentValues.put(SCHEDULE_COLUMN_END_MINUTE, endMinute);
            contentValues.put(SCHEDULE_COLUMN_DAYS, days);
            db.update(SCHEDULE_TABLE_NAME, contentValues, SCHEDULE_COLUMN_ID + "= ? ", new String[]{Integer.toString(id)});

            return true;
        }

        public Cursor getClassSchedule(int id) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor res = db.rawQuery( "SELECT * FROM " + SCHEDULE_TABLE_NAME + " WHERE " +
                    SCHEDULE_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
            return res;
        }
        public ArrayList<com.helper.groupa.studenthelper.Calendar.ClassSchedule> getAllClassSchedules() {
            ArrayList<com.helper.groupa.studenthelper.Calendar.ClassSchedule> allSchedules = new ArrayList<com.helper.groupa.studenthelper.Calendar.ClassSchedule>();

            SQLiteDatabase db = getReadableDatabase();
            Cursor res = db.rawQuery( "SELECT * FROM " + SCHEDULE_TABLE_NAME, null );

            if(res.moveToFirst()){
                do{
                    com.helper.groupa.studenthelper.Calendar.ClassSchedule classSchedule = new com.helper.groupa.studenthelper.Calendar.ClassSchedule(res.getInt(0),res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                            res.getInt(5),res.getString(6));
                    allSchedules.add(classSchedule);
                }while(res.moveToNext());
            }
            res.close();

            return allSchedules;
        }

        public void deleteClassSchedule(Integer id) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(SCHEDULE_TABLE_NAME,
                    SCHEDULE_COLUMN_ID + " = ? ",
                    new String[] { Integer.toString(id) });
        }

    }

    //**************************
   // AGENDA TABLE DB METHODS
   // **************************
    public boolean insertAssignment(String className, String assignment, String dueDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AGENDA_COLUMN_CLASS_NAME, className);
        contentValues.put(AGENDA_COLUMN_ASSIGNMENT, assignment);
        contentValues.put(AGENDA_COLUMN_DUE_DATE, dueDate);
        contentValues.put(AGENDA_COLUMN_VISIBILITY_BOOL, 1);
        db.insert(AGENDA_TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateAssignment(Integer id, String className, String assignment, String dueDate, Integer visibleBool){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AGENDA_COLUMN_CLASS_NAME, className);
        contentValues.put(AGENDA_COLUMN_ASSIGNMENT, assignment);
        contentValues.put(AGENDA_COLUMN_DUE_DATE, dueDate);
        contentValues.put(AGENDA_COLUMN_VISIBILITY_BOOL, visibleBool);
        db.update(AGENDA_TABLE_NAME, contentValues, AGENDA_COLUMN_ID + "= ? ", new String[]{Integer.toString(id)});

        return true;
    }




    public Cursor getAssignment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + AGENDA_TABLE_NAME + " WHERE " +
                AGENDA_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        res.moveToFirst();
        return res;
    }

    public Cursor getAllAssignments() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + AGENDA_TABLE_NAME, null );
        return res;
    }

    public Integer deleteAssignment(String classStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(AGENDA_TABLE_NAME,
                AGENDA_COLUMN_CLASS_NAME + " = ? ",
                new String[] {classStr});
    }

    public Integer deleteAssignmentById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(AGENDA_TABLE_NAME, AGENDA_COLUMN_ID + " = ?", new String[] {Integer.toString(id)} );
    }


 //GPA database methods
    public boolean insertSemester(String semesterTitle, String setGPA, double GPA) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SEMESTER_TITLE, semesterTitle);
        contentValues.put(COLUMN_SEMESTER_SET_GPA, setGPA);
        contentValues.put(COLUMN_SEMESTER_GPA, GPA);
        db.insert(TABLE_SEMESTERS, null, contentValues);

        return true;
    }

    public boolean updateSemester(int semesterID, String semesterTitle, String setGPA, double GPA) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SEMESTER_TITLE, semesterTitle);
        contentValues.put(COLUMN_SEMESTER_SET_GPA, setGPA);
        contentValues.put(COLUMN_SEMESTER_GPA, GPA);

        db.update(TABLE_SEMESTERS, contentValues, COLUMN_SEMESTER_ID + " = ? ",
                new String[]{"" + semesterID});

        return true;
    }

    public Cursor getAllSemesters() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_SEMESTERS, null );
        return res;
    }

    public boolean deleteSemesterString(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int semesterId = findID(title);
        db.delete(TABLE_SEMESTERS, COLUMN_SEMESTER_TITLE + " = ? ", new String[] {title});

        //deletes connected courses by looping through the cursor
        Cursor tempCursor = getAllSemesterCourses(semesterId);
        if (tempCursor != null && tempCursor.getCount() > 0){
            do{
                String titleCourse = tempCursor.getString(tempCursor.getColumnIndex(DBHelper.COLUMN_COURSE_TITLE));

                //deletes the course
                deleteCourseString(titleCourse);
                Log.d("DBHelper", "course deleted");
            }while(tempCursor.moveToNext());
        }
        tempCursor.close();

        Log.d("DBHelper", "Delete was run!");
        return true;
    }

    public boolean deleteCourseString(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSES, COLUMN_COURSE_TITLE + " = ? ", new String[]{title});
        Log.d("DBHelper", "Delete was run!");
        return true;
    }

    public boolean insertCourse(String courseTitle, int creditHours, String grade, double gradeDouble, double qualityPoints, int semester_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSE_TITLE, courseTitle);
        contentValues.put(COLUMN_COURSE_CREDIT_HOURS, creditHours);
        contentValues.put(COLUMN_COURSE_GRADE, grade);
        contentValues.put(COLUMN_COURSE_QUALITY_POINTS, qualityPoints);
        contentValues.put(COLUMN_COURSE_GRADE_DOUBLE, gradeDouble);
        contentValues.put(COLUMN_COURSE_SEMESTER_ID, semester_id);
        db.insert(TABLE_COURSES, null, contentValues);

        return true;
    }

    public boolean updateCourse(int courseID, String courseTitle, int creditHours, String grade, double gradeDouble, double qualityPoints, int semester_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSE_ID, courseID);
        contentValues.put(COLUMN_COURSE_TITLE, courseTitle);
        contentValues.put(COLUMN_COURSE_CREDIT_HOURS, creditHours);
        contentValues.put(COLUMN_COURSE_GRADE, grade);
        contentValues.put(COLUMN_COURSE_QUALITY_POINTS, qualityPoints);
        contentValues.put(COLUMN_COURSE_GRADE_DOUBLE, gradeDouble);
        contentValues.put(COLUMN_COURSE_SEMESTER_ID, semester_id);

        db.update(TABLE_COURSES, contentValues, COLUMN_SEMESTER_ID + " = ? ",
                new String[]{"" + courseID});

        return true;
    }


    public Cursor getAllSemesterCourses(int value) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = value;
        String[] fields = new String[] {
                COLUMN_COURSE_ID,
                COLUMN_COURSE_TITLE,
                COLUMN_COURSE_CREDIT_HOURS,
                COLUMN_COURSE_GRADE,
                //COLUMN_COURSE_SEMESTER_ID,
                COLUMN_COURSE_ID + " as _id"
        };

        Cursor cursor = db.query(TABLE_COURSES, fields,
                COLUMN_COURSE_SEMESTER_ID + " = ?",
                new String [] {
                        "" + id
                },
                null,
                null,
                COLUMN_COURSE_TITLE);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getSemesterCoursesDoubles (int values) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = values;
        String[] columns = new String[] {
                COLUMN_COURSE_ID,
                COLUMN_COURSE_CREDIT_HOURS,
                COLUMN_COURSE_QUALITY_POINTS,
                COLUMN_COURSE_ID + " as _id"
        };

        Cursor cursor = db.query(TABLE_COURSES, columns,
                COLUMN_COURSE_SEMESTER_ID + " = ?",
                new String [] {
                        "" + id
                },
                null,
                null,
                COLUMN_COURSE_QUALITY_POINTS);
        cursor.moveToFirst();
        return cursor;
    }

    public Integer findID(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_SEMESTER_ID + " FROM " + TABLE_SEMESTERS +
                " WHERE " + COLUMN_SEMESTER_TITLE + " = ?; ", new String[] {title});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }

        return id;
    }

    public boolean updateGPA(int id, double GPA) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SEMESTER_GPA, GPA);
        db.update(TABLE_SEMESTERS, contentValues, COLUMN_SEMESTER_ID + " = ? ", new String[] {Integer.toString(id)} );

        return true;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEMESTERS, null, null);
        db.delete(TABLE_COURSES, null, null);
        db.delete(AGENDA_TABLE_NAME, null, null);
    }

    public void deleteAllGPAData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEMESTERS, null, null);
        db.delete(TABLE_COURSES, null, null);
    }

    public boolean CheckDataAlreadyInDB(String TableName,
                                               String columnName, String valueToCheck) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + columnName + " = " + '"' + valueToCheck + '"';
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



}
