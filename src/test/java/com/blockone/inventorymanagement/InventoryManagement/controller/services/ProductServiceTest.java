package com.blockone.inventorymanagement.InventoryManagement.controller.services;

import com.blockone.inventorymanagement.InventoryManagement.controller.validation.ProductConstraints;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ProductServiceTest {
    @Mock
    private ProductConstraints productConstraints;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    Product productA;
    Product productB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productA = new Product("A", "A product", 100, 25.0);
        productB = new Product("B", "B product", 200, 40.0);
        Mockito.when(productDao.genericFindById(productA.getId())).thenReturn(productA);
        Mockito.when(productDao.genericFindById(productB.getId())).thenReturn(productB);
    }

    @Test
    void getById() {
        productService.getById(productA.getId());
        Mockito.verify(productConstraints, Mockito.times(1)).checkIdConstraint(productA.getId());
        Mockito.verify(productDao, Mockito.times(1)).genericFindById(productA.getId());
    }

    @Test
    void addProduct() {
        Product productWithNoId = new Product(null, "C product", 100, 25.0);
        Mockito.when(productDao.genericSaveEntity(productWithNoId)).thenReturn(productWithNoId);
        productService.addProduct(productWithNoId);
        Mockito.verify(productConstraints, Mockito.times(1)).checkCreatedProductConstraints(productWithNoId);
        Mockito.verify(productDao, Mockito.times(1)).genericSaveEntity(productWithNoId);
    }

    @Test
    void editProduct() {
        Mockito.when(productDao.genericFindById(productA.getId())).thenReturn(productA);
        Mockito.when(productDao.genericSaveEntity(productA)).thenReturn(productA);
        Product newA = productA;
        productA.setDescription("New desc");
        productService.editProduct(newA);
        Mockito.verify(productConstraints, Mockito.times(1)).checkEditedProductConstraints(productA);
        Mockito.verify(productDao, Mockito.times(1)).genericFindById(productA.getId());
        Mockito.verify(productDao, Mockito.times(1)).genericSaveEntity(productA);
    }

    @Test
    void getAllProducts() {
        productService.getAllProducts();
        Mockito.verify(productDao, Mockito.times(1)).genericFindAll();
    }

    @Test
    void removeProduct() {
        String productId = "test";
        productService.removeProduct(productId);
        Mockito.verify(productConstraints).checkIdConstraint("test");
        Mockito.verify(productDao).genericRemoveEntityById("test");
    }
}