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

public class DeleteGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private static final Logger LOG = LogManager.getLogger(DeleteGenero.class);
    private String id;

    public DeleteGenero() {
        this.generoService = new GeneroService();
    }

    public DeleteGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciar o processo para deletar o genero!");
        getData(input);
        try {
            this.generoService.deletarGenero(this.id);
            return ApiGatewayResponse.builder()
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .setObjectBody(new Response("O genero com id " + input.get("id") + " foi excluido!", input))
                    .setStatusCode(204)
                    .build();
        } catch (RuntimeException e) {
            return ApiGatewayResponse.builder()
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .setObjectBody(new Response(e.getMessage(), input))
                    .setStatusCode(400)
                    .build();
        }
    }

    private void getData(Map<String, Object> input) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode body = objectMapper.readTree( (String) input.get("body") );
            this.id = body.get("id").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
