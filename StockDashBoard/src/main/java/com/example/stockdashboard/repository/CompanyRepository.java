package com.example.stockdashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stockdashboard.model.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {}
