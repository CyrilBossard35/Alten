package com.example.Alten.controller;

// CONTROLLER WHERE WE CREATE THE API

import com.example.Alten.AltenApplication;
import com.example.Alten.model.Product;
import com.example.Alten.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ProductController
{
    /*
    // We test the API with an object Product
    Product gel = new Product (
            UUID.randomUUID(),
            "ZED",
            "Gel",
            "Gel Yves Rocher, cheveux soyeux",
            "Photo",
            "Soin du corps",
            15.5,
            500,
            "AASSDD55",
            100,
            "IN STOCK",
            8.7,
            LocalDateTime.now(),
            LocalDateTime.now()
    ); // Use args constructor
    */

    // Using the ProductService that handled the json file
    private final ProductService productservice;

    //Make the constructor for ProjectController
    public ProductController(ProductService productservice)
    {
        this.productservice = productservice;
    }

    @GetMapping ("/products") // Map HTTP GET requests to the method right under
    @Operation(summary = "Get all products")
    //@ResponseStatus(HttpStatus.OK)
    // We use Rest API so we use the url to transfer datas
    public List<Product> getAllProducts () throws IOException //Return a list because loadProducts returns a List
    {
        //We return the list
        return this.productservice.getAllProducts();
    }

    @GetMapping ("/products/{id}") // Map HTTP GET requests to the method right under. {id} to look for the product required
    public Product getProduct (@PathVariable("id") UUID productId) throws IOException //Return a list because loadProducts returns a List
    {
        //We return the list
        return this.productservice.getProduct(productId);
    }

    @PostMapping ("/products/add")
    public void postProduct (@RequestBody Product product_to_add) throws IOException //We use @Request Body to map the json body into the object Product
    {
        // We send product_to_add to the request POST
        this.productservice.postProduct(product_to_add);
    }

    @PatchMapping ("/products/{id}")
    public void patchProduct (@PathVariable("id") UUID productId, @RequestBody Product product_newdatas) throws IOException //We use @Request Body to map the json body into the object Product
    {
        // We send product_to_add to the request POST
        this.productservice.patchProduct(productId, product_newdatas);
    }

    @DeleteMapping ("/products/{id}")
    public void deleteProduct (@PathVariable("id") UUID productId) throws IOException //Return a list because loadProducts returns a List
    {
        // Logger logger = LoggerFactory.getLogger(AltenApplication.class);
        // logger.info("delete method");

        //We delete the product from the list
        this.productservice.deleteProduct(productId);


    }

}
