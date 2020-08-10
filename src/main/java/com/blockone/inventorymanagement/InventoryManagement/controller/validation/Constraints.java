package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import org.springframework.util.StringUtils;
/* Base validation class
 */
public abstract class Constraints{
    public static final double MARGIN = 0.0001;
    /* Common Id constraint as all entities have the same type of id
     */
    public boolean checkIdConstraint(String id) {
        return !StringUtils.isEmpty(id);
    }
}
