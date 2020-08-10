package com.blockone.inventorymanagement.InventoryManagement.controller.services;

import com.blockone.inventorymanagement.InventoryManagement.controller.validation.ProductConstraints;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
/* Service to handle creation, deletion and modification of products
 */
@Component
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductConstraints productConstraints;

    public Product getById(String id) {
        productConstraints.checkIdConstraint(id);
        return productDao.genericFindById(id);
    }

    public String addProduct(Product newProduct) {
        productConstraints.checkCreatedProductConstraints(newProduct);
        return productDao.genericSaveEntity(newProduct).getId();
    }

    public boolean editProduct(Product product) {
        getById(product.getId()); // see if product id exists
        productConstraints.checkEditedProductConstraints(product);
        productDao.genericSaveEntity(product);
        return true;
    }

    public boolean removeProduct(String productId) {
        getById(productId); // check if product id exists
        productDao.genericRemoveEntityById(productId);
        return true;
    }

    public List<Product> getAllProducts() {
        return productDao.genericFindAll();
    }
}
