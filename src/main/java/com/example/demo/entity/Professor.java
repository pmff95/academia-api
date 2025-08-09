package com.example.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario {
}
