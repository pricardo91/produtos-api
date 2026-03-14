package com.mixfiscal.produtos_api.mapper;

import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.dto.out.TaxResponse;
import com.mixfiscal.produtos_api.entity.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toDomain(ProdutoCreateRequest request) {
        return Produto.builder()
                .nome(request.getNome())
                .sku(request.getSku())
                .precoBase(request.getPrecoBase())
                .codigoNcm(request.getCodigoNcm())
                .build();
    }

    public ProdutoResponse toResponse(Produto produto) {
        return ProdutoResponse.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .sku(produto.getSku())
                .precoBase(produto.getPrecoBase())
                .codigoNcm(produto.getCodigoNcm())
                .build();
    }

    public ProdutoResponse toResponse(Produto produto, TaxResponse taxResponse) {
        return ProdutoResponse.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .sku(produto.getSku())
                .precoBase(produto.getPrecoBase())
                .codigoNcm(produto.getCodigoNcm())
                .imposto(taxResponse)
                .build();
    }
}
