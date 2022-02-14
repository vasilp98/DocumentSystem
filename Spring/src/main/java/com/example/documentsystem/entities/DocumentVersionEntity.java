package com.example.documentsystem.entities;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "versions")
@Data
public class DocumentVersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "DOCUMENT_ID", insertable = false, updatable = false)
    @OnDelete( action = OnDeleteAction.CASCADE )
    private DocumentEntity document;

    @Column(name = "DOCUMENT_ID")
    private Long documentId;
}
