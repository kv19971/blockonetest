package com.blockone.inventorymanagement.InventoryManagement.exceptions.user;

/*General exception class for user-readable DB exceptions */
public class DBException extends RuntimeException {
    public static final String ERR_MSG = "Sorry! Something went wrong with our database. Please try again later";
    public DBException() {
        super(ERR_MSG);
    }
}
