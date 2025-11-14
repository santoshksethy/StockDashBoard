package com.example.stockdashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stockdashboard.model.StockData;
import java.time.LocalDate;
import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, Long> {
    List<StockData> findBySymbolOrderByDateDesc(String symbol);
    List<StockData> findBySymbolAndDateBetweenOrderByDateDesc(String symbol, LocalDate from, LocalDate to);
    List<StockData> findBySymbolOrderByDateAsc(String symbol);
    StockData findFirstBySymbolOrderByDateDesc(String symbol);
}
