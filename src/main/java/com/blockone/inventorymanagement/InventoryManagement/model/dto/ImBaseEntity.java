package com.blockone.inventorymanagement.InventoryManagement.model.dto;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public abstract class ImBaseEntity implements Serializable {
    @Id
    protected String id;

    public String getId() {
        return id;
    }
}
