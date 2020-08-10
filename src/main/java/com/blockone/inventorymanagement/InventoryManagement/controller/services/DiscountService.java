package com.blockone.inventorymanagement.InventoryManagement.controller.services;


import com.blockone.inventorymanagement.InventoryManagement.controller.validation.DiscountConstraints;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/*
Service to handle creation, deletion and modification of discount schemes
 */
@Component
public class DiscountService {
    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private DiscountConstraints discountConstraints;

    public Discount getById(String id) {
        discountConstraints.checkIdConstraint(id);
        return discountDao.genericFindById(id);
    }

    public String addDiscount(Discount newDiscount) {
        discountConstraints.checkCreatedDiscountConstraints(newDiscount);
        Discount savedDiscount = discountDao.genericSaveEntity(newDiscount);
        return savedDiscount.getId();
    }

    public boolean editDiscount(Discount discount) {
        discountConstraints.checkEditedDiscountConstraints(discount);
        discountDao.genericFindById(discount.getId());
        discountDao.genericSaveEntity(discount);
        return true;
    }

    public List<Discount> getAllDiscounts() {
        return discountDao.genericFindAll();
    }

    public boolean removeDiscount(String discountId) {
        getById(discountId); // check if discount exists, throw exception if it doesnt
        discountDao.genericRemoveEntityById(discountId);
        return true;
    }

}
