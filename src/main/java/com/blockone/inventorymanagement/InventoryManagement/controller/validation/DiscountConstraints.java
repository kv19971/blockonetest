package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.DiscountSpec;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/* Validation for discount operations
 */
@Component
public class DiscountConstraints extends Constraints{
    public void checkGeneralConstraints(Discount discount) throws InputException {
        if(null == discount.getPrerequisiteProducts() || discount.getPrerequisiteProducts().isEmpty()) {
            throw new MalformedRequestException("Prerequisite product dict cannot be empty");
        }
        if(null == discount.getDiscountApplied() || discount.getDiscountApplied().isEmpty()) {
            throw new MalformedRequestException("Discounted item dict cannot be empty");
        }
        for(Map.Entry<String, Integer> prerequisite : discount.getPrerequisiteProducts().entrySet()) {
            if(prerequisite.getValue() <= 0) {
                throw new BadParametersException("Please give positive qty to prerequisite product " + prerequisite.getKey());
            }
        }
        Map<String, Integer> sumDiscountQtys = new HashMap<>();
        for(DiscountSpec discountedItem : discount.getDiscountApplied()) {
            if(discountedItem.getDiscountFactor() - 0.0 < MARGIN || discountedItem.getDiscountFactor() - 1.0 > MARGIN) {
                throw new BadParametersException("Discounted item " + discountedItem.getProductId() + " discount factor needs to be between 0, 1");
            }
            if(!discount.getPrerequisiteProducts().containsKey(discountedItem.getProductId())) {
                throw new BadParametersException("Discounted item " + discountedItem.getProductId() + " not present in prerequisites list");
            }
            if(discount.getPrerequisiteProducts().get(discountedItem.getProductId()) < discountedItem.getQty()) {
                throw new BadParametersException("Prerequisite qty has to be greater than discounted qty for item " + discountedItem.getProductId());
            }
            sumDiscountQtys.put(discountedItem.getProductId(), sumDiscountQtys.getOrDefault(discountedItem.getProductId(), 0) + discountedItem.getQty());
        }
        for(Map.Entry<String, Integer> sumQtys : sumDiscountQtys.entrySet()) {
            if(sumQtys.getValue() > discount.getPrerequisiteProducts().get(sumQtys.getKey())) {
                throw new BadParametersException("Total prerequisite qty has to be greater than discounted qty for item " + sumQtys.getKey());
            }
        }
    }

    public void checkEditedDiscountConstraints(Discount discount) throws InputException {
        if(!checkIdConstraint(discount.getId())) {
            throw new MalformedRequestException("No ID given");
        }
        checkGeneralConstraints(discount);
    }

    public void checkCreatedDiscountConstraints(Discount discount) throws InputException {
        if(checkIdConstraint(discount.getId())) {
            throw new MalformedRequestException("No ID needed for creating entity");
        }
        checkGeneralConstraints(discount);
    }
}
