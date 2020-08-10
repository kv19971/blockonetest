package com.blockone.inventorymanagement.InventoryManagement.exceptions.user;
/* Bad request exception
 */
public class MalformedRequestException extends InputException {
    public MalformedRequestException() {
        super("Sorry! Malformed request");
    }

    public MalformedRequestException(String msg) {
        super(msg);
    }
}
