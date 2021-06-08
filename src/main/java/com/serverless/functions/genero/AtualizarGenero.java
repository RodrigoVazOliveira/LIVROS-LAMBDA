package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class AtualizarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final GeneroService generoService = new GeneroService();
    private final Genero genero = new Genero();
    private static final Logger LOG = LogManager.getLogger(AtualizarGenero.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        LOG.info("Capturando as informações enviadas.");

        this.genero.setId(stringObjectMap.get("Id").toString());
        this.genero.setNome(stringObjectMap.get("nome").toString());

        LOG.info("Iniciando o processo de atualização no DynamoDB");

        return verificarResposta(stringObjectMap);
    }
}
