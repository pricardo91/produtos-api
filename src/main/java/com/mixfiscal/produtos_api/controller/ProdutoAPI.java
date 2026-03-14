package com.mixfiscal.produtos_api.controller;

import com.mixfiscal.produtos_api.dto.in.ProdutoCreateRequest;
import com.mixfiscal.produtos_api.dto.in.ProdutoUpdateRequest;
import com.mixfiscal.produtos_api.dto.out.ProdutoResponse;
import com.mixfiscal.produtos_api.handler.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Produto", description = "Endpoints para gerenciamento de produtos")
@RequestMapping(value = "/v1/produtos")
public interface ProdutoAPI {

    @Operation(
            summary = "Cadastrar produto",
            description = "Endpoint responsável para cadastrar novos produtos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request Body inválido ou inexistente!",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "SKU já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProdutoResponse criarProduto(@RequestBody @Valid ProdutoCreateRequest produtoNovo);

    @Operation(
            summary = "Listar produto",
            description = "Endpoint responsável para listar todos os produtos cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ProdutoResponse> listarProdutos(Pageable pageable);

    @Operation(
            summary = "Listar produto por id",
            description = "Endpoint responsável por listar produto cadastrado por id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto listado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @GetMapping("/{idProduto}")
    @ResponseStatus(HttpStatus.OK)
    ProdutoResponse listarProdutoPorId(@PathVariable UUID idProduto);

    @Operation(
            summary = "Atualizar produto",
            description = "Endpoint responsável por atualizar produtos cadastrado por id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request Body inválido ou inexistente!",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @PatchMapping("/{idProduto}")
    @ResponseStatus(HttpStatus.OK)
    ProdutoResponse atualizarCadastroProduto(@PathVariable UUID idProduto,
                                             @RequestBody @Valid ProdutoUpdateRequest produtoUpdateRequest);

    @Operation(
            summary = "Deletar produto",
            description = "Endpoint responsável por deletar produto cadastrado por id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @DeleteMapping("/{idProduto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletarProdutoPorId(@PathVariable UUID idProduto);
}
