package com.megasoft.calculator.repository;

import com.megasoft.calculator.domain.Calculator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalculatorRepository extends JpaRepository<Calculator,Long> {

    Optional<Calculator> findTopByOrderByIdDesc();
}
