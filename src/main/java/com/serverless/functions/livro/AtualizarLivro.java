package com.serverless.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.services.LivroService;

import java.util.Map;

public class AtualizarLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private LivroService livroService;

    public AtualizarLivro() {
        this.livroService = new LivroService();
    }

    public AtualizarLivro(LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return null;
    }
}
