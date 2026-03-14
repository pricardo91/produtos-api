package com.mixfiscal.produtos_api.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProdutoCreateRequest {

    @Schema(description = "Nome do produto", example = "Notebook Dell i5 8GB")
    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 carateres")
    private String nome;

    @Schema(description = "SKU do produto", example = "PROD-001-8GB-I5")
    @NotBlank(message = "SKU do produto é obrigatório")
    @Size(min = 3, max = 100, message = "O SKU deve conter entre 3 e 100 carateres")
    private String sku;

    @Schema(description = "Preço base do produto", example = "2859.99", format = "decimal")
    @NotNull(message = "Preço base do produto é obrigatório")
    @Positive(message = "Valor base do produto deve ser maior que zero")
    private BigDecimal precoBase;

    @Schema(description = "Código NCM do produto", example = "8471.30.12")
    @NotBlank(message = "NCM do produto é obrigatório")
    @Pattern(regexp = "\\d{4}\\.\\d{2}\\.\\d{2}", message = "Código NCM deve seguir o padrão 0000.00.00")
    private String codigoNcm;
}
