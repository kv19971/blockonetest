package com.blockone.inventorymanagement.InventoryManagement.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class DiscountSpec implements Serializable {
    private String productId;
    private int qty;
    private double discountFactor;

    public DiscountSpec(String productId, int qty, double discountFactor) {
        this.productId = productId;
        this.qty = qty;
        this.discountFactor = discountFactor;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }

    public void setDiscountFactor(double discountFactor) {
        this.discountFactor = discountFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountSpec that = (DiscountSpec) o;
        return qty == that.qty &&
                productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, qty);
    }
}
