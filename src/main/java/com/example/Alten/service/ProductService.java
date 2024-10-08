package com.example.Alten.service;

import com.example.Alten.AltenApplication;
import com.example.Alten.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService
{
    //Protecting it with private and final
    private final String pathjsonfile =  "data/products.json";

    // GET - Retrieve all products
    public List<Product> getAllProducts() throws IOException
    {
        //Using Object Mapper to translate the json file for the Product
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the date datas correctly (couldn't handle the date data at first)
        objectMapper.registerModule(new JavaTimeModule());

        // Keep the information in a correct format
        TypeReference<List<Product>> typeReference = new TypeReference<List<Product>>() {};

        // Get the datas from the json file
        File jsonFile = new File(pathjsonfile);
        InputStream inputStream = new FileInputStream(jsonFile);
        // stream is a sequence of elements (in this case Products)

        // If file doesn't exist we thrown an error
        if (inputStream == null)
        {
            throw new IOException("File not found: " +pathjsonfile);
        }

        //Return a list
        return objectMapper.readValue(inputStream, typeReference);
    }

    // GET - Retrieve details for 1 product
    public Product getProduct (UUID productId) throws IOException
    {
        //Using Object Mapper to translate the json file for the Product
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the date datas correctly (couldn't handle the date data at first)
        objectMapper.registerModule(new JavaTimeModule());

        // Get the datas from the json file
        File jsonFile = new File(pathjsonfile);
        InputStream inputStream = new FileInputStream(jsonFile);

        // If file doesn't exist we thrown an error
        if (inputStream == null)
        {
            throw new IOException("File not found: " + pathjsonfile);
        }

        // Put json into a list of Product objects from the InputStream
        List<Product> products = objectMapper.readValue(inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        // Find the product by ID in the list
        return products.stream()
                .filter(product -> productId.equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new IOException("Product not found with ID: " + productId)); // If we ca,n't find the product
    }

    // POST - Add 1 product from the body
    public void postProduct (Product product_to_add) throws IOException
    {
        //Check the inventoryStatus
        checkstatus(product_to_add);

        //Generate an id for product_to_add
        product_to_add.setId(UUID.randomUUID());

        // THIS PART COULD BE REUSABLE : TO PUT IN A SEPERATED METHOD
        //Using Object Mapper to translate the json file for the Product
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the date datas correctly (couldn't handle the date data at first)
        objectMapper.registerModule(new JavaTimeModule());

        // Get the datas from the json file
        File jsonFile = new File(pathjsonfile);
        InputStream inputStream = new FileInputStream(jsonFile);

        // If file doesn't exist we thrown an error
        if (inputStream == null)
        {
            throw new IOException("File not found: " + pathjsonfile);
        }

        // Put json into a list of Product objects from the InputStream
        List<Product> products = objectMapper.readValue(inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        //

        // Add the new product to the list
        products.add(product_to_add);

        // Close the stream after reading
        inputStream.close();

        // Write the updated list back to the jeson file
        OutputStream outputStream = new FileOutputStream(jsonFile);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, products);

        // Close the outputstream after writing
        outputStream.close();

    }

    // PATCH - Update 1 product from the body and id
    public void patchProduct (UUID productId, Product product_newdatas) throws IOException
    {
        //Check the inventoryStatus
        checkstatus(product_newdatas);

        //Using Object Mapper to translate the json file for the Product
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the date datas correctly (couldn't handle the date data at first)
        objectMapper.registerModule(new JavaTimeModule());

        // Get the datas from the json file
        File jsonFile = new File(pathjsonfile);
        InputStream inputStream = new FileInputStream(jsonFile);

        // If file doesn't exist we thrown an error
        if (inputStream == null)
        {
            throw new IOException("File not found: " + pathjsonfile);
        }

        // Put json into a list of Product objects from the InputStream
        List<Product> products = objectMapper.readValue(inputStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        // Find the product by ID in the list
        Product product_to_modify = products.stream()
                .filter(product -> productId.equals(product.getId()))  // Assuming Product has a getId() method
                .findFirst()
                .orElseThrow(() -> new IOException("Product not found with ID: " + productId)); // If we ca,n't find the product

        //We found the product thanks to the ID now we modify it before adding it to the list

        // I put the id of the product_to_modify into the product_newdatas
        product_newdatas.setId(product_to_modify.getId());

        // Remove the product_to_modify from the list
        products.remove(product_to_modify);

        // Add the updated product_newdatas to the list
        products.add(product_newdatas);

        // Save the updated product list back to the JSON file
        updateJSONfile(products);
    }

    // DELETE - Delete 1 product, no return needed so it's void
    // I put all the datas into the list of Product, I delete the product with the correct id and then rewrite the file completly
    public void deleteProduct (UUID productId) throws IOException
    {
        //Using Object Mapper to translate the json file for the Product
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the date datas correctly (couldn't handle the date data at first)
        objectMapper.registerModule(new JavaTimeModule());

        //Logger logger = LoggerFactory.getLogger(ProductService.class);

        // Get the datas from the json file
        File jsonFile = new File(pathjsonfile);
        InputStream inputStream = new FileInputStream(jsonFile);

        // If file doesn't exist we thrown an error
        if (inputStream == null)
        {
            throw new IOException("File not found: " + pathjsonfile);
        }

        // Put json into a list of Product objects from the InputStream
        List<Product> products = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        // Filter out the product with the correctt productId
        List<Product> updatedProducts = products.stream()
                .filter(product -> !productId.equals(product.getId()))  // Remove the product with the matching UUID
                .collect(Collectors.toList()); // collect() transforms the elements of the stream into a new collection

        // Check if a product was really removed
        if (updatedProducts.size() == products.size())
        {
            throw new IOException("Product with ID " + productId + " not found.");
        }

        // Save the updated product list back to the JSON file
        updateJSONfile(updatedProducts);

    }

    // Method used to update the json file. We can manipulate the list of products as we wish and just update the file once it's done
    // Will be used for DELETE, POST, PATCH
    public void updateJSONfile (List<Product> products) throws IOException
    {
        // Open a FileOutputStream to write the updated list of products to the file
        OutputStream outputStream = new FileOutputStream(pathjsonfile);

        // Create ObjectMapper to write the products into to the file aftert
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Write the updated list back to the json file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, products);

        // Close the OutputStream
        outputStream.close();
    }

    public void checkstatus (Product product_to_add) throws IOException
    {
        if (!product_to_add.getInventoryStatus().equals("INSTOCK") && !product_to_add.getInventoryStatus().equals("LOWSTOCK") && !product_to_add.getInventoryStatus().equals("OUTOFSTOCK")) {

            throw new IOException("The value of inventoryStatus should be INSTOCK, LOWSTOCK, or OUTOFSTOCK");
        }
    }

}