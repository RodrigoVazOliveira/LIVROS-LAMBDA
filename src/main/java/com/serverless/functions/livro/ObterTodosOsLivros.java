package com.serverless.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ObterTodosOsLivros implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private LivroService livroService;
    private static final Logger LOG = LogManager.getLogger(ObterTodosOsLivros.class);
    private List<Livro> livros;
    private String message;
    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();
    private ApiGatewayResponse response;
    private Map<String, Object> input;

    public ObterTodosOsLivros() {
        this.livroService = new LivroService();
    }

    public ObterTodosOsLivros(LivroService livroService) {
        this.livroService = livroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciando processo para obter os livros do dynamoDB");
        this.input = input;
        this.livros = this.livroService.obterTodosOsLivros();
        convertListLivrosToJson();
        buildResponse();
        return this.response;
    }


}
