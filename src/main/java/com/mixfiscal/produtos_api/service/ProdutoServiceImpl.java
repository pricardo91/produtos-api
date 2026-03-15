package com.mixfiscal.produtos_api.service;

import com.mixfiscal.produtos_api.client.TaxIntegrationClient;
import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.in.ProdutoUpdateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.dto.out.TaxResponse;
import com.mixfiscal.produtos_api.entity.Produto;
import com.mixfiscal.produtos_api.exception.ProdutoNaoEncontradoException;
import com.mixfiscal.produtos_api.exception.SkuJaCadastradoException;
import com.mixfiscal.produtos_api.mapper.ProdutoMapper;
import com.mixfiscal.produtos_api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TaxIntegrationClient taxIntegrationClient;
    private final ProdutoMapper mapper;

    @Transactional
    public ProdutoResponse criarNovoProduto(ProdutoCreateRequest produtoNovo) {
        log.info("[start] - {} - criando produto", getClass().getSimpleName());
        if (produtoRepository.existsBySku(produtoNovo.getSku())) {
            throw new SkuJaCadastradoException(produtoNovo.getSku());
        }
        Produto produto = mapper.toDomain(produtoNovo);
        Produto produtoSalvo = produtoRepository.save(produto);
        ProdutoResponse response = mapper.toResponse(produtoSalvo);
        log.info("[end] - {} - criando produto", getClass().getSimpleName());
        return response;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listaProdutos(Pageable pageable) {
        log.info("[start] - {} - listando produtos", getClass().getSimpleName());
        Page<ProdutoResponse> produtos = produtoRepository.findAll(pageable)
                .map(mapper::toResponse);
        log.info("[end] - {} - listando produtos", getClass().getSimpleName());
        return produtos;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse listarProdutoPorId(UUID idProduto) {
        log.info("[start] - {} - listando produto por ID: {}", getClass().getSimpleName(), idProduto);
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(idProduto));
        TaxResponse taxResponse = taxIntegrationClient.consultarAliqImpostoPorNcm(produto.getCodigoNcm());
        ProdutoResponse produtoResponse = mapper.toResponse(produto, taxResponse);
        log.info("[end] - {} - listando produto por ID: {}", getClass().getSimpleName(), idProduto);
        return produtoResponse;
    }

    @Transactional
    public void deletaProdutoPorId(UUID idProduto) {
        log.info("[start] - {} - deletarProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
        if (!produtoRepository.existsById(idProduto)) {
            throw new ProdutoNaoEncontradoException(idProduto);
        }
        produtoRepository.deleteById(idProduto);
        log.info("[end] - {} - deletarProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
    }

    @Transactional
    public ProdutoResponse atualizaCadastroProdutoPorId(UUID idProduto, ProdutoUpdateRequest request) {
        log.info("[start] - {} - atualizaCadastroProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(idProduto));

        produto.atualizar(request.getNome(), request.getPrecoBase(), request.getCodigoNcm());

        log.info("[end] - {} - atualizaCadastroProdutoPorId | ID: {}", getClass().getSimpleName(), idProduto);
        return mapper.toResponse(produtoRepository.save(produto));
    }
}
