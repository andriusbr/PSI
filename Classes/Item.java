package com.example.andrius.kurjeriuapp.Classes;

/**
 * Created by Tautvydas on 2016-02-14.
 */
public class Item {

    private String name;
    private int units;
    private int id;

    public Item () {}

    public Item (String name, int units) {
        this.name = name;
        this.units = units;
    }

    public Item (String name, int units, int id) {
        this.name = name;
        this.units = units;
        this.id = id;
    }

    public void setValues (String name, int units, int id) {
        this.name = name;
        this.units = units;
        this.id = id;
    }

    public String getName () {
        return this.name;
    }

    public int getUnits () {
        return this.units;
    }

    public int getId () {
        return this.id;
    }

    public void setId(int id){
        this.id=id;
    }

}
