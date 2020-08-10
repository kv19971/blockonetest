package com.blockone.inventorymanagement.InventoryManagement.dao;

import com.blockone.inventorymanagement.InventoryManagement.controller.validation.ProductConstraints;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.NotEnoughProductsException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

class ProductDaoTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConstraints productConstraints;

    @InjectMocks
    private ProductDao productDao;

    List<Product> allProducts;
    Product productA;
    Product productB;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productA = new Product("A", "Product A", 100, 24.0);
        Mockito.when(productRepository.findById("A")).thenReturn(java.util.Optional.ofNullable(productA));
    }

    @Test
    @Transactional
    void changeProductQty() {
        Product changedProduct = new Product(productA.getId(), productA.getDescription(), productA.getQuantity()-10, productA.getPrice());
        productDao.checkoutProductQty("A", 10);
        Mockito.verify(productRepository, Mockito.times(1)).save(changedProduct);
    }

    @Test
    @Transactional
    void changeProductQtyNotEnoughProducts() {
        Product changedProduct = new Product(productA.getId(), productA.getDescription(), productA.getQuantity()-110, productA.getPrice());
        Assertions.assertThrows(NotEnoughProductsException.class, () -> {
            productDao.checkoutProductQty("A", 110);
        });
    }
}