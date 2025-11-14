package com.example.stockdashboard.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.stockdashboard.model.Company;
import com.example.stockdashboard.model.StockData;
import com.example.stockdashboard.repository.CompanyRepository;
import com.example.stockdashboard.repository.StockDataRepository;

@Service
public class StockService {

    private final StockDataRepository stockRepo;
    private final CompanyRepository companyRepo;

    public StockService(StockDataRepository stockRepo, CompanyRepository companyRepo) {
        this.stockRepo = stockRepo;
        this.companyRepo = companyRepo;
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    // last N days (by calendar) — return sorted ascending by date
    public List<StockData> getLastNDays(String symbol, int n) {
        List<StockData> all = stockRepo.findBySymbolOrderByDateAsc(symbol);
        if (all.isEmpty()) return Collections.emptyList();
        int size = all.size();
        int fromIndex = Math.max(0, size - n);
        List<StockData> last = all.subList(fromIndex, size);
        // compute 7-day moving avg for those entries (simple moving average of close)
        for (int i = 0; i < last.size(); i++) {
            int start = Math.max(0, i - 6); // 7-day window (i included)
            int end = i;
            double sum = 0; int count = 0;
            for (int j = start; j <= end; j++) {
                sum += last.get(j).getClosePrice();
                count++;
            }
            last.get(i).setMovingAvg7(sum / count);
        }
        return last;
    }

    // summary: 52-week high/low (approx 365 days) and average close
    public Map<String, Object> getSummary(String symbol) {
        LocalDate today = LocalDate.now();
        LocalDate past52 = today.minusDays(365);
        List<StockData> list = stockRepo.findBySymbolAndDateBetweenOrderByDateDesc(symbol, past52, today);
        Map<String, Object> map = new HashMap<>();
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        double high = list.stream().mapToDouble(StockData::getHighPrice).max().orElse(Double.NaN);
        double low = list.stream().mapToDouble(StockData::getLowPrice).min().orElse(Double.NaN);
        double avgClose = list.stream().mapToDouble(StockData::getClosePrice).average().orElse(Double.NaN);

        map.put("52WeekHigh", high);
        map.put("52WeekLow", low);
        map.put("avgClose", avgClose);
        // Add volatility score (std dev of daily returns)
        double mean = list.stream().mapToDouble(d -> d.getDailyReturn() != null ? d.getDailyReturn() : 0.0).average().orElse(0.0);
        double variance = list.stream().mapToDouble(d -> Math.pow((d.getDailyReturn() != null ? d.getDailyReturn() : 0.0) - mean, 2)).average().orElse(0.0);
        double sd = Math.sqrt(variance);
        map.put("volatility", sd);
        return map;
    }

    // Compare two symbols for last N days — returns map with both lists
    public Map<String, List<StockData>> compare(String s1, String s2, int days) {
        Map<String, List<StockData>> result = new HashMap<>();
        result.put(s1, getLastNDays(s1, days));
        result.put(s2, getLastNDays(s2, days));
        return result;
    }
}
