package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
class BucketConstraintsTest {
    Bucket bucketToTest;

    @InjectMocks
    BucketConstraints bucketConstraints;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        HashMap<String, Integer> productsMap = new HashMap<>();
        productsMap.put("P1", 10);
        productsMap.put("P2", 5);

        HashMap<String, Integer> discountedProductsMap = new HashMap<>();
        productsMap.put("P3", 10);
        productsMap.put("P2", 2);

        bucketToTest = new Bucket("B1", new ArrayList<String>(), productsMap, discountedProductsMap);
    }

    @Test
    void checkExistingItemConstraintsForNullList() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            bucketConstraints.checkExistingItemConstraints(bucketToTest, null);
        });
    }

    @Test
    void checkExistingItemConstraintsForEmptyList() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            bucketConstraints.checkExistingItemConstraints(bucketToTest, new ArrayList<>());
        });
    }

    @Test
    void checkExistingItemConstraintsForValidList() {
        List<String> validList = new ArrayList<>();
        validList.add("P1");
        validList.add("P2");
        validList.add("P3");

        Assertions.assertDoesNotThrow(() -> {
            bucketConstraints.checkExistingItemConstraints(bucketToTest, validList);
        });
    }

    @Test
    void checkExistingItemConstraintsForValidListOfDiscountedProducts() {
        List<String> validList = new ArrayList<>();
        validList.add("P3");

        Assertions.assertDoesNotThrow(() -> {
            bucketConstraints.checkExistingItemConstraints(bucketToTest, validList);
        });
    }

    @Test
    void checkExistingItemConstraintsForInvalidList() {
        List<String> invalidList = new ArrayList<>();
        invalidList.add("P1");
        invalidList.add("P10");

        Assertions.assertThrows(BadParametersException.class, () -> {
            bucketConstraints.checkExistingItemConstraints(bucketToTest, invalidList);
        });
    }

    @Test
    void checkAddedItemConstraintsForNullMap() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            bucketConstraints.checkAddedItemConstraints(null);
        });
    }

    @Test
    void checkAddedItemConstraintsForEmptyMap() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            bucketConstraints.checkAddedItemConstraints(new HashMap<>());
        });
    }

    @Test
    void checkAddedItemConstraintsForValidMap() {
        Map<String, Integer> validMap = new HashMap<>();
        validMap.put("P5", 10);
        validMap.put("P6", 4);
        Assertions.assertDoesNotThrow( () -> {
            bucketConstraints.checkAddedItemConstraints(validMap);
        });
    }

    @Test
    void checkAddedItemConstraintsForInvalidMap() {
        Map<String, Integer> invalidMap = new HashMap<>();
        invalidMap.put("P5", 10);
        invalidMap.put("P6", -4);
        Assertions.assertThrows(BadParametersException.class, () -> {
            bucketConstraints.checkAddedItemConstraints(invalidMap);
        });
    }

    @Test
    void checkAddedItemConstraintsForInvalidMapZeroQty() {
        Map<String, Integer> invalidMap = new HashMap<>();
        invalidMap.put("P6", 0);
        Assertions.assertThrows(BadParametersException.class, () -> {
            bucketConstraints.checkAddedItemConstraints(invalidMap);
        });
    }
}