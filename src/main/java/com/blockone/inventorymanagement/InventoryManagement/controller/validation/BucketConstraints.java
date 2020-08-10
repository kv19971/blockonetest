package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/* Validation for bucket operations
 */
@Component
public class BucketConstraints extends Constraints{
    /* Check if existing items in bucket maps
     */
    public void checkExistingItemConstraints(Bucket bucket, List<String> products) throws InputException {
        if(null == products || products.isEmpty()) {
            throw new MalformedRequestException("List cannot be empty!");
        }
        for(String productId : products) {
            boolean inBucket = false;
            for(Map<String, Integer> items : bucket.getItems().values()) {
                if(items.containsKey(productId)) {
                    inBucket = true;
                }
            }
            if(!inBucket) {
                throw new BadParametersException("Product id" + productId+ " not found");
            }
        }
    }

    /* Check to add items
     */
    public void checkAddedItemConstraints(Map<String, Integer> products) throws InputException {
        if(null == products || products.isEmpty()) {
            throw new MalformedRequestException("Product dict cannot be empty");
        }
        for(Map.Entry<String, Integer> item : products.entrySet()) {
            if(item.getValue() <= 0) {
                throw new BadParametersException("Product qty cannot be < 0");
            }
        }
    }
}
