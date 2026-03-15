package com.mixfiscal.produtos_api.service;

import com.mixfiscal.produtos_api.client.TaxIntegrationClientMock;
import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.in.ProdutoUpdateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.dto.out.TaxResponse;
import com.mixfiscal.produtos_api.entity.Produto;
import com.mixfiscal.produtos_api.exception.ProdutoNaoEncontradoException;
import com.mixfiscal.produtos_api.exception.SkuJaCadastradoException;
import com.mixfiscal.produtos_api.mapper.ProdutoMapper;
import com.mixfiscal.produtos_api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private TaxIntegrationClientMock taxIntegrationClientMock;
    @Mock
    private ProdutoMapper mapper;

    @InjectMocks
    private ProdutoServiceImpl produtoServiceImpl;

    private UUID produtoId;
    private Produto produto;
    private ProdutoResponse produtoResponse;
    private TaxResponse taxResponse;

    @BeforeEach
    void setUp() {
        produtoId = UUID.randomUUID();

        produto = Produto.builder()
                .id(produtoId)
                .nome("Notebook Dell")
                .sku("NOTE-001")
                .precoBase(new BigDecimal("4599.90"))
                .codigoNcm("8471.30.12")
                .build();

        produtoResponse = ProdutoResponse.builder()
                .id(produtoId)
                .nome("Notebook Dell")
                .sku("NOTE-001")
                .precoBase(new BigDecimal("4599.90"))
                .codigoNcm("8471.30.12")
                .build();

        taxResponse = TaxResponse.builder()
                .ncm("8471.30.12")
                .descricao("Notebook")
                .aliquotaII(new BigDecimal("16.00"))
                .aliquotaIpi(new BigDecimal("15.00"))
                .aliquotaPis(new BigDecimal("2.10"))
                .aliquotaCofins(new BigDecimal("9.65"))
                .build();
    }

    private void mockSaveAndMapperPassthrough() {

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(chamada -> chamada.getArgument(0));

        when(mapper.toResponse(any(Produto.class))).thenAnswer(
                chamada -> {
                    Produto p = chamada.getArgument(0);
                    return ProdutoResponse.builder()
                            .id(p.getId())
                            .nome(p.getNome())
                            .sku(p.getSku())
                            .precoBase(p.getPrecoBase())
                            .codigoNcm(p.getCodigoNcm())
                            .build();
                });
    }

    @Nested
    @DisplayName("criarNovoProduto")
    class CriarNovoProduto {

        @Test
        @DisplayName("deve criar produto com sucesso quando SKU não existe")
        void deveCriarProdutoComSucesso() {
            // ARRANGE
            ProdutoCreateRequest request = ProdutoCreateRequest.builder()
                    .nome("Notebook Dell")
                    .sku("NOTE-001")
                    .precoBase(new BigDecimal("4599.90"))
                    .codigoNcm("8471.30.12")
                    .build();

            when(produtoRepository.existsBySku("NOTE-001")).thenReturn(false);
            when(mapper.toDomain(request)).thenReturn(produto);
            when(produtoRepository.save(produto)).thenReturn(produto);
            when(mapper.toResponse(produto)).thenReturn(produtoResponse);

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.criarNovoProduto(request);

            // ASSERT
            assertThat(resultado).isNotNull();
            assertThat(resultado.getSku()).isEqualTo("NOTE-001");
            assertThat(resultado.getNome()).isEqualTo("Notebook Dell");
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("deve lançar SkuJaCadastradoException quando SKU já existe")
        void deveLancarExcecaoQuandoSkuJaExiste() {
            // ARRANGE
            ProdutoCreateRequest request = ProdutoCreateRequest.builder()
                    .nome("Notebook Dell")
                    .sku("NOTE-001")
                    .precoBase(new BigDecimal("4599.90"))
                    .codigoNcm("8471.30.12")
                    .build();

            when(produtoRepository.existsBySku("NOTE-001")).thenReturn(true);

            // ACT + ASSERT
            assertThatThrownBy(() -> produtoServiceImpl.criarNovoProduto(request))
                    .isInstanceOf(SkuJaCadastradoException.class);

            verify(produtoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("listarProdutoPorId")
    class ListarProdutoPorId {

        @Test
        @DisplayName("deve retornar produto com dados de imposto quando ID existe")
        void deveRetornarProdutoComImpostoQuandoIdExiste() {
            // ARRANGE
            ProdutoResponse responseComImposto = ProdutoResponse.builder()
                    .id(produtoId)
                    .nome("Notebook Dell")
                    .sku("NOTE-001")
                    .precoBase(new BigDecimal("4599.90"))
                    .codigoNcm("8471.30.12")
                    .imposto(taxResponse)
                    .build();

            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
            when(taxIntegrationClientMock.consultarAliqImpostoPorNcm("8471.30.12")).thenReturn(taxResponse);
            when(mapper.toResponse(produto, taxResponse)).thenReturn(responseComImposto);

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.listarProdutoPorId(produtoId);

            // ASSERT
            assertThat(resultado.getImposto()).isNotNull();
            assertThat(resultado.getImposto().getNcm()).isEqualTo("8471.30.12");
            assertThat(resultado.getImposto().getAliquotaII()).isEqualByComparingTo("16.00");
        }

        @Test
        @DisplayName("deve lançar ProdutoNaoEncontradoException quando ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            // ARRANGE
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

            // ACT + ASSERT
            assertThatThrownBy(() -> produtoServiceImpl.listarProdutoPorId(produtoId))
                    .isInstanceOf(ProdutoNaoEncontradoException.class);
        }
    }

    @Nested
    @DisplayName("listaProdutos")
    class ListaProdutos {

        @Test
        @DisplayName("deve retornar página com produtos quando existem registros")
        void deveRetornarPaginaComProdutos() {
            // ARRANGE
            Pageable pageable = PageRequest.of(0, 10);
            when(produtoRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(produto)));
            when(mapper.toResponse(produto)).thenReturn(produtoResponse);

            // ACT
            Page<ProdutoResponse> resultado = produtoServiceImpl.listaProdutos(pageable);

            // ASSERT
            assertThat(resultado.getContent()).hasSize(1);
            assertThat(resultado.getContent().get(0).getSku()).isEqualTo("NOTE-001");
        }

        @Test
        @DisplayName("deve retornar página vazia quando não há produtos cadastrados")
        void deveRetornarPaginaVaziaQuandoNaoHaProdutos() {
            // ARRANGE
            Pageable pageable = PageRequest.of(0, 10);
            when(produtoRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

            // ACT
            Page<ProdutoResponse> resultado = produtoServiceImpl.listaProdutos(pageable);

            // ASSERT
            assertThat(resultado.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("deletaProdutoPorId")
    class DeletaProdutoPorId {

        @Test
        @DisplayName("deve deletar produto com sucesso quando ID existe")
        void deveDeletarProdutoComSucesso() {
            // ARRANGE
            when(produtoRepository.existsById(produtoId)).thenReturn(true);

            // ACT
            produtoServiceImpl.deletaProdutoPorId(produtoId);

            // ASSERT
            verify(produtoRepository, times(1)).deleteById(produtoId);
        }

        @Test
        @DisplayName("deve lançar ProdutoNaoEncontradoException quando ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            // ARRANGE
            when(produtoRepository.existsById(produtoId)).thenReturn(false);

            // ACT + ASSERT
            assertThatThrownBy(() -> produtoServiceImpl.deletaProdutoPorId(produtoId))
                    .isInstanceOf(ProdutoNaoEncontradoException.class);

            verify(produtoRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("atualizaCadastroProdutoPorId")
    class AtualizaCadastroProdutoPorId {

        @Test
        @DisplayName("deve atualizar produto com sucesso quando ID existe")
        void deveAtualizarProdutoComSucesso() {
            // ARRANGE
            ProdutoUpdateRequest request = ProdutoUpdateRequest.builder()
                    .nome("Notebook Dell Atualizado")
                    .precoBase(new BigDecimal("5000.00"))
                    .codigoNcm("8471.30.12")
                    .build();

            ProdutoResponse responseAtualizado = ProdutoResponse.builder()
                    .id(produtoId)
                    .nome("Notebook Dell Atualizado")
                    .sku("NOTE-001")
                    .precoBase(new BigDecimal("5000.00"))
                    .codigoNcm("8471.30.12")
                    .build();

            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(mapper.toResponse(any(Produto.class))).thenReturn(responseAtualizado);

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.atualizaCadastroProdutoPorId(produtoId, request);

            // ASSERT
            assertThat(resultado.getNome()).isEqualTo("Notebook Dell Atualizado");
            assertThat(resultado.getPrecoBase()).isEqualByComparingTo("5000.00");
        }

        @Test
        @DisplayName("deve lançar ProdutoNaoEncontradoException quando ID não existe")
        void deveLancarExcecaoQuandoIdNaoExiste() {
            // ARRANGE
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

            // ACT + ASSERT
            assertThatThrownBy(() -> produtoServiceImpl.atualizaCadastroProdutoPorId(produtoId,
                    ProdutoUpdateRequest.builder().nome("Qualquer Nome").build()))
                    .isInstanceOf(ProdutoNaoEncontradoException.class);
        }

        @Test
        @DisplayName("deve manter nome original quando nome não é informado no request")
        void deveManterNomeOriginalQuandoNomeNaoInformado() {
            // ARRANGE
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
            mockSaveAndMapperPassthrough();

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.atualizaCadastroProdutoPorId(produtoId,
                    ProdutoUpdateRequest.builder().precoBase(new BigDecimal("5000.00")).build());

            // ASSERT
            assertThat(resultado.getNome()).isEqualTo("Notebook Dell");
        }

        @Test
        @DisplayName("deve manter preço original quando preço não é informado no request")
        void deveManterPrecoOriginalQuandoPrecoNaoInformado() {
            // ARRANGE
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
            mockSaveAndMapperPassthrough();

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.atualizaCadastroProdutoPorId(produtoId,
                    ProdutoUpdateRequest.builder().nome("Notebook Dell Atualizado").build());

            // ASSERT
            assertThat(resultado.getPrecoBase()).isEqualByComparingTo("4599.90");
        }

        @Test
        @DisplayName("deve manter NCM original quando NCM não é informado no request")
        void deveManterNcmOriginalQuandoNcmNaoInformado() {
            // ARRANGE
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
            mockSaveAndMapperPassthrough();

            // ACT
            ProdutoResponse resultado = produtoServiceImpl.atualizaCadastroProdutoPorId(produtoId,
                    ProdutoUpdateRequest.builder().nome("Notebook Dell Atualizado").build());

            // ASSERT
            assertThat(resultado.getCodigoNcm()).isEqualTo("8471.30.12");
        }
    }
}