package com.example.andrius.kurjeriuapp.Classes;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tauvai2
 */

public class Orders {

    private List<Order> orders = new LinkedList<Order>();
    private List<Integer> ids = new LinkedList<Integer>();

    public Orders() { }

    public Orders(List<Order> orders) {
        this.orders = orders;
        for (int i=0;i<orders.size();i++){
            ids.add(orders.get(i).getId());
        }
    }

    public Order get(int i) {
        return orders.get(i);
    }

    public List<Order> getList(){return orders;}

    public void add(Order order) {
        if(order.getId() < 0) {
            order.setId(generateID());
        }
        orders.add(order);
    }

    public void add(int i, Order order) {
        if(order.getId() < 0) {
            order.setId(generateID());
        }
        orders.add(i, order);
    }

    public int size(){return orders.size();}

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public boolean contains(Order order) {
        return orders.contains(order);
    }

    private int generateID() {
        if(ids.isEmpty()){
            return 0;
        }
        int lastID=ids.get(ids.size()-1);
        int newID=lastID+1;
        while(!containsId(newID)){
            newID++;
        }
        ids.add(newID);
        return newID;
    }

    private boolean containsId(int id) {
        return ids.contains(id);
    }

    public boolean removeAllCarriedOutOrders() {
        for (int i=0;i<orders.size();i++){
            if(orders.get(i).isCarriedOut()){
                orders.remove(i);
            }
        }
        return true;
    }
}
 
