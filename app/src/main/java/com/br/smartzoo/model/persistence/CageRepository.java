package com.br.smartzoo.model.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.br.smartzoo.model.entity.jail.Cage;

import java.util.List;

/**
 * Created by dhb_s on 5/7/2016.
 */
public class CageRepository {

    public static void save(Cage cage){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = CageContract.getContentValues(cage);

        if(cage.getId()==null){
            db.insert(CageContract.TABLE,null,values);
        }
        else{
            String where = " id = " + cage.getId();

            db.update(CageContract.TABLE,values,where,null);
        }

        db.close();
        databaseHelper.close();

    }


    public static List<Cage> getAllCages(){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(CageContract.TABLE,CageContract.columns,null,null,null,null,CageContract.NAME);
        List<Cage> allCages = CageContract.getCages(cursor);

        return allCages;

    }


    public static Cage getCageById(Long id){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = " id = " + id;

        Cursor cursor = db.query(CageContract.TABLE,CageContract.columns,where,null,null,null,CageContract.NAME);

        Cage cageById = CageContract.getCage(cursor);
        return cageById;
    }

}