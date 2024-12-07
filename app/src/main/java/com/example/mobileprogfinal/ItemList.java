package com.example.mobileprogfinal;

public class ItemList {
    private String name;
    private String quantity;

    public ItemList(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ItemList() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
