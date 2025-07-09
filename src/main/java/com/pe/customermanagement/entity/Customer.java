package com.pe.customermanagement.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a customer entity in the customer management system.
 * This class is mapped to the "customers" collection in MongoDB.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Setter @Getter
@Builder @NoArgsConstructor @AllArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private LocalDateTime creationDate;
    private String status;

}
