package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.DiscountSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ActiveProfiles("test")
class DiscountConstraintsTest {

    @InjectMocks
    DiscountConstraints discountConstraints;

    Discount discount;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, Integer> prerequisiteItems = new HashMap<>();
        prerequisiteItems.put("P1", 4);
        prerequisiteItems.put("P2", 3);

        Set<DiscountSpec> discountedItems = new HashSet<>();
        discountedItems.add(new DiscountSpec("P1", 2, 0.3));
        discountedItems.add(new DiscountSpec("P2", 1, 1.0));

        discount = new Discount("D1", prerequisiteItems, discountedItems);
    }

    @Test
    void checkGeneralConstraintsForNonExistingPrerequisiteItems() {
        discount.setPrerequisiteProducts(null);
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForEmptyPrerequisiteItems() {
        discount.setPrerequisiteProducts(new HashMap<>());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForNonExistingDiscountItems() {
        discount.setDiscountApplied(null);
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForEmptyDiscountItems() {
        discount.setDiscountApplied(new HashSet<>());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForValidDiscountedAndPrerequisites() {
        Assertions.assertDoesNotThrow(() -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForLowPrerequisitesQty() {
        discount.getPrerequisiteProducts().put("P1", 1);
        Assertions.assertThrows(BadParametersException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    //in case user adds many discounted products of same type accidentally
    @Test
    void checkGeneralConstraintsForLowOverallPrerequisitesQty() {
        discount.getDiscountApplied().add(new DiscountSpec("P1", 2, 0.2));
        discount.getDiscountApplied().add(new DiscountSpec("P1", 3, 0.2));
        Assertions.assertThrows(BadParametersException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForMissingPrerequisites() {
        discount.getPrerequisiteProducts().remove("P1");
        Assertions.assertThrows(BadParametersException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForInvalidDiscountFactor() {
        discount.getDiscountApplied().remove(new DiscountSpec("P1", 2, 0));
        discount.getDiscountApplied().add(new DiscountSpec("P1", 2, -0.2));
        Assertions.assertThrows(BadParametersException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkGeneralConstraintsForNoDiscountFactor() {
        discount.getDiscountApplied().remove(new DiscountSpec("P1", 2, 0));
        discount.getDiscountApplied().add(new DiscountSpec("P1", 2, 0.0));
        Assertions.assertThrows(BadParametersException.class, () -> {
            discountConstraints.checkGeneralConstraints(discount);
        });
    }

    @Test
    void checkEditedDiscountConstraintsForNullId() {
        Discount noId = new Discount(null, discount.getPrerequisiteProducts(), discount.getDiscountApplied());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkEditedDiscountConstraints(noId);
        });
    }

    @Test
    void checkEditedDiscountConstraintsForEmptyId() {
        Discount noId = new Discount("", discount.getPrerequisiteProducts(), discount.getDiscountApplied());
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkEditedDiscountConstraints(noId);
        });
    }

    @Test
    void checkEditedDiscountConstraintsForValidId() {
        Assertions.assertDoesNotThrow(() -> {
            discountConstraints.checkEditedDiscountConstraints(discount);
        });
    }

    @Test
    void checkCreatedDiscountConstraintsForNullId() {
        Discount noId = new Discount(null, discount.getPrerequisiteProducts(), discount.getDiscountApplied());
        Assertions.assertDoesNotThrow( () -> {
            discountConstraints.checkCreatedDiscountConstraints(noId);
        });
    }

    @Test
    void checkCreatedDiscountConstraintsForEmptyId() {
        Discount noId = new Discount("", discount.getPrerequisiteProducts(), discount.getDiscountApplied());
        Assertions.assertDoesNotThrow( () -> {
            discountConstraints.checkCreatedDiscountConstraints(noId);
        });
    }

    @Test
    void checkCreatedDiscountConstraintsForValidId() {
        Assertions.assertThrows(MalformedRequestException.class, () -> {
            discountConstraints.checkCreatedDiscountConstraints(discount);
        });
    }
}