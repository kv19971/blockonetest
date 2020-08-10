package com.blockone.inventorymanagement.InventoryManagement.controller.rest;

import com.blockone.inventorymanagement.InventoryManagement.controller.services.ProductService;
import com.blockone.inventorymanagement.InventoryManagement.model.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value="/product/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProductById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PostMapping(value = "/product/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PostMapping(value = "/product/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean editProduct(@RequestBody Product product) {
        return productService.editProduct(product);
    }

    @GetMapping(value = "/product/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value="/product/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean removeProduct(@PathVariable String id) {
        return productService.removeProduct(id);
    }


}