package com.serverless.functions.livro;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class AtualizarLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private LivroService livroService;
    private static final Logger LOG = LogManager.getLogger(AtualizarLivro.class);
    private Livro livro;
    private Map<String, Object> input;
    private ApiGatewayResponse response;
    private final ObjectMapper objectMapper = ObjectMapperProxy.getObjectMapper();

    public AtualizarLivro() {
    	livroService = new LivroService();
    }
    
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciiar processo para atualizar o livro");
        this.input = input;
        atualizar();
        return this.response;
    }

    public void convertInputToNode() {
        JsonNode body = GetInput.getBody(this.input);
        createObjectLivro(body);
    }

    public void createObjectLivro(JsonNode body) {
        LOG.info("Criando objeto livro");
        this.objectMapper.findAndRegisterModules();
        this.livro = this.objectMapper.convertValue(body, Livro.class);
    }

    public void atualizar() {
        LOG.info("Atualizar o livro no serviço de dynamoDB");
        convertInputToNode();
        try {
            this.livroService.atualizarLivro(this.livro);
            this.response = buildResponseSuccess();
        } catch (RuntimeException e) {
            this.response = buildResponseError(e);
        }
    }

    private ApiGatewayResponse buildResponseError(RuntimeException e) {
        LOG.info("Criando resposta com erro");
        return ApiGatewayResponse.builder()
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(400).build();
    }

    private ApiGatewayResponse buildResponseSuccess() {
        LOG.info("Criando resposta com sucesso!");
        return ApiGatewayResponse.builder()
                .setObjectBody(new Response("Livro atualizado com sucesso!", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .setStatusCode(204).build();
    }

	public void setLivroService(LivroService livroService) {
		this.livroService = livroService;
	}
}
