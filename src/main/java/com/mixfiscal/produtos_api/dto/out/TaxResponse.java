package com.mixfiscal.produtos_api.dto.out;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TaxResponse {
    private String ncm;
    private String descricao;

    @Schema(description = "Alíquota de Importação (II)", example = "16.00")
    private BigDecimal aliquotaII;

    @Schema(description = "Alíquota IPI", example = "15.00")
    private BigDecimal aliquotaIpi;

    @Schema(description = "Alíquota PIS/PASEP Ad Valorem", example = "2.10")
    private BigDecimal aliquotaPis;

    @Schema(description = "Alíquota COFINS Ad Valorem", example = "9.65")
    private BigDecimal aliquotaCofins;
}
