package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "M2Expense.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_trip";
    private static final String TABLE_NAME2 = "my_expense";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "trip_name";
    private static final String COLUMN_DEPT = "trip_dept";
    private static final String COLUMN_DEST = "trip_dest";
    private static final String COLUMN_DATE_FROM = "trip_date_from";
    private static final String COLUMN_DATE_TO = "trip_date_to";
    private static final String COLUMN_RISK = "trip_risk";
    private static final String COLUMN_DESC = "trip_desc";
    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DEPT + " TEXT, " +
                        COLUMN_DEST + " TEXT, " +
                        COLUMN_DATE_FROM + " TEXT, " +
                        COLUMN_DATE_TO + " TEXT, " +
                        COLUMN_RISK + " TEXT, " +
                        COLUMN_DESC + " TEXT);";
        String query2 =
                "CREATE TABLE " + TABLE_NAME2 +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,type TEXT,amount TEXT,time TEXT,address TEXT,cmt TEXT,trip_id INTEGER)";

        db.execSQL(query);
        db.execSQL(query2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }
    void addTrip(String name,String dept, String dest, String date_from,String date_to, String risk, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_DEPT,dept);
        cv.put(COLUMN_DEST,dest);
        cv.put(COLUMN_DATE_FROM,date_from);
        cv.put(COLUMN_DATE_TO,date_to);
        cv.put(COLUMN_RISK,risk);
        cv.put(COLUMN_DESC,desc);
        long result = db.insert(TABLE_NAME,null,cv);
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    void updateData(int row_id,String name,String dept,String dest,String datefrom,String dateto,String risk,String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_DEPT,dept);
        cv.put(COLUMN_DEST,dest);
        cv.put(COLUMN_DATE_FROM,datefrom);
        cv.put(COLUMN_DATE_TO,dateto);
        cv.put(COLUMN_RISK,risk);
        cv.put(COLUMN_DESC,desc);
        long result = db.update(TABLE_NAME,cv,COLUMN_ID + "=" + row_id,null);
    }

    void deleteOneRow(int row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result =db.delete(TABLE_NAME,"_id=?",new String[]{String.valueOf(row_id)});
    }
    void deleteExpense(int row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result =db.delete(TABLE_NAME2,"id=?",new String[]{String.valueOf(row_id)});
    }
    void updateExpense(int row_id,String name,String amount,String date,String address,String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type",name);
        cv.put("amount",amount);
        cv.put("time",date);
        cv.put("address",address);
        cv.put("cmt",desc);

        long result = db.update(TABLE_NAME2,cv, "id=" + row_id,null);
    }
    void addExpense(String type, String amount, String time,String address, String cmt, int trip_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type",type);
        cv.put("amount",amount);
        cv.put("time",time);
        cv.put("address",address);
        cv.put("cmt",cmt);
        cv.put("trip_id",trip_id);
        long result = db.insert(TABLE_NAME2,null,cv);

    }
    Cursor getExpenseByTripId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME2+" where trip_id = ?", new String[] {String.valueOf(id)});
        return cursor;

    }
    int getSumAmountOfExpenseByTrip() {
        String query = "select sum(amount) from my_expense WHERE trip_id in (SELECT _id FROM my_trip where substr(trip_date_from,4,2) == strftime('%m', 'now'))\n";
        SQLiteDatabase db = this.getWritableDatabase();
        int sum = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        return sum;
    }
    int getSumAmountOfToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());

        String query = "SELECT sum(amount) FROM my_expense WHERE time = '"+date+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        int sum = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        return sum;
    }
}
