package com.example.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario {
    private String cref;

    private LocalDate crefValidade;
}
