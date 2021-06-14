package com.serverless.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GravarLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(GravarLivro.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        return null;
    }
}
