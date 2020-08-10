package com.blockone.inventorymanagement.InventoryManagement.controller.services;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.DiscountApplierService;
import com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity.PriceCalculatorService;
import com.blockone.inventorymanagement.InventoryManagement.controller.validation.BucketConstraints;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.NotEnoughProductsException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.BucketDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.ItemTypes;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@ActiveProfiles("test")
class BucketServiceTest {

    @Mock
    private BucketDao bucketDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private DiscountDao discountDao;

    @Mock
    private BucketConstraints bucketConstraints;

    @Mock
    private PriceCalculatorService priceCalculatorService;

    @Mock
    private DiscountApplierService discountApplierService;

    @InjectMocks
    private BucketService bucketService;

    Product p1, p2, p3, p4;

    Discount discount1, discount2;

    Bucket bucket;

    HashMap<String, Integer> prerequisiteItems1, prerequisiteItems2;

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

        discount1 = new Discount("D1", new HashMap<>(), new HashSet<>());

        prerequisiteItems2 = new HashMap<>();
        prerequisiteItems2.put("P3", 10);
        prerequisiteItems2.put("P4", 7);

        discount2 = new Discount("D2", new HashMap<>(), new HashSet<>());

        Mockito.when(discountDao.genericFindById(discount1.getId())).thenReturn(discount1);
        Mockito.when(discountDao.genericFindById(discount2.getId())).thenReturn(discount2);

        bucket = new Bucket("B1", new ArrayList<>(), new HashMap<>(), new HashMap<>());
        Mockito.when(bucketDao.genericFindById(bucket.getId())).thenReturn(bucket);
    }

    @Test
    void getBucketForNull() {
        Mockito.when(bucketDao.genericFindById(bucket.getId())).thenReturn(new Bucket(bucket.getId(), null, null, null));
        Bucket returnedBucket = bucketService.getBucket(bucket.getId());
        Assertions.assertNotEquals(null, returnedBucket.getDiscountCoupons());
        Assertions.assertNotEquals(null, returnedBucket.getItems());
        Assertions.assertNotEquals(null, returnedBucket.getItems().get(ItemTypes.REGULAR));
        Assertions.assertNotEquals(null, returnedBucket.getItems().get(ItemTypes.DISCOUNTED));
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkIdConstraint(bucket.getId());
        Mockito.verify(bucketDao, Mockito.times(1)).genericFindById(bucket.getId());
    }

    @Test
    void getBucketPopulated() {
        List<String> list = new ArrayList<>();
        list.add("test");
        HashMap<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("B", 1);
        Bucket populatedBucket = new Bucket("NB", list, map1, map2);
        Mockito.when(bucketDao.genericFindById(populatedBucket.getId())).thenReturn(populatedBucket);

        Bucket returnedBucket = bucketService.getBucket(populatedBucket.getId());
        Assertions.assertEquals(list, returnedBucket.getDiscountCoupons());
        Assertions.assertNotEquals(null, returnedBucket.getItems());
        Assertions.assertEquals(map1, returnedBucket.getItems().get(ItemTypes.REGULAR));
        Assertions.assertEquals(map2, returnedBucket.getItems().get(ItemTypes.DISCOUNTED));
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkIdConstraint(populatedBucket.getId());
        Mockito.verify(bucketDao, Mockito.times(1)).genericFindById(populatedBucket.getId());
    }

    @Test
    void calculatePrice() {
        bucketService.calculatePrice(bucket.getId());
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkIdConstraint(bucket.getId());
        Mockito.verify(bucketDao, Mockito.times(1)).genericFindById(bucket.getId());
        Mockito.verify(priceCalculatorService, Mockito.times(1)).calculatePrice(bucket);
    }

    @Test
    void checkout() {
        bucketService.checkout(bucket.getId());
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkIdConstraint(bucket.getId());
        Mockito.verify(bucketDao, Mockito.times(1)).genericFindById(bucket.getId());
        Mockito.verify(bucketDao, Mockito.times(1)).checkout(bucket);
    }

    @Test
    void applyDiscount() {
        bucketService.applyDiscount("B1", "D1");
        Mockito.verify(bucketDao, Mockito.times(1)).genericFindById(bucket.getId());
        Mockito.verify(discountDao, Mockito.times(1)).genericFindById(discount1.getId());
        Mockito.verify(discountApplierService, Mockito.times(1)).applyDiscount(bucket, discount1);
    }

    @Test
    void removeItemsFromBucketNoDiscountedItems() {
        List<String> toBeRemoved = new ArrayList<>(prerequisiteItems1.keySet());
        bucket.getItems().put(ItemTypes.REGULAR, prerequisiteItems1);
        Bucket expectedBucket = new Bucket(bucket.getId(), bucket.getDiscountCoupons(), bucket.getItems().get(ItemTypes.REGULAR), bucket.getItems().get(ItemTypes.REGULAR));
        Mockito.when(bucketDao.genericFindById(bucket.getId())).thenReturn(bucket);
        bucketService.removeItemsFromBucket(bucket.getId(), new ArrayList<>(prerequisiteItems1.keySet()));
        ArgumentCaptor<Bucket> bucketArgumentCaptor = ArgumentCaptor.forClass(Bucket.class);
        ArgumentCaptor<ArrayList> listArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkExistingItemConstraints(bucketArgumentCaptor.capture(), listArgumentCaptor.capture());
        Assertions.assertEquals(expectedBucket, bucketArgumentCaptor.getValue());
        Assertions.assertEquals(toBeRemoved, listArgumentCaptor.getValue());
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkExistingItemConstraints(bucket, toBeRemoved);
        Assertions.assertEquals( new HashMap<>(), bucket.getItems().get(ItemTypes.REGULAR));
    }

    @Test
    void removeItemsFromBucketAllDiscountedItems() {
        List<String> toBeRemoved = new ArrayList<>(prerequisiteItems1.keySet());
        bucket.getItems().put(ItemTypes.DISCOUNTED, prerequisiteItems1);
        Bucket expectedBucket = new Bucket(bucket.getId(), bucket.getDiscountCoupons(), bucket.getItems().get(ItemTypes.REGULAR), bucket.getItems().get(ItemTypes.REGULAR));
        Mockito.when(bucketDao.genericFindById(bucket.getId())).thenReturn(bucket);
        bucketService.removeItemsFromBucket(bucket.getId(), new ArrayList<>(prerequisiteItems1.keySet()));
        ArgumentCaptor<Bucket> bucketArgumentCaptor = ArgumentCaptor.forClass(Bucket.class);
        ArgumentCaptor<ArrayList> listArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkExistingItemConstraints(bucketArgumentCaptor.capture(), listArgumentCaptor.capture());
        Assertions.assertEquals(expectedBucket, bucketArgumentCaptor.getValue());
        Assertions.assertEquals(toBeRemoved, listArgumentCaptor.getValue());
        Assertions.assertEquals( new HashMap<>(), bucket.getItems().get(ItemTypes.REGULAR));
    }

    @Test
    void removeItemsFromBucketSomeItemsRegularAndDiscounted() {
        List<String> itemsToRemove = new ArrayList<>();
        itemsToRemove.add("P1");
        itemsToRemove.add("P3");
        bucket.getItems().put(ItemTypes.REGULAR, prerequisiteItems1);
        bucket.getItems().put(ItemTypes.DISCOUNTED, prerequisiteItems2);
        Bucket expectedBucket = new Bucket(bucket.getId(), bucket.getDiscountCoupons(), bucket.getItems().get(ItemTypes.REGULAR), bucket.getItems().get(ItemTypes.DISCOUNTED));
        Mockito.when(bucketDao.genericFindById(bucket.getId())).thenReturn(bucket);
        bucketService.removeItemsFromBucket(bucket.getId(), itemsToRemove);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkIdConstraint(bucket.getId());
        ArgumentCaptor<Bucket> bucketArgumentCaptor = ArgumentCaptor.forClass(Bucket.class);
        ArgumentCaptor<ArrayList> listArgumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkExistingItemConstraints(bucketArgumentCaptor.capture(), listArgumentCaptor.capture());
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkExistingItemConstraints(bucket, itemsToRemove);
        HashMap<String, Integer> remainingRegularItems = new HashMap<>();
        remainingRegularItems.put("P2", 3);
        HashMap<String, Integer> remainingDiscountedItems = new HashMap<>();
        remainingDiscountedItems.put("P4", 7);
        Assertions.assertEquals( remainingRegularItems, bucket.getItems().get(ItemTypes.REGULAR));
        Assertions.assertEquals(remainingDiscountedItems, bucket.getItems().get(ItemTypes.DISCOUNTED));
    }

    @Test
    void addItemsToBucketSingleItem() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 3);
        HashMap<String, Integer> itemsToAdd = new HashMap<>();
        itemsToAdd.put("P1", 4);
        bucketService.addItemsToBucket(bucket.getId(), itemsToAdd);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkAddedItemConstraints(itemsToAdd);
        HashMap<String, Integer> expected = new HashMap<>(itemsToAdd);
        expected.put("P1", 7);
        Assertions.assertEquals(expected, bucket.getItems().get(ItemTypes.REGULAR));
    }

    @Test
    void addItemsToBucketMultipleItemsNonExisting() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 3);
        bucket.getItems().get(ItemTypes.REGULAR).put("P2", 1);
        HashMap<String, Integer> itemsToAdd = new HashMap<>();
        itemsToAdd.put("P1", 4);
        itemsToAdd.put("P2", 10);
        itemsToAdd.put("P3", 5);
        bucketService.addItemsToBucket(bucket.getId(), itemsToAdd);
        Mockito.verify(bucketConstraints, Mockito.times(1)).checkAddedItemConstraints(itemsToAdd);
        HashMap<String, Integer> expected = new HashMap<>(itemsToAdd);
        expected.put("P1", 7);
        expected.put("P2", 11);
        expected.put("P3", 5);
        Assertions.assertEquals(expected, bucket.getItems().get(ItemTypes.REGULAR));
    }

    @Test
    void addItemsToBucketSingleItemNotEnoughQty() {
        bucket.getItems().get(ItemTypes.REGULAR).put("P1", 3);
        HashMap<String, Integer> itemsToAdd = new HashMap<>();
        itemsToAdd.put("P1", 110);
        Assertions.assertThrows(NotEnoughProductsException.class, () -> {
            bucketService.addItemsToBucket(bucket.getId(), itemsToAdd);
        });
    }
}