package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class AtualizarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private final Genero genero = new Genero();
    private static final Logger LOG = LogManager.getLogger(AtualizarGenero.class);

    public AtualizarGenero() {
        this.generoService = new GeneroService();
    }

    public AtualizarGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Capturando as informações enviadas.");
        getData(input);
        LOG.info("Iniciando o processo de atualização no DynamoDB");

        return verificarResposta(input);
    }

    private void getData(Map<String, Object> input) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode body = objectMapper.readTree( (String) input.get("body") );
            this.genero.setId(body.get("id").asText());
            this.genero.setNome(body.get("nome").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiGatewayResponse verificarResposta(Map<String, Object> input) {
        LOG.info("Gerando resposta");
        try {
            this.generoService.atualizarGenero(this.genero);
            return ApiGatewayResponse.builder()
                    .setStatusCode(201)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .setObjectBody(new Response("Genero com id " +
                            this.genero.getId() + " atualizado com sucesso!", input))
                    .build();
        } catch (RuntimeException e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .setObjectBody(new Response(e.getMessage(), input))
                    .build();
        }
    }
}
