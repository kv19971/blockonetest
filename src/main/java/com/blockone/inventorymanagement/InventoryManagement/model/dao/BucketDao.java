package com.blockone.inventorymanagement.InventoryManagement.model.dao;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.PriceCalculatorService;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.BucketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/* Data access layer for bucket
 */
@Component
public class BucketDao extends ImDao<Bucket> {
    @Autowired
    private BucketRepository bucketRepository;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private PriceCalculatorService priceCalculatorService;

    Logger logger = LoggerFactory.getLogger(BucketDao.class);

    /* checkout bucket: calculate price, remove items from inventory then return price
    this is all done as an atomic db operation (@Transactional)

     */
    @Transactional
    public double checkout(Bucket bucket) {
        double price = priceCalculatorService.calculatePrice(bucket);
        for(Map<String, Integer> items : bucket.getItems().values()) {
            for(Map.Entry<String, Integer> item : items.entrySet()) {
                productDao.checkoutProductQty(item.getKey(), item.getValue());
            }
        }
        genericRemoveEntityById(bucket.getId());
        return price;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected MongoRepository getRepository() {
        return bucketRepository;
    }

    @Override
    protected String getEntityName() {
        return "Bucket";
    }
}
