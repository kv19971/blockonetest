package com.blockone.inventorymanagement.InventoryManagement.controller.services;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.DiscountApplierService;
import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.PriceCalculatorService;
import com.blockone.inventorymanagement.InventoryManagement.controller.validation.BucketConstraints;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.NotEnoughProductsException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.BucketDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ItemTypes;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/*
Service to handle creation, deletion and modification of buckets
 */
@Component
public class BucketService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private BucketDao bucketDao;

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private BucketConstraints bucketConstraints;

    @Autowired
    private PriceCalculatorService priceCalculatorService;

    @Autowired
    private DiscountApplierService discountApplierService;


    public String getNewBucket() {
        Bucket bucket = new Bucket();
        Bucket savedBucket = bucketDao.genericSaveEntity(bucket);
        return savedBucket.getId();
    }

    private void initializeBucket(Bucket bucket) {
        if(bucket.getItems() == null) {
            bucket.setItems(new EnumMap<ItemTypes, Map<String, Integer>>(ItemTypes.class));
        }
        if(bucket.getDiscountCoupons() == null) {
            bucket.setDiscountCoupons(new ArrayList<>());
        }
        bucket.getItems().computeIfAbsent(ItemTypes.REGULAR, k -> new HashMap<>());
        bucket.getItems().computeIfAbsent(ItemTypes.DISCOUNTED, k -> new HashMap<>());
    }

    public Bucket getBucket(String bucketId) {
        bucketConstraints.checkIdConstraint(bucketId);
        Bucket bucket = bucketDao.genericFindById(bucketId);
        initializeBucket(bucket);
        return bucket;
    }

    public double calculatePrice(String bucketId) {
        Bucket bucket = getBucket(bucketId);
        double price = priceCalculatorService.calculatePrice(bucket);
        return price;
    }

    public double checkout(String bucketId) {
        return bucketDao.checkout(getBucket(bucketId));
    }

    public boolean applyDiscount(String bucketId, String discountId) throws InputException, DBException {
        Bucket bucket = getBucket(bucketId);
        Discount discount = discountDao.genericFindById(discountId);
        bucketDao.genericSaveEntity(discountApplierService.applyDiscount(bucket, discount));
        return true;
    }

    public boolean removeItemsFromBucket(String id, List<String> productIds) throws InputException, DBException {
        Bucket bucket = getBucket(id);
        bucketConstraints.checkExistingItemConstraints(bucket, productIds);
        for(String productId : productIds) {
            for(ItemTypes type : bucket.getItems().keySet()) {
                if(bucket.getItems().get(type).containsKey(productId)) {
                    bucket.getItems().get(type).remove(productId);
                }
            }
        }
        return true;
    }

    public boolean addItemsToBucket(String id, Map<String, Integer> moreItems) throws InputException, DBException {
        Bucket bucket = getBucket(id);
        bucketConstraints.checkAddedItemConstraints(moreItems);

        for(Map.Entry<String, Integer> newEntry : moreItems.entrySet()) {
            if(checkIfProductExists(newEntry.getKey(), newEntry.getValue() +
                    bucket.getItems().get(ItemTypes.REGULAR).getOrDefault(newEntry.getKey(), 0))) {
                bucket.getItems().get(ItemTypes.REGULAR).put(newEntry.getKey(),
                        bucket.getItems().get(ItemTypes.REGULAR).getOrDefault(newEntry.getKey(), 0) + newEntry.getValue());
            } else {
                throw new NotEnoughProductsException();
            }
        }
        bucketDao.genericSaveEntity(bucket);
        return true;
    }

    /* General sanity check to see if enough of product exists. This check will be done again at checkout (as atomic operation)
     */
    private boolean checkIfProductExists(String productId, int requiredQty) {
        Product product = productDao.genericFindById(productId);
        if(product.getQuantity() < requiredQty) {
            return false;
        } else {
            return true;
        }
    }
}
