package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.Jackson;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class ObterTodosTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private TipoDeLivroService tipoDeLivroService;
    private static final Logger LOG = LogManager.getLogger(ObterTodosTipoDeLivro.class);

    public ObterTodosTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciar processo para obter todos os tipo de livros");

        Iterable<TipoDeLivro> tipoDeLivros = this.tipoDeLivroService.obterTodosTipoDeLivro();
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(new Response(Jackson.toJsonString(tipoDeLivros), input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
