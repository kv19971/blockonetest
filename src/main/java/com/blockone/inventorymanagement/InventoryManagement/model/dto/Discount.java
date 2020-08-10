package com.blockone.inventorymanagement.InventoryManagement.model.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Document
public class Discount extends ImBaseEntity {

    private Map<String, Integer> prerequisiteProducts; //map of products that are required in the bucket to have discount

    private Set<DiscountSpec> discountApplied; //mapping from productid, qty -> discount factor

    public Discount(String id, Map<String, Integer> prerequisiteProducts, Set<DiscountSpec>  discountApplied) {
        this.id = id;
        this.prerequisiteProducts = prerequisiteProducts;
        this.discountApplied = discountApplied;
    }

    public Discount() {

    }

    public Map<String, Integer> getPrerequisiteProducts() {
        return prerequisiteProducts;
    }

    public void setPrerequisiteProducts(Map<String, Integer> prerequisiteProducts) {
        this.prerequisiteProducts = prerequisiteProducts;
    }

    public Set<DiscountSpec> getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(Set<DiscountSpec> discountApplied) {
        this.discountApplied = discountApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return id.equals(discount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id='" + id + '\'' +
                ", prerequisiteProducts=" + prerequisiteProducts +
                ", discountApplied=" + discountApplied +
                '}';
    }
}
