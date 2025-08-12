package com.example.demo.service;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Usuario;
import com.example.demo.mapper.AcademiaMapper;
import com.example.demo.repository.AcademiaRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
public class AcademiaService {
    private final AcademiaRepository repository;
    private final AcademiaMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AcademiaService(AcademiaRepository repository, AcademiaMapper mapper, PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public String create(AcademiaDTO dto) {
        try {
            Academia entity = mapper.toEntity(dto);
            Usuario admin = entity.getAdmin();
            String senha = SenhaUtil.gerarSenhaNumerica(6);
            admin.setSenha(passwordEncoder.encode(senha));
            admin.setPerfil(Perfil.ADMIN);
            admin.setAcademia(entity);
            repository.save(entity);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        emailService.enviarSenha(admin.getEmail(), senha, entity.getUuid().toString());
                    } catch (Exception e) {
                        log.error("Erro ao enviar email", e);
                    }
                }
            });
            return "Academia criada com sucesso";
        } catch (Exception e) {
            log.error("Erro ao criar academia", e);
            return "Erro ao criar academia";
        }
    }

    public Page<AcademiaDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
