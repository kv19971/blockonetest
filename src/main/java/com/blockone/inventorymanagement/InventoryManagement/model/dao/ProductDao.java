package com.blockone.inventorymanagement.InventoryManagement.model.dao;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.NotEnoughProductsException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/* product dao layer
 */
@Component
public class ProductDao extends ImDao<Product> {
    @Autowired
    private ProductRepository productRepository;

    private Logger logger = LoggerFactory.getLogger(ProductDao.class);
    /* checkout products - done as transaction
    read and then reduce product inventory
     */
    @Transactional
    public void checkoutProductQty(String productId, int qtyChange) throws InputException, DBException {
        Product product = genericFindById(productId);
        if(product.getQuantity() < qtyChange) {
            throw new NotEnoughProductsException();
        }
        product.setQuantity(product.getQuantity()-qtyChange);
        genericSaveEntity(product);
    }

    @Override
    protected MongoRepository<Product, String> getRepository() {
        return productRepository;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

}
