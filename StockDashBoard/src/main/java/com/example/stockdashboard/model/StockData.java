package com.example.stockdashboard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Table(indexes = {@Index(name="idx_symbol_date", columnList = "symbol,date")})
public class StockData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private LocalDate date;

    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Long volume;

    // derived metrics
    private Double dailyReturn; // (close-open)/open
    private Double movingAvg7;  // optional, calculate in service
}
