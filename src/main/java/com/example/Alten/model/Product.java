package com.example.Alten.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

// MODEL FOR THE BDD

@Getter // Setting the getter for Product thanks to Lombok
@Setter // Setting the setter for Product thanks to Lombok
@AllArgsConstructor // Generate a constructor automaticallyy
@NoArgsConstructor // Generate a constructor without any arguments
public class Product
{
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Double price;
    private Integer quantity;
    private String internalReference;
    private Integer shellId;
    private String inventoryStatus = "IN STOCK";
    /*
    private enum InventoryStatus
    {
        IN_STOCK("IN STOCK"),
        LOW_STOCK("LOW STOCK"),
        OUT_OF_STOCK("OUT OF STOCK");

        private final String status;

        // Constructor to initialize the status
        InventoryStatus(String status)
         {
            this.status = status;
        }


        // Method to get the status name
        public String getStatus()
        {
            return status;
        }
    }
    */

    private double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
