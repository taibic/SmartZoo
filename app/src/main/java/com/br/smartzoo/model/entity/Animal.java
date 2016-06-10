package com.br.smartzoo.model.entity;


import com.br.smartzoo.R;
import com.br.smartzoo.model.business.NewsFeedBusiness;
import com.br.smartzoo.model.environment.Clock;
import com.br.smartzoo.model.interfaces.Observer;
import com.br.smartzoo.util.ApplicationUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by taibic on 12/04/16.
 */
public class Animal implements Observer {

    private int tick = 0;

    private int image;
    private String status;
    private Long id;
    private String name;
    private String type;
    private Integer age;
    private Double weight;
    private String sex;
    private Cage cage;
    private boolean isHealthy;
    private Double foodToBeSatisfied;
    private Integer resistance;
    private Integer popularity;
    private Double price;
    private Integer staminaToBeCured;



    public Animal(int image, String type, Integer age,Double price, Double weight
            , Cage cage, Integer resistance,  boolean isHealthy) {
        this.type = type;
        this.image= image;
        this.age = age;
        this.weight = weight;
        this.price = price;
        this.cage = cage;
        this.cage.getAnimals().add(this);
        this.isHealthy = isHealthy;
        this.resistance = resistance;
        foodToBeSatisfied = weight*0.15;
    }

    public Animal(){

    }


    public Cage getCage() {
        return cage;
    }

    public void setCage(Cage cage) {
        this.cage = cage;
        this.cage.getAnimals().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
        foodToBeSatisfied = weight*0.15;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setIsHealthy(boolean isHealthy) {
        this.isHealthy = isHealthy;
    }


    public Double getFoodToBeSatisfied() {
        return foodToBeSatisfied;
    }

    public void setFoodToBeSatisfied(Double foodToBeSatisfied) {
        this.foodToBeSatisfied = foodToBeSatisfied;
    }

    public Integer getResistance() {
        return resistance;
    }

    public void setResistance(Integer resistance) {
        this.resistance = resistance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(Double price) {
        //Calculate the price based on animal age and health status. If the animal is a newborn, the price is equal the default price offered by the enum
        price -= (price*age*0.02);
        if(!isHealthy){
            price = price*0.5;
        }

        this.price = price;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public void  eat() {
        Double foodEaten = 0.0;
        final List<Food> cageFoods = cage.getFoods();
        List<Food> foodsToRemove = new ArrayList<>();


        if(cageFoods.isEmpty()){
            status = ApplicationUtil.applicationContext.getString(R.string.starving);
            Thread starving = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (cageFoods.isEmpty()) {
                            try {
                                Thread.sleep(Clock.starvingTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            setWeight(weight*0.99);
                            status = ApplicationUtil.applicationContext.getString(R.string.losing_weight);
                        }
                        eat();
                    }
                });
                 starving.start();
        }
        else {
            status = ApplicationUtil.applicationContext.getString(R.string.eating);
            //SE O ANIMAL COMEÇAR A COMER O FOOD E FICAR SATISFEITO ENQUANTO ESTIVER COMENDO, ELE COME O FOOD ATÉ O FIM
            for (Food food : cageFoods) {
                if (foodEaten < foodToBeSatisfied) {
                    foodEaten += food.getWeight();
                    foodsToRemove.add(food);


                    //Checa validade da comida e se estiver estragada o animal tem chance de ficar doente
                    Calendar expirationDate = Calendar.getInstance();
                    expirationDate.setTime(food.getExpirationDate());
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) + 1);
                    if (currentDate.after(expirationDate)) {
                        Random random = new Random();
                        int i = random.nextInt(8) + 1;
                        if (this.isHealthy) {
                            if (i > resistance) {
                                isHealthy = false;
                                NewsFeedBusiness.addNew(New.TagEnum.ANIMAL_SICK.getTag(),this);
                            }
                        }
                    }

                } else {
                    break;
                }
            }


            cageFoods.removeAll(foodsToRemove);


            weight = weight + foodEaten;;


            status = ApplicationUtil.applicationContext.getString(R.string.digesting);

            //Digerindo
            tick = 0;
            while(tick< Clock.timeToDigest){
                    if(tick% Clock.digestingInterval==0)
                    weight = weight - foodEaten / 30;

            }
            setWeight(weight - foodEaten*0.95);
            afterDigest();

                }


    }

    private void afterDigest() {
        cage.setDirtyFactor(cage.getDirtyFactor()+1);
        status = ApplicationUtil.applicationContext.getString(R.string.digestion_finished);

        //Tempo para sentir fome novamente
        tick = 0;
        while(tick< Clock.timeToFeelHungry){
            status = ApplicationUtil.applicationContext.getString(R.string.not_hungry);
        }

        status = ApplicationUtil.applicationContext.getString(R.string.starving);



    }


    public Integer getStaminaToBeCured() {
        return staminaToBeCured;
    }

    public void setStaminaToBeCured(Integer staminaToBeCured) {
        this.staminaToBeCured = staminaToBeCured;
    }

    @Override
    public void onTick() {
        tick++;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }




    public enum AnimalEnum {

        Lion(R.drawable.ic_animal_lion, R.string.animal_lion, 300.0, 3, 150, "Fine"),
        // Jaguar(R.string.animal_jaguar, 200.0, 2, 100),
        //Leopard(R.string.animal_leopard, 400.0, 4, 200),
        Tiger(R.drawable.ic_animal_tiger, R.string.animal_tiger, 500.0, 5, 250, "Fine"),
        //Boar(R.string.animal_boar, 200.0, 2, 100),
        //Macaw(R.string.animal_macaw, 400.0, 4, 200),
        //Snake(R.drawable.ic_snake_icon,R.string.animal_snake, 100.0, 1, 50),
        Turtle(R.drawable.ic_turtle, R.string.animal_turtle, 100.0, 1, 50, "Fine"),
        Goose(R.drawable.ic_animal_goose, R.string.animal_goose, 200.0, 2, 100, "Fine"),
        Monkey(R.drawable.ic_animal_monkey, R.string.animal_monkey, 600.0, 6, 300, "Fine"),
        Giraffe(R.drawable.ic_giraffe, R.string.animal_giraffe, 700.0, 7, 350, "Fine"),
        Elephant(R.drawable.ic_animal_elephant, R.string.animal_elephant, 700.0, 7, 350, "Fine"),
        // Spider(R.string.animal_spider, 300.0, 3, 150),
        //Parrot(R.string.animal_parrot, 200.0, 2, 100),
        //Toucan(R.string.animal_toucan, 200.0, 2, 100),
        Bear(R.drawable.ic_animal_bear, R.string.animal_bear, 400.0, 4, 200, "Fine");

        AnimalEnum(int image, int type, Double price, int popularity, int reputationToUnlock
                , String status) {
            this.type = type;
            this.image = image;
            this.price = price;
            this.popularity = popularity;
            this.reputationToUnlock = reputationToUnlock;
            this.status = status;
        }

        private String status;
        private int image;
        private int type;
        private Double price;
        private int popularity;
        private int reputationToUnlock;

        public Double getPrice() {
            return price;
        }

        public int getPopularity() {
            return popularity;
        }

        public int getType() {
            return this.type;
        }

        public int getImage() {
            return image;
        }

        public int getReputationToUnlock() {
            return reputationToUnlock;
        }

        public String getStatus() {
            return status;
        }

    }
}
