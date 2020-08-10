package com.blockone.inventorymanagement.InventoryManagement;

import com.blockone.inventorymanagement.InventoryManagement.config.MongoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = {"com.blockone.inventorymanagement.InventoryManagement"})
public class InventoryManagementApplication {
	public static final int SERVER_PORT = 8083; //server port
	public static void main(String[] args) {

		SpringApplication app = new SpringApplication (InventoryManagementApplication.class);
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(MongoConfig.class);
		ctx.refresh();
		app.setDefaultProperties(Collections.singletonMap("server.port", SERVER_PORT));
		app.run(args);
	}

}
