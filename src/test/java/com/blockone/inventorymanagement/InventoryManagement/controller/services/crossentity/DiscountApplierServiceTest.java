package com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InvalidDiscountException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@ActiveProfiles("test")
class DiscountApplierServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    @Spy
    private DiscountApplierService discountApplierService;

    Product p1, p2;
    Discount discount;
    Bucket bucket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, Integer> prerequisiteItems = new HashMap<>();
        prerequisiteItems.put("P1", 4);
        prerequisiteItems.put("P2", 3);

        Set<DiscountSpec> discountedItems = new HashSet<>();
        discountedItems.add(new DiscountSpec("P1", 2, 0.3));
        discountedItems.add(new DiscountSpec("P2", 1, 1.0));

        p1 = new Product("P1", "P1", 100, 24.0);
        p2 = new Product("P2", "P2", 100, 10.0);
        discount = new Discount("D1", prerequisiteItems, discountedItems);

        Mockito.when(productDao.genericFindById(p1.getId())).thenReturn(p1);
        Mockito.when(productDao.genericFindById(p2.getId())).thenReturn(p2);

        bucket = new Bucket("B1", new ArrayList<>(), new HashMap<>(), new HashMap<>());
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>(prerequisiteItems));
    }

    @Test
    void calculateDiscountOneItem() {
        discount.getDiscountApplied().remove(new DiscountSpec("P2", 1, 0));
        Assertions.assertEquals( 14.4, discountApplierService.calculateDiscountReduction(discount), 0.0001);
    }

    @Test
    void calculateDiscountOneFreeItem() {
        discount.getDiscountApplied().remove(new DiscountSpec("P1", 2, 0));
        Assertions.assertEquals(discountApplierService.calculateDiscountReduction(discount), 10.0, 0.0001);
    }

    @Test
    void calculateDiscountOnAllDiscounted() {
        discount.getDiscountApplied().remove(new DiscountSpec("P2", 1, 0.7)); //hashed using productid and qty
        discount.getDiscountApplied().add(new DiscountSpec("P2", 1, 0.7));
        Assertions.assertEquals(21.4, discountApplierService.calculateDiscountReduction(discount), 0.0001);
    }

    @Test
    void calculateDiscountOnDiscountedAndFree() {
        Assertions.assertEquals( 24.4,discountApplierService.calculateDiscountReduction(discount), 0.0001);
    }

    @Test
    void applyDiscountAllItemsInBucket() {
        Bucket changedBucket = discountApplierService.applyDiscount(bucket, discount);
        Mockito.verify(discountApplierService).checkIfDiscountCanBeApplied(changedBucket, discount);
        Assertions.assertEquals(0, changedBucket.getItems().get(ItemTypes.REGULAR).size());
        Assertions.assertEquals(discount.getPrerequisiteProducts(), changedBucket.getItems().get(ItemTypes.DISCOUNTED));
    }

    @Test
    void applyDiscountSomeItemsInBucket() {
        discount.getPrerequisiteProducts().remove("P2");
        discount.getDiscountApplied().remove(Pair.of("P2", 1));
        Bucket changedBucket = discountApplierService.applyDiscount(bucket, discount);
        Mockito.verify(discountApplierService).checkIfDiscountCanBeApplied(changedBucket, discount);
        HashMap<String, Integer> expectedMapOfLeftover = new HashMap<>();
        expectedMapOfLeftover.put("P2", 3);
        Assertions.assertEquals(expectedMapOfLeftover, changedBucket.getItems().get(ItemTypes.REGULAR));
        Assertions.assertEquals(discount.getPrerequisiteProducts(), changedBucket.getItems().get(ItemTypes.DISCOUNTED));
    }

    @Test
    void applyDiscountSomeItemsSomeQuantityLeftInBucket() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 10);
        Bucket changedBucket = discountApplierService.applyDiscount(bucket, discount);
        Mockito.verify(discountApplierService).checkIfDiscountCanBeApplied(changedBucket, discount);
        HashMap<String, Integer> expectedMapOfLeftover = new HashMap<>();
        expectedMapOfLeftover.put("P1", 6);
        Assertions.assertEquals(expectedMapOfLeftover, changedBucket.getItems().get(ItemTypes.REGULAR));
        Assertions.assertEquals(discount.getPrerequisiteProducts(), changedBucket.getItems().get(ItemTypes.DISCOUNTED));
    }

    @Test
    void checkIfDiscountCanBeApplied() {
        Assertions.assertDoesNotThrow(() -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void checkIfDiscountCanBeAppliedForPrerequisiteMissing() {
        bucket.getItems().get(ItemTypes.REGULAR).remove("P1");
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void checkIfDiscountCanBeAppliedForLowPrerequisiteQty() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 1);
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void checkIfDiscountCanBeAppliedForLowPrerequisiteQtyAndMissingPrerequisite() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 1);
        bucket.getItems().get(ItemTypes.REGULAR).remove("P2");
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void verifyDiscountValid() {
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(bucket.getItems().get(ItemTypes.REGULAR)));
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>());
        Assertions.assertDoesNotThrow(() -> {
            discountApplierService.verifyDiscountValid(bucket, discount);
        });
    }

    @Test
    void verifyDiscountValidForPrerequisiteMissing() {
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(bucket.getItems().get(ItemTypes.REGULAR)));
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>());
        bucket.getItems().get(ItemTypes.DISCOUNTED).remove("P1");
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void verifyDiscountValidForLowPrerequisiteQty() {
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(bucket.getItems().get(ItemTypes.REGULAR)));
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>());
        bucket.getItems().get(ItemTypes.DISCOUNTED).put("P1", 1);
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }

    @Test
    void verifyDiscountValidForLowPrerequisiteQtyAndMissingPrerequisite() {
        bucket.getItems().put(ItemTypes.DISCOUNTED, new HashMap<>(bucket.getItems().get(ItemTypes.REGULAR)));
        bucket.getItems().put(ItemTypes.REGULAR, new HashMap<>());
        bucket.getItems().get(ItemTypes.DISCOUNTED).put("P1", 1);
        bucket.getItems().get(ItemTypes.DISCOUNTED).remove("P2");
        Assertions.assertThrows(InvalidDiscountException.class, () -> {
            discountApplierService.checkIfDiscountCanBeApplied(bucket, discount);
        });
    }
}