package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ProductConstraintsTest {

    @InjectMocks
    ProductConstraints productConstraints;

    Product product;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        product = new Product("P1", "A product", 100, 24.0);
    }

    @Test
    void checkGeneralConstraintsValidProduct() {
        Assertions.assertDoesNotThrow(() -> {
            productConstraints.checkGeneralConstraints(product);
        });
    }

    @Test
    void checkGeneralConstraintsNoDescription() {
        product.setDescription("");
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            productConstraints.checkGeneralConstraints(product);
        });
    }

    @Test
    void checkGeneralConstraintsInvalidPrice() {
        product.setPrice(-0.3);
        Assertions.assertThrows(BadParametersException.class, () -> {
            productConstraints.checkGeneralConstraints(product);
        });
    }

    @Test
    void checkGeneralConstraintsInvalidQty() {
        product.setQuantity(-100);
        Assertions.assertThrows(BadParametersException.class, () -> {
            productConstraints.checkGeneralConstraints(product);
        });
    }

    @Test
    void checkEditedProductConstraintsForNullId() {
        Product noId = new Product(null, product.getDescription(), product.getQuantity(), product.getPrice());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            productConstraints.checkEditedProductConstraints(noId);
        });
    }

    @Test
    void checkEditedProductConstraintsForEmptyId() {
        Product noId = new Product("", product.getDescription(), product.getQuantity(), product.getPrice());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            productConstraints.checkEditedProductConstraints(noId);
        });
    }

    @Test
    void checkEditedProductConstraintsForValidId() {
        Assertions.assertDoesNotThrow( () -> {
            productConstraints.checkEditedProductConstraints(product);
        });
    }

    @Test
    void checkCreatedProductConstraintsForNullId() {
        Product noId = new Product("", product.getDescription(), product.getQuantity(), product.getPrice());
        Assertions.assertDoesNotThrow( () -> {
            productConstraints.checkCreatedProductConstraints(noId);
        });
    }

    @Test
    void checkCreatedProductConstraintsForEmptyId() {
        Product noId = new Product("", product.getDescription(), product.getQuantity(), product.getPrice());
        Assertions.assertDoesNotThrow( () -> {
            productConstraints.checkCreatedProductConstraints(noId);
        });
    }

    @Test
    void checkCreatedProductConstraintsForValidId() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            productConstraints.checkCreatedProductConstraints(product);
        });
    }

    @Test
    void checkCreatedProductConstraintsForZeroQty() {
        Product noIdAndQty= new Product("", product.getDescription(), 0, product.getPrice());
        Assertions.assertThrows(BadParametersException.class, () -> {
            productConstraints.checkCreatedProductConstraints(noIdAndQty);
        });
    }
}