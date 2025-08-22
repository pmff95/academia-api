package com.example.demo.dto;

import java.time.LocalDate;

public class TreinoDesempenhoDTO {
    private LocalDate data;
    private double percentual;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }
}
