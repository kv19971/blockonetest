package com.blockone.inventorymanagement.InventoryManagement.exceptions.user;
/* Discount scheme applied is invalid exception
 */
public class InvalidDiscountException extends BadParametersException{
    public InvalidDiscountException() {
        super("Sorry! Discount coupon does not meet prerequisites");
    }

    public InvalidDiscountException(String msg) {
        super(msg);
    }
}
