package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GravarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private static final Logger LOG = LogManager.getLogger(GravarGenero.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        LOG.info("Gravando o novo genero no dynamoDB");

        this.generoService = new GeneroService();
        this.generoService.cadastrarNovoGeneroDeLivro(stringObjectMap.get("nome").toString());

        LOG.info("Gravação efetuada com sucesso!");

        Response response = new Response("Gravação efetuada com sucesso!", stringObjectMap);

        return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(response)
                .build();
    }
}