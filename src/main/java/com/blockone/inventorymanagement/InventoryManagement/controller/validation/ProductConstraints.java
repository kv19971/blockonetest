package com.blockone.inventorymanagement.InventoryManagement.controller.validation;

import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.BadParametersException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.InputException;
import com.blockone.inventorymanagement.InventoryManagement.exceptions.user.MalformedRequestException;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/* Validation for product operations
 */
@Component
public class ProductConstraints extends Constraints{

    public void checkGeneralConstraints(Product product) throws InputException {
        if(StringUtils.isEmpty(product.getDescription())) {
            throw new MalformedRequestException("Need description!");
        }
        if(product.getPrice() < MARGIN) {
            throw new BadParametersException("Price cannot be <= 0");
        }
        if(product.getQuantity() < 0) {
            throw new BadParametersException("Qty cannot be <= 0");
        }
    }

    public void checkEditedProductConstraints(Product newProduct) throws InputException {
        if(!checkIdConstraint(newProduct.getId())) {
            throw new MalformedRequestException("Need product id");
        }
        checkGeneralConstraints(newProduct);
    }

    public void checkCreatedProductConstraints(Product product) throws InputException {
        if(checkIdConstraint(product.getId())) {
            throw new MalformedRequestException("Dont need product id");
        }
        if(product.getQuantity() == 0){
            throw new BadParametersException("Product qty cannot be 0 at time of creation!");
        }
        checkGeneralConstraints(product);
    }
}
