package com.blockone.inventorymanagement.InventoryManagement.dao;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.PriceCalculatorService;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.BucketDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ItemTypes;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.BucketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

class BucketDaoTest {
    @Mock
    private BucketRepository bucketRepository;

    @Mock
    private ProductDao productDao;

    @Mock
    private PriceCalculatorService priceCalculatorService;

    @InjectMocks
    private BucketDao bucketDao;

    Bucket bucket;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bucket = new Bucket("B1", new ArrayList<>(), null, null);
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>());
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>());
        Mockito.when(bucketRepository.findById(bucket.getId())).thenReturn(Optional.of(bucket));
    }

    @Test
    void checkoutSingleRegularItem() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 1);
        bucketDao.checkout(bucket);
        Mockito.verify(priceCalculatorService, Mockito.times(1)).calculatePrice(bucket);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P1", 1);
        Mockito.verify(bucketRepository, Mockito.times(1)).deleteById(bucket.getId());
    }

    @Test
    void checkoutManyRegularItems() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 1);
        bucket.getItems().get(ItemTypes.REGULAR).put("P2", 3);
        bucketDao.checkout(bucket);
        Mockito.verify(priceCalculatorService, Mockito.times(1)).calculatePrice(bucket);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P1", 1);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P2", 3);
        Mockito.verify(bucketRepository, Mockito.times(1)).deleteById(bucket.getId());
    }

    @Test
    void checkoutManyRegularAndDiscountedItems() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 1);
        bucket.getItems().get(ItemTypes.REGULAR).put("P2", 3);
        bucket.getItems().get(ItemTypes.DISCOUNTED).put("P3", 3);
        bucket.getItems().get(ItemTypes.DISCOUNTED).put("P4", 5);
        bucketDao.checkout(bucket);
        Mockito.verify(priceCalculatorService, Mockito.times(1)).calculatePrice(bucket);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P1", 1);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P2", 3);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P3", 3);
        Mockito.verify(productDao, Mockito.times(1)).checkoutProductQty("P4", 5);
        Mockito.verify(bucketRepository, Mockito.times(1)).deleteById(bucket.getId());
    }
}