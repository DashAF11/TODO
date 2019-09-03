package com.example.to_do;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.provider.Contacts.SettingsColumns.KEY;


public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "TODO.db";

    public static final String TABLE_NAME = "categories";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CAT_NAME";
    public static final String COL_3 = "FAV";


    public static final String TABLE_NAME2 = "task";
    public static final String COL_1_task = "TASK_ID";
    public static final String COL_2_task = "TASK_NAME";
    public static final String COL_3_task = "TASK_DATE";
    public static final String COL_4_task = "TASK_TIME";
    public static final String COL_5_task = "TASK_12HR_TIME";
    public static final String COL_6_task = "TASK_STATUS";
    public static final String COL_7_task = "CAT_ID";

    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,CAT_NAME TEXT,FAV TEXT)");
        db.execSQL("create table " + TABLE_NAME2 + " (TASK_ID integer primary key autoincrement, TASK_NAME text not null, TASK_DATE date not null, TASK_TIME time not null, TASK_12HR_TIME time not null, TASK_STATUS text, CAT_ID integer,FOREIGN KEY (CAT_ID) REFERENCES categories(ID))");
        this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData(String cat_name)
    {
        String str="notfav";
        db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,cat_name);
        contentValues.put(COL_3,str);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public  Cursor time_exceed()
    {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");         //date
        String date = df.format(Calendar.getInstance().getTime());

        DateFormat tf = new SimpleDateFormat("HH:mm");            //time
        String time = tf.format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" select * from "+TABLE_NAME2+" where date(substr("+COL_3_task+",7)||substr("+COL_3_task+",4,2)||substr("+COL_3_task+",1,2)) < date(substr('"+date+"',7)||substr('"+date+"',4,2)||substr('"+date+"',1,2)) or (date(substr("+COL_3_task+",7)||substr("+COL_3_task+",4,2)||substr("+COL_3_task+",1,2)) = date(substr('"+date+"',7)||substr('"+date+"',4,2)||substr('"+date+"',1,2))) and "+COL_4_task+" <= '"+time+"' order by "+COL_4_task+" and "+COL_4_task+" ",null);
        return res;
    }

    public  Cursor done_task()
    {
        String status="done";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" where "+COL_6_task+" = '"+status+"' ",null);
        return res;
    }

    public  Cursor catname(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_1+" = '"+id+"' ",null);
        return res;
    }


    public  Cursor fav_cat()
    {
        String status="fav";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_3+" = '"+status+"' ",null);
        return res;
    }

    public boolean insertDatatask(String task_name, String task_date, String task_time, String task_12hrTime, String task_status, String task_ref_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_task,task_name);
        contentValues.put(COL_3_task,task_date);
        contentValues.put(COL_4_task,task_time);
        contentValues.put(COL_5_task,task_12hrTime);
        contentValues.put(COL_6_task,task_status);
        contentValues.put(COL_7_task,task_ref_id);

        long result = db.insert(TABLE_NAME2,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public  Cursor search_categoriesname(String str)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_2+" like '%"+str+"%' ",null);
        return res;
    }

    public  Cursor search_taskname(String str)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" where "+COL_2_task+" like '%"+str+"%' ",null);
        return res;
    }


    public Cursor getAllcategories()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getRefcatID(String taskid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" where "+COL_1_task+" = '"+taskid+"' ",null);
        return res;
    }

    public Cursor getcategoriename(String catid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL_1+" = '"+catid+"' ",null);
        return res;
    }

    public Cursor getAllDatatask(String refid)
    {
        String str="notdone";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2+" where  "+COL_7_task+" = "+refid+" order by "+COL_6_task+" = null and "+COL_6_task+" = '"+str+"' " ,null);
        return res;
    }

    public Cursor getcatCount(String refid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT  COUNT(*) FROM " + TABLE_NAME2 +" where "+COL_7_task+" = "+refid+" ",null);
        return res;
    }

    public Cursor getcatCountpending(String refid)
    {
        String str="notdone";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT  COUNT(*) FROM " + TABLE_NAME2 +" where "+COL_7_task+" = "+refid+" and "+COL_6_task+" = '"+str+"' ",null);
        return res;
    }

    public boolean updateDatastatus(String status, String id)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6_task,status);
        db.execSQL("update "+TABLE_NAME2+" set "+COL_6_task+" = '"+status+"' where "+COL_1_task+"= "+id+" ");
        return true;
    }

    public boolean update_catname(String name, String id)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        db.execSQL("update "+TABLE_NAME+" set "+COL_2+" = '"+name+"' where "+COL_1+"= "+id+" ");
        return true;
    }

    public boolean update_editTask(String name, String date, String time, String time12hr, String id)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_task,name);
        contentValues.put(COL_3_task,date);
        contentValues.put(COL_4_task,time);
        contentValues.put(COL_5_task,time12hr);
        contentValues.put(COL_1,id);
        db.execSQL("update "+TABLE_NAME2+" set "+COL_2_task+" = '"+name+"', "+COL_3_task+" = '"+date+"', "+COL_4_task+" = '"+time+"',"+COL_5_task+" = '"+time12hr+"'  where "+COL_1_task+" = "+id+" ");
        return true;
    }

    public boolean update_checked_time_date( String date, String time, String id)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3_task,date);
        contentValues.put(COL_4_task,time);
        contentValues.put(COL_1,id);
        db.execSQL("update "+TABLE_NAME2+" set "+COL_3_task+" = '"+date+"',"+COL_4_task+" = '"+time+"' where "+COL_1_task+"= "+id+" ");
        return true;
    }

    public boolean update_cat_fav(String status, String id)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,status);
        db.execSQL("update "+TABLE_NAME+" set "+COL_3+" = '"+status+"' where "+COL_1+"= "+id+" ");
        return true;
    }

    public boolean delete_category (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" where "+COL_1+" = '"+id+"' ");
        return true;
    }

    public boolean deleteall_task (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME2+" where "+COL_7_task+" = '"+id+"' ");
        return true;
    }

    public boolean delete_task (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME2+" where "+COL_1_task+" = '"+id+"' ");
        return true;
    }
}