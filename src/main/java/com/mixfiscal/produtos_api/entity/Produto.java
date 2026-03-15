package com.mixfiscal.produtos_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "produtos")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_produto")
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(name = "preco_base", nullable = false)
    private BigDecimal precoBase;

    @Column(name = "ncm", nullable = false)
    private String codigoNcm;

    public void atualizar(String nome, BigDecimal precoBase, String codigoNcm) {
        if (nome != null) {
            this.nome = nome;
        }
        if (precoBase != null) {
            this.precoBase = precoBase;
        }
        if (codigoNcm != null) {
            this.codigoNcm = codigoNcm;
        }
    }
}
