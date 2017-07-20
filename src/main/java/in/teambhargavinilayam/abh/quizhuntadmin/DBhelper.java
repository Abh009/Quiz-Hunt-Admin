package in.teambhargavinilayam.abh.quizhuntadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ABH on 14-07-2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "databases.db";
    private static String TABLE_QUESTION = "QUESTIONS";

    private static String COL_ID = "ID";
    private static String COL_QUESTION = " QUEST ";
    private static String COL_OPTION_A = " OPTION_A ";
    private static String COL_OPTION_B = " OPTION_B ";
    private static String COL_OPTION_C = " OPTION_C ";
    private static String COL_OPTION_D = " OPTION_D ";
    private static String COL_RIGHT = " RIGHT_OPT ";

    private static String TABLE_USER_DETAILS = "user_details";
    private static String COL_USER_ID = "ID";
    private static String COL_USER_EMAIL = "USER_EMAIL";
    private static String COL_USER_NAME = "USER_NAME";
    private static String COL_USER_IMG_URL = "USER_IMG_URL";

    private static int DB_VERSION = 1;

    DBhelper(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_QUESTION + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_QUESTION + " TEXT UNIQUE NOT NULL,"
                + COL_OPTION_A + " TEXT NOT NULL,"
                + COL_OPTION_B + " TEXT NOT NULL,"
                + COL_OPTION_C + " TEXT NOT NULL,"
                + COL_OPTION_D + " TEXT NOT NULL,"
                + COL_RIGHT + " TEXT NOT NULL)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_USER_DETAILS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USER_NAME + " TEXT UNIQUE NOT NULL,"
                + COL_USER_EMAIL + " TEXT NOT NULL,"
                + COL_USER_IMG_URL + " TEXT NOT NULL)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_QUESTION;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + TABLE_USER_DETAILS;
        db.execSQL(query);
        DB_VERSION = newVersion;
        onCreate(db);

    }

    boolean InsertQuestion(String quest, String OptA, String OptB, String OptC, String OptD, String rightOpt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QUESTION, quest);
        cv.put(COL_OPTION_A, OptA);
        cv.put(COL_OPTION_B, OptB);
        cv.put(COL_OPTION_C, OptC);
        cv.put(COL_OPTION_D, OptD);
        cv.put(COL_RIGHT, rightOpt);

        /*
        String query = "INSERT INTO " + TABLE_QUESTION
                + "( " + COL_QUESTION + "," + COL_OPTION_A + "," + COL_OPTION_B + "," + COL_OPTION_C + "," + COL_OPTION_D
                + "," + COL_RIGHT + ") VALUES(" + quest + "," + OptA + "," + OptB + "," + OptC + "," + OptD + "," + rightOpt + ")";
        */
        long insertedRows = db.insert(TABLE_QUESTION,null,cv);

        if (insertedRows != -1 )
            return true;
        else
            return false;
    }

    boolean UpdateQuestion(String id ,String quest, String OptA, String OptB, String OptC, String OptD, String rightOpt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QUESTION, quest);
        cv.put(COL_OPTION_A, OptA);
        cv.put(COL_OPTION_B, OptB);
        cv.put(COL_OPTION_C, OptC);
        cv.put(COL_OPTION_D, OptD);
        cv.put(COL_RIGHT, rightOpt);

        /*
        String query = "INSERT INTO " + TABLE_QUESTION
                + "( " + COL_QUESTION + "," + COL_OPTION_A + "," + COL_OPTION_B + "," + COL_OPTION_C + "," + COL_OPTION_D
                + "," + COL_RIGHT + ") VALUES(" + quest + "," + OptA + "," + OptB + "," + OptC + "," + OptD + "," + rightOpt + ")";
        */
        long updatedRows = db.update(TABLE_QUESTION,cv," ID = " + id,null);

        if (updatedRows != -1 )
            return true;
        else
            return false;
    }
    Cursor ShowAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_QUESTION ;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
    Cursor ShowSpecificQuestion(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_QUESTION + " WHERE " + COL_ID + " = " + id;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    boolean DeleteSpecificQuestion(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_QUESTION, " ID = " + id, null);
        if (deletedRows != -1){
            return true;
        }
        return false;
    }

    int GetLastQuestionIndex(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sqlite_sequence";
        Cursor data = db.rawQuery(query,null);
        data.moveToFirst();
        if (data.getCount() == 0)
            return 0;
        return Integer.parseInt(data.getString(1));
    }

    Cursor ShowUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USER_DETAILS;
        return db.rawQuery(query,null);
    }

    boolean InsertUserData(String UserName,String Email, String imgUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor data = ShowUserData();
        if (data.getCount() == 1){

            cv.put(COL_USER_NAME, UserName);
            cv.put(COL_USER_EMAIL,Email);
            cv.put(COL_USER_IMG_URL,imgUrl);
            long inserted = db.update(TABLE_USER_DETAILS,cv," ID = 1" , null);
            if (inserted == -1){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            cv.put(COL_USER_NAME, UserName);
            cv.put(COL_USER_EMAIL, Email);
            cv.put(COL_USER_IMG_URL,imgUrl);
            long inserted = db.insert(TABLE_USER_DETAILS,null,cv);
            if (inserted == -1){
                return false;
            }
            else {
                return true;
            }

        }
    }
}
