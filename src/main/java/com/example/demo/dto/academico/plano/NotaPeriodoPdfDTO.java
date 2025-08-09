package com.example.demo.dto.academico.plano;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaPeriodoPdfDTO {
    public String nota;
    public String faltas;
    public String freq;

    public NotaPeriodoPdfDTO(String nota, String faltas, String freq) {
        this.nota = nota;
        this.faltas = faltas;
        this.freq = freq;
    }
}
