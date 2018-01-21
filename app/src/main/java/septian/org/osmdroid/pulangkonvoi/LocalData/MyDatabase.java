package septian.org.osmdroid.pulangkonvoi.LocalData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

import septian.org.osmdroid.pulangkonvoi.Kelas.Graph;
import septian.org.osmdroid.pulangkonvoi.Utility.Converter;

/**
 * Created by root on 1/19/18.
 */

public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "nodes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ID="id";
    private static final String NAME="name";
    private static final String DESCRIPTION="description";
    private static final String IMAGE="image";
    private static final String BENEFITS="benefits";
    private static final String POSES_TABLE="poses";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public int sum(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM node";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }

    public ArrayList<Graph> graphs(){
        ArrayList<Graph> graphs = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM node";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            Graph graph = new Graph();
            graph.setId_node(cursor.getInt(cursor.getColumnIndex("id_node")));
            graph.setStartlat(cursor.getDouble(cursor.getColumnIndex("startlat")));
            graph.setStartlong(cursor.getDouble(cursor.getColumnIndex("startlong")));
            graph.setEndlat(cursor.getDouble(cursor.getColumnIndex("endlat")));
            graph.setEndlong(cursor.getDouble(cursor.getColumnIndex("endlong")));
            graph.setHeuristik(cursor.getInt(cursor.getColumnIndex("heuristik")));

            graph.setUrutan_first(Converter.first_order(cursor.getString(cursor.getColumnIndex("urutan"))));
//            Log.d("asd", "graphs: "+cursor.getString(cursor.getColumnIndex("urutan")).split("-")[1]);
            graph.setUrutan_last(Converter.last_order(cursor.getString(cursor.getColumnIndex("urutan"))));
            graphs.add(graph);
        }
        return graphs;
    }

    /*public ArrayList<Poses> getPoses(){
        SQLiteDatabase db=getWritableDatabase();
        String[] columns={MyDatabase.ID,MyDatabase.NAME,MyDatabase.DESCRIPTION,MyDatabase.BENEFITS,MyDatabase.IMAGE};
//        String[] selectionArgs={categoryId+"",subjectId+"",yearId+""};
        Cursor cursor=db.query(MyDatabase.POSES_TABLE, columns, null, null, null, null, null);
//        Cursor cursor=db.query(MyDatabase.TABLE_NAME, columns, null,null, null, null, null);
        ArrayList<Poses> questionsArrayList=new ArrayList<>();

        while(cursor.moveToNext()){
            Poses questions=new Poses();
            questions.id=cursor.getInt(cursor.getColumnIndex(MyDatabase.ID));
            questions.name=cursor.getString(cursor.getColumnIndex(MyDatabase.NAME));
            questions.description=cursor.getString(cursor.getColumnIndex(MyDatabase.DESCRIPTION));
            questions.benefits=cursor.getString(cursor.getColumnIndex(MyDatabase.BENEFITS));
            questions.image=cursor.getString(cursor.getColumnIndex(MyDatabase.IMAGE));
            questionsArrayList.add(questions);
        }
        return questionsArrayList;
    }*/



}
