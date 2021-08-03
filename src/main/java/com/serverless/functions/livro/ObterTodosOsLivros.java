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

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciando processo para obter os livros do dynamoDB");
        this.input = input;
        buildResponse();
        return this.response;
    }

    private void convertListLivrosToJson() {
        LOG.info("Converter objetos para JSON");
        try {
            this.message = objectMapper.writeValueAsString(this.livros);
        } catch (JsonProcessingException e) {
            LOG.error("Erro ao converter o objeto para JSON");
            e.printStackTrace();
        }
    }

    private void responseWithSuccess() {
        LOG.info("Gerando a resposta com sucesso!");
        this.response = ApiGatewayResponse.builder()
                .setObjectBody(new Response(this.message, this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(200).build();
    }

    private void responseWithError(RuntimeException e) {
        LOG.info("Gerando a resposta com erro");
        this.response = ApiGatewayResponse.builder()
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(500).build();
    }

    private void buildResponse() {
        try {
            LOG.info("Obtendo dados no dynamoDB com serviço de livro!");
            this.livros = this.livroService.obterTodosOsLivros();
            convertListLivrosToJson();
            responseWithSuccess();
        } catch (RuntimeException e) {
            LOG.error("Ocorreu um erro ao obter os dados no dynamoDB");
            responseWithError(e);
        }
    }
}
