package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class GravarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private static final Logger LOG = LogManager.getLogger(GravarGenero.class);
    private String nome;

    public GravarGenero() {
        this.generoService = new GeneroService();
    }

    public GravarGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Gravando o novo genero no dynamoDB");

        getData(input);

        this.generoService.cadastrarNovoGeneroDeLivro(this.nome);

        LOG.info("Gravação efetuada com sucesso!");

        Response response = new Response("Gravação efetuada com sucesso!", input);

        return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(response)
                .build();
    }

    private void getData(Map<String, Object> input) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode body = objectMapper.readTree( (String) input.get("body") );
            this.nome = body.get("nome").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}