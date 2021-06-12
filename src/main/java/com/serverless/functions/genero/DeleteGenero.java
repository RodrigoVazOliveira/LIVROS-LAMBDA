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

public class DeleteGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private GeneroService generoService;
    private static final Logger LOG = LogManager.getLogger(DeleteGenero.class);

    public DeleteGenero() {
        this.generoService = new GeneroService();
    }

    public DeleteGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciar o processo para deletar o genero!");

        try {
            this.generoService.deletarGenero(input.get("id").toString());
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
}
