package com.example.andrius.kurjeriuapp.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrius on 2016-03-11.
 */
public class Couriers {

    private List<Courier> couriers=new ArrayList<>();

    public Couriers(){}

    public Couriers(List<Courier> couriers){
        this.couriers=couriers;
    }

    public Courier get(int i){return couriers.get(i);}

    public List<Courier> getList(){return couriers;}

    public void add(Courier courier){
        couriers.add(courier);
    }

    public boolean isEmpty(){
        return couriers.isEmpty();
    }

    public boolean contains(Courier courier){
        return couriers.contains(courier);
    }
}
