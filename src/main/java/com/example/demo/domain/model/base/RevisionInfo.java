package com.example.demo.domain.model.base;

import com.example.demo.common.audit.RevisionInfoListener;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "revinfo", schema = "audit")
@RevisionEntity(RevisionInfoListener.class)
@Getter
@Setter
public class RevisionInfo {

    @Id
    @GeneratedValue
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

    private String nome;
    private Long usuarioId;
    private Long escolaId;

    public RevisionInfo() {
    }

    public RevisionInfo(String nome, Long usuarioId, Long escolaId) {
        this.nome = nome;
        this.usuarioId = usuarioId;
        this.escolaId = escolaId;
    }
}
