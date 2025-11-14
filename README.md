# Stock Market Dashboard (Spring Boot + MySQL)
Developed a full-stack stock analytics platform featuring a modern animated UI with light/dark theme, real-time chart rendering, and interactive company search. Built REST APIs using Spring Boot, implemented CSV-to-database ingestion, 7-day moving averages, volatility scoring, and 52-week summaries.

###A modern, fast, animated Stock Market Dashboard built with:
 ->Java 17 + Spring Boot
 ->MySQL (JPA/Hibernate)
 ->REST API
 ->HTML/CSS/JS Frontend
 ->Light/Dark UI Themes
 ->Searchable Sidebar

 ## 📂 Project Structure
StockDashboard/
│
├── src/main/java/com/example/stockdashboard/
│   ├── controller/
│   ├── service/
│   ├── model/
│   ├── repository/
│   └── config/
│
├── src/main/resources/
│   ├── static/index.html
│   ├── application.properties
│   ├── application-prod.properties
│   └── CSV files (INFY.csv, TCS.csv)
│
├── Dockerfile
├── docker-compose.yml
└── README.md

## 🛠️ Backend API Documentation
###✔ GET /api/companies 
    Returns all company names.
###✔ GET /api/data/{symbol}?days=30
    Returns last N days of data with moving averages.
###✔ GET /api/summary/{symbol}
    Returns:
            52-week high
            52-week low
            Average close
            Volatility
###✔ GET /api/compare?s1=TCS&s2=INFY&days=30
    Compare two stocks.
 
