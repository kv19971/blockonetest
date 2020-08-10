package com.blockone.inventorymanagement.InventoryManagement.controller.rest;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.DiscountService;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping(value="/discount/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Discount getDiscountById(@PathVariable String id) {
        return discountService.getById(id);
    }

    @PostMapping(value = "/discount/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addDiscount(@RequestBody Discount discount) {
        return discountService.addDiscount(discount);
    }

    @PostMapping(value = "/discount/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean editDiscount(@RequestBody Discount discount) {
        return discountService.editDiscount(discount);
    }

    @GetMapping(value = "/discount/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping(value="/discount/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean removeDiscount(@PathVariable String id) {
        return discountService.removeDiscount(id);
    }
}