package com.mixfiscal.produtos_api.service;

import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.in.ProdutoUpdateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProdutoService {

    ProdutoResponse criarNovoProduto(ProdutoCreateRequest produtoNovo);

    Page<ProdutoResponse> listaProdutos(Pageable pageable);

    ProdutoResponse listarProdutoPorId(UUID idProduto);

    void deletaProdutoPorId(UUID idProduto);

    ProdutoResponse atualizaCadastroProdutoPorId(UUID idProduto, ProdutoUpdateRequest request);
}
