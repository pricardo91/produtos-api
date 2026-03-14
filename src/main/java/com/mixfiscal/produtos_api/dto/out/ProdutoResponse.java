package com.mixfiscal.produtos_api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProdutoResponse {

    @Schema(description = "ID único do produto")
    private UUID id;

    @Schema(description = "Nome do produto", example = "Notebook Dell i5 8GB")
    private String nome;

    @Schema(description = "SKU do produto", example = "PROD-001-8GB-I5")
    private String sku;

    @Schema(description = "Preço base do produto", example = "2859.99", format = "decimal")
    private BigDecimal precoBase;

    @Schema(description = "Código NCM do produto", example = "8471.30.12")
    private String codigoNcm;

    @Schema(description = "Informações tributárias do produto")
    private TaxResponse imposto;
}
