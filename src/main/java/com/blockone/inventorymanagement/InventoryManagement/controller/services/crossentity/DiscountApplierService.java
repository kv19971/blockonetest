package com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InvalidDiscountException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.DiscountSpec;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ItemTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
Service to apply discount to buckets
 */
@Component
public class DiscountApplierService {


    @Autowired
    private ProductDao productDao;

    private void addDiscountedProductsToBucket(Bucket bucket, Discount discount) {
        for(Map.Entry<String, Integer> item : discount.getPrerequisiteProducts().entrySet()) {
            int leftoverCount = bucket.getItems().get(ItemTypes.REGULAR).get(item.getKey()) - item.getValue();
            if(leftoverCount == 0) {
                bucket.getItems().get(ItemTypes.REGULAR).remove(item.getKey());
            } else {
                bucket.getItems().get(ItemTypes.REGULAR).put(item.getKey(), leftoverCount);
            }
            bucket.getItems().get(ItemTypes.DISCOUNTED).put(item.getKey(), item.getValue());
        }
    }

    public Bucket applyDiscount(Bucket bucket, Discount discount) throws InputException, DBException {
        checkIfDiscountCanBeApplied(bucket, discount);
        addDiscountedProductsToBucket(bucket, discount);
        bucket.getDiscountCoupons().add(discount.getId());
        return bucket;
    }

    public void checkIfDiscountCanBeApplied(Bucket bucket, Discount discount) throws InvalidDiscountException{
        Map.Entry<String, Integer> prereqItem = checkIfItemsMeetDiscountPrerequisites(bucket, discount, ItemTypes.REGULAR);
        if(prereqItem != null) {
            throw new InvalidDiscountException("Discount " + discount.getId() + " cannot be applied to bucket " + bucket.getId()
                    + " as bucket does not have enough " + prereqItem.getKey());
        }
    }

    private Map.Entry<String, Integer> checkIfItemsMeetDiscountPrerequisites(Bucket bucket, Discount discount, ItemTypes itemType) {
        for(Map.Entry<String, Integer> prereqItem : discount.getPrerequisiteProducts().entrySet()) {
            if(! (bucket.getItems().get(itemType).containsKey(prereqItem.getKey())
                    && bucket.getItems().get(itemType).get(prereqItem.getKey()) >= prereqItem.getValue())) {
                return prereqItem;
            }
        }
        return null;
    }

    public void verifyDiscountValid(Bucket bucket, Discount discount) throws InvalidDiscountException {
        Map.Entry<String, Integer> prereqItem = checkIfItemsMeetDiscountPrerequisites(bucket, discount, ItemTypes.DISCOUNTED);
        if(prereqItem != null) {
            throw new InvalidDiscountException("Discount " + discount.getId() + " cannot be applied to bucket " + bucket.getId()
                + " as bucket does not have enough " + prereqItem.getKey());
        }
    }

    public double calculateDiscountReduction(Discount discount) throws InputException, DBException {
        double reduction = 0.0;
        for(DiscountSpec discountSpec: discount.getDiscountApplied()) {
            reduction += productDao.genericFindById(discountSpec.getProductId()).getPrice() * discountSpec.getQty() * discountSpec.getDiscountFactor();
        }
        return reduction;
    }
}
