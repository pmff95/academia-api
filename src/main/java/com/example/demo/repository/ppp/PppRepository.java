package com.example.demo.repository.ppp;

import com.example.demo.domain.model.ppp.Ppp;
import com.example.demo.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PppRepository extends BaseRepository<Ppp, Long> {
    Optional<Ppp> findByUuid(UUID uuid);
}
