package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeleteGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private static final Logger LOG = LogManager.getLogger(DeleteGenero.class);
    private String id;
    private Map<String, Object> input;

    public DeleteGenero() {
        this.generoService = new GeneroService();
    }

    public DeleteGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciar o processo para deletar o genero!");
        this.input = input;
        getData();

        return deletarGenero();
    }

    private ApiGatewayResponse deletarGenero() {
        try {
            this.generoService.deletarGenero(this.id);
            return construirRespostaComSucesso();
        } catch (RuntimeException e) {
            return construirRespostaComErro(e);
        }
    }

    private ApiGatewayResponse construirRespostaComSucesso() {
        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("O genero com id " + this.id + " foi excluido!",
                        this.input))
                .setStatusCode(204)
                .build();
    }

    private ApiGatewayResponse construirRespostaComErro(RuntimeException e) {
        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setStatusCode(400)
                .build();
    }

    private void getData() {
        JsonNode body = GetInput.getBody(this.input);
        this.id = body.get("id").asText();
    }
}
