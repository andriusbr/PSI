package com.example.andrius.kurjeriuapp.Classes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tautvydas on 2016-02-14.
 */
public class Items {

    private List<Item> items = new ArrayList<Item>();
    private List<Integer> ids = new ArrayList<Integer>();

    public Items () {}

    public Items (List<Item> items) {
        this.items = items;
        for (int i=0;i<items.size();i++){
            ids.add(items.get(i).getId());
        }
    }

    public Item get (int i) {
        return items.get(i);
    }

    public void add (Item item) {
        item.setId(generateID());
        items.add(item);
    }

    public boolean isEmpty () {
        return items.isEmpty();
    }

    public boolean contains (Item item) {
        return items.contains(item);
    }

    public int generateID () {
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

    public int getSize(){return items.size();}

    public boolean containsId (int id) {
        return ids.contains(id);
    }
}
