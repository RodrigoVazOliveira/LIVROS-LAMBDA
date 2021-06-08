package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.services.GeneroService;

import java.util.Map;

public class AtualizarGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final GeneroService generoService = new GeneroService();

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        return null;
    }
}
