package com.blockone.inventorymanagement.InventoryManagement.controller.rest;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.BucketService;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BucketController {

    @Autowired
    private BucketService bucketService;

    @GetMapping(value="/bucket/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getNewBucket() {
        return bucketService.getNewBucket();
    }

    @GetMapping(value="/bucket/get/{bucketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Bucket getBucket(@PathVariable String bucketId) {
        return bucketService.getBucket(bucketId);
    }

    @GetMapping(value="/bucket/applydiscount/{bucketId}/{discountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean applyDiscount(@PathVariable String bucketId, @PathVariable String discountId) {
        return bucketService.applyDiscount(bucketId, discountId);
    }

    @GetMapping(value="/bucket/calculateprice/{bucketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public double calculatePrice(@PathVariable String bucketId) {
        return bucketService.calculatePrice(bucketId);
    }

    @GetMapping(value="/bucket/checkout/{bucketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public double checkout(@PathVariable String bucketId) {
        return bucketService.checkout(bucketId);
    }

    @PostMapping(value="/bucket/addto/{bucketId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean modifyBucket(@RequestBody Map<String, Integer> newItems, @PathVariable String bucketId) {
        return bucketService.addItemsToBucket(bucketId, newItems);
    }

    @PostMapping(value="/bucket/deletefrom/{bucketId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteFromBucket(@RequestBody List<String> items, @PathVariable String bucketId) {
        return bucketService.removeItemsFromBucket(bucketId, items);
    }
}
