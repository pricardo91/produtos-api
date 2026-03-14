package com.mixfiscal.produtos_api.controller;

import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.in.ProdutoUpdateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProdutoController implements ProdutoAPI {

    private final ProdutoService produtoService;

    @Override
    public ProdutoResponse criarProduto(ProdutoCreateRequest produtoNovo) {
        log.info("[start] - {} - criando produto", getClass().getSimpleName());
        ProdutoResponse produtoSalvo = produtoService.criarNovoProduto(produtoNovo);
        log.info("[end] - {} - criando produto", getClass().getSimpleName());
        return produtoSalvo;
    }

    @Override
    public Page<ProdutoResponse> listarProdutos(Pageable pageable) {
        log.info("[start] - {} - listarProdutos", getClass().getSimpleName());
        Page<ProdutoResponse> produtosResponse = produtoService.listaProdutos(pageable);
        log.info("[end] - {} - listarProdutos", getClass().getSimpleName());
        return produtosResponse;
    }

    @Override
    public ProdutoResponse listarProdutoPorId(UUID idProduto) {
        log.info("[start] - {} - listarProdutoPorId", getClass().getSimpleName());
        ProdutoResponse produtoResponse = produtoService.listarProdutoPorId(idProduto);
        log.info("[end] - {} - listarProdutoPorId", getClass().getSimpleName());
        return produtoResponse;
    }

    @Override
    public ProdutoResponse atualizarCadastroProduto(UUID idProduto, ProdutoUpdateRequest produtoUpdateRequest) {
        log.info("[start] - {} - atualizarCadastroProduto", getClass().getSimpleName());
        ProdutoResponse produtoResponse = produtoService.atualizaCadastroProdutoPorId(idProduto, produtoUpdateRequest);
        log.info("[end] - {} - atualizarCadastroProduto", getClass().getSimpleName());
        return produtoResponse;
    }

    @Override
    public void deletarProdutoPorId(UUID idProduto) {
        log.info("[start] - {} - deletarProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
        produtoService.deletaProdutoPorId(idProduto);
        log.info("[end] - {} - deletarProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
    }
}
