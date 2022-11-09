package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.todo.Model.ToDoModel;



public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String name="ToDoListDatabase";
    private static final String TODOTABLE="ToDo";
    private static final String ID="id";
    private static final String Task="task";
    private static final String Status="Status";
    private static final String CreateToDoTable="CREATE TABLE "+TODOTABLE+"("+ID+"Integer primary key autoincrement,"+
            Task+"Text, "+Status+" Integer)";
    private SQLiteDatabase db;
    public DatabaseHandler(Context context){
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CreateToDoTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TODOTABLE);
        onCreate(db);
    }
    public void OpenDatabase(){
        db=this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv=new ContentValues();
        cv.put(Task,task.getTask());
        cv.put(Status,0);
        db.insert(TODOTABLE,null,cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList=new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur= db.query(TODOTABLE,null,null,null,null,null,null,null);
            if(cur !=null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task=new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(Task)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(Status)));
                        taskList.add(task);
                    }while(cur.moveToNext());
                }

            }
        }finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;

    }
    public void UpdateStatus(int id,int status){
        ContentValues cv=new ContentValues();
        cv.put(Status,status);
        db.update(TODOTABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv=new ContentValues();
        cv.put(Task, task);
        db.update(TODOTABLE, cv, ID+"=?",new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODOTABLE,ID+"=?",new String[] {String.valueOf(id)});
    }


}
