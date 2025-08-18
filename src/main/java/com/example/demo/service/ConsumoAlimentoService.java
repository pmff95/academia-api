package com.example.demo.service;

import com.example.demo.dto.ConsumoDiarioDTO;
import com.example.demo.dto.ConsumoItemDTO;
import com.example.demo.entity.Alimento;
import com.example.demo.entity.ConsumoAlimento;
import com.example.demo.entity.Refeicao;
import com.example.demo.repository.AlimentoRepository;
import com.example.demo.repository.ConsumoAlimentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumoAlimentoService {
    private final ConsumoAlimentoRepository consumoRepository;
    private final AlimentoRepository alimentoRepository;

    public ConsumoAlimentoService(ConsumoAlimentoRepository consumoRepository,
                                  AlimentoRepository alimentoRepository) {
        this.consumoRepository = consumoRepository;
        this.alimentoRepository = alimentoRepository;
    }

    @Transactional
    public String registrar(ConsumoDiarioDTO dto) {
        List<ConsumoAlimento> consumos = new ArrayList<>();
        LocalDate data = dto.getData();
        adicionar(dto.getLancheManha(), Refeicao.LANCHE_MANHA, data, consumos);
        adicionar(dto.getAlmoco(), Refeicao.ALMOCO, data, consumos);
        adicionar(dto.getLancheTarde(), Refeicao.LANCHE_TARDE, data, consumos);
        adicionar(dto.getJanta(), Refeicao.JANTA, data, consumos);
        adicionar(dto.getCeia(), Refeicao.CEIA, data, consumos);
        consumoRepository.saveAll(consumos);
        return "Consumo registrado com sucesso";
    }

    private void adicionar(List<ConsumoItemDTO> itens, Refeicao refeicao, LocalDate data,
                           List<ConsumoAlimento> destino) {
        if (itens == null) {
            return;
        }
        for (ConsumoItemDTO item : itens) {
            Alimento alimento = alimentoRepository.findById(item.getAlimentoUuid())
                    .orElseThrow(() -> new IllegalArgumentException("Alimento não encontrado"));
            ConsumoAlimento consumo = new ConsumoAlimento();
            consumo.setData(data);
            consumo.setRefeicao(refeicao);
            consumo.setAlimento(alimento);
            consumo.setQuantidade(item.getQuantidade());
            destino.add(consumo);
        }
    }
}
