package com.pe.customermanagement.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter @Getter
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private LocalDateTime creationDate;
    private boolean status;

}
