package com.example.andrius.kurjeriuapp.Classes;
/**
 *
 * @author TauVai2
 */
public class Order {
    private int id;
    private String address;
    private Items items;
    private String personsFirstName;
    private String personsLastName;
    private double price;
    private String couriers_username;
    private boolean carriedOut;
    
    public Order() {}
    
    public Order(String address, Items items, String firstName, String lastName, String couriers_username)
    {
        id=-1;
        this.address = address;
        this.items = items;
        this.personsFirstName = firstName;
        this.personsLastName = lastName;
        this.couriers_username = couriers_username;
    }

    public Order(int id, String address, Items items, double price, String couriers_username)
    {
        this.id = id;
        this.address = address;
        this.price = price;
        this.items = items;
        this.couriers_username = couriers_username;
    }
    
    public Order(int id ,String address, Items items, double price, String firstName, String lastName)
    {
        this.id = id;
        this.address = address;
        this.items = items;
        this.price = price;
        this.personsFirstName = firstName;
        this.personsLastName = lastName;
    }
    
    public int getId() { return id;}
    public String getAddress() { return address;}
    public Items getItems() { return items;}
    public String getFirstName() { return personsFirstName;}
    public String getLastName() { return personsLastName;}
    public String getCourier(){return couriers_username;}
    public boolean isCarriedOut() { return carriedOut;}
    
    public void setId(int id){ this.id=id; }
}