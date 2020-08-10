package com.blockone.inventorymanagement.InventoryManagement.model.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document
public class Bucket extends ImBaseEntity{

    private List<String> discountCoupons;

    // allow bucket to hold different types of items
    private EnumMap<ItemTypes, Map<String, Integer>> items;


    public Bucket(String id, List<String> discountCoupons, Map<String, Integer> items, Map<String, Integer> discountedItems) {
        this.id = id;
        this.discountCoupons = discountCoupons;
        this.items = new EnumMap<>(ItemTypes.class);
        this.items.put(ItemTypes.REGULAR, items);
        this.items.put(ItemTypes.DISCOUNTED, items); // this is done to ensure that discount coupon isnt applied twice to same bucket
    }

    public Bucket() { }

    public List<String> getDiscountCoupons() {
        return discountCoupons;
    }

    public void setDiscountCoupons(List<String> discountCoupons) {
        this.discountCoupons = discountCoupons;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumMap<ItemTypes, Map<String, Integer>> getItems() {
        return items;
    }

    public void setItems(EnumMap<ItemTypes, Map<String, Integer>> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bucket bucket = (Bucket) o;
        return id.equals(bucket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "id='" + id + '\'' +
                ", discountCoupons=" + discountCoupons +
                ", items=" + items +
                '}';
    }
}
