package com.example.andrius.kurjeriuapp.Classes;

/**
 * Created by Andrius on 2016-03-11.
 */
public class Courier {

    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Orders orders;

    public Courier(String username, String password, String firstName, String lastName){
        this.username=username;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Courier(int id, String username, String password, String firstName, String lastName){
        this.id=id;
        this.username=username;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Courier(int id, String username, String password, String firstName, String lastName, Orders orders){
        this.id=id;
        this.username=username;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
        this.orders=orders;
    }

    public int getId(){return id;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public Orders getOrders(){return orders;}

    public void setId(int id){
        this.id=id;
    }
}
