package com.serverless.functions.tipodelivro;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.helper.GetInput;
import com.serverless.services.TipoDeLivroService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DeleteTipoDeLivro implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private TipoDeLivroService tipoDeLivroService;
    private static final Logger LOG = LogManager.getLogger(DeleteTipoDeLivro.class);
    private String id;
    private Map<String, Object> input;
    private ApiGatewayResponse response;

    public DeleteTipoDeLivro() {
        this.tipoDeLivroService = new TipoDeLivroService();
    }

    public DeleteTipoDeLivro(TipoDeLivroService tipoDeLivroService) {
        this.tipoDeLivroService = tipoDeLivroService;
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        this.input = input;
        getData();
        LOG.info("Iniciando serviço do tipo do livro para deletar o tipo de livro no ndynamoDB");
        deletarTipoDeLivro();

        return this.response;
    }

    private void getData() {
        LOG.info("Iniciando o lambda e tratando os dados enviados");
        JsonNode body = GetInput.getBody(this.input);
        this.id = body.get("id").asText();
    }

    private void deletarTipoDeLivro() {
        try {
            LOG.info("Inicinado o processo para deletar o tipo de livro");
            this.tipoDeLivroService.deleteTipoDeLivro(this.id);
            this.response = gerarRespostadeleteTipoDeLivroComSucesso();
        } catch (RuntimeException e) {
            LOG.error("Ocorreu um erro ao deletar um tipo de livro");
            this.response = gerarRespostaDeleteTipoDeLivroComErro(e);
        }
    }

    private ApiGatewayResponse gerarRespostadeleteTipoDeLivroComSucesso() {
        LOG.info("Gerar resposta com sucesso!");
        return ApiGatewayResponse.builder()
                .setStatusCode(204)
                .setObjectBody(new Response("tipo de livro deletado!", this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }

    private ApiGatewayResponse gerarRespostaDeleteTipoDeLivroComErro(RuntimeException e) {
        LOG.info("Gerar resposta com erro");
        return ApiGatewayResponse.builder()
                .setStatusCode(400)
                .setObjectBody(new Response(e.getMessage(), this.input))
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
