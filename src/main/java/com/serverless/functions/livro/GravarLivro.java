package com.serverless.functions.livro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.helper.ObjectMapperProxy;
import com.serverless.models.Livro;
import com.serverless.services.LivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class GravarLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(GravarLivro.class);
    private LivroService livroService;
    private Livro livro;
    private Map<String, Object> input;
    private ApiGatewayResponse response;
    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();

    public GravarLivro() {
        this.livroService = new LivroService();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Iniciando processo de gravação do livro");
        this.input = input;
        getData();
        gravarNovoLivroNoDynamoDB();
        return this.response;
    }

    private void getData() {
        LOG.info("Tratando os dados");
        JsonNode body = GetInput.getBody(this.input);
        createLivro(body);
    }

    private void createLivro(JsonNode body) {
        LOG.info("Criando objeto livro para ser gravado");
        this.objectMapper.findAndRegisterModules();
        this.livro = ObjectMapperProxy.getObjectMapper().convertValue(body, Livro.class);
    }


    private void gravarNovoLivroNoDynamoDB() {
        try {
            LOG.info("Persistindo os dados no dynamoDB");
            this.livroService.gravarNovoLivro(this.livro);
            this.response = gerarRespostaComSucesso();
        } catch (RuntimeException e) {
            LOG.error("ocorre um erro ao persistir os dados no dynamoDB");
            this.response = gerarRespostaComErro(e);
        }
    }

    private ApiGatewayResponse gerarRespostaComSucesso() {
        LOG.info("Gerando resposta com sucesso!");
        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response("Livro gravado com sucesso!", this.input))
                .setStatusCode(201).build();
    }

    private ApiGatewayResponse gerarRespostaComErro(RuntimeException e) {
        LOG.info("Gerando resposta com erro");
        return ApiGatewayResponse.builder()
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setStatusCode(500).build();
    }
}
