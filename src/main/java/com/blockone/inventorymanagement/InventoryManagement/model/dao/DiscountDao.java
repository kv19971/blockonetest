package com.blockone.inventorymanagement.InventoryManagement.model.dao;

import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/* discount dao layer
 */
@Component
public class DiscountDao extends ImDao<Discount> {
    @Autowired
    private DiscountRepository discountRepository;

    private Logger logger = LoggerFactory.getLogger(DiscountDao.class);

    @Override
    protected MongoRepository getRepository() {
        return discountRepository;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getEntityName() {
        return "Discount";
    }
}
