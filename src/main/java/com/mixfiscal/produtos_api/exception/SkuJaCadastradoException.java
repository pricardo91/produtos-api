package com.mixfiscal.produtos_api.exception;

import com.mixfiscal.produtos_api.handler.ApiException;
import org.springframework.http.HttpStatus;

public class SkuJaCadastradoException extends ApiException {
    public SkuJaCadastradoException(String sku) {
        super(
                HttpStatus.CONFLICT,
                "Produto já foi cadastrado | SKU: " + sku,
                "Não é possível cadastrar dois produtos com o mesmo SKU!",
                null
        );
    }
}
