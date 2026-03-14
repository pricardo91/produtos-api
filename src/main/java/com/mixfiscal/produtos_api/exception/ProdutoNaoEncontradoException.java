package com.mixfiscal.produtos_api.exception;

import com.mixfiscal.produtos_api.handler.ApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class ProdutoNaoEncontradoException extends ApiException {
    public ProdutoNaoEncontradoException(UUID idProduto) {
        super(
                HttpStatus.NOT_FOUND,
                "Produto não encontrado com o ID: " + idProduto,
                "Por favor, insira um ID de produto existente!",
                null
        );
    }
}
