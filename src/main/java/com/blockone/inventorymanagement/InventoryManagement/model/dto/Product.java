package com.blockone.inventorymanagement.InventoryManagement.model.dto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Product extends ImBaseEntity{

        private String description;
        private int quantity;

        private double price;

        public Product() {}

        public double getPrice() {
            return price;
        }

        public Product(String id, String description, int quantity, double price) {
            this.id = id;
            this.description = description;
            this.quantity = quantity;
            this.price = price;
        }



        @Override
        public String toString() {
            return String.format(
                    "Customer[id=%s, Description='%s', Quantity='%s']",
                    id, description, quantity);
        }

        public String getDescription() {
        return description;
    }

        public int getQuantity() {
        return quantity;
    }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return id.equals(product.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setPrice(double price) {
            this.price = price;
        }
}
