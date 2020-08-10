package com.blockone.inventorymanagement.InventoryManagement.model.repositories;

import com.blockone.inventorymanagement.InventoryManagement.model.dto.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface DiscountRepository extends MongoRepository<Discount, String> {
}
