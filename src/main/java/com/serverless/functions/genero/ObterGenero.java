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

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> stringObjectMap, Context context) {
        LOG.info("Iniciar processo para gerar lista de generos!");

        GeneroService generoService = new GeneroService();
        String generos = converterListaDeGeneroParaJson(generoService.obterTodosGeneros());

        LOG.info("Gerado lista de generos!");

        return criarResposta(generos, stringObjectMap);
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
        Response response = new Response(mensagem, stringObjectMap);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(response)
                .build();
    }
}
