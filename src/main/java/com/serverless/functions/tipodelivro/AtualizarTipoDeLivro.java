package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.models.TipoDeLivro;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class AtualizarTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private TipoDeLivroService tipoDeLivroService;
    private final static Logger LOG = LogManager.getLogger(AtualizarTipoDeLivro.class);
    private TipoDeLivro tipoDeLivro;
    private Map<String, Object> input;
    private ApiGatewayResponse response;

    public AtualizarTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Inciiando o funcionamento do lambda para atualizar");
        this.input = input;
        getData();
        atualizarGenero();

        return this.response;
    }

    private void getData() {
        JsonNode body = GetInput.getBody(this.input);
        this.tipoDeLivro = new TipoDeLivro();
        this.tipoDeLivro.setId(body.get("id").asText());
        this.tipoDeLivro.setNome(body.get("nome").asText());
    }

    private void atualizarGenero() {
        try {
            this.tipoDeLivroService.atualizarTipoDeLivro(this.tipoDeLivro);
            this.response = gerarRespostaComSucesso();
        } catch (RuntimeException e) {
            this.response = gerarRespostaComError(e);
        }
    }

    private ApiGatewayResponse gerarRespostaComSucesso() {
        LOG.info("Inciando da atualização do tipo de livro!");
        return ApiGatewayResponse.builder()
                .setStatusCode(201)
                .setObjectBody(new Response("Dados atualizado com sucesso! " + this.tipoDeLivro,
                        this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }

    private ApiGatewayResponse gerarRespostaComError(RuntimeException e) {
        LOG.info("erro na atualização do tipo de livro!");
        return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
