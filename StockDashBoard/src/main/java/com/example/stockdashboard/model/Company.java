package com.example.stockdashboard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Company {
    @Id
    private String symbol; // e.g., INFY, TCS
    private String name;
}
