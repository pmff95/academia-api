package com.example.demo.service;

import com.example.demo.dto.AcademiaDTO;
import com.example.demo.entity.Academia;
import com.example.demo.entity.Usuario;
import com.example.demo.mapper.AcademiaMapper;
import com.example.demo.repository.AcademiaRepository;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.common.util.SenhaUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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
        Academia entity = mapper.toEntity(dto);
        Usuario admin = entity.getAdmin();
        String senha = SenhaUtil.gerarSenhaNumerica(6);
        admin.setSenha(passwordEncoder.encode(senha));
        admin.setPerfil(Perfil.ADMIN);
        repository.save(entity);
        emailService.enviarSenha(admin.getEmail(), senha);
        return "Academia criada com sucesso";
    }

    public Page<AcademiaDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }
}
