package com.blockone.inventorymanagement.InventoryManagement.dao;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.DBException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.EntityNotFoundException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dao.DiscountDao;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.repositories.DiscountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.*;

class DiscountDaoTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountDao discountDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void genericSaveEntityValid() {
        Discount discount = new Discount("id", new HashMap<>(), new HashSet<>());
        Mockito.when(discountRepository.save(discount)).thenReturn(discount);
        Assertions.assertEquals(discount, discountDao.genericSaveEntity(discount));
    }

    @Test
    void genericSaveEntityButDataAccessError() {
        Discount discount = new Discount("id", new HashMap<>(), new HashSet<>());
        Mockito.when(discountRepository.save(Mockito.any(Discount.class))).thenThrow(new DataAccessException("") {});
        Assertions.assertThrows(DBException.class, () -> {
            discountDao.genericSaveEntity(discount);
        });
    }

    @Test
    void genericRemoveEntityByIdNullId() {
        Mockito.doThrow(IllegalArgumentException.class)
                .when(discountRepository).deleteById(Mockito.nullable(String.class));
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountDao.genericRemoveEntityById(null);
        });
    }

    @Test
    void genericRemoveEntityByIdButDataAccessError() {
        Mockito.doThrow(new DataAccessException("") {
        }).when(discountRepository).deleteById("test");
        Assertions.assertThrows(DBException.class, () -> {
            discountDao.genericRemoveEntityById("test");
        });
    }

    @Test
    void genericFindByIdValid() {
        Discount testDiscount = new Discount("test", null, null);
        Mockito.when(discountRepository.findById("test")).thenReturn(Optional.of(testDiscount));
        Assertions.assertEquals(testDiscount, discountDao.genericFindById("test"));
    }

    @Test
    void genericFindByIdNullId() {
        Mockito.doThrow(IllegalArgumentException.class)
                .when(discountRepository).findById(Mockito.nullable(String.class));
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountDao.genericFindById(null);
        });
    }

    @Test
    void genericFindByIdDataAccessError() {
        Mockito.doThrow(new DataAccessException("") {})
                .when(discountRepository).findById("test");
        Assertions.assertThrows(DBException.class, () -> {
            discountDao.genericFindById("test");
        });
    }

    @Test
    void genericFindByIdNotFound() {
        Mockito.when(discountRepository.findById("test")).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            discountDao.genericFindById("test");
        });
    }

    @Test
    void genericFindAllValid() {
        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount("A", null, null));
        discounts.add(new Discount("B", null, null));
        Mockito.when(discountRepository.findAll()).thenReturn(discounts);
        Assertions.assertEquals(discounts, discountDao.genericFindAll());
    }

    @Test
    void genericFindAllDataAccessError() {
        Mockito.doThrow(new DataAccessException("") {})
                .when(discountRepository).findAll();
        Assertions.assertThrows(DBException.class, () -> {
            discountDao.genericFindAll();
        });
    }
}