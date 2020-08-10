package com.blockone.inventorymanagement.InventoryManagement.controller.services.crossentity;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.ProductDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Bucket;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/*
Service to calculate total price of buckets
 */
@Component
public class PriceCalculatorService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private DiscountApplierService discountApplierService;

    private double sumItemPrices(Map<String, Integer> items) throws InputException, DBException {
        double price = 0.0;
        for(Map.Entry<String, Integer> item : items.entrySet()) {
            price += productDao.genericFindById(item.getKey()).getPrice() * item.getValue();
        }
        return price;
    }

    private double sumDiscountValue(Bucket bucket) throws InputException, DBException{
        double price = 0.0;
        for(String discountId : bucket.getDiscountCoupons()) {
            Discount discount = discountDao.genericFindById(discountId);
            discountApplierService.verifyDiscountValid(bucket, discount);
            price += discountApplierService.calculateDiscountReduction(discount);
        }
        return price;
    }

    @Transactional
    public double calculatePrice(Bucket bucket) throws InputException, DBException {
        double price = 0.0;
        for(Map<String, Integer> items : bucket.getItems().values()) {
            if(items != null) {
                price += sumItemPrices(items);
            }
        }
        if(bucket.getDiscountCoupons() != null)
            price -= sumDiscountValue(bucket);
        return price;
    }

}
