package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class AtualizarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private final Genero genero = new Genero();
    private static final Logger LOG = LogManager.getLogger(AtualizarGenero.class);
    private Map<String, Object> input;

    public AtualizarGenero() {
        this.generoService = new GeneroService();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Capturando as informações enviadas.");
        this.input = input;
        getData();
        LOG.info("Iniciando o processo de atualização no DynamoDB");

        return verificarResposta();
    }

    private void getData() {
        JsonNode body = GetInput.getBody(this.input);
        this.genero.setId(body.get("id").asText());
        this.genero.setNome(body.get("nome").asText());
    }

    private ApiGatewayResponse verificarResposta() {
        LOG.info("Gerando resposta");
        try {
            this.generoService.atualizarGenero(this.genero);
            return retornoApiGatewayComSucesso();
        } catch (RuntimeException e) {
            LOG.error("Ocorreu um erro ao procurar o genero");
            return retornoApiGatewayComErro(e);
        }
    }

    private ApiGatewayResponse retornoApiGatewayComSucesso() {
        return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Genero com id " +
                        this.genero.getId() + " atualizado com sucesso!", this.input))
                .build();
    }

    private ApiGatewayResponse retornoApiGatewayComErro(RuntimeException e) {
        return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(e.getMessage(), this.input))
                .build();
    }
}
