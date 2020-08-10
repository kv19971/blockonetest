package com.blockone.inventorymanagement.InventoryManagement.controller.services;

import com.blockone.inventorymanagement.InventoryManagement.controller.validation.DiscountConstraints;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class DiscountServiceTest {
    @Mock
    private DiscountConstraints discountConstraints;
    @Mock
    private DiscountDao discountDao;

    @InjectMocks
    private DiscountService discountService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getById() {
        Discount discount = new Discount("D1", null, null);
        discountService.getById(discount.getId());
        Mockito.verify(discountConstraints, Mockito.times(1)).checkIdConstraint(discount.getId());
        Mockito.verify(discountDao, Mockito.times(1)).genericFindById(discount.getId());
    }

    @Test
    void addDiscount() {
        Discount discountWithNoId =  new Discount(null, null, null);
        Mockito.when(discountDao.genericSaveEntity(discountWithNoId)).thenReturn(discountWithNoId);
        discountService.addDiscount(discountWithNoId);
        Mockito.verify(discountConstraints, Mockito.times(1)).checkCreatedDiscountConstraints(discountWithNoId);
        Mockito.verify(discountDao, Mockito.times(1)).genericSaveEntity(discountWithNoId);
    }

    @Test
    void editDiscount() {
        Discount discount = new Discount("D1", null, null);
        Mockito.when(discountDao.genericFindById(discount.getId())).thenReturn(discount);
        Mockito.when(discountDao.genericSaveEntity(discount)).thenReturn(discount);
        discountService.editDiscount(discount);
        Mockito.verify(discountConstraints, Mockito.times(1)).checkEditedDiscountConstraints(discount);
        Mockito.verify(discountDao, Mockito.times(1)).genericFindById(discount.getId());
        Mockito.verify(discountDao, Mockito.times(1)).genericSaveEntity(discount);
    }

    @Test
    void getAllDiscounts() {
        discountService.getAllDiscounts();
        Mockito.verify(discountDao, Mockito.times(1)).genericFindAll();
    }

    @Test
    void removeDiscount() {
        String discountId = "test";
        discountService.removeDiscount(discountId);
        Mockito.verify(discountConstraints).checkIdConstraint("test");
        Mockito.verify(discountDao).genericRemoveEntityById("test");
    }
}