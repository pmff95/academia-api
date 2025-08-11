package com.example.demo.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.demo.dto.AlunoDTO;
import com.example.demo.entity.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Page<Aluno> findByAcademiaUuid(UUID uuid, Pageable pageable);
}
