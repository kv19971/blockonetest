package com.blockone.inventorymanagement.InventoryManagement.exceptions.user;
/* Not enough products in inventory exception
 */
public class NotEnoughProductsException extends BadParametersException{
    public NotEnoughProductsException() {
        super("Sorry! Not enough products in inventory");
    }

    public NotEnoughProductsException(String msg) {
        super(msg);
    }
}