package com.blockone.inventorymanagement.InventoryManagement.model.dto;

public enum ItemTypes {
    REGULAR("REGULAR"), DISCOUNTED("DISCOUNTED");

    private String name;

    private ItemTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ItemTypes getByName(String name){
        return ItemTypes.valueOf(name);
    }
}
