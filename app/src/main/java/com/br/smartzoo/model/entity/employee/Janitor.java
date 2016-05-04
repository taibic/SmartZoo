package com.br.smartzoo.model.entity.employee;




import android.os.CountDownTimer;

import com.br.smartzoo.model.entity.jail.Cage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by adenilson on 18/04/16.
 */
public class Janitor extends Employee {

    private int timeToRest = 1000*60*30;
    private String status;

    private List<Cage> cages;
    private int expedient;
    private Timer tasks;

    public Janitor(List<Cage> cages, int expedient) {
        this.cages = cages;
        this.expedient = expedient;
        this.tasks = new Timer();
    }

    public Janitor(String name, Integer age, String cpf, String startDate, String endDate, Double salary, List<Cage> cages, int expedient) {
        super(name, age, cpf, startDate, endDate, salary);
        this.cages = cages;
        this.expedient = expedient;
        this.tasks = new Timer();
    }

    public Janitor(){
        this.tasks = new Timer();
    }


    @Override
    public Double calculateSalary() {
        return cages.isEmpty() && expedient == 0 ? super.getSalary() : super.getSalary() +(cages.size() * expedient);
    }

    public List<Cage> getCages() {
        return cages;
    }

    public void setCages(List<Cage> cages) {
        this.cages = cages;
    }

    public int getExpedient() {
        return expedient;
    }

    public void setExpedient(int expedient) {
        this.expedient = expedient;
    }
    
    public void clear(final Cage cage){
        long timeToCleanCage = cage.getDirtyFactor() * 30000;

        //Limpando jaula
        new CountDownTimer(30000,timeToCleanCage){
            int dirtyCleaned = 0;
            @Override
            public void onTick(long millisUntilFinished) {
               dirtyCleaned++;
            }

            @Override
            public void onFinish() {
                cage.setClean(true);
                status = "Descansando";
            }
        }.start();

    	if(!cages.contains(cage)){
    		cages.add(cage);
    	}

        //Descansando
        tasks.schedule(new TimerTask() {
            @Override
            public void run() {
              status = "Pronto";
            }
        },timeToRest);
    }
}