package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@Data
@NoArgsConstructor
public class DocumentLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "DOCUMENT_ID", insertable = false, updatable = false)
    @OnDelete( action = OnDeleteAction.CASCADE )
    private DocumentEntity document;

    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    private String password;

    private LocalDate validUntil;

    public DocumentLinkEntity(String token, Long documentId, String password, LocalDate validUntil) {
        this.token = token;
        this.documentId = documentId;
        this.password = password;
        this.validUntil = validUntil;
    }
}
