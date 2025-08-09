package com.example.demo.repository;

import com.example.demo.entity.Academia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademiaRepository extends JpaRepository<Academia, Long> {
}
