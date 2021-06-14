package com.serverless.functions.genero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.models.Genero;
import com.serverless.services.GeneroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class ObterGenero implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(ObterGenero.class);
    private GeneroService generoService;

    public ObterGenero() {
        this.generoService = new GeneroService();
    }

    public ObterGenero(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciar processo para gerar lista de generos!");

        String generos = converterListaDeGeneroParaJson(this.generoService.obterTodosGeneros());

        LOG.info("Gerado lista de generos!");

        return criarResposta(generos, input);
    }

    private String converterListaDeGeneroParaJson(Iterable<Genero> generos) {
        ObjectMapper objectMapper = new ObjectMapper();
        String resultado;

        try {
            resultado = objectMapper.writeValueAsString(generos);
        } catch (JsonProcessingException e) {
            resultado = e.getMessage();
        }

        return resultado;
    }

    private ApiGatewayResponse criarResposta(String mensagem, Map<String, Object> stringObjectMap) {
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(mensagem, stringObjectMap))
                .build();
    }
}
