package com.example.finder;

public class Item {

    public String item;
    public String key;

    public Item(){}

    public Item(String item,String key)
    {
        this.item = item;
        this.key = key;
    }

    public String getItem() {
        return item;
    }

    public String getKey() { return  key;}
}
