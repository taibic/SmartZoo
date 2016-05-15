package com.br.smartzoo.model.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import com.br.smartzoo.model.entity.Veterinary;
import com.br.smartzoo.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Douglas on 5/10/2016.
 */
public class VeterinaryContract {

    public static String TABLE = "veterinary";
    public static String ID =  "id";
    public static String CREDENTIAL = "credential";
    public static String ANIMALSTABLE = "animalsTreatedByVet";
    public static String ANIMALID = "animalId";
    public static String VETERINARYID = "veterinaryId";
    public static String DATE = "date";


    public static String[] columns = {ID,CREDENTIAL};



    public static String createTable(){
        StringBuilder table = new StringBuilder();

        table.append(" create table " + TABLE + " ( ");
        table.append(ID + " integer primary key, ");
        table.append(CREDENTIAL + " text not null, ");
        table.append(" ); ");

        return table.toString();
    }


    public static String createTableAnimalsTreatedByVet(){
        StringBuilder table = new StringBuilder();

        table.append(" create table " + ANIMALSTABLE + " ( " );
        table.append(ID + " integer primary key, ");
        table.append(ANIMALID + " integer not null, ");
        table.append(VETERINARYID + " integer not null, ");
        table.append(DATE + " text not null,");
        table.append("Foreign key ("+ANIMALID+") references " + AnimalContract.TABLE +"(" + AnimalContract.ID+")," );
        table.append("Foreign key ("+VETERINARYID+") references " + TABLE +"(" +ID+") ");

        return table.toString();
    }

    public static ContentValues createContentValues(Veterinary veterinary){
        ContentValues values = new ContentValues();

        values.put(ID, veterinary.getId());
        values.put(CREDENTIAL, veterinary.getCredential());

        return values;
    }



    public static Veterinary getVeterinary(Cursor cursor){
        Veterinary veterinary = new Veterinary();

        if(!cursor.isBeforeFirst() || cursor.moveToNext()){
            veterinary.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            veterinary.setCredential(cursor.getString(cursor.getColumnIndex(CREDENTIAL)));
            veterinary.setAge(cursor.getInt(cursor.getColumnIndex(EmployeeContract.AGE)));
            veterinary.setName(cursor.getString(cursor.getColumnIndex(EmployeeContract.NAME)));
            veterinary.setSalary(cursor.getDouble(cursor.getColumnIndex(EmployeeContract.SALARY)));

            Date startDate  = DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(EmployeeContract.STARTDATE)));
            Date endDate = DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(EmployeeContract.ENDDATE)));
            veterinary.setStartDate(startDate);
            veterinary.setEndDate(endDate);
            veterinary.setCpf(cursor.getString(cursor.getColumnIndex(EmployeeContract.CPF)));
        }

        return veterinary;
    }


    public static List<Veterinary> getVeterinaries(Cursor cursor){
        List<Veterinary> veterinaries = new ArrayList<>();

        while(cursor.moveToNext()){
            veterinaries.add(getVeterinary(cursor));
        }

        return veterinaries;
    }


    public static HashMap<Integer,Integer> getAnimalsCount(Cursor cursor){
        HashMap<Integer, Integer> animalsCount = new HashMap<>();

        while(cursor.moveToNext()){
            animalsCount.put(cursor.getInt(cursor.getColumnIndex("animalId")), cursor.getInt(cursor.getColumnIndex("count")));
        }

        return animalsCount;
    }

}