package com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity;

import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ItemTypes;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
class PriceCalculatorServiceTest {
    @Mock
    private ProductDao productDao;

    @Mock
    private DiscountDao discountDao;

    @Mock
    private DiscountApplierService discountApplierService;

    @InjectMocks
    private PriceCalculatorService priceCalculatorService;

    Product p1, p2, p3, p4;
    Discount discount1, discount2;
    Map<String, Integer> prerequisiteItems1, prerequisiteItems2;
    Bucket bucket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        p1 = new Product("P1", "P1", 100, 24.0);
        p2 = new Product("P2", "P2", 100, 10.0);
        p3 = new Product("P3", "P3", 50, 50.0);
        p4 = new Product("P4", "P4", 40, 75.0);

        Mockito.when(productDao.genericFindById(p1.getId())).thenReturn(p1);
        Mockito.when(productDao.genericFindById(p2.getId())).thenReturn(p2);
        Mockito.when(productDao.genericFindById(p3.getId())).thenReturn(p3);
        Mockito.when(productDao.genericFindById(p4.getId())).thenReturn(p4);

        prerequisiteItems1 = new HashMap<>();
        prerequisiteItems1.put("P1", 4);
        prerequisiteItems1.put("P2", 3);
        //discounted value is mocked
        discount1 = new Discount("D1", prerequisiteItems1, null);

        prerequisiteItems2 = new HashMap<>();
        prerequisiteItems2.put("P3", 10);
        prerequisiteItems2.put("P4", 7);

        //discounted value is mocked
        discount2 = new Discount("D2", prerequisiteItems2, null);

        Mockito.when(discountDao.genericFindById(discount1.getId())).thenReturn(discount1);
        Mockito.when(discountDao.genericFindById(discount2.getId())).thenReturn(discount2);

        Mockito.when(discountApplierService.calculateDiscountReduction(discount1)).thenReturn(24*2*0.3 + 10);
        Mockito.when(discountApplierService.calculateDiscountReduction(discount2)).thenReturn(50*5*0.5 + 75*2*0.7);

        bucket = new Bucket("B1", new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    @Test
    void calculatePriceForEmptyBucket() {
        Assertions.assertEquals( 0.0, priceCalculatorService.calculatePrice(bucket), 0.0001);
    }

    @Test
    void calculatePriceForNoDiscountsBucket() {
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>(prerequisiteItems1));
        Assertions.assertEquals(126.0, priceCalculatorService.calculatePrice(bucket),  0.0001);
    }

    @Test
    void calculatePriceForOnlyDiscountedItemsBucket() {
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(prerequisiteItems1));
        bucket.getDiscountCoupons().add("D1");
        Assertions.assertEquals(101.6, priceCalculatorService.calculatePrice(bucket),  0.0001);
    }

    @Test
    void calculatePriceForSomeDiscountedItemsBucket() {
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>(prerequisiteItems2));
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(prerequisiteItems1));
        bucket.getDiscountCoupons().add("D1");
        Assertions.assertEquals(1126.6, priceCalculatorService.calculatePrice(bucket),  0.0001);
    }

    @Test
    void calculatePriceForSomeDiscountedAndLeftoverItemsBucket() {
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>(prerequisiteItems1));
        bucket.getItems().get(ItemTypes.REGULAR).put("P3", 4);
        bucket.getItems().get(ItemTypes.REGULAR).put("P4", 2);
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(prerequisiteItems2));
        bucket.getDiscountCoupons().add("D2");
        Assertions.assertEquals( 1271.0, priceCalculatorService.calculatePrice(bucket), 0.0001);
    }

    @Test
    void calculatePriceForMultipleDiscountsOnBucket() {
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>(prerequisiteItems1));
        bucket.getItems().get(ItemTypes.REGULAR).put("P3", 4);
        bucket.getItems().get(ItemTypes.REGULAR).put("P4", 2);
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(prerequisiteItems2));
        for(Map.Entry<String, Integer> items : prerequisiteItems2.entrySet()) {
            bucket.getItems().get(ItemTypes.DISCOUNTED).put(items.getKey(), items.getValue());
        }
        bucket.getDiscountCoupons().add("D1");
        bucket.getDiscountCoupons().add("D2");
        Assertions.assertEquals( 1246.6, priceCalculatorService.calculatePrice(bucket), 0.0001);
    }
}