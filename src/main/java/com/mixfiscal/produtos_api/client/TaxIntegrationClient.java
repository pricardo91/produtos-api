package com.mixfiscal.produtos_api.client;

import com.mixfiscal.produtos_api.config.TaxIntegrationProperties;
import com.mixfiscal.produtos_api.dto.out.TaxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TaxIntegrationClient {
    private final TaxIntegrationProperties taxIntegrationProperties;

    public TaxResponse consultarAliqImpostoPorNcm(String ncm) {
        // Simulação de chamada à API externa: GET {taxIntegrationProperties.getUrl()}/{ncm}
        // Substituir pelo cliente HTTP real quando a API estiver disponível

        return TaxResponse.builder()
                .ncm(ncm)
                .descricao("De peso inferior a 3,5 kg, com tela de área superior a")
                .aliquotaII(new BigDecimal("16.00"))
                .aliquotaIpi(new BigDecimal("15.00"))
                .aliquotaPis(new BigDecimal("2.10"))
                .aliquotaCofins(new BigDecimal("9.65"))
                .build();
    }
}
