package com.example.test3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
        public final static String DATABASE_NAME = "KKW Alerts";
	    public final static String TABLE_NAME = "UserInfo";
	    public static final String COL_1 = "ID";
	    public static final String COL_2 = "year";
	    public static final String COL_3 = "division";
	   // public static final String COL_4 = "MARKS";

	    public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }

        	    @Override
	    public void onCreate(SQLiteDatabase db)
        {
        	 db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,year TEXT,division TEXT)");
        }

        @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        	 db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        	 onCreate(db);
        }

        public boolean insertData(String year, String division)
        {
        	        SQLiteDatabase db = this.getWritableDatabase();
        	        ContentValues cv = new ContentValues();
                    cv.put(COL_2, year);
        	        cv.put(COL_3, division);
        	       // cv.put(COL_4, marks);
        	        long result = db.insert(TABLE_NAME, null, cv);
        	        if (result == -1)
        	            return false;
        	        else
        	            return true;
        }

        public Cursor getData(String id)
        {
        	  SQLiteDatabase db = this.getWritableDatabase();
        	  String query="SELECT * FROM "+TABLE_NAME+" WHERE ID='"+id+"'";
        	  Cursor  cursor = db.rawQuery(query,null);
        	  return cursor;
        }

        public boolean updateData(String id, String name, String surname, String marks)
        {
        	        SQLiteDatabase db = this.getWritableDatabase();
        	        ContentValues contentValues = new ContentValues();
        	        contentValues.put(COL_1, id);
        	        contentValues.put(COL_2, name);
        	        contentValues.put(COL_3, surname);
        	       // contentValues.put(COL_4, marks);
        	        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{id});
        	        return true;
        }

        public Integer deleteData (String id)
        {
        	        SQLiteDatabase db = this.getWritableDatabase();
        	        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
        }

        public Cursor getAllData()
        {
        	        SQLiteDatabase db = this.getWritableDatabase();
        	        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        	        return res;
        }
}