package com.mixfiscal.produtos_api.client;

import com.mixfiscal.produtos_api.dto.out.TaxResponse;

public interface TaxIntegrationClient {
    TaxResponse consultarAliqImpostoPorNcm(String ncm);
}
