package com.mixfiscal.produtos_api.mapper;

import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.dto.out.TaxResponse;
import com.mixfiscal.produtos_api.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toDomain(ProdutoCreateRequest produtoCreateRequest);

    ProdutoResponse toResponse(Produto produto);

    @Mapping(source = "taxResponse", target = "imposto")
    ProdutoResponse toResponse(Produto produto, TaxResponse taxResponse);
}
