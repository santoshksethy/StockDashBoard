package com.example.stockdashboard.controller;

import com.example.stockdashboard.model.Company;
import com.example.stockdashboard.model.StockData;
import com.example.stockdashboard.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;
    public StockController(StockService stockService) { this.stockService = stockService; }

    // GET /api/companies
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> companies() {
        return ResponseEntity.ok(stockService.getAllCompanies());
    }

    // GET /api/data/{symbol}?days=30
    @GetMapping("/data/{symbol}")
    public ResponseEntity<List<StockData>> getData(@PathVariable String symbol, @RequestParam(required=false, defaultValue="30") int days) {
        List<StockData> list = stockService.getLastNDays(symbol.toUpperCase(), days);
        return ResponseEntity.ok(list);
    }

    // GET /api/summary/{symbol}
    @GetMapping("/summary/{symbol}")
    public ResponseEntity<Map<String,Object>> summary(@PathVariable String symbol) {
        Map<String,Object> map = stockService.getSummary(symbol.toUpperCase());
        return ResponseEntity.ok(map);
    }

    // Optional: compare ?symbol1=INFY&symbol2=TCS&days=30
    @GetMapping("/compare")
    public ResponseEntity<Map<String, List<StockData>>> compare(@RequestParam String symbol1, @RequestParam String symbol2, @RequestParam(required=false, defaultValue="30") int days) {
        return ResponseEntity.ok(stockService.compare(symbol1.toUpperCase(), symbol2.toUpperCase(), days));
    }
}
