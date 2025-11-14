package com.example.stockdashboard.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.stockdashboard.model.Company;
import com.example.stockdashboard.model.StockData;
import com.example.stockdashboard.repository.CompanyRepository;
import com.example.stockdashboard.repository.StockDataRepository;
import com.opencsv.CSVReader;

@Component
public class CsvLoader implements CommandLineRunner {

    private final StockDataRepository stockRepo;
    private final CompanyRepository companyRepo;

    public CsvLoader(StockDataRepository stockRepo, CompanyRepository companyRepo) {
        this.stockRepo = stockRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // filenames you placed in resources/data
        String[] files = {"INFY.csv", "TCS.csv"}; // add more
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (String f : files) {
            try {
                ClassPathResource resource = new ClassPathResource("data/" + f);
                if (!resource.exists()) continue;
                String symbol = f.replace(".csv", "").toUpperCase();

                // create company if not exists
                companyRepo.save(new Company(symbol, symbol + " Ltd"));

                try (InputStream is = resource.getInputStream();
                     CSVReader reader = new CSVReader(new InputStreamReader(is))) {
                  //  String[] header = reader.readNext();
                    String[] line;
                    while ((line = reader.readNext()) != null) {

                        // Skip header or empty lines
                        if (line.length == 0 || line[0] == null) continue;
                        if (line[0].trim().equals("") || line[0].equalsIgnoreCase("Date")) continue;

                        // Parse date
                        LocalDate date = LocalDate.parse(line[0].trim(), fmt);

                        Double open = parseDouble(line[1]);
                        Double high = parseDouble(line[2]);
                        Double low = parseDouble(line[3]);
                        Double close = parseDouble(line[4]);
                        Long volume = parseLong(line[6]);

                        StockData s = new StockData();
                        s.setSymbol(symbol);
                        s.setDate(date);
                        s.setOpenPrice(open);
                        s.setHighPrice(high);
                        s.setLowPrice(low);
                        s.setClosePrice(close);
                        s.setVolume(volume);
                        s.setDailyReturn((close - open) / open);

                        stockRepo.save(s);
                    }

                }
            } catch (Exception ex) {
                System.err.println("Error loading " + f + ": " + ex.getMessage());
            }
        }
        System.out.println("CSV load complete.");
    }

    private Double parseDouble(String s) {
        try { return Double.parseDouble(s.replaceAll(",", "")); }
        catch (Exception e) { return null; }
    }
    private Long parseLong(String s) {
        try { return Long.parseLong(s.replaceAll(",", "")); }
        catch (Exception e) { return 0L; }
    }
}
